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
