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

package com.yalin.fidoclient.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yalin.fidoclient.R;
import com.yalin.fidoclient.api.UAFClientApi;
import com.yalin.fidoclient.msg.AsmInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private View rootCoordinator;

    private AsmInfo asmInfo;

    private Button btnDefaultAsm;

    private boolean hasDefault = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        rootCoordinator = findViewById(R.id.root_coordinator);

        btnDefaultAsm = (Button) findViewById(R.id.setting_btn_asm_name);
        btnDefaultAsm.setOnClickListener(this);
    }

    private void initData() {
        asmInfo = UAFClientApi.getDefaultAsmInfo(getApplicationContext());
        if (asmInfo != null) {
            if (!TextUtils.isEmpty(asmInfo.appName)) {
                hasDefault = true;
                btnDefaultAsm.setText(asmInfo.appName);
            } else if (!TextUtils.isEmpty(asmInfo.pack)) {
                hasDefault = true;
                btnDefaultAsm.setText(asmInfo.pack);
            } else {
                hasDefault = false;
                btnDefaultAsm.setText(R.string.default_asm_none);
            }
        } else {
            hasDefault = false;
            btnDefaultAsm.setText(R.string.default_asm_none);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_btn_asm_name:
                if (hasDefault) {
                    showAsmInfoAction(asmInfo);
                } else {
                    showNoAsmInfoAction();
                }
                break;
        }
    }

    private void showAsmInfoAction(final AsmInfo asmInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(asmInfo.appName);
        builder.setIcon(asmInfo.icon);
        builder.setMessage(getString(R.string.asm_pack_prompt, asmInfo.pack));
        builder.setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UAFClientApi.clearDefaultAsm(getApplicationContext());
                initData();
            }
        });
        builder.setNegativeButton(R.string.confirm, null);

        builder.create().show();
    }

    private void showNoAsmInfoAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.no_default_asm_prompt));
        builder.setPositiveButton(R.string.confirm, null);

        builder.create().show();
    }
}
