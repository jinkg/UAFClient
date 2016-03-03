package com.yalin.fidoclient.asm.api;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;


/**
 * Created by YaLin on 2016/1/13.
 */
public class ASMApi {
    public static void doOperation(Fragment fragment, int requestCode, String asmMessage, String pack) {
        if (TextUtils.isEmpty(asmMessage)) {
            throw new IllegalArgumentException("asmMessage can not be null");
        }
        Intent intent = ASMIntent.getASMOperationIntent(asmMessage);
        if (!TextUtils.isEmpty(pack)) {
            intent.setPackage(pack);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void doDiscover(Fragment fragment, int requestCode, String asmMessage) {
        if (TextUtils.isEmpty(asmMessage)) {
            throw new IllegalArgumentException("asmMessage can not be null");
        }
        Intent intent = ASMIntent.getASMOperationIntent(asmMessage);
        fragment.startActivityForResult(intent, requestCode);
    }
}
