package com.jyx.android.activity.purchase;

import android.os.Bundle;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yiyi on 2015/11/23.
 */
public class WeixinPayActivity extends BaseActivity {
    @Bind(R.id.tv_weixinpay_amount)
    TextView mTvAmoutTxt;
    @Bind(R.id.tv_weixinpay_payee)
    TextView mTvPayeeTxt;
    @Bind(R.id.tv_weixinpay_goods)
    TextView mTvGoodsTxt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weixinpay;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_weixinpay;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @OnClick(R.id.btn_weixinpay_paynow)
    void clickPaynow() {
        Application.showToastShort("立即支付");
    }
}
