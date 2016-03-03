package com.yalin.fidoclient.msg;

import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;

import java.util.Arrays;

/**
 * Created by YaLin on 2016/1/21.
 */
public class MatchCriteria {
    public String[] aaid;
    //public String[] vendorID;
    public String[] keyIDs;
    //	public long userVerification;
//	public int keyProtection;
    //public int matcherProtection;
    public long attachmentHint;
    //public int tcDisplay;
    //public int[] authenticationAlgorithms;
    public String[] assertionSchemes;
    //public int[] attestationTypes;
    public int authenticatorVersion;
    public Extension[] exts;

    public boolean isMatch(AuthenticatorInfo info) {
        boolean match = false;
        if (aaid != null) {
            if (!Arrays.asList(aaid).contains(info.aaid)) {
                return false;
            }
            match = true;
        }
        if (match) {
            if (assertionSchemes != null) {
                for (String assertionScheme : assertionSchemes) {
                    if (!assertionScheme.equals(info.assertionScheme)) {
                        return false;
                    }
                }
            }
        }
        return match;
    }
}
