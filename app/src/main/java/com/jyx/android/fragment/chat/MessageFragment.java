package com.jyx.android.fragment.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jyx.android.R;
import com.jyx.android.adapter.chat.MessageListAdapter;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ConversationInfo;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.model.MessageInfo;
import com.jyx.android.model.UserEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 消息-消息界面
 * Author : Tree
 * Date : 2015-10-30
 */
public class MessageFragment extends BaseFragment {


    @Bind(R.id.et_item_search)
    EditText mSearchView;

    private String tag = "MessageFragment";

    @Bind(R.id.rcv_message)
    RecyclerView recyclerView;

    ArrayList<ConversationInfo> mDataSet;

    MessageListAdapter mAdapter;

    //计数器 确保所有数据获取之后才更新
//    int count;

//    ACache mCache;

//     String KEY_MESSAGEFRAGMENT = "key_messagefragment";

//    ArrayList<ConversationInfo> mCacheList;

    //最大缓存数目
    final int MaxCache = 20;
    private List<Conversation> conversationList;
    private List<ConversationInfo> conversationInfoList;
//    ConversationListAdapter conversationListAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initData();
    }

    @Override
    public void initView(View view) {
        super.initView(view);

    }

    @Override
    public void onResume() {
        super.onResume();
        //获取好友列表和群信息列表
        //获取聊天数据
        conversationList = RongIM.getInstance().getRongIMClient()
                .getConversationList(Conversation.ConversationType.GROUP, Conversation
                        .ConversationType.PRIVATE, Conversation.ConversationType
                        .SYSTEM, Conversation.ConversationType.PUSH_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.PUBLIC_SERVICE);


//        List<Message> historyMessages = RongIM.getInstance().getRongIMClient()
//                .getHistoryMessages(Conversation
//                .ConversationType.GROUP, "", -1,20);
//
//        List<Message> lastMessage = RongIM.getInstance().getRongIMClient()
//                .getLatestMessages(Conversation
//                .ConversationType.GROUP,"",20);
//        for(Message message:historyMessages){
//            Log.e(tag,"history message = "+message.toString());
//        }
//
//        for(Message message:lastMessage){
//            Log.e(tag,"last message = "+ message.toString());
//        }

//        count = 0;
//        List<ConversationInfo> personList = new ArrayList<>();
//        List<ConversationInfo> groupList = new ArrayList<>();
        if(conversationList != null){
            for(Conversation info : conversationList){
                Log.e(tag,String.format("获取聊天列表get conversation list title=%s," +
                                "time=%s," +
                                "lastMessage=%s," +
                                "sendUserName=%s,id=%s,unreadCount=%d,byte=%s",
                        info.getConversationTitle(),new Date(info.getReceivedTime()).toString(),
                        info.getConversationType().getName(),info
                                .getSenderUserName(),info
                                .getSenderUserId(),info.getUnreadMessageCount(),new
                                String(info.getLatestMessage().encode())));
//                if(!info.getSenderUserId().equals(UserRecord.getInstance().getUserId())){
                    ConversationInfo conversationInfo = new ConversationInfo(info,
                            UserRecord.getInstance().getUserId());
                conversationInfoList.clear();
                conversationInfoList.add(conversationInfo);
//                    if(info.getConversationType() == Conversation.ConversationType.GROUP){
//                        groupList.add(conversationInfo);
//                    }else{
//                        personList.add(conversationInfo);
//                    }
//                    count ++;
//                }
            }
//            updateUserInfoList(personList);
//            updateGroupInfoList(groupList);
        }

        if(conversationInfoList != null ){
//            conversationInfoList.clear();
            updataUserList(conversationInfoList);
            updateGroupList(conversationInfoList);
        }
//        updateListView();
    }

    private void updateGroupList(final List<ConversationInfo> conversationList) {
        String xmlString = "";
        xmlString = "{\"function\":\"getmygrouplist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .queryGroups(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<GroupInfo>>, List<GroupInfo>>() {
                    @Override
                    public List<GroupInfo> call(BaseEntry<List<GroupInfo>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                }).subscribe(new Subscriber<List<GroupInfo>>() {
            @Override
            public void onCompleted() {
                updateListView();
            }

            @Override
            public void onError(Throwable e) {
                CommonExceptionHandler.handleBizException(e);
            }

            @Override
            public void onNext(final List<GroupInfo> groupInfos) {
                for(ConversationInfo info:conversationList){
                    for(GroupInfo groupInfo:groupInfos){
                        if(info.getmTargetId().equals(groupInfo.getGroupId())){
                            info.setConversationAvatar(groupInfo.getImageJson());
                            info.setConversationName(groupInfo
                                    .getGroupName());
                        }
                    }
                }
            }
        });
    }



    private void updataUserList(final List<ConversationInfo> conversationList) {
        String xmlString = "";
        xmlString = "{\"function\":\"getmaillist\",\"userid\":\"" +
                UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .getMailList(xmlString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<List<UserEntity>>, List<UserEntity>>() {
                    @Override
                    public List<UserEntity>call(BaseEntry<List<UserEntity>> listBaseEntry) {
                        return listBaseEntry.getData();
                    }
                })
                .subscribe(new Subscriber<List<UserEntity>>() {
                    @Override
                    public void onCompleted() {
                        updateListView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final List<UserEntity> userList) {
                        for(ConversationInfo info:conversationList){
                            for(UserEntity entity:userList){
                                if(info.getmTargetId().equals(entity.getUserId())
                                        || info.getmSenderId().equals(entity.getUserId())){
                                    info.setConversationName(entity.getNickName());
                                    info.setConversationAvatar(entity.getPortraitUri());
                                    info.setMobilePhoneNumber(entity.getMobilePhoneNumber
                                            ());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void initData() {
        super.initData();
        //区分不同的用户的缓存，用userid区分
//        KEY_MESSAGEFRAGMENT = KEY_MESSAGEFRAGMENT+UserRecord.getInstance().getUserId();
//        mCache = ACache.get(getActivity());
//        mCacheList = (ArrayList<ConversationInfo>)mCache.getAsObject
//                (KEY_MESSAGEFRAGMENT);
//        if(mCacheList == null)
//            mCacheList = new ArrayList<>();
        mDataSet = new ArrayList<>();
        conversationInfoList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new MessageListAdapter(mDataSet,getActivity());


        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mAdapter != null){
                    if(s.length() == 0){
                        mAdapter.restore();
                    }else{
                        mAdapter.getFilter().filter(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MessageListAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, Object data) {
                ConversationInfo info = (ConversationInfo)data;
                info.setUnReadCount(0);
                updateItem(info);
                switch (info.getConversationType()){
                    case ConversationInfo.TYPE_GROUP://群消息
                        if (RongIM.getInstance() != null)
                            RongIM.getInstance().startGroupChat(getActivity(),info
                                    .getConversationId(),info.getConversationName());
                        Log.e(tag,"on item click go to group  =="+info.getConversationId
                                ());
                        break;
                    case ConversationInfo.TYPE_PERSONAL://个人消息
                        if (RongIM.getInstance() != null)
                            RongIM.getInstance().startPrivateChat(getActivity(), info
                                    .getConversationId(), info.getConversationName());
                        Log.e(tag,"on item click go to private");
                        break;
                    case ConversationInfo.TYPE_SYSTEM://系统消息
                        Log.e(tag,"on item click go to private");
                        break;
                }
            }
        });




        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
//                Log.e(tag,"conversation list update on REceive");
                int j;
                for(j=0;j<mDataSet.size();j++){
                    final ConversationInfo info = mDataSet.get(j);
                    if(info.getConversationId().equals(message.getTargetId())){
                        info.setUnReadCount(info.getUnReadCount()+1);
                        try {
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setMsgCreateTime(String.valueOf(message.getSentTime()));
                            switch (message.getObjectName()){
                                case "RC:VcMsg":
                                    messageInfo.setMsgContent("[语音]");
                                    break;
                                case "RC:ImgMsg":
                                    messageInfo.setMsgContent("[图片]");
                                    break;
                                case "RC:LBSMsg":
                                    messageInfo.setMsgContent("[位置]");
                                    break;
                                case "RC:ImgTextMsg":
                                    messageInfo.setMsgContent("[礼物]");
                                    break;
                                case "RC:LuckyMoneyMsg":
                                    messageInfo.setMsgContent("[红包]");
                                    break;
                                case "RC:TransferMsg":
                                    messageInfo.setMsgContent("[转账]");
                                    break;
                                case "RC:TxtMsg":
                                    JSONObject jsonObject = new JSONObject(new String
                                            (message.getContent().encode()));
                                    String content = jsonObject.getString("content");
                                    messageInfo.setMsgContent(content);
                                    break;
                            }
                            info.setLastMsgInfo(messageInfo);

                            //局部更新
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateItem(info);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });

    }

    /**
     * 局部更新
     * @param info
     */
    void updateItem(ConversationInfo info){
        for(int i=0;i<mDataSet.size();i++){
            if(info.getConversationId().equals(mDataSet.get(i).getConversationId())){
                mAdapter.notifyItemChanged(i);
            }
        }
    }
    /**
     * 更新列表
     */
    void updateListView(){
        //跟缓存一起，最多20条
            mDataSet.clear();
            mDataSet.addAll(conversationInfoList);
            mAdapter.notifyDataSetChanged();
            //写入20条进缓存
//            ArrayList<ConversationInfo> list = new ArrayList<>();
//            if(mDataSet.size() >MaxCache){
//                for(int i=0;i<MaxCache;i++){
//                    list.add(mDataSet.get(i));
//                }
//            }else{
//                list = mDataSet;
//            }
//            mCache.put(KEY_MESSAGEFRAGMENT,list);
    }

    /**
     * 查询并更新用户信息
     */
    private void updateUserInfoList(final List<ConversationInfo> list) {
        String xmlString = "";
            xmlString = "{\"function\":\"getmaillist\",\"userid\":\"" +
                    UserRecord.getInstance().getUserId() + "\"}";
            ApiManager.getApi()
                    .getMailList(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<UserEntity>>, List<UserEntity>>() {
                        @Override
                        public List<UserEntity>call(BaseEntry<List<UserEntity>> listBaseEntry) {
                            return listBaseEntry.getData();
                        }
                    })
                    .subscribe(new Subscriber<List<UserEntity>>() {
                        @Override
                        public void onCompleted() {
//                            count--;
                            updateListView();
                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(final List<UserEntity> userList) {
                              for(ConversationInfo info:list){
                                  for(UserEntity entity:userList){
                                      if(info.getmSenderId().equals(entity.getUserId())
                                              || info.getmTargetId().equals(entity.getUserId())){
                                          info.setConversationName(entity.getNickName());
                                          info.setConversationAvatar(entity.getPortraitUri());
                                          info.setMobilePhoneNumber(entity.getMobilePhoneNumber());
//                                          updateCacheList(info);
                                      }
                                  }
                              }
                            updateListView();
                        }
                    });
        }



    /**
     * 查询并更新群信息
     */
    private void updateGroupInfoList(final List<ConversationInfo> list) {
        String xmlString = "";
            xmlString = "{\"function\":\"getmygrouplist\",\"userid\":\"" +
                    UserRecord.getInstance().getUserId() + "\"}";
            ApiManager.getApi()
                    .queryGroups(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<GroupInfo>>, List<GroupInfo>>() {
                        @Override
                        public List<GroupInfo> call(BaseEntry<List<GroupInfo>> listBaseEntry) {
                            return listBaseEntry.getData();
                        }
                    }).subscribe(new Subscriber<List<GroupInfo>>() {
                @Override
                public void onCompleted() {
//                    count--;
                    updateListView();
                }

                @Override
                public void onError(Throwable e) {
                    CommonExceptionHandler.handleBizException(e);
                }

                @Override
                public void onNext(final List<GroupInfo> groupInfos) {
                   for(ConversationInfo info:list){
                       for(GroupInfo groupInfo:groupInfos){
                           if(info.getConversationId().equals(groupInfo.getGroupId())){
                               info.setConversationAvatar(groupInfo.getImageJson());
                               info.setConversationName(groupInfo.getGroupName());
//                               updateCacheList(info);
                           }
                       }
                   }
                    updateListView();
                }
            });
        }
    /**
     * 查询并更新群信息
     */
//    private void updateGroupInfo(final ConversationInfo conversationInfo) {
//        Log.e(tag,"update group info");
//        String xmlString = "";
//        if (!TextUtils.isEmpty(conversationInfo.getConversationId())) {
//            xmlString = "{\"function\":\"getmygrouplist\",\"userid\":\"" +
//                    UserRecord.getInstance().getUserId() + "\"}";
//            ApiManager.getApi()
//                    .queryGroups(xmlString)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .map(new Func1<BaseEntry<List<GroupInfo>>, GroupInfo>() {
//                        @Override
//                        public GroupInfo call(BaseEntry<List<GroupInfo>> listBaseEntry) {
//                            for(GroupInfo info :listBaseEntry.getData()){
//                                if(info.getGroupId().equals(conversationInfo
//                                        .getConversationId())){
//                                    return info;
//                                }
//                            }
//                            return null;
//                        }
//                    }).subscribe(new Subscriber<GroupInfo>() {
//                        @Override
//                        public void onCompleted() {
//                            count--;
//                            updateListView();
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            CommonExceptionHandler.handleBizException(e);
//                        }
//
//                        @Override
//                        public void onNext(final GroupInfo info) {
//                            conversationInfo.setConversationName(info.getGroupName());
//                            conversationInfo.setConversationAvatar(info.getImageJson());
//                            updateCacheList(conversationInfo);
//                        }
//                    });
//        }
//    }

    /**
     * 更新缓存列表
     */
//    void updateCacheList(ConversationInfo conversationInfo){
//        int i = 0;
//        for(;i<mCacheList.size();i++){
//            ConversationInfo info1 = mCacheList.get(i);
//            if(info1.getConversationId().equals(conversationInfo.getConversationId())){
//                break;
//            }
//        }
//        if(i == mCacheList.size()){
//            //缓存没有,添加
//            mCacheList.add(conversationInfo);
//        }else{
//            //缓存中存在，直接更新
//            mCacheList.remove(i);
//            mCacheList.add(conversationInfo);
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search_view:

                break;
        }
    }


}
