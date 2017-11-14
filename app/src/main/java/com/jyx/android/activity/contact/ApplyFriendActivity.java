package com.jyx.android.activity.contact;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.activity.MySqlistHelpe;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.widget.ClearableEditText;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-23
 */
public class ApplyFriendActivity extends BaseActivity {


    @Bind(R.id.et_apply_msg)
    ClearableEditText mEtApplyMsg;
    private SQLiteDatabase db;
    private String mFriendId;

    public static final String KEY_FRIEND_ID = "friend_id";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_friend;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.apply_friend;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        db=new MySqlistHelpe(this).getReadableDatabase();
        mFriendId = getIntent().getStringExtra(KEY_FRIEND_ID);
        mTvActionRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFriend();
            }
        });
    }


    private void applyFriend() {
        if(TextUtils.isEmpty(mEtApplyMsg.getText().toString())){
            return;
        }

        showWaitDialog();
        String param = "{\"function\":\"addfriend\",\"userid\":\""
                + UserRecord.getInstance().getUserId() + "\",\"friendid\":\""
                + mFriendId +"\",\"applymsg\":\"" + mEtApplyMsg.getText().toString() + "\"}";
        ApiManager.getApi().applyFriend(param)
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
                    public void onNext(List<Void> voidBaseEntry) {
                        hideWaitDialog();
                        db.execSQL("insert into follow(userid) values(?)",new String[]{mFriendId});
                        setResult(RESULT_OK);
                        finish();
                    }
                });

    }
}
