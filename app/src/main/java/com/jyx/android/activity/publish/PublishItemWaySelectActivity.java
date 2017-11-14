package com.jyx.android.activity.publish;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

/**
 * Created by zfang on 2015-12-30.
 */
public class PublishItemWaySelectActivity extends BaseActivity {

    private String TAG = "PublishItemTypeSelectActivity";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_select_item_way;
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
        setActionBarTitle("取物方式");
        setActionRightText("确定");
    }
}
