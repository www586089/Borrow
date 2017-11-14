package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ActivityItemBean implements Parcelable {
    private String activity_id;
    private String content;
    private String detailer;
    private String expirydate;
    private String imagejson;
    private String nickname;
    private String portraituri;
    private String theme;
    private String user_id;
    private String createdat;
    private int itemType = -1;

    public ActivityItemBean() {

    }
    public String getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(String activity_id) {
        this.activity_id = activity_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetailer() {
        return detailer;
    }

    public void setDetailer(String detailer) {
        this.detailer = detailer;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public String getImagejson() {
        return imagejson;
    }

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public ActivityItemBean(Parcel in) {
        this.activity_id = in.readString();
        this.content = in.readString();
        this.detailer = in.readString();
        this.expirydate = in.readString();
        this.imagejson = in.readString();
        this.nickname = in.readString();
        this.portraituri = in.readString();
        this.theme = in.readString();
        this.user_id = in.readString();
        this.createdat = in.readString();
    }

    public static final Parcelable.Creator<ActivityItemBean> CREATOR = new Creator<ActivityItemBean>() {
        @Override
        public ActivityItemBean[] newArray(int size) {
            return new ActivityItemBean[size];
        }

        @Override
        public ActivityItemBean createFromParcel(Parcel in) {
            return new ActivityItemBean(in);
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activity_id);
        dest.writeString(this.content);
        dest.writeString(this.detailer);
        dest.writeString(this.expirydate);
        dest.writeString(this.imagejson);
        dest.writeString(this.nickname);
        dest.writeString(this.portraituri);
        dest.writeString(this.theme);
        dest.writeString(this.user_id);
        dest.writeString(this.createdat);
    }
}
