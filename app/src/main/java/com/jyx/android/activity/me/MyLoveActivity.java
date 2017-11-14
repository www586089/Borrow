package com.jyx.android.activity.me;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyLoveActivity extends BaseActivity {
    private String user_id;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_love;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_me_my_love;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = UserRecord.getInstance().getUserId();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        MyLoveFragment myLoveFragment = new MyLoveFragment();
//
//        fragmentTransaction.add(R.id.fragment_mylove, myLoveFragment);
//        fragmentTransaction.commit();
    }

}
