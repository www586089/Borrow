package com.jyx.android.event;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import com.jyx.android.activity.chat.NotifitationActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupSerializable;
import com.jyx.android.model.UserCenterEntity;
import com.jyx.android.model.UserEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.utils.ACache;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;
import java.util.ArrayList;
import java.util.List;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 蓉c
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements RongIMClient.OnReceiveMessageListener,
        RongIM.OnSendMessageListener,
        RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationListBehaviorListener,
        ApiCallback
        {

    private static final String TAG = RongCloudEvent.class.getSimpleName();
    private static RongCloudEvent mRongCloudInstance;
            private Context mContext;


            /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {
            synchronized (RongCloudEvent.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }


    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者
        RongIM.setConversationListBehaviorListener(this);
    }

    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {

        MessageContent messageContent = message.getContent();


        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-informationNotificationMessage:" + informationNotificationMessage.getMessage());
        } else if (messageContent instanceof ContactNotificationMessage) {//好友添加消息
            ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
            Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;" + contactContentMessage.getExtra());
            Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:" + contactContentMessage.getMessage().toString());
        } else{
        }
        return false;
    }



    @Override
    public Message onSend(Message message) {
        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        if (message.getSentStatus() == Message.SentStatus.FAILED) {

            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {//不在聊天室

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {//不在讨论组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {//不在群组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {//你在他的黑名单中
            }
        }


        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
        } else {
            Log.d(TAG, "onSent-其他消息，自己来判断处理");
        }
        return false;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        ArrayList<UserEntity> list = (ArrayList<UserEntity>)ACache.get(mContext)
                .getAsObject(Constants.KEY_USER);
            for(UserEntity entity:list){
                if(entity.getUserId().equals(userId)){
                    UserInfo info = new UserInfo(entity.getUserId(),entity.getNickName(),
                            Uri.parse(entity.getPortraitUri()));
                    return info;
                }
                //没找到，需要单独获取
                updateUserInfo(userId);
            }
        return null;
    }

    /**
     * 查询并更新用户信息
     */
    private void updateUserInfo(final String userId) {
        String xmlString = "";
            xmlString = "{\"function\":\"getusercenter\",\"userid\":\"" +
                    userId + "\"}";
            ApiManager.getApi()
                    .getUserCenter(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<UserCenterEntity>>, UserCenterEntity>() {
                        @Override
                        public UserCenterEntity call(BaseEntry<List<UserCenterEntity>> listBaseEntry) {
                            if(listBaseEntry.getResult() == 0)
                                return listBaseEntry.getData().get(0);
                            else
                                return null;
                        }
                    })
                    .subscribe(new Subscriber<UserCenterEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(final UserCenterEntity userCenterEntity) {
                            if(userCenterEntity != null){
                                UserInfo info = new UserInfo(userCenterEntity.getUser_id(),
                                        userCenterEntity.getNickname(),
                                        Uri.parse(userCenterEntity.getPortraituri()));
                                RongIM.getInstance().refreshUserInfoCache(info);
                            }
                        }
                    });

    }


    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public Group getGroupInfo(String groupId) {
        List<GroupSerializable> list = (ArrayList<GroupSerializable>)ACache.get(mContext).getAsObject
                (Constants
                .KEY_GROUP);
        for(GroupSerializable info:list){
            if(info.getGroupId().equals(groupId)){
                Group group = new Group(info.getGroupId(),info.getGroupName(),Uri.parse
                        (info.getPortraitUri()));
                return group;
            }
        }
        return null;
    }



    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        if (status.getMessage().equals(ConnectionStatus.DISCONNECTED.getMessage())) {
        }
    }



    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view, final UIConversation conversation) {
        switch (conversation.getConversationType()){
            case GROUP:
                RongIM.getInstance().startGroupChat(context,conversation
                        .getConversationTargetId(),conversation.getUIConversationTitle());
                break;
            case PRIVATE:
                RongIM.getInstance().startPrivateChat(context, conversation
                        .getConversationTargetId(), conversation.getUIConversationTitle());
                break;
            case SYSTEM:
                RongIMClient.getInstance().getLatestMessages(Conversation
                                .ConversationType.SYSTEM, conversation.getConversationTargetId()

                        , 1, new RongIMClient.ResultCallback<List<Message>>() {
                            @Override
                            public void onSuccess(List<Message> messages) {
                                Message message = messages.get(0);
                                message.getReceivedStatus().setListened();
                                RongIMClient.getInstance().setMessageReceivedStatus
                                        (conversation.getLatestMessageId(), message
                                                .getReceivedStatus());
//                                                , new RongIMClient.ResultCallback<Boolean>() {
//
//
//                                            @Override
//                                            public void onSuccess(Boolean aBoolean) {
//
//                                            }
//
//                                            @Override
//                                            public void onError(RongIMClient.ErrorCode errorCode) {
//
//                                            }
//                                        });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                context.startActivity(new Intent(context, NotifitationActivity.class));
                break;
        }
        return true;
    }

    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
        return false;
    }

    @Override
    public void onComplete(AbstractHttpRequest abstractHttpRequest, Object o) {
    }

    @Override
    public void onFailure(AbstractHttpRequest abstractHttpRequest, BaseException e) {

    }
}
