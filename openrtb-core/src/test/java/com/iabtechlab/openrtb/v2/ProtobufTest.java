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

package com.iabtechlab.openrtb.v2;

import static java.util.Arrays.asList;

import com.iabtechlab.adcom.v1.enums.Enums.Creative.Attribute;
import com.iabtechlab.adcom.v1.enums.Enums.PlacementPosition;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Content;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Device;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Imp.Banner;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Publisher;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.Site;
import com.iabtechlab.openrtb.v2.OpenRtb.BidRequest.User;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse.SeatBid;
import com.iabtechlab.openrtb.v2.OpenRtb.BidResponse.SeatBid.Bid;
import com.iabtechlab.openrtb.v3.Enums.AuctionType;
import org.junit.Test;

/**
 * Implements some examples from the OpenRTB spec.
 */
public class ProtobufTest {

  @Test
  public void testRequest_5_1_1() {
    BidRequest.newBuilder()
        .setId("1234534625254")
        .setAt(AuctionType.SECOND_PRICE_PLUS.getNumber())
        .setTmax(120)
        .addImp(Imp.newBuilder()
            .setId("1")
            .setBanner(Banner.newBuilder()
                .setW(300)
                .setH(250)
                .setPos(PlacementPosition.ATF.getNumber())
                .addBattr(Attribute.USER_INTERACTIVE.getNumber())))
         .addBadv("company1.com")
         .addBadv("company2.com")
         .setSite(Site.newBuilder()
             .setId("234563")
             .setName("Site ABCD")
             .setDomain("siteabcd.com")
             .addAllCat(asList("IAB2-1", "IAB2-2"))
             .setPrivacypolicy(true)
             .setPage("http://siteabcd.com/page.htm")
             .setRef("http://referringsite.com/referringpage.htm")
             .setPublisher(Publisher.newBuilder()
                 .setId("pub12345")
                 .setName("Publisher A"))
             .setContent(Content.newBuilder()
                 .setKeywords("keyword a,keyword b,keyword c")))
         .setDevice(Device.newBuilder()
             .setIp("64.124.253.1")
             .setUa("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; en-US; rv:1.9.2.16)")
             .setOs("OS X")
             .setFlashver("10.1")
             .setJs(true))
         .setUser(User.newBuilder()
             .setId("45asdf987656789adfad4678rew656789")
             .setBuyeruid("5df678asd8987656asdf78987654"))
     .build();
  }

  @Test
  public void testResponse_5_2_1() {
    BidResponse.newBuilder()
        .setId("1234567890")
        .addSeatbid(SeatBid.newBuilder()
            .addBid(Bid.newBuilder()
                .setId("1")
                .setImpid("102")
                .setPrice(9.43)
                .setAdid("314")
                .setNurl("http://adserver.com/winnotice?impid=102")
                .setAdm(
                      "%3C!DOCTYPE%20html%20PUBLIC%20%5C%22-"
                    + "%2F%2FW3C%2F%2FDTD%20XHTML%201.0%20Transitional%2F%2FEN%5C%22%20%5C%22htt"
                    + "p%3A%2F%2Fwww.w3.org%2FTR%2Fxhtml1%2FDTD%2Fxhtml1-"
                    + "transitional.dtd%5C%22%3E%3Chtml%20xmlns%3D%5C%22http%3A%2F%2Fwww.w3.org%2F1"
                    + "999%2Fxhtml%5C%22%20xml%3Alang%3D%5C%22en%5C%22%20lang%3D%5C%22en%5C%22"
                    + "%3E...%3C%2Fhtml%3E")
                .addAdomain("advertiserdomain.com")
                .setIurl("http://adserver.com/pathtosampleimage")
                .setCid("campaign111")
                .setCrid("creative112")
                .addAllAttr(asList(Attribute.ANNOYING.getNumber(), Attribute.AUDIO_AUTOPLAY.getNumber())))
            .setSeat("512"))
        .setBidid("abc1123")
        .setCur("USD")
        .build();
  }
}
