package com.yalin.fidoclient;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.google.gson.Gson;
import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.net.response.FacetIdListResponse;
import com.yalin.fidoclient.op.ASMMessageHandler;
import com.yalin.fidoclient.op.Completion;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testBase64Encoded() {
        String base64String = "VkNhalNrYWVsajhHclZJdG5hMkF5eXRIMVlrQ3pBb3Z6T1ZHblRNTg";
        String base64String1 = base64String.trim();
        assertTrue(base64String1.matches(Constants.BASE64_REGULAR));
    }

    public void testFacetIdMatch() {
        String facetIdListJson = "{\"trustedFacets\":[{\"version\":{\"major\":1,\"minor\":0},\"ids\":[\"android:apk-key-hash:q/e9IaNQq0aVXc8lllq7i/AsAX0\",\"android:apk-key-hash:fX7gLfYPhfPY74RGgK0NYG6Qn8Y\",\"android:apk-key-hash:Tr3wU3S3ZzXhrdXsQY8ykrKPYok\",\"android:apk-key-hash:EVXld/jIwGnA0Lpa+HZ+r+095m4\",\"android:apk-key-hash:LGVXnmy3laAWV/WZJ7PBiA9ASkg\",\"ios:bundle-id:com.raonsecure.fido.rp1\",\"android:apk-key-hash:Df+2X53Z0UscvUu6obxC3rIfFyk\",\"android:apk-key-hash:/G50F1N5KDg/rxYX1gYwLlp7FvM\",\"ios:bundle-id:com.kt.fidocert\",\"android:apk-key-hash:NVvf2qB+6Pr/9cLsS35pOpv67As\",\"ios:bundle-id:com.raonsecure.fidocert\",\"android:apk-key-hash:Y9a2IZqAfkqQtyVXx7p5sFr4RCc\",\"android:apk-key-hash:NVvf2qB+6Pr/9cLsS35pOpv67As\",\"android:apk-key-hash:f+jGL/M5P0cm1tqfxD05s/851eE\",\"android:apk-key-hash:/M90Btryt52setdCdLo1ih96MXg\",\"ios:bundle-id:com.crosscert.fidorpc\",\"android:apk-key-hash:AwHDCRdzkMAsOa7aDPbnG2J95Is\",\"ios:bundle-id:com.crosscert.fidorpc\"]}]}";
        String facetId = "android:apk-key-hash:q/e9IaNQq0aVXc8lllq7i/AsAX0";
        String facetId1 = "android:apk-key-hash:SvYZ4Sgas9T2+6DpNj566iscuns";
        FacetIdListResponse facetIdListResponse = new Gson().fromJson(facetIdListJson, FacetIdListResponse.class);
        boolean match = ASMMessageHandler.checkFacetId(facetId, facetIdListResponse.trustedFacets);
        boolean match1 = ASMMessageHandler.checkFacetId(facetId1, facetIdListResponse.trustedFacets);
        assertTrue(match);
        assertTrue(!match1);
    }
}