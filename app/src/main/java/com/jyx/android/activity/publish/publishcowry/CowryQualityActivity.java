package com.jyx.android.activity.publish.publishcowry;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

/**
 * Created by cooldemo on 15/10/29.
 */
public class CowryQualityActivity extends BaseActivity {

    private String TAG = "CowryQualityActivity";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_cowryquality;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle("宝贝新旧情况");
        setActionRightText("");
    }
}
