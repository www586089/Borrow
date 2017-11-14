package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-27
 */
public class EditGroupInfoActivity extends BaseActivity {


    @Bind(R.id.et_group_name)
    EditText mEtGroupName;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_group;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.group_name;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mEtGroupName.setText(getIntent().getStringExtra(GroupActivity.KEY_GROUP_NAME));
    }

    @OnClick(R.id.btn_submit)
    public void onClick(){
        if(TextUtils.isEmpty(mEtGroupName.getText().toString())){
            return;
        }
        String param = "{\"function\":\"updategroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"groupid\":\"" + getIntent().getStringExtra(GroupActivity.KEY_GROUP_ID)
                + "\",\"groupname\":\"" + mEtGroupName.getText().toString() + "\",\"describe\":\"\",\"email\":\"\"}";
        showWaitDialog();
        ApiManager.getApi()
                .commonQuery(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<Void>>())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> strings) {
                        hideWaitDialog();
                        Intent intent = new Intent();
                        intent.putExtra(GroupActivity.KEY_GROUP_NAME, mEtGroupName.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
    }
}
