package com.jyx.android.activity.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2/18/2016.
 */
public class TransferDetailActivity extends BaseActivity {

    @Bind(R.id.iv_sign)
    ImageView mIvSign;
    @Bind(R.id.tv_description)
    TextView mTvDescription;
    @Bind(R.id.btn_sure)
    Button mBtnSure;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfer_detail;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.transfer_detail;
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
        setActionRightText("");
        mTvDescription.setText("待确认收钱"+"\n"+"￥250.00");
    }

    @OnClick(R.id.btn_sure)
    void onSure(){
        mIvSign.setBackgroundResource(R.mipmap.icon_ok);
        mTvDescription.setText("已收钱"+"\n"+"￥250.00");
        mBtnSure.setVisibility(View.INVISIBLE);
    }
}