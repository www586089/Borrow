package com.jyx.android.activity.buy;

import android.os.Bundle;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created zfang on 2015/10/29.
 */
public abstract class BaseSuccessActivity extends BaseActivity{

    protected  TextView success_msg_tv;
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_after_pay_success;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        success_msg_tv = (TextView) findViewById(R.id.success_msg_tv);
        setTitleCenter();
        setSuccessMsg();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_ok)
    void clickAlipay() {
        finish();
    }

    abstract protected void setTitleCenter();
    abstract protected void setSuccessMsg();
}
