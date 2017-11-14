package com.jyx.android.activity.me;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;

/**
 * Created by HOM on 2016/4/24.
 */
public class ChangeBackground extends FragmentActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_change_background);

        TextView mChange= (TextView) findViewById(R.id.changge);
        TextView mDissms= (TextView) findViewById(R.id.dissmis);
        mChange.setOnClickListener(this);
        mDissms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changge:
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 3);
                ActivityHelper.goSelectPicExt(this, bundle);
                break;
            case R.id.dissmis:
                finish();
                break;
            default:
                break;
        }


    }
}
