package com.jyx.android.activity.contact;

import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.contact.AddContactFriendFragment;

/**
 * Author : Tree
 * Date : 2016-01-23
 */
public class AddContactFriendActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact_friend;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.contact_friend;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_content, new AddContactFriendFragment(), null)
                    .commit();
        }

    }

}
