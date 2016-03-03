package com.yalin.fidoclient.constants;

/**
 * Created by YaLin on 2015/10/21.
 */
public interface Constants {
    String ACTION_FIDO_OPERATION = "org.fidoalliance.intent.FIDO_OPERATION";

    String PERMISSION_FIDO_CLIENT = "org.fidoalliance.uaf.permissions.FIDO_CLIENT";

    String FIDO_CLIENT_INTENT_MIME = "application/fido.uaf_client+json";
    String FIDO_ASM_INTENT_MIME = "application/fido.uaf_asm+json";

    int CHALLENGE_MAX_LEN = 64;
    int CHALLENGE_MIN_LEN = 8;

    int USERNAME_MAX_LEN = 128;

    int APP_ID_MAX_LEN = 512;

    int SERVER_DATA_MAX_LEN = 1536;

    int KEY_ID_MAX_LEN = 2048;
    int KEY_ID_MIN_LEN = 32;

    String APP_ID_PREFIX = "https://";

    String BASE64_REGULAR = "^[a-zA-Z0-9-_]+={0,2}$";
}
