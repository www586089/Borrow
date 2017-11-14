package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : zfang
 * Date : 2016-01-09
 */
public class SysDataItem implements Parcelable{

    private String type_id;
    private String name;
    private String remark;

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SysDataItem(Parcel in) {
        this.type_id = in.readString();
        this.name = in.readString();
        this.remark = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type_id);
        dest.writeString(this.name);
        dest.writeString(this.remark);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<SysDataItem> CREATOR = new Creator<SysDataItem>() {
        @Override
        public SysDataItem[] newArray(int size) {
            return new SysDataItem[size];
        }

        @Override
        public SysDataItem createFromParcel(Parcel in) {
            return new SysDataItem(in);
        }
    };
}
