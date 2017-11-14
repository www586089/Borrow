package com.jyx.android.activity.discovery.event;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.model.ActivityItemBean;
import com.jyx.android.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;

public class EventDetailActivity extends BaseActivity {


    private String TAG = EventDetailActivity.class.getSimpleName();
    private ActivityItemBean activityItemBean;
    @Bind(R.id.activity_detail_title)
    TextView activity_detail_title;
    @Bind(R.id.activity_detail_create_time)
    TextView activity_detail_create_time;
    @Bind(R.id.activity_detail_img)
    ImageView activity_detail_img;
    @Bind(R.id.activity_detail_content)
    TextView activity_detail_content;

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_detail;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(R.string.event_detail_title);
        setActionRightText("");
        getBundleData();
        initUI();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            activityItemBean = bundle.getParcelable("itemActivityBean");
        }
    }

    private void initUI() {
        activity_detail_title.setText(activityItemBean.getTheme());
        activity_detail_create_time.setText(activityItemBean.getExpirydate());
        String url = activityItemBean.getImagejson();
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, activity_detail_img, ImageOptions.get_gushi_Options());
        }
        activity_detail_content.setText(Html.fromHtml(activityItemBean.getContent()));
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
        }
    }
}