package com.jyx.android.model;

import org.json.JSONObject;

import java.io.Serializable;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Author : Tree
 * Date : 2015-11-04
 */
public class ConversationInfo implements Serializable{
    public static final int TYPE_PERSONAL = 0;

    public static final int TYPE_GROUP = 1;

    public static final int TYPE_SYSTEM = 2;

    private String conversationId;

    private int unReadCount;

    private int conversationType;

    private MessageInfo lastMsgInfo;

    private String conversationName;

    private String conversationAvatar;

    public String getmSenderId() {
        return mSenderId;
    }

    public String getmTargetId() {
        return mTargetId;
    }

    private String mSenderId;
    private String mTargetId;

    //请求类型
    private int requestType;
    public  static final int GROUP_TYPE =0;
    public static  final int PERSON_TYPE =1;

    //普通内容
    private String content;

    private String mobilePhoneNumber;

    private String applytypemsg;

    private String applytype;

    public String getApplytype() {
        return applytype;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public String getApplytypemsg() {
        return applytypemsg;
    }

    public void setApplytypemsg(String applytypemsg) {
        this.applytypemsg = applytypemsg;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //处理状态
    private int state;

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public  static  final int STATE_WAIT = 0;//等待我处理的好友请求
    //已同意
    public  static final int STATE_OK = 1;//同意的好友请求
    //已拒绝
    public static final int STATE_REFUSE =2;//拒绝的好友请求

    public static final int STATE_WAIT_BY = 3;//等待对方处理的请求

    /**
     * 是否消息免打扰
     */
    private boolean isDND;


    public ConversationInfo() {
    }

    public ConversationInfo(Conversation info, String userId) {
        switch (info.getConversationType().getValue()){
            case 1:
                conversationType = TYPE_PERSONAL;
                if(info.getSenderUserId().equals(userId)){
                    conversationId = info.getTargetId();//私聊是接受者的id
                }else{
                    conversationId = info.getSenderUserId();//私聊是发送者的id
                }
                mSenderId = info.getSenderUserId();
                mTargetId = info.getTargetId();
                conversationName = info.getSenderUserId();
                break;
            case 3:
                conversationType = TYPE_GROUP;//群聊要获取targetid
                conversationId = info.getTargetId();
                conversationName = info.getTargetId();
                mSenderId = info.getSenderUserId();
                mTargetId = info.getTargetId();
                break;

        }
        unReadCount = info.getUnreadMessageCount();

        try {
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setMsgCreateTime(String.valueOf(info.getReceivedTime()));
            switch (info.getObjectName()){
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
                    JSONObject jsonObject = new JSONObject(new String(info.getLatestMessage()
                            .encode()));
                    String content = jsonObject.getString("content");
                    messageInfo.setMsgContent(content);
                    break;
            }
            lastMsgInfo = messageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ConversationInfo(Message message) {
        switch (message.getConversationType().getValue()){
            case 1:
                conversationType = TYPE_PERSONAL;
                conversationId = message.getSenderUserId();//私聊是发送者的id
                conversationName = message.getSenderUserId();
                break;
            case 3:
                conversationType = TYPE_GROUP;//群聊要获取targetid
                conversationId = message.getTargetId();
                conversationName = message.getTargetId();
                break;

        }
//        unReadCount = message.getUnreadMessageCount();
            unReadCount = 1;
        try {
            MessageInfo messageInfo = new MessageInfo();
//            JSONObject jsonObject = new JSONObject(new String(info.getLatestMessage()
//                    .encode()));
//            String content = jsonObject.getString("content");
//            info.setConversationName(message.getContent().getJSONUserInfo()
//                    .getString("content"));
            messageInfo.setMsgContent(message.getContent().getJSONUserInfo().getString
                    ("content"));
//            messageInfo.setMsgContent(content);
            messageInfo.setMsgCreateTime(String.valueOf(message.getReceivedTime()));
//            messageInfo.setMsgCreateTime(String.valueOf(info.getReceivedTime()));
            lastMsgInfo = messageInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public int getConversationType() {
        return conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public MessageInfo getLastMsgInfo() {
        return lastMsgInfo;
    }

    public void setLastMsgInfo(MessageInfo lastMsgInfo) {
        this.lastMsgInfo = lastMsgInfo;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getConversationAvatar() {
        return conversationAvatar;
    }

    public void setConversationAvatar(String conversationAvatar) {
        this.conversationAvatar = conversationAvatar;
    }

    public boolean isDND() {
        return isDND;
    }

    public void setIsDND(boolean isDND) {
        this.isDND = isDND;
    }

    @Override
    public String toString() {
        return "ConversationInfo{" +
                "conversationId='" + conversationId + '\'' +
                ", unReadCount=" + unReadCount +
                ", conversationType=" + conversationType +
                ", lastMsgInfo=" + lastMsgInfo +
                ", conversationName='" + conversationName + '\'' +
                ", conversationAvatar='" + conversationAvatar + '\'' +
                '}';
    }
}
