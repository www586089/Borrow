package com.jyx.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GiftEntity;
import com.jyx.android.model.WaitFriendEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2/17/2016.
 */
public class SystemNotifitionActivity extends BaseActivity{


    @Bind(R.id.lv_listview)
    ListView mListView;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_notifition;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.system_message;
    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        getWaitFriendApply();

    }





    /**
     * 获取申请好友列表
     {"result":"0","msg":"操作成功","datas":
     [{"createdat":"2016-03-15 10:23:08.0",
     "portraituri":"http://demo.erongchuang.com:8888/JYX/ImageShow?image=154718280492286976.png",
     "user_id":"137520969395766272","signature":"签名","nickname":"cai",
     "apply_id":"159245044975436800","apply_msg":"Caixiong",
     "applytype":"等待审核"}]}

     */
    private void getWaitFriendApply(){
        String xmlString = "";
        xmlString = "{\"function\":\"getwaitfriendapply\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .getWaitFriendApply(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<WaitFriendEntity>>, List<WaitFriendEntity>>() {
                    @Override
                    public List<WaitFriendEntity>call(BaseEntry<List<WaitFriendEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<WaitFriendEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<WaitFriendEntity> giftEntityList) {

                    }
                });
    }
}
