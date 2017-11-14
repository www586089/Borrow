package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.contact.GroupPagerFragment;

import de.greenrobot.event.EventBus;

/**
 * Author : Tree
 * Date : 2016-01-26
 */
public class GroupActivity extends BaseActivity{
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_GROUP_NAME = "group_name";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact_friend;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_message;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if(savedInstanceState == null){
            GroupPagerFragment groupPagerFragment = new GroupPagerFragment();
            groupPagerFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_content, groupPagerFragment, null)
                    .commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            EventBus.getDefault().post(data.getStringExtra(KEY_GROUP_NAME));
        }
    }
}
