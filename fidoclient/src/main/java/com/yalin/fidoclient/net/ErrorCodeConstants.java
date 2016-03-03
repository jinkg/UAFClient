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
