package com.yalin.fidoclient.tlv;

/**
 * Created by YaLin on 2016/1/21.
 */
public enum TagsEnum {
    UAF_CMD_STATUS_ERR_UNKNOWN(0x01),
    TAG_UAFV1_REG_ASSERTION(0x3E01),
    TAG_UAFV1_AUTH_ASSERTION(0x3E02),
    TAG_UAFV1_KRD(0x3E03),
    TAG_UAFV1_SIGNED_DATA(0x3E04),
    TAG_ATTESTATION_CERT(0x2E05),
    TAG_SIGNATURE(0x2E06),
    TAG_ATTESTATION_BASIC_FULL(0x3E07),
    TAG_ATTESTATION_BASIC_SURROGATE(0x3E08),
    TAG_KEYID(0x2E09),
    TAG_FINAL_CHALLENGE(0x2E0A),
    TAG_AAID(0x2E0B),
    TAG_PUB_KEY(0x2E0C),
    TAG_COUNTERS(0x2E0D),
    TAG_ASSERTION_INFO(0x2E0E),
    TAG_AUTHENTICATOR_NONCE(0x2E0F),
    TAG_TRANSACTION_CONTENT_HASH(0x2E10),
    TAG_EXTENSION(0x3E11),
    TAG_EXTENSION_NON_CRITICAL(0x3E12),
    TAG_EXTENSION_ID(0x2E13),
    TAG_EXTENSION_DATA(0x2E14);

    final public int id;

    TagsEnum(int id) {
        this.id = id;
    }

    public static TagsEnum get(int id) {
        for (TagsEnum tag : TagsEnum.values()) {
            if (tag.id == id) {
                return tag;
            }
        }
        return null;
    }
}
