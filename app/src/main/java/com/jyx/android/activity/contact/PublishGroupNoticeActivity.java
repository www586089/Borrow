package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-29
 * 群通知界面
 */
public class PublishGroupNoticeActivity extends BaseActivity{
    @Bind(R.id.et_title)
    EditText et_title;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.group_notice;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_group_notice;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent= getIntent();
        final String mGroupid= intent.getStringExtra(GroupActivity.KEY_GROUP_ID);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(title)) {
                    if (!TextUtils.isEmpty(content)) {
                        //发送通知;
                        String param = "{\"function\":\"sendgroupnotice\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                                + "\",\"groupid\":\"" + mGroupid + "\",\"title\":\"" + title + "\",\"content\":\"" + content + "\"}";
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
                                    public void onNext(List<Void> voids) {
                                        hideWaitDialog();
                                        showToast("提交成功");
                                        finish();
                                    }
                                });
                    } else {
                        showToast("通知内容不能为空");
                    }
                } else {
                    showToast("主题内容不能为空");
                }
            }
        });
        super.init(savedInstanceState);
    }

}
