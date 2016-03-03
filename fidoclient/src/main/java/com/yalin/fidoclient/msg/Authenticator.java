package com.yalin.fidoclient.msg;

import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;

/**
 * Created by YaLin on 2016/1/21.
 */
public class Authenticator {
    public String title;
    public String aaid;
    public String description;
    public Version[] supportedUAFVersions;
    public String assertionScheme;
    public int authenticationAlgorithm;
    public int[] attestationTypes;
    public long userVerification;
    public int keyProtection;
    public int matcherProtection;
    public long attachmentHint;
    public boolean isSecondFactorOnly;
    public int tcDisplay;
    public String tcDisplayContentType;
    public DisplayPNGCharacteristicsDescriptor[] tcDisplayPNGCharacteristics;
    public String icon;
    public String[] supportedExtensionIDs;

    public static Authenticator[] fromInfo(AuthenticatorInfo[] infos) {
        if (infos == null) {
            return null;
        }
        Authenticator[] authenticators = new Authenticator[infos.length];
        for (int i = 0; i < infos.length; i++) {
            authenticators[i] = new Authenticator();
            authenticators[i].aaid = infos[i].aaid;
            authenticators[i].assertionScheme = infos[i].assertionScheme;
            authenticators[i].title = infos[i].title;
            authenticators[i].description = infos[i].description;
            authenticators[i].authenticationAlgorithm = infos[i].authenticationAlgorithm;
            authenticators[i].attestationTypes = infos[i].attestationTypes;
            authenticators[i].userVerification = infos[i].userVerification;
            authenticators[i].keyProtection = infos[i].keyProtection;
            authenticators[i].matcherProtection = infos[i].matcherProtection;
            authenticators[i].attachmentHint = infos[i].attachmentHint;
            authenticators[i].isSecondFactorOnly = infos[i].isSecondFactorOnly;
            authenticators[i].tcDisplay = infos[i].tcDisplay;
            authenticators[i].tcDisplayContentType = infos[i].tcDisplayContentType;
            authenticators[i].tcDisplayPNGCharacteristics = infos[i].tcDisplayPNGCharacteristics;
            authenticators[i].icon = infos[i].icon;
            authenticators[i].supportedExtensionIDs = infos[i].supportedExtensionIDs;

            authenticators[i].supportedUAFVersions = new Version[1];
            authenticators[i].supportedUAFVersions[0] = new Version(1, 0);
        }
        return authenticators;
    }

}
