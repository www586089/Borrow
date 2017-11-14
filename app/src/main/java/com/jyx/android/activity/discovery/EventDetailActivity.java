package com.jyx.android.activity.discovery;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

/**
 * Created by Tonlin on 2015/10/27.
 */
public class EventDetailActivity extends BaseActivity {

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_discovery_event_detail;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle("活动详情");
    }
}
