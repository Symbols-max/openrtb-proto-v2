/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iabtechlab.openrtb.v2.util;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Arrays.asList;

import com.codahale.metrics.MetricRegistry;
import com.iabtechlab.adcom.v1.enums.Enums.Creative.Attribute;
import com.iabtechlab.adcom.v1.enums.Enums.Creative.AudioVideoType;
import com.iabtechlab.adcom.v1.enums.Enums.LinearityMode;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp.Audio;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp.Banner;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp.Video;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse.SeatBid;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse.SeatBid.Bid;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link OpenRtbValidator}.
 */
public class OpenRtbValidatorTest {
  private static BidRequest requestBanner = BidRequest.newBuilder()
      .setId("1")
      .addAllBadv(asList("adv1.com", "adv2.com"))
      .addAllBcat(asList("IAB1", "IAB2"))
      .addImp(Imp.newBuilder()
          .setId("1")
          .setBanner(Banner.newBuilder()
              .setId("1")
              .addAllBattr(asList(Attribute.ANNOYING.getNumber(), Attribute.POP.getNumber()))))
      .build();
  private static BidRequest requestVideo = BidRequest.newBuilder()
      .setId("1")
      .addImp(Imp.newBuilder()
          .setId("1")
          .setVideo(Video.newBuilder()
              .setLinearity(LinearityMode.LINEAR.getNumber())
              .addProtocols(AudioVideoType.VAST_3_0.getNumber())
              .setMinduration(0)
              .setMaxduration(0)
              .addAllBattr(asList(Attribute.ANNOYING.getNumber(), Attribute.POP.getNumber()))
              .addCompanionad(Banner.newBuilder()
                  .setId("1")
                  .addAllBattr(asList(Attribute.TEXT_ONLY.getNumber())))))
      .build();
  private static BidRequest requestAudio = BidRequest.newBuilder()
      .setId("1")
      .addImp(Imp.newBuilder()
          .setId("1")
          .setAudio(Audio.newBuilder()
              .addProtocols(AudioVideoType.VAST_3_0.getNumber())
              .setMinduration(0)
              .setMaxduration(0)
              .addAllBattr(asList(Attribute.ANNOYING.getNumber(), Attribute.POP.getNumber()))
              .addCompanionad(Banner.newBuilder()
                  .setId("1")
                  .addAllBattr(asList(Attribute.TEXT_ONLY.getNumber())))))
      .build();

  private MetricRegistry metricRegistry;
  private OpenRtbValidator validator;

  @Before
  public void setUp() {
    metricRegistry = new MetricRegistry();
    validator = new OpenRtbValidator(metricRegistry);
  }

  @Test
  public void testBannerGoodAttrs() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber())));
    validator.validate(requestBanner, response);
    assertThat(OpenRtbUtils.bids(response)).isNotEmpty();
  }

  @Test
  public void testBannerNoAttrs() {
    BidResponse.Builder response = testResponse(testBid());
    validator.validate(requestBanner, response);
    assertThat(OpenRtbUtils.bids(response)).isNotEmpty();
  }

  @Test
  public void testBannerBlockedAdvertiser() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber())));
    validator.validate(requestBanner, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testBannerBlockedAttribute() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber(), Attribute.ANNOYING.getNumber())));
    validator.validate(requestBanner, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testVideoAudioOk() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber())));
    validator.validate(requestVideo, response);
    assertThat(OpenRtbUtils.bids(response)).isNotEmpty();
    validator.validate(requestAudio, response);
    assertThat(OpenRtbUtils.bids(response)).isNotEmpty();
  }

  @Test
  public void testVideoBlockedAttribute() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber(), Attribute.ANNOYING.getNumber())));
    validator.validate(requestVideo, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testAudioBlockedAttribute() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber(), Attribute.ANNOYING.getNumber())));
    validator.validate(requestAudio, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testVideoCompanionBlockedAttribute() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber(), Attribute.TEXT_ONLY.getNumber())));
    validator.validate(requestVideo, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testAudioCompanionBlockedAttribute() {
    BidResponse.Builder response = testResponse(testBid()
        .addAllAdomain(asList("adv0.com", "adv2.com", "adv3.com"))
        .addAllAttr(asList(Attribute.SURVEYS.getNumber(), Attribute.TEXT_ONLY.getNumber())));
    validator.validate(requestAudio, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  @Test
  public void testNoImp() {
    BidResponse.Builder response = testResponse(testBid().setImpid("2"));
    validator.validate(requestBanner, response);
    assertThat(OpenRtbUtils.bids(response)).isEmpty();
  }

  private static BidResponse.Builder testResponse(Bid.Builder bid) {
    return BidResponse.newBuilder().addSeatbid(SeatBid.newBuilder().addBid(bid));
  }

  private static Bid.Builder testBid() {
    return Bid.newBuilder()
        .setId("1")
        .setImpid("1")
        .setPrice(100.0);
  }
}
