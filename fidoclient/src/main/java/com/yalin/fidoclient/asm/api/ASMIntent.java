package com.yalin.fidoclient.asm.api;

import android.content.Intent;
import android.os.Bundle;

import com.yalin.fidoclient.constants.Constants;

/**
 * Created by YaLin on 2016/1/13.
 */
public class ASMIntent {
    public static final String MESSAGE_KEY = "message";

    public static Intent getASMIntent(){
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_ASM_INTENT_MIME);

        return intent;
    }

    public static Intent getASMOperationIntent(String asmRequest) {
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_ASM_INTENT_MIME);

        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_KEY, asmRequest);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getASMOperationResultIntent(String asmResponse) {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE_KEY, asmResponse);
        intent.putExtras(bundle);
        return intent;
    }
}
