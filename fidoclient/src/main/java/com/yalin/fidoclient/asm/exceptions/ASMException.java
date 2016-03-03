package com.yalin.fidoclient.asm.exceptions;

/**
 * Created by YaLin on 2016/1/13.
 */
public class ASMException extends Exception {
    public short statusCode;

    public ASMException(short statusCode) {
        this.statusCode = statusCode;
    }
}
