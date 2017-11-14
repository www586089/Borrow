package com.jyx.android.model;

/**
 * Created by caixiong on 15/12/18.
 */
public class UserRelation {
    private String objId;
    private String userMyId;
    private String userFriendId;
    private String userFriendName;

    private String imageUrl;


    private String pyFull;
    private String simple;
    private String type;
    private String createdAt;
    private String updatedAt;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getUserMyId() {
        return userMyId;
    }

    public void setUserMyId(String userMyId) {
        this.userMyId = userMyId;
    }

    public String getUserFriendId() {
        return userFriendId;
    }

    public void setUserFriendId(String userFriendId) {
        this.userFriendId = userFriendId;
    }

    public String getUserFriendName() {
        return userFriendName;
    }

    public void setUserFriendName(String userFriendName) {
        this.userFriendName = userFriendName;
    }

    public String getPyFull() {
        return pyFull;
    }

    public void setPyFull(String pyFull) {
        this.pyFull = pyFull;
    }

    public String getSimple() {
        return simple;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSortKey() {

        return simple.substring(0, 1);
    }
}
