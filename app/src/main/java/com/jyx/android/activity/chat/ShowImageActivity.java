package com.jyx.android.activity.chat;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;

/**
 * Created by Administrator on 3/30/2016.
 */
public class ShowImageActivity extends BaseActivity {

    @Bind(R.id.iv_avatar)
    ImageView mSimpleDraweeView;
    Uri mUri;


    @Override protected int getLayoutId() {
        return R.layout.activity_show_image;
    }


    @Override protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        String url=getIntent().getStringExtra("url");
        mUri = Uri.parse(url);
        ImageLoader.getInstance().displayImage(url,mSimpleDraweeView);
    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }


    @Override
    protected boolean hasBackButton() {
        return  true;
    }




    @Override
    protected int getActionBarTitle() {
        return R.string.title_image;
    }


}
