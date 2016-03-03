package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class AuthenticationRequest {
	public OperationHeader header;
	public String challenge;
	public Transaction[] transaction;
	public Policy policy;
}
