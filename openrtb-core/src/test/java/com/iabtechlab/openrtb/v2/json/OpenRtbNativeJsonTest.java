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

package com.iabtechlab.openrtb.v2.json;

import static com.google.common.truth.Truth.assertThat;
import static com.iabtechlab.openrtb.v2.json.OpenRtbJsonFactoryHelper.newJsonFactory;
import static java.util.Arrays.asList;

import com.iabtechlab.adcom.v1.enums.Enums.Creative.AudioVideoType;
import com.iabtechlab.adcom.v1.enums.Enums.DisplayContextType;
import com.iabtechlab.adcom.v1.enums.Enums.DisplayPlacementType;
import com.iabtechlab.adcom.v1.enums.Enums.EventTrackingMethod;
import com.iabtechlab.adcom.v1.enums.Enums.EventType;
import com.iabtechlab.adcom.v1.enums.Enums.NativeDataAssetType;
import com.iabtechlab.adcom.v1.enums.Enums.NativeImageAssetType;
import com.iabtechlab.openrtb.v2.OpenRtb.AdUnitId;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest;
import com.iabtechlab.openrtb.v2.OpenRtb.ContextType;
import com.iabtechlab.openrtb.v2.OpenRtb.LayoutId;
import com.iabtechlab.openrtb.v2.OpenRtb.NativeRequest;
import com.iabtechlab.openrtb.v2.OpenRtb.NativeResponse;
import com.iabtechlab.openrtb.v2.Test.Test1;
import com.iabtechlab.openrtb.v2.Test.Test2;
import com.iabtechlab.openrtb.v2.TestExt;
import com.iabtechlab.openrtb.v2.TestNExt;
import java.io.IOException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for {@link OpenRtbJsonFactory},
 * {@link OpenRtbNativeJsonReader}, {@link OpenRtbNativeJsonWriter}.
 */
public class OpenRtbNativeJsonTest {
  private static final Logger logger = LoggerFactory.getLogger(OpenRtbNativeJsonTest.class);
  private static final Test1 test1 = Test1.newBuilder().setTest1("test1").build();
  private static final Test2 test2 = Test2.newBuilder().setTest2("test2").build();

  @Test
  public void testRequest() throws IOException {
    testRequest(newJsonFactory(), newNativeRequest().build());
  }

  @Test
  public void testRequest_emptyMessage() throws IOException {
    testRequest(newJsonFactory(), NativeRequest.newBuilder()
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(1)
            .setTitle(NativeRequest.Asset.Title.newBuilder().setLen(100)))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(2)
            .setImg(NativeRequest.Asset.Image.newBuilder()))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(3)
            .setVideo(BidRequest.Imp.Video.newBuilder()
                .setMinduration(100)
                .setMaxduration(200)))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(4)
            .setData(NativeRequest.Asset.Data.newBuilder().setType(NativeDataAssetType.SPONSORED.getNumber())))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(5))
        .build());
    testRequest(newJsonFactory(), NativeRequest.newBuilder().build());
  }

  @Test
  public void testResponse() throws IOException {
    testResponse(newJsonFactory(), newNativeResponse().build());
  }

  @Test
  public void testResponse_emptyMessage() throws IOException {
    testResponse(newJsonFactory(), NativeResponse.newBuilder()
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(1)
            .setRequired(true)
            .setTitle(NativeResponse.Asset.Title.newBuilder().setText("title")))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(2)
            .setImg(NativeResponse.Asset.Image.newBuilder().setUrl("http://image.gif")))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(3)
            .setVideo(NativeResponse.Asset.Video.newBuilder().setVasttag("http://vast.xml")))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(4)
            .setData(NativeResponse.Asset.Data.newBuilder().setValue("v"))
            .setLink(NativeResponse.Link.newBuilder().setUrl("http://go.there.com")))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(5))
        .setLink(NativeResponse.Link.newBuilder().setUrl("http://go.there.com"))
        .build());
    testResponse(newJsonFactory(), NativeResponse.newBuilder()
        .setLink(NativeResponse.Link.newBuilder().setUrl("http://go.there.com"))
        .build());
  }

  @Test
  public void testRequest_emptyToNull() throws IOException {
    OpenRtbNativeJsonReader reader = OpenRtbJsonFactory.create().setStrict(false).newNativeReader();
    assertThat(reader.readNativeRequest("")).isNull();
    assertThat(reader.readNativeResponse("")).isNull();
  }

  static void testRequest(OpenRtbJsonFactory jsonFactory, NativeRequest req) throws IOException {
    String jsonReq = jsonFactory.newNativeWriter().writeNativeRequest(req);
    logger.info(jsonReq);
    NativeRequest req2 = jsonFactory.newNativeReader().readNativeRequest(jsonReq);
    assertThat(req2).isEqualTo(req);
  }

  static void testResponse(OpenRtbJsonFactory jsonFactory, NativeResponse resp) throws IOException {
    String jsonResp = jsonFactory.newNativeWriter().writeNativeResponse(resp);
    logger.info(jsonResp);
    NativeResponse resp2 = jsonFactory.newNativeReader().readNativeResponse(jsonResp);
    assertThat(resp2).isEqualTo(resp);
  }

  static NativeRequest.Builder newNativeRequest() {
    return NativeRequest.newBuilder()
        .setVer("1")
        .setLayout(LayoutId.APP_WALL.getNumber())
        .setAdunit(AdUnitId.PROMOTED_LISTING.getNumber())
        .setPlcmtcnt(4)
        .setSeq(5)
        .setContext(ContextType.PRODUCT.getNumber())
        .setContextsubtype(DisplayContextType.AUDIO.getNumber())
        .setPlcmttype(DisplayPlacementType.FEED_CONTENT.getNumber())
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(1)
            .setRequired(true)
            .setTitle(NativeRequest.Asset.Title.newBuilder()
                .setLen(100)
                .setExtension(TestNExt.testNReqTitle, test1))
            .setExtension(TestNExt.testNReqAsset, test1))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(2)
            .setImg(NativeRequest.Asset.Image.newBuilder()
                .setType(NativeImageAssetType.ICON.getNumber())
                .setW(2)
                .setWmin(2)
                .setH(3)
                .setHmin(4)
                .addAllMimes(asList("a", "b"))
                .setExtension(TestNExt.testNReqImage, test1)))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(3)
            .setVideo(BidRequest.Imp.Video.newBuilder()
                .addAllMimes(asList("a", "b"))
                .setMinduration(100)
                .setMaxduration(200)
                .addProtocols(AudioVideoType.VAST_3_0.getNumber())
                .setExtension(TestExt.testVideo, test1)))
        .addAssets(NativeRequest.Asset.newBuilder()
            .setId(4)
            .setData(NativeRequest.Asset.Data.newBuilder()
                .setType(NativeDataAssetType.SPONSORED.getNumber())
                .setLen(10)
                .setExtension(TestNExt.testNReqData, test1)))
        .setAurlsupport(true)
        .setDurlsupport(true)
        .setPrivacy(true)
        .addEventtrackers(NativeRequest.EventTrackers.newBuilder()
            .setEvent(EventType.IMPRESSION.getNumber())
            .addAllMethods(asList(EventTrackingMethod.IMAGE_PIXEL.getNumber(), EventTrackingMethod.JAVASCRIPT.getNumber())))
        .setExtension(TestNExt.testNRequest1, test1)
        .setExtension(TestNExt.testNRequest2, test2);
  }

  static NativeResponse.Builder newNativeResponse() {
    return NativeResponse.newBuilder()
        .setVer("1")
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(1)
            .setRequired(true)
            .setTitle(NativeResponse.Asset.Title.newBuilder()
                .setText("title")
                .setLen(10)
                .setExtension(TestNExt.testNRespTitle, test1))
            .setLink(NativeResponse.Link.newBuilder()
                .setUrl("url")
                .addAllClicktrackers(asList("a", "b"))
                .setFallback("f")
                .setExtension(TestNExt.testNRespLink, test1))
            .setExtension(TestNExt.testNRespAsset, test1))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(2)
            .setImg(NativeResponse.Asset.Image.newBuilder()
                .setUrl("url")
                .setType(NativeImageAssetType.MAIN.getNumber())
                .setW(2)
                .setH(3)
                .setExtension(TestNExt.testNRespImage, test1)))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(2)
            .setVideo(NativeResponse.Asset.Video.newBuilder()
                .setVasttag("vast")
                .setExtension(TestNExt.testNRespVideo, test1)))
        .addAssets(NativeResponse.Asset.newBuilder()
            .setId(2)
            .setData(NativeResponse.Asset.Data.newBuilder()
                .setType(NativeDataAssetType.CTA_TEXT.getNumber())
                .setLen(15)
                .setLabel("l")
                .setValue("v")
                .setExtension(TestNExt.testNRespData, test1)))
        .setLink(NativeResponse.Link.newBuilder()
            .setUrl("url")
            .addAllClicktrackers(asList("a", "b"))
            .setFallback("f")
            .setExtension(TestNExt.testNRespLink, test1))
        .addAllImptrackers(asList("a"))
        .setJstracker("b")
        .setAssetsurl("http://assets.com")
        .setDcourl("http://dyncontent.com")
        .setPrivacy("http://privacy.com")
        .addEventtrackers(NativeResponse.EventTracker.newBuilder()
            .setEvent(EventType.VIEWABLE_MRC50.getNumber())
            .setMethod(EventTrackingMethod.IMAGE_PIXEL.getNumber())
            .setUrl("http://bidder.com/impression"))
    .setExtension(TestNExt.testNResponse1, test1)
    .setExtension(TestNExt.testNResponse2, test2);
  }
}
