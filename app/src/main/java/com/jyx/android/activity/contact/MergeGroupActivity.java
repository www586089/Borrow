package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.activity.chat.NewSubGroupActivity;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.contact.GroupFragment;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.List;
import java.util.Set;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-02-18
 */
public class MergeGroupActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact_friend;
    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.str_group;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if(savedInstanceState == null){
            GroupFragment groupFragment = new GroupFragment();
            Bundle bundle = getIntent().getExtras();
            bundle.putInt(GroupFragment.KEY_MODE, GroupFragment.MODE_SINGLE_CHOICE);
            groupFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_content, groupFragment, GroupFragment.TAG)
                    .commit();
        }

    }

    @Override
    protected void onActionRightClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(GroupFragment.TAG);
        if(f != null && f instanceof GroupFragment){
            List<GroupInfo> groupIds = ((GroupFragment) f).getSelectGroupItems();
            mergeGroup(groupIds);
        }
    }

    private void mergeGroup(final List<GroupInfo> groupIds) {
        if(groupIds.size() == 0){
            return;
        }
        String param = "{\"function\":\"mergegroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"mergefrom\":\"" + groupIds.get(0).getGroupId()
                + "\",\"mergeto\":\"" + getIntent().getExtras().getString(NewSubGroupActivity.KEY_PARENT_GROUP_ID) + "\"}";
        showWaitDialog();
        ApiManager.getApi()
                .commonQuery(param)
                .subscribeOn(Schedulers.io())
                .map(new ResultConvertFunc<List<Void>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        if (e.getMessage().equals("群不存在")){
                            showToast("你不是群主没有权限建子群！");
                        }else {
                            CommonExceptionHandler.handleBizException(e);
                        }
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        hideWaitDialog();
                        Intent intent = new Intent();
                        intent.putExtra("data", groupIds.get(0));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }


}
