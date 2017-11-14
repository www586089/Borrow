package com.jyx.android.activity.me.mypersonalcenter;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

/**
 * Created by Dell on 2016/4/20.
 */
public class ChageEditPrice extends BaseActivity{


    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }


    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_price;
    }
    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_withdraw;
    }
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

}
