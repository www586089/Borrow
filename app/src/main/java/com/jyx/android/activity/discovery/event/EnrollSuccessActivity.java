package com.jyx.android.activity.discovery.event;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.activity.buy.BaseSuccessActivity;

/**
 * Created zfang on 2016-03-19
 */
public class EnrollSuccessActivity extends BaseSuccessActivity {
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

    @Override
    protected void setTitleCenter() {
        setActionBarTitle(R.string.event_enroll_success_title_center);
    }

    @Override
    protected void setSuccessMsg() {
        success_msg_tv.setText(R.string.event_enroll_success_msg);
    }
}
