package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class RegistrationRequest {
	public OperationHeader header;
	public String challenge;
	public String username;
	public Policy policy;
}
