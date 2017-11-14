package com.jyx.android.activity.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jyx.android.R;
import com.jyx.android.adapter.chat.NotifitationAdapter;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ConversationInfo;
import com.jyx.android.model.WaitFriendEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 1/25/2016.
 */
public class NotifitationActivity extends BaseActivity {

    @Bind(R.id.rcv_message)
    RecyclerView recyclerView;

    NotifitationAdapter mAdapter;
    List<ConversationInfo> mDataSet;
    private String tag = "NotifitationActivity";
    final String WAIT_FRIEND_APPLY = "getwaitfriendapply";//38、	获取等待我审核的添加好友请求
    final  String FRIEND_APPLY_HIS = "getfriendapplyhis";//39、	获取我提交的处理结束添加好友请求
    final String MY_FRIEND_APPLY = "getmyfriendapply";//36、	获取我提交的待审核添加好友请求
    final String MY_FRIEND_APPLY_HIS = "getmyfriendapplyhis";//37、	获取我提交的处理结束添加好友请求
    final String MY_GORPI_ADDPLY = "getapplyjoingroup";//获取群请求

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notifition;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText("");
        initData();
    }

    private void initData() {
        mDataSet = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new NotifitationAdapter(mDataSet,this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        Log.e(tag, "本人的id 是 === " + UserRecord.getInstance().getUserId());
        getFriend(WAIT_FRIEND_APPLY);
        getFriend(FRIEND_APPLY_HIS);
        getFriend(MY_FRIEND_APPLY);
        getFriend(MY_FRIEND_APPLY_HIS);
        getFriend(MY_GORPI_ADDPLY);
    }


    @Override
    protected int getActionBarTitle() {
        return R.string.title_message;
    }


    /**
     {"result":"0","msg":"操作成功","datas":
     [{"createdat":"2016-03-15 10:23:08.0",
     "portraituri":"http://demo.erongchuang.com:8888/JYX/ImageShow?image=154718280492286976.png",
     "user_id":"137520969395766272","signature":"签名","nickname":"cai",
     "apply_id":"159245044975436800","apply_msg":"Caixiong",
     "applytype":"等待审核"}]}
     */
    private void getFriend(final String url){
        String xmlString = "";
        xmlString = "{\"function\":\""+url+"\",\"userid\":\"" +
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
                    public void onNext(final List<WaitFriendEntity> EntityList) {
                            for(WaitFriendEntity entity:EntityList){
                                ConversationInfo info = new ConversationInfo();
                                info.setConversationAvatar(entity.getPortraituri());
                                info.setConversationName(entity.getNickname());
                                info.setContent(entity.getApply_msg());
                                info.setApplytypemsg(entity.getApplytypemsg());
                                if(entity.getApplytype().equals("等待审核")){
                                    if(url.equals(WAIT_FRIEND_APPLY)){
                                        info.setState(ConversationInfo.STATE_WAIT);

                                    }else{
                                        info.setState(ConversationInfo.STATE_WAIT_BY);
                                    }
                                }else if(entity.equals("审核未通过")){
                                    info.setState(ConversationInfo.STATE_REFUSE);
                                }else if (entity.getApplytype().equals("1")){
                                    info.setState(ConversationInfo.STATE_WAIT);
                                }
                                else if (entity.getApplytype().equals("2")){
                                    info.setState(ConversationInfo.STATE_OK);
                                }
                                else if (entity.getApplytype().equals("3")){
                                    info.setState(ConversationInfo.STATE_REFUSE);
                                }
                                else{
                                    info.setState(ConversationInfo.STATE_OK);
                                }
                                info.setApplytype(entity.getApplytype());
                                info.setConversationId(entity.getApply_id());
                                mDataSet.add(info);
                            }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


}
