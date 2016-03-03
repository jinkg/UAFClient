package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class DeregistrationRequest {
	public OperationHeader header;
	public DeregisterAuthenticator[] authenticators;
}
