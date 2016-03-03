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

package com.yalin.fidoclient.constants;

/**
 * Created by YaLin on 2015/10/21.
 */
public interface Constants {
    String ACTION_FIDO_OPERATION = "org.fidoalliance.intent.FIDO_OPERATION";

    String PERMISSION_FIDO_CLIENT = "org.fidoalliance.uaf.permissions.FIDO_CLIENT";

    String FIDO_CLIENT_INTENT_MIME = "application/fido.uaf_client+json";
    String FIDO_ASM_INTENT_MIME = "application/fido.uaf_asm+json";

    int CHALLENGE_MAX_LEN = 64;
    int CHALLENGE_MIN_LEN = 8;

    int USERNAME_MAX_LEN = 128;

    int APP_ID_MAX_LEN = 512;

    int SERVER_DATA_MAX_LEN = 1536;

    int KEY_ID_MAX_LEN = 2048;
    int KEY_ID_MIN_LEN = 32;

    String APP_ID_PREFIX = "https://";

    String BASE64_REGULAR = "^[a-zA-Z0-9-_]+={0,2}$";
}
