package com.jyx.android.activity.buy;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.model.ItemBean;

/**
 * Created by zfang on 2016-02-29.
 */
public class LoveItemUserActivity extends BaseActivity {

    private String TAG = "LoveItemUserActivity";
    private ItemBean itemBean = null;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_love_item_user;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        getBundleData();
        super.onAttachFragment(fragment);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(R.string.love_item_user_title_center);
        setActionRightText("");
    }
    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            itemBean = bundle.getParcelable("item");
        }
    }

    public String getItemId() {
        return itemBean.getItem_id();
    }
}
