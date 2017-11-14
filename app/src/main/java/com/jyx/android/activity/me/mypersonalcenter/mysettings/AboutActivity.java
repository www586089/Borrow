package com.jyx.android.activity.me.mypersonalcenter.mysettings;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

/**
 * Created by Administrator on 2016/3/4.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_setting;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }
}
