package com.jyx.android.model;

/**
 * Created by user on 2015/12/1.
 */
public class RongLogin {

    private String userId;
    private String name;
    private String portraitUri;
    private String returnCode;//返回码

    public String getReturnCode() {
        return returnCode;
    }
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }
    //用户头像 URI，最大长度 1024 字节。用来在 Push 推送时显示用户的头像。（可选）
    private String objectId;//第三方数据库表id
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPortraitUri() {
        return portraitUri;
    }
    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
