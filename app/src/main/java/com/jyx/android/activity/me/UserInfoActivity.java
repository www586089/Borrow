package com.jyx.android.activity.me;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.me.MeFragment;

/**
 * Author : Tree
 * Date : 2016-02-06
 */
public class UserInfoActivity extends BaseActivity{

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_transparent;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mActionBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, MeFragment.newInstance(getIntent().getExtras()), null)
                    .commit();
        }

    }
}
