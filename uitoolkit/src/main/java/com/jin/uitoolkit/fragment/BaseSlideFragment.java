/*
 * Copyright 2016 YaLin Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jin.uitoolkit.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jin.uitoolkit.R;
import com.jin.uitoolkit.util.Utils;


/**
 * Created by 雅麟 on 2015/3/26.
 */
public abstract class BaseSlideFragment extends BaseLoadingFragment implements View.OnTouchListener {

    private float lastX;
    private float dis;

    private boolean lockSlide = false;

    private boolean enable = true;

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setupShadow(inflater, createShadowContentView(inflater, container, savedInstanceState));
    }

    public abstract View createShadowContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected View setupShadow(LayoutInflater inflater, View view) {
        View root = inflater.inflate(R.layout.fragment_shadow, null, false);
        RelativeLayout rlContent = (RelativeLayout) root.findViewById(R.id.shadow_fl_content);
        rlContent.addView(view);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!enable) {
            return true;
        }
        Utils.closeInput(getActivity());
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (event.getRawX() < 50) {
                    lockSlide = true;
                    break;
                } else {
                    lockSlide = false;
                }
                lastX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!lockSlide) {
                    final float currentX = event.getRawX();
                    dis = currentX - lastX;
                    if (dis > 0) {
                        v.setTranslationX(dis);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!lockSlide) {
                    if (dis > v.getWidth() / 3) {
                        slideIn(v);
                    } else {
                        slideOut(v);
                    }
                }
                break;
        }
        return true;
    }

    protected void slideOut(View v) {
        v.animate().translationX(0)
                .setDuration(300)
                .start();
    }

    protected void slideIn(View v) {
        v.animate().translationX(v.getWidth())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                    }
                })
                .start();
    }

    public BaseSlideFragment enableSlide(boolean enable) {
        this.enable = enable;
        return this;
    }

}

