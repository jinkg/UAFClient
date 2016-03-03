package com.yalin.fidoclient.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jin.uitoolkit.fragment.BaseLoadingFragment;
import com.yalin.fidoclient.R;
import com.yalin.fidoclient.api.UAFIntent;
import com.yalin.fidoclient.asm.api.ASMIntent;
import com.yalin.fidoclient.asm.exceptions.ASMException;
import com.yalin.fidoclient.asm.msg.obj.AuthenticatorInfo;
import com.yalin.fidoclient.msg.client.UAFMessage;
import com.yalin.fidoclient.op.ASMMessageHandler;
import com.yalin.fidoclient.op.traffic.Traffic;
import com.yalin.fidoclient.ui.AuthenticatorAdapter;
import com.yalin.fidoclient.ui.UAFClientActivity;
import com.yalin.fidoclient.utils.StatLog;

import java.util.List;

/**
 * Created by YaLin on 2016/2/26.
 */
public class AuthenticatorListFragment extends BaseLoadingFragment implements ASMMessageHandler.StateChangeListener, ASMMessageHandler.UAFErrorCallback {
    private static final String TAG = AuthenticatorListFragment.class.getSimpleName();

    private View mCoordinator;
    private TextView tvOpType;
    private TextView tvInfo;
    private RecyclerView rvAuthenticators;

    private String mIntentType;
    private String mMessage;
    private String mChannelBinding;

    private int mCallerUid;

    private ASMMessageHandler asmMessageHandler;

    public static AuthenticatorListFragment getInstance(String intentType, String message, String channelBinding, int uid) {
        AuthenticatorListFragment fragment = new AuthenticatorListFragment();
        fragment.mIntentType = intentType;
        fragment.mMessage = message;
        fragment.mChannelBinding = channelBinding;
        fragment.mCallerUid = uid;
        return fragment;

    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authenticator_list, container, false);
        findView(view);
        initData();
        return view;
    }

    @Override
    protected String getRequestTag() {
        return TAG;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doConfirm();
    }

    private void findView(View view) {
        mCoordinator = view.findViewById(R.id.root_coordinator);
        rvAuthenticators = (RecyclerView) view.findViewById(R.id.rv_authenticators);
        tvOpType = (TextView) view.findViewById(R.id.tv_op_type);
        tvInfo = (TextView) view.findViewById(R.id.tv_prompt);

        rvAuthenticators.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    private void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ASMMessageHandler.REQUEST_ASM_OPERATION) {
                String resultStr = data.getExtras().getString(ASMIntent.MESSAGE_KEY);
                try {
                    asmMessageHandler.traffic(resultStr);
                } catch (ASMException e) {
                    Snackbar.make(mCoordinator, ASMException.class.getSimpleName() + ":" + e.statusCode, Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    public void showAuthenticator(List<AuthenticatorInfo> infos, AuthenticatorAdapter.OnAuthenticatorClickCallback callback) {
        if (getActivity() != null) {
            AuthenticatorAdapter adapter = new AuthenticatorAdapter(getActivity(), infos, callback);
            rvAuthenticators.setAdapter(adapter);
        }
    }

    private void doConfirm() {
        if (TextUtils.isEmpty(mIntentType)) {
            showError(R.string.client_op_type_error);
            return;
        }
        processMessage(mMessage);
    }

    private void processMessage(String inUafOperationMsg) {
        StatLog.printLog(TAG, "process message:" + inUafOperationMsg);
        String inMsg = extract(inUafOperationMsg);
        StatLog.printLog(TAG, "extract message is:" + inMsg);
        asmMessageHandler = ASMMessageHandler.parseMessage(this, mIntentType, inMsg, mChannelBinding, mCallerUid);
        asmMessageHandler.setStateChangeListener(this);
        asmMessageHandler.setUafErrorCallback(this);
        asmMessageHandler.setAsmPackage(UAFClientActivity.getAsmPack(getActivity().getApplicationContext()));
        asmMessageHandler.start();
    }

    private String extract(String inMsg) {
        if (TextUtils.isEmpty(inMsg)) {
            return null;
        }
        UAFMessage uafMessage = new UAFMessage();
        uafMessage.loadFromJson(inMsg);
        return uafMessage.uafProtocolMessage;
    }

    private void showError(int errorId) {
        tvInfo.setText(errorId);
    }

    private void setFailedIntent(short errorCode) {
        Intent intent = UAFIntent.getUAFOperationErrorIntent(getActivity().getComponentName().flattenToString(), errorCode);
        getActivity().setResult(Activity.RESULT_CANCELED, intent);
    }

    public void finishWithError(short errorCode) {
        if (getActivity() != null) {
            setFailedIntent(errorCode);
            getActivity().finish();
        }
    }

    @Override
    public void onStateChange(Traffic.OpStat newState, Traffic.OpStat oldState) {

    }

    public void loading() {
        showLoading();
    }

    public void loadingComplete() {
        if (getActivity() != null) {
            dismissLoading();
        }
    }

    @Override
    public void onError(short errorCode) {
        finishWithError(errorCode);
    }
}
