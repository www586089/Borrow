package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.chat.NewSubGroupActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.contact.FriendFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ReturnGroupEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.rx.RxUtils;

import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-24
 */
public class EstablishGroupActivity extends BaseActivity {
    private ReturnGroupEntity mGroupEntity;
    private String mGroupId;
    private String mParentGroupId;

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.friend;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_contact_friend;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mGroupId = getIntent().getStringExtra(GroupActivity.KEY_GROUP_ID);
        mParentGroupId = getIntent().getStringExtra(NewSubGroupActivity.KEY_PARENT_GROUP_ID);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_content, FriendFragment.newInstance(ContactListAdapter.PAGE_ESTABLISH_GROUP), FriendFragment.TAG)
                    .commit();

        }
    }

    @Override
    protected void onActionRightClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(FriendFragment.TAG);
        if(f != null && f instanceof FriendFragment){
            Set<String> userIds = ((FriendFragment) f).getSelectedUserIds();
            if(userIds.size() == 0){
                return;
            }
            if(TextUtils.isEmpty(mGroupId)){
                establishGroup(userIds, TextUtils.isEmpty(mParentGroupId) ? "0" : mParentGroupId);
            }else {
                appendMembers(userIds);
            }

        }

    }

    private void establishGroup(final Set<String> userIds, String parentGroupId) {
        showWaitDialog();
        String param = "{\"function\":\"newgroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"parentid\":\"" + parentGroupId + "\",\"groupname\":\"新建群\",\"describe\":\"\",\"email\":\"\"}";
        ApiManager.getApi()
                .createGroup(param)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseEntry<List<ReturnGroupEntity>>, Observable<BaseEntry<List<String>>>>() {
                    @Override
                    public Observable<BaseEntry<List<String>>> call(BaseEntry<List<ReturnGroupEntity>> listBaseEntry) {
                        RxUtils.checkResult(listBaseEntry);
                        try {
                            mGroupEntity = listBaseEntry.getData().get(0);
                            String groupId = mGroupEntity.getGroupid();
                            String param = "{\"function\":\"joingroupbatch\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                                    + "\",\"groupid\":\"" + groupId + "\",\"adduserids\":" + new Gson().toJson(userIds) + "}";
                            return ApiManager.getApi().batchAdd2Group(param);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new BizException(-1, "建群失败");
                        }
                    }
                })
                .map(new ResultConvertFunc<List<String>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<String> voids) {
                        hideWaitDialog();
                        Application.showToast("建群成功");
                        Intent intent = new Intent();
                        if(!TextUtils.isEmpty(mParentGroupId)){
                            intent.putExtra("data", mGroupEntity);

                        }
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

    }

    private void appendMembers(final Set<String> userIds) {
        String param = "{\"function\":\"joingroupbatch\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"groupid\":\"" + mGroupId + "\",\"adduserids\":" + new Gson().toJson(userIds) + "}";
        showWaitDialog();
        ApiManager.getApi()
                .batchAdd2Group(param)
                .subscribeOn(Schedulers.io())
                .map(new ResultConvertFunc<List<String>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<String> voids) {
                        hideWaitDialog();
                        setResult(RESULT_OK);
                        finish();
                    }
                });

    }
}
