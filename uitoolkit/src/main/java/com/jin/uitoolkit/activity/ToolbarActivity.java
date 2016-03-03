package com.jin.uitoolkit.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.jin.uitoolkit.R;


/**
 * Created by 雅麟 on 2015/4/22.
 */
public class ToolbarActivity extends AppCompatActivity {
    Toolbar mToolbar;
    FrameLayout mContent;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_toolbar);
        View view = View.inflate(this, layoutResID, null);
        findView();
        mContent.addView(view);
    }

    private void findView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContent = (FrameLayout) findViewById(R.id.base_content);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    finish();
                }
                break;
        }
        return false;
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }
}
