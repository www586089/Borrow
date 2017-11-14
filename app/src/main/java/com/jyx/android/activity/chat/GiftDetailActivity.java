package com.jyx.android.activity.chat;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2/18/2016.
 */
public class GiftDetailActivity extends Activity {


    @Bind(R.id.iv_close)
    ImageView mIvClose;
    @Bind(R.id.sdv_image)
    SimpleDraweeView mSdvImage;
    @Bind(R.id.tv_description)
    TextView mTvDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        ButterKnife.bind(this);
        mSdvImage.setImageURI(Uri.parse(getIntent().getStringExtra("imageUrl")));
        mTvDesc.setText(getIntent().getStringExtra("description"));
    }

    @OnClick(R.id.iv_close)
    void onClose(){
        finish();
    }

}
