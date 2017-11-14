package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Author : Tree
 * Date : 2016-01-13
 */
public class GroupInfo implements Parcelable{

    /**
     * ismanagement : 1
     * group_describe : 群描述2
     * group_id : 136070074950779904
     * group_name : 测试群aa
     */

    @SerializedName("portraituri")
    private String portraitUri;
    @SerializedName("ismanagement")
    private String isManagement;
    @SerializedName("group_describe")
    private String groupDescrib;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("group_name")
    private String groupName;
    @SerializedName("imagejson")
    private String imageJson;

    public GroupInfo(){

    }

    protected GroupInfo(Parcel in) {
        portraitUri = in.readString();
        isManagement = in.readString();
        groupDescrib = in.readString();
        groupId = in.readString();
        groupName = in.readString();
        imageJson = in.readString();
        pinyin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(portraitUri);
        dest.writeString(isManagement);
        dest.writeString(groupDescrib);
        dest.writeString(groupId);
        dest.writeString(groupName);
        dest.writeString(imageJson);
        dest.writeString(pinyin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupInfo> CREATOR = new Creator<GroupInfo>() {
        @Override
        public GroupInfo createFromParcel(Parcel in) {
            return new GroupInfo(in);
        }

        @Override
        public GroupInfo[] newArray(int size) {
            return new GroupInfo[size];
        }
    };

    public String getImageJson() {
        return imageJson;
    }

    public void setImageJson(String imageJson) {
        this.imageJson = imageJson;
    }

    private String pinyin;

    public void setIsManagement(String isManagement) {
        this.isManagement = isManagement;
    }

    public void setGroupDescrib(String groupDescrib) {
        this.groupDescrib = groupDescrib;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIsManagement() {
        return isManagement;
    }

    public String getGroupDescrib() {
        return groupDescrib;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupInfo groupInfo = (GroupInfo) o;

        return groupId.equals(groupInfo.groupId);

    }

    @Override
    public int hashCode() {
        return groupId.hashCode();
    }
}
