package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : zfang
 * Date : 2016-03-07
 */
public class GroupInfoBean implements Parcelable{

    private String imagejson;
    private String ismanagement;
    private String group_describe;
    private String group_id;
    private String group_name;
    private String parentid;

    protected GroupInfoBean(Parcel in) {
        imagejson = in.readString();
        ismanagement = in.readString();
        group_describe = in.readString();
        group_id = in.readString();
        group_name = in.readString();
        parentid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagejson);
        dest.writeString(ismanagement);
        dest.writeString(group_describe);
        dest.writeString(group_id);
        dest.writeString(group_name);
        dest.writeString(parentid);
    }

    public String getImagejson() {
        return imagejson;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public String getIsmanagement() {
        return ismanagement;
    }

    public void setIsmanagement(String ismanagement) {
        this.ismanagement = ismanagement;
    }

    public String getGroup_describe() {
        return group_describe;
    }

    public void setGroup_describe(String group_describe) {
        this.group_describe = group_describe;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupInfoBean> CREATOR = new Creator<GroupInfoBean>() {
        @Override
        public GroupInfoBean createFromParcel(Parcel in) {
            return new GroupInfoBean(in);
        }

        @Override
        public GroupInfoBean[] newArray(int size) {
            return new GroupInfoBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupInfoBean groupInfo = (GroupInfoBean) o;

        return group_id.equals(groupInfo.group_id);

    }

    @Override
    public int hashCode() {
        return group_id.hashCode();
    }
}
