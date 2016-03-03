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
