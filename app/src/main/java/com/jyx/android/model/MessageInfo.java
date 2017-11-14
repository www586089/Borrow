package com.jyx.android.model;

import java.io.Serializable;

/**
 * Author : Tree
 * Date : 2015-11-04
 */
public class MessageInfo implements Serializable{
    private int msgId;

    private String msgContent;

    private int msgPosterId;

    private String msgPosterName;

    private String msgPosterAvatar;

    private String msgCreateTime;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgPosterId() {
        return msgPosterId;
    }

    public void setMsgPosterId(int msgPosterId) {
        this.msgPosterId = msgPosterId;
    }

    public String getMsgPosterName() {
        return msgPosterName;
    }

    public void setMsgPosterName(String msgPosterName) {
        this.msgPosterName = msgPosterName;
    }

    public String getMsgPosterAvatar() {
        return msgPosterAvatar;
    }

    public void setMsgPosterAvatar(String msgPosterAvatar) {
        this.msgPosterAvatar = msgPosterAvatar;
    }

    public String getMsgCreateTime() {
        return msgCreateTime;
    }

    public void setMsgCreateTime(String msgCreateTime) {
        this.msgCreateTime = msgCreateTime;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "msgId=" + msgId +
                ", msgContent='" + msgContent + '\'' +
                ", msgPosterId='" + msgPosterId + '\'' +
                ", msgPosterName=" + msgPosterName +
                ", msgPosterAvatar='" + msgPosterAvatar + '\'' +
                ", msgCreateTime='" + msgCreateTime + '\'' +
                '}';
    }
}
