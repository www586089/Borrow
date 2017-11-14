package com.jyx.android.activity.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.contact.EstablishGroupActivity;
import com.jyx.android.activity.contact.MergeGroupActivity;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.model.ReturnGroupEntity;

import butterknife.Bind;

/**
 * 建子群
 * Author : Tree
 * Date : 2015-11-14
 */
public class NewSubGroupActivity extends BaseActivity {
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_GROUP_NAME = "group_name";
    public static final String KEY_GROUP_LOGO = "group_logo";
    public static final String KEY_PARENT_GROUP_ID = "parent_group_id";

    private static final int ADD_SUB_GROUP_CODE = 100;
    private static final int MERGE_SUB_GROUP_CODE = 101;

    @Bind(R.id.toolbar_left_title)
    TextView mToolbarLeftTitle;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.sdv_group_logo)
    SimpleDraweeView mSdvGroupLogo;
    @Bind(R.id.tv_group_name)
    TextView mTvGroupName;
    @Bind(R.id.tv_merge_group)
    TextView mTvMergeGroup;
    @Bind(R.id.tv_direct_add_contact)
    TextView mTvDirectAddContact;
    @Bind(R.id.sdv_merge_other_group)
    SimpleDraweeView mSdvMergeOtherGroup;
    @Bind(R.id.sdv_add_sub_group)
    SimpleDraweeView mSdvAddSubGroup;
    @Bind(R.id.tv_merge_sub_group_name)
    TextView mTvMergeSubGroupName;
    @Bind(R.id.tv_add_sub_group_name)
    TextView mTvAddSubGroupName;
    @Bind(R.id.toolbar_dissmis)
    TextView mToolbarDissmis;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_sub_group;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mToolbar.setTitle("");
        mToolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mToolbarLeftTitle.setOnClickListener(this);

        if (!TextUtils.isEmpty(getIntent().getStringExtra(KEY_GROUP_LOGO))) {
            mSdvGroupLogo.setImageURI(Uri.parse(getIntent().getStringExtra(KEY_GROUP_LOGO)));
        } else {
            Uri uri = Uri.parse("res://com.jyx.android/" + R.mipmap.tree_group_node);
            mSdvGroupLogo.setImageURI(uri);
        }
        mTvGroupName.setText(getIntent().getStringExtra(KEY_GROUP_NAME));
        mTvMergeGroup.setOnClickListener(this);
        mTvDirectAddContact.setOnClickListener(this);
        mToolbarDissmis.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_title:
                onBackPressed();
                break;
            case R.id.tv_merge_group: {
                Intent intent = new Intent(this, MergeGroupActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_PARENT_GROUP_ID, getIntent().getStringExtra(KEY_GROUP_ID));
                intent.putExtras(bundle);
                startActivityForResult(intent, MERGE_SUB_GROUP_CODE);
                break;
            }
            case R.id.tv_direct_add_contact:
            {
                Intent intent = new Intent(this, EstablishGroupActivity.class);
                intent.putExtra(KEY_PARENT_GROUP_ID, getIntent().getStringExtra(KEY_GROUP_ID));
                startActivityForResult(intent, ADD_SUB_GROUP_CODE);
                break;
            }
            case R.id.toolbar_dissmis:
                onBackPressed();
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ADD_SUB_GROUP_CODE) {
            ReturnGroupEntity returnGroupEntity = data.getParcelableExtra("data");
            mTvDirectAddContact.setVisibility(View.GONE);
            mTvAddSubGroupName.setVisibility(View.VISIBLE);
            mSdvAddSubGroup.setVisibility(View.VISIBLE);
            mTvAddSubGroupName.setText(returnGroupEntity.getGroup_name());
            if(!TextUtils.isEmpty(returnGroupEntity.getImageJson())){
                mSdvAddSubGroup.setImageURI(Uri.parse(returnGroupEntity.getImageJson()));
            }else {
                mSdvAddSubGroup.setImageURI(Uri.parse("res://com.jyx.android/" + R.mipmap.tree_group_node));
            }
        }else if(resultCode == RESULT_OK && requestCode == MERGE_SUB_GROUP_CODE){
            GroupInfo groupInfo = data.getParcelableExtra("data");
            mTvMergeGroup.setVisibility(View.GONE);
            mTvMergeSubGroupName.setVisibility(View.VISIBLE);
            mSdvMergeOtherGroup.setVisibility(View.VISIBLE);
            mTvMergeSubGroupName.setText(groupInfo.getGroupName());
            if(!TextUtils.isEmpty(groupInfo.getImageJson())){
                mSdvMergeOtherGroup.setImageURI(Uri.parse(groupInfo.getImageJson()));
            }else {
                mSdvMergeOtherGroup.setImageURI(Uri.parse("res://com.jyx.android/" + R.mipmap.tree_group_node));
            }

        }
    }

}
