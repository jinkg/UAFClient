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

/**
 * Created by YaLin on 2016/1/13.
 */
public interface UAFClientError {
    short NO_ERROR = 0x0;
    short WAIT_USER_ACTION = 0x1;
    short INSECURE_TRANSPORT = 0x2;
    short USER_CANCELLED = 0x3;
    short UNSUPPORTED_VERSION = 0x4;
    short NO_SUITABLE_AUTHENTICATOR = 0x5;
    short PROTOCOL_ERROR = 0x6;
    short UNTRUSTED_FACET_ID = 0x7;
    short UNKNOWN = 0xFF;
}
