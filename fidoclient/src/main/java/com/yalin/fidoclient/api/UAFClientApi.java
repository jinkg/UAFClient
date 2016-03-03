/*
 * Copyright 2016 YaLin Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yalin.fidoclient.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.yalin.fidoclient.msg.AsmInfo;
import com.yalin.fidoclient.msg.DeregisterAuthenticator;
import com.yalin.fidoclient.msg.DeregistrationRequest;
import com.yalin.fidoclient.msg.RegRecord;
import com.yalin.fidoclient.msg.client.UAFMessage;
import com.yalin.fidoclient.ui.UAFClientActivity;
import com.yalin.fidoclient.utils.Utils;

import java.util.List;

/**
 * Created by YaLin on 2016/1/11.
 */
public class UAFClientApi {
    public static void doDiscover(Activity activity, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getDiscoverIntent();
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doCheckPolicy(Activity activity, int requestCode, String responseMessage) {
        if (activity == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getCheckPolicyIntent(new UAFMessage(responseMessage).toJson(), activity.getApplication().getPackageName());
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doOperation(Activity activity, int requestCode, String responseMessage, String channelBinding) {
        if (activity == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getUAFOperationIntent(new UAFMessage(responseMessage).toJson(), null, channelBinding);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void doOperation(Fragment fragment, int requestCode, String responseMessage, String channelBinding) {
        if (fragment == null || TextUtils.isEmpty(responseMessage)) {
            throw new IllegalArgumentException();
        }
        Intent intent = UAFIntent.getUAFOperationIntent(new UAFMessage(responseMessage).toJson(), null, channelBinding);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static List<RegRecord> getRegRecords(String username) {
        if (TextUtils.isEmpty(username)) {
            throw new IllegalArgumentException();
        }
        return null;
    }

    public static DeregistrationRequest[] getDeRegistrationRequests(RegRecord regRecord) {
        if (regRecord == null) {
            throw new IllegalArgumentException("regRecord must not be null!");
        }
        DeregistrationRequest[] deRegistrationRequests = new DeregistrationRequest[1];
        deRegistrationRequests[0] = new DeregistrationRequest();
        deRegistrationRequests[0].authenticators = new DeregisterAuthenticator[1];
        deRegistrationRequests[0].authenticators[0] = new DeregisterAuthenticator();
        deRegistrationRequests[0].authenticators[0].aaid = regRecord.aaid;
        deRegistrationRequests[0].authenticators[0].keyID = regRecord.keyId;

        return deRegistrationRequests;
    }

    public static AsmInfo getDefaultAsmInfo(Context context) {
        return UAFClientActivity.getAsmInfo(context);
    }

    public static void clearDefaultAsm(Context context) {
        UAFClientActivity.setAsmInfo(context, null);
    }
}
