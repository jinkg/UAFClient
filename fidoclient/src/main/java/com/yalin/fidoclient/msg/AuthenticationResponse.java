package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class AuthenticationResponse {
	public OperationHeader header;
	public String fcParams;
	public AuthenticatorSignAssertion[] assertions;
}
