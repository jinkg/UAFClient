package com.jin.uitoolkit.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.jin.uitoolkit.R;
import com.jin.uitoolkit.widget.RevealBackgroundView;


/**
 * Created by 雅麟 on 2015/5/7.
 */
public class RevealBackgroundNoToolbarActivity extends ActionBarActivity implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    RevealBackgroundView backgroundView;
    RelativeLayout rlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupBackground();
//        setupRevealBackground();
    }

    protected void setupBackground() {
        View view = View.inflate(this, R.layout.activity_reveal_background, null);
        backgroundView = (RevealBackgroundView) view.findViewById(R.id.vRevealBackground);
        rlContent = (RelativeLayout) view.findViewById(R.id.reveal_rl_content);
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        View contentView = rootView.getChildAt(0);
        rootView.removeView(contentView);
        rlContent.addView(contentView);
        rootView.addView(view);
    }

    private void setupRevealBackground() {
        backgroundView.setOnStateChangeListener(this);
        final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
        backgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                backgroundView.startFromLocation(startingLocation);
                return true;
            }
        });
    }

    protected void startRevealBackground(final int[] location) {
        backgroundView.startFromLocation(location);
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            rlContent.setVisibility(View.VISIBLE);
            onStateFinish();
        } else {
            rlContent.setVisibility(View.INVISIBLE);
        }
    }

    protected void onStateFinish() {

    }
}
