package com.jyx.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 3/24/2016.
 */
public class GroupRedEnvelopResultBean implements Parcelable{
    /**
     "user_id": "132512379702379520",
     "nickname": "",
     "receivetime": "2016-03-23 14:53:26.0"
     */
    String user_id;
    String nickname;
    String portraituri;
    String receivetime;
    double sum;


    public String getUser_id() {
        return user_id;
    }


    public void setUser_id(String user_id) {
        this.user_id = user_id;
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


    public String getReceivetime() {
        return receivetime;
    }


    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }


    public double getSum() {
        return sum;
    }


    public void setSum(double sum) {
        this.sum = sum;
    }


    public GroupRedEnvelopResultBean(String user_id, String nickname, String portraituri, String receivetime, double sum) {
        this.user_id = user_id;
        this.nickname = nickname;
        this.portraituri = portraituri;
        this.receivetime = receivetime;
        this.sum = sum;
    }


    @Override public int describeContents() { return 0; }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.nickname);
        dest.writeString(this.portraituri);
        dest.writeString(this.receivetime);
        dest.writeDouble(this.sum);
    }


    public GroupRedEnvelopResultBean() {}


    protected GroupRedEnvelopResultBean(Parcel in) {
        this.user_id = in.readString();
        this.nickname = in.readString();
        this.portraituri = in.readString();
        this.receivetime = in.readString();
        this.sum = in.readDouble();
    }


    public static final Creator<GroupRedEnvelopResultBean> CREATOR
            = new Creator<GroupRedEnvelopResultBean>() {
        @Override
        public GroupRedEnvelopResultBean createFromParcel(Parcel source) {
            return new GroupRedEnvelopResultBean(source);
        }


        @Override
        public GroupRedEnvelopResultBean[] newArray(int size) {return new GroupRedEnvelopResultBean[size];}
    };
}
