package com.yalin.fidoclient.msg.client;

/**
 * Created by YaLin on 2016/1/21.
 */
public enum UAFIntentType {
    DISCOVER,
    DISCOVER_RESULT,

    //Perform a no-op check if a message could be processed.
    CHECK_POLICY,

    //Check Policy results.
    CHECK_POLICY_RESULT,

    //Process a Registration, Authentication, Transaction Confirmation or Deregistration message.
    UAF_OPERATION,

    //UAF Operation results.
    UAF_OPERATION_RESULT,

    //Inform the FIDO UAF Client of the completion status of a Registration, Authentication,
    // Transaction Confirmation or Deregistration message.
    UAF_OPERATION_COMPLETION_STATUS


}
