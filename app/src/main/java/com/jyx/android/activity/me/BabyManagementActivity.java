package com.jyx.android.activity.me;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;

/**
 * 宝贝管理
 * Created by gaobo on 2015/11/9
 */
public class BabyManagementActivity  extends BaseActivity {
    private String user_id;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_babymanage;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_babymanage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        user_id = UserRecord.getInstance().getUserId();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        BabyManageFragment babyManageFragment = new BabyManageFragment();
//
//        fragmentTransaction.add(R.id.fragment_myitemmanage, babyManageFragment);
//        fragmentTransaction.commit();
    }

}
