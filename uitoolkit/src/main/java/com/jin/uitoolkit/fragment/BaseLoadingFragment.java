package com.jin.uitoolkit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jin.uitoolkit.R;


public abstract class BaseLoadingFragment extends Fragment {
    private View root;

    private View failedView;
    private TextView failedWarningView;
    private View loadingView;
    private Button btnReload;

    ViewStub loadingStub;
    ViewStub failedStub;
    ViewStub failedWarningStub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        failedView = null;
        failedWarningView = null;
        loadingView = null;
        btnReload = null;
        return setupLoading(inflater, createContentView(inflater, container, savedInstanceState));
    }

    public abstract View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract String getRequestTag();

    protected View setupLoading(LayoutInflater inflater, View view) {
        root = inflater.inflate(R.layout.fragment_base_loading, null, false);
        failedWarningStub = (ViewStub) root.findViewById(R.id.vs_failed_header);
        failedStub = (ViewStub) root.findViewById(R.id.vs_failed);
        loadingStub = (ViewStub) root.findViewById(R.id.vs_loading);

        RelativeLayout rlContent = (RelativeLayout) root.findViewById(R.id.rl_content);
        rlContent.addView(view);
        return root;
    }

    protected void showLoading() {
        if (loadingView == null) {
            loadingView = loadingStub.inflate();
        }
        dismissToLoad();
        loadingView.setVisibility(View.VISIBLE);
    }

    private void dismissToLoad() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (failedView != null) {
            failedView.setVisibility(View.GONE);
        }
    }

    protected void dismissLoading() {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (failedView != null) {
            failedView.setVisibility(View.GONE);
        }
        if (failedWarningView != null) {
            failedWarningView.setVisibility(View.GONE);
        }
    }

    protected void dismissByFailed(int tag) {
        if (failedView == null) {
            failedView = failedStub.inflate();
            btnReload = (Button) failedView.findViewById(R.id.loading_btn_reload);
            btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload(v);
                }
            });
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        btnReload.setTag(tag);
        failedView.setVisibility(View.VISIBLE);
    }

    protected void failedWithContent(final int tag, String warning) {
        if (failedWarningView == null) {
            failedWarningView = (TextView) failedWarningStub.inflate();
            failedWarningView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload(v);
                }
            });
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
        if (failedView != null) {
            failedView.setVisibility(View.GONE);
        }
        failedWarningView.setTag(tag);
        failedWarningView.setText(warning);
        failedWarningView.setVisibility(View.VISIBLE);
    }

    private void reload(View v) {
        int tag = (int) v.getTag();
        onReload(tag);
    }

    protected void onReload(int tag) {

    }
}
