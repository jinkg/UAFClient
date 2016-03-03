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

package com.yalin.fidoclient.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.yalin.fidoclient.R;
import com.yalin.fidoclient.api.UAFClientError;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMIntent;
import com.yalin.fidoclient.msg.AsmInfo;
import com.yalin.fidoclient.ui.fragment.AsmListFragment;
import com.yalin.fidoclient.ui.fragment.AuthenticatorListFragment;
import com.yalin.fidoclient.utils.StatLog;

import java.util.List;


/**
 * Created by YaLin on 2015/10/21.
 */
public class UAFClientActivity extends AppCompatActivity implements AsmListFragment.AsmItemPickListener {
    private static final String TAG = UAFClientActivity.class.getSimpleName();

    private static final String ASM_INFO_SP = "asm_pack";
    private static final String ASM_PACK_KEY = "asm_pack_key";
    private static final String ASM_APP_NAME_KEY = "asm_app_name_key";

    private String intentType;
    private String message;
    private String channelBinding;

    private int callerUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fido_client);
        Bundle extras = this.getIntent().getExtras();
        intentType = extras.getString(UAFIntent.UAF_INTENT_TYPE_KEY);
        message = extras.getString(UAFIntent.MESSAGE_KEY);
        channelBinding = extras.getString(UAFIntent.CHANNEL_BINDINGS_KEY);
        callerUid = getUid();
        StatLog.printLog(TAG, "onCreate intentType:" + intentType + " message:" + message + " channelBinding:" + channelBinding);

        if (TextUtils.isEmpty(getAsmPack(getApplicationContext()))) {
            choseAsm();
        } else {
            showAuthenticator();
        }
    }

    private void choseAsm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, AsmListFragment.getInstance(this))
                .commit();
    }

    private void showAuthenticator() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_content, AuthenticatorListFragment.getInstance(intentType, message, channelBinding, callerUid))
                .commit();
    }

    private int getUid() {
        final PackageManager pm = getPackageManager();
        try {
            return pm.getActivityInfo(getCallingActivity(), PackageManager.GET_INTENT_FILTERS).applicationInfo.uid;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public void onBackPressed() {
        finishWithError(UAFClientError.USER_CANCELLED);
    }

    private void setFailedIntent(short errorCode) {
        Intent intent = UAFIntent.getUAFOperationErrorIntent(getComponentName().flattenToString(), errorCode);
        setResult(Activity.RESULT_CANCELED, intent);
    }

    private void finishWithError(short errorCode) {
        setFailedIntent(errorCode);
        finish();
    }

    @Override
    public void onAsmItemPick(AsmInfo info) {
        setAsmInfo(getApplicationContext(), info);
        showAuthenticator();
    }

    public static String getAsmPack(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                ASM_INFO_SP, Context.MODE_PRIVATE);

        return sp.getString(ASM_PACK_KEY, null);
    }

    public static AsmInfo getAsmInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                ASM_INFO_SP, Context.MODE_PRIVATE);
        String name = sp.getString(ASM_APP_NAME_KEY, null);
        String pack = sp.getString(ASM_PACK_KEY, null);
        ResolveInfo resolveInfo = resolve(context, pack);
        AsmInfo info = new AsmInfo();
        info.appName(name)
                .pack(pack);
        if (resolveInfo != null) {
            info.icon = resolveInfo.loadIcon(context.getPackageManager());
        }

        return info;
    }

    public static void setAsmInfo(Context context, AsmInfo info) {
        if (info == null) {
            info = new AsmInfo();
        }
        SharedPreferences sp = context.getSharedPreferences(
                ASM_INFO_SP, Context.MODE_PRIVATE);
        sp.edit().putString(ASM_PACK_KEY, info.pack)
                .putString(ASM_APP_NAME_KEY, info.appName)
                .apply();
    }

    private static ResolveInfo resolve(Context context, String pack) {
        if (TextUtils.isEmpty(pack)) {
            return null;
        }
        Intent intent = ASMIntent.getASMIntent();
        intent.setPackage(pack);

        List<ResolveInfo> infos = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        if (infos != null && infos.size() > 0) {
            return infos.get(0);
        } else {
            return null;
        }
    }
}
