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

package com.yalin.fidoclient.net;


/**
 * Created by 雅麟 on 2015/6/19.
 */
public enum ErrorCodeConstants {
    Success(1200, "success"),
    NetworkError(7777, "network error"),
    ServerError(8888, "server error"),
    UnknownError(9999, "unknown");

    public final int number;
    public final String msg;

    ErrorCodeConstants(int number, String msg) {
        this.number = number;
        this.msg = msg;
    }

    public static ErrorCodeConstants getValue(int error) {
        for (ErrorCodeConstants item : values()) {
            if (error == item.number) {
                return item;
            }
        }
        return UnknownError;
    }
}
