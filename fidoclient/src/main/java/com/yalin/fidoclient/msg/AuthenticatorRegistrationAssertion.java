package com.yalin.fidoclient.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class AuthenticatorRegistrationAssertion {
	public String assertionScheme;
	public String assertion;
	public DisplayPNGCharacteristicsDescriptor[] tcDisplayPNGCharacteristics;
	public Extension[] exts;
}
