package com.yalin.fidoclient.api;

import android.content.Intent;
import android.os.Bundle;

import com.yalin.fidoclient.constants.Constants;
import com.yalin.fidoclient.msg.client.UAFIntentType;

/**
 * Created by YaLin on 2016/1/13.
 */
public class UAFIntent {
    public static final String UAF_INTENT_TYPE_KEY = "UAFIntentType";
    public static final String DISCOVERY_DATA_KEY = "discoveryData";
    public static final String COMPONENT_NAME_KEY = "componentName";
    public static final String ERROR_CODE_KEY = "errorCode";
    public static final String MESSAGE_KEY = "message";
    public static final String ORIGIN_KEY = "origin";
    public static final String CHANNEL_BINDINGS_KEY = "channelBindings";
    public static final String RESPONSE_CODE_KEY = "responseCode";

    /**
     * This Android intent invokes the FIDO UAF Client to discover the available authenticators and capabilties.
     * The FIDO UAF Client generally will not show a UI associated with the handling of this intent,
     * but immediately return the JSON structure. The calling application cannot depend on this however,
     * as the FIDO UAF Client may show a UI for privacy purposes,
     * allowing the user to choose whether and which authenticators to disclose to the calling application.
     * <p/>
     * This intent must be invoked with startActivityForResult().
     *
     * @return
     */
    public static Intent getDiscoverIntent() {
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_CLIENT_INTENT_MIME);

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.DISCOVER.name());
        intent.putExtras(bundle);

        return intent;
    }


    /**
     * An intent with this type is returned by the FIDO UAF Client as an argument to onActivityResult()
     * in response to receiving an intent of type DISCOVER.
     * If the resultCode passed to onActivityResult() is RESULT_OK,
     * and the intent extra errorCode is NO_ERROR, this intent has an extra, discoveryData,
     * containing a String representation of a DiscoveryData JSON dictionary with the available authenticators and capabilities.
     *
     * @param discoveryData
     * @param componentName
     * @param errorCode
     * @return
     */
    public static Intent getDiscoverResultIntent(String discoveryData, String componentName, short errorCode) {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.DISCOVER_RESULT.name());
        bundle.putString(DISCOVERY_DATA_KEY, discoveryData);
        bundle.putString(COMPONENT_NAME_KEY, componentName);
        bundle.putShort(ERROR_CODE_KEY, errorCode);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * This intent invokes the FIDO UAF Client to discover if it would be able to process the supplied message
     * without prompting the user. The action handling this intent should not show a UI to the user.
     * <p/>
     * This intent must be invoked with startActivityForResult().
     * <p/>
     * This intent requires the following extras:
     *
     * @param message containing a String representation of a UAFMessage representing the request message to test.
     * @param origin  an optional extra that allows a caller with the
     *                org.fidoalliance.uaf.permissions.ACT_AS_WEB_BROWSER permission to supply
     *                an RFC6454 Origin [RFC6454] string to be used instead of the application's own identity.
     * @return
     */
    public static Intent getCheckPolicyIntent(String message, String origin) {
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_CLIENT_INTENT_MIME);

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.CHECK_POLICY.name());
        bundle.putString(MESSAGE_KEY, message);
        bundle.putString(ORIGIN_KEY, origin);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * This Android intent is returned by the FIDO UAF Client as an argument to onActivityResult()
     * in response to receiving a CHECK_POLICY intent.
     * In addition to the resultCode passed to onActivityResult(),
     * this intent has an extra,
     *
     * @param componentName
     * @param errorCode     containing an ErrorCode value indicating the specific error condition
     *                      or NO_ERROR if the FIDO UAF Client could process the message.
     * @return
     */
    public static Intent getCheckPolicyResultIntent(String componentName, short errorCode) {
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.CHECK_POLICY_RESULT.name());
        bundle.putString(COMPONENT_NAME_KEY, componentName);
        bundle.putShort(ERROR_CODE_KEY, errorCode);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * This Android intent invokes the FIDO UAF Client to process the supplied request message
     * and return a response message ready for delivery to the FIDO UAF Server.
     * The sender should assume that the FIDO UAF Client will display a user interface
     * allowing the user to prepare this intent, for example, prompting the user to complete their verification ceremony.
     * <p/>
     * This intent must be invoked with startActivityForResult().
     * <p/>
     * This intent requires the following extras:
     *
     * @param uafMessage      containing a String representation of a UAFMessage representing the request message to process.
     * @param origin          an optional parameter that allows a caller with the
     *                        org.fidoalliance.uaf.permissions.ACT_AS_WEB_BROWSER permission to supply
     *                        an RFC6454 Origin [RFC6454] string to be used instead of the application's own identity.
     * @param channelBindings containing a String representation of a JSON dictionary as defined
     *                        by the ChannelBinding structure in the FIDO UAF Protocol Specification [UAFProtocol].
     * @return
     */
    public static Intent getUAFOperationIntent(String uafMessage, String origin, String channelBindings) {
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_CLIENT_INTENT_MIME);

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.UAF_OPERATION.name());
        bundle.putString(MESSAGE_KEY, uafMessage);
        bundle.putString(ORIGIN_KEY, origin);
        bundle.putString(CHANNEL_BINDINGS_KEY, channelBindings);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * This intent is returned by the FIDO UAF Client as an argument to onActivityResult(),
     * in response to receiving a UAF_OPERATION intent.
     * If the resultCode passed to onActivityResult() is RESULT_CANCELLED,
     * If the resultCode passed to onActivityResult() is RESULT_OK, and the errorCode is NO_ERROR,
     *
     * @param componentName componentName
     * @param uafMessage    containing a String representation of a UAFMessage,
     *                      being the UAF protocol response message to be delivered to the FIDO Server.
     * @return intent
     */
    public static Intent getUAFOperationResultIntent(String componentName, String uafMessage) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.UAF_OPERATION_RESULT.name());
        bundle.putString(COMPONENT_NAME_KEY, componentName);
        bundle.putShort(ERROR_CODE_KEY, UAFClientError.NO_ERROR);
        bundle.putString(MESSAGE_KEY, uafMessage);
        intent.putExtras(bundle);

        return intent;
    }

    public static Intent getUAFOperationErrorIntent(String componentName, short errorCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.UAF_OPERATION_RESULT.name());
        bundle.putString(COMPONENT_NAME_KEY, componentName);
        bundle.putShort(ERROR_CODE_KEY, errorCode);
        intent.putExtras(bundle);

        return intent;
    }

    /**
     * This intent must be delivered to the FIDO UAF Client to indicate
     * the processing status of a FIDO UAF message delivered to the remote server.
     * This is especially important as a new registration may be considered by the client
     * to be in a pending state until it is communicated that the server accepted it.
     *
     * @param message
     * @param responseCode
     * @return
     */
    public static Intent getUAFOperationCompletionStatusIntent(String message, short responseCode) {
        Intent intent = new Intent(Constants.ACTION_FIDO_OPERATION);
        intent.setType(Constants.FIDO_CLIENT_INTENT_MIME);

        Bundle bundle = new Bundle();
        bundle.putString(UAF_INTENT_TYPE_KEY, UAFIntentType.UAF_OPERATION_COMPLETION_STATUS.name());
        bundle.putString(MESSAGE_KEY, message);
        bundle.putShort(RESPONSE_CODE_KEY, responseCode);
        intent.putExtras(bundle);

        return intent;
    }
}
