package com.yalin.fidoclient.asm.api;

/**
 * Created by YaLin on 2016/1/13.
 */
public interface StatusCode {
    short UAF_ASM_STATUS_OK = 0x00;
    short UAF_ASM_STATUS_ERROR = 0x01;
    short UAF_ASM_STATUS_ACCESS_DENIED = 0x02;
    short UAF_ASM_STATUS_USER_CANCELLED = 0x03;
}
