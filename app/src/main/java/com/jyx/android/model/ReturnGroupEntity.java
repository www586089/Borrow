package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : Tree
 * Date : 2016-01-24
 */
public class ReturnGroupEntity implements Parcelable{

    /**
     * imageJson : http://demo.erongchuang.com:8888/JYX/GetGroupImage?groupid=150004132273488896
     * createUserId : 133631443107611648
     * group_describe :
     * group_name : 新建群
     * level : 1
     * groupid : 150004132273488896
     */

    private String imageJson;
    private String createUserId;
    private String group_describe;
    private String group_name;
    private String level;
    private String groupid;

    public ReturnGroupEntity(){

    }

    protected ReturnGroupEntity(Parcel in) {
        imageJson = in.readString();
        createUserId = in.readString();
        group_describe = in.readString();
        group_name = in.readString();
        level = in.readString();
        groupid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageJson);
        dest.writeString(createUserId);
        dest.writeString(group_describe);
        dest.writeString(group_name);
        dest.writeString(level);
        dest.writeString(groupid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReturnGroupEntity> CREATOR = new Creator<ReturnGroupEntity>() {
        @Override
        public ReturnGroupEntity createFromParcel(Parcel in) {
            return new ReturnGroupEntity(in);
        }

        @Override
        public ReturnGroupEntity[] newArray(int size) {
            return new ReturnGroupEntity[size];
        }
    };

    public void setImageJson(String imageJson) {
        this.imageJson = imageJson;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public void setGroup_describe(String group_describe) {
        this.group_describe = group_describe;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getImageJson() {
        return imageJson;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public String getGroup_describe() {
        return group_describe;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getLevel() {
        return level;
    }

    public String getGroupid() {
        return groupid;
    }
}
