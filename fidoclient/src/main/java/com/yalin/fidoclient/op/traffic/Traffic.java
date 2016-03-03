package com.yalin.fidoclient.op.traffic;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Traffic {
    public enum OpStat {
        PREPARE,
        GET_INFO_PENDING,
        REG_PENDING,
        AUTH_PENDING,
        DEREG_PENDING,

        PICK_AUTHENTICATOR_PENDING,
    }
}
