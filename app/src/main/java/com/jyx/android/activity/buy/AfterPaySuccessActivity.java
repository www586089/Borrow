package com.jyx.android.activity.buy;

import android.os.Bundle;

import com.jyx.android.R;

/**
 * Created zfang on 2015/10/29.
 */
public class AfterPaySuccessActivity extends BaseSuccessActivity {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void setTitleCenter() {
        setActionBarTitle(R.string.pay_success_title_center);
    }

    @Override
    protected void setSuccessMsg() {
        success_msg_tv.setText(R.string.pay_success_notice_str);
    }
}
