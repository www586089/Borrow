package com.jyx.android.activity.publish;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zfang on 2015/12/22.
 */
public class PublishActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.img_publish_rental)
    void clickPublishRental() {//发布产品
        //ActivityHelper.goPublishItem(this);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 1);
        ActivityHelper.goSelectPicExt(this, bundle);
        finish();
    }

    @OnClick(R.id.img_publish_dynamic)
    void clickPublishDynamic() {//发布动态
        //ActivityHelper.goPublishDynamic(this);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", 2);
        ActivityHelper.goSelectPicExt(this, bundle);
        finish();
    }

    @OnClick(R.id.img_publish_close)
    void clickPublishClose() {
        finish();
    }
}
