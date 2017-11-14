package com.jyx.android.activity.chat.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by Administrator on 2/17/2016.
 */
@MessageTag(value = "RC:LuckyMoneyMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class HongbaoMessage extends MessageContent{

    //描述
    String description;
    //金额
    double sum;
    //个数
    int number;
    //是否群红包 1 为群红包
    int isGroup;
    //红包id
    String redPacketsId;


    public String getRedPacketsId() {
        return redPacketsId;
    }


    public void setRedPacketsId(String redPacketsId) {
        this.redPacketsId = redPacketsId;
    }


    public String getDescription() {
        return description;
    }

    public int getNumber() {
        return number;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public double getSum() {
        return sum;

    }


    public HongbaoMessage(String description,double sum,int number,int
            isGroup,String redPacketsId) {
        this.description = description;
        this.isGroup = isGroup;
        this.number = number;
        this.sum = sum;
        this.redPacketsId = redPacketsId;
    }

    public HongbaoMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            description = jsonObj.optString("description");
            sum = jsonObj.optDouble("sum");
            number = jsonObj.optInt("number");
            isGroup = jsonObj.optInt("isGroup");
            redPacketsId = jsonObj.optString("redPacketsId");
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

    }
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("description", description);
            jsonObj.put("sum",sum);
            jsonObj.put("number",number);
            jsonObj.put("isGroup",isGroup);
            jsonObj.put("redPacketsId",redPacketsId);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    //给消息赋值。
    public HongbaoMessage(Parcel in) {
        description=ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        sum = ParcelUtils.readDoubleFromParcel(in);
        number = ParcelUtils.readIntFromParcel(in);
        isGroup = ParcelUtils.readIntFromParcel(in);
        redPacketsId = ParcelUtils.readFromParcel(in);
        //这里可继续增加你消息的属性
    }


    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<HongbaoMessage> CREATOR = new Creator<HongbaoMessage>() {

        @Override
        public HongbaoMessage createFromParcel(Parcel source) {
            return new HongbaoMessage(source);
        }

        @Override
        public HongbaoMessage[] newArray(int size) {
            return new HongbaoMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, description);//该类为工具类，对消息中属性进行序列化
        ParcelUtils.writeToParcel(dest,sum);
        ParcelUtils.writeToParcel(dest,number);
        ParcelUtils.writeToParcel(dest,isGroup);
        ParcelUtils.writeToParcel(dest,redPacketsId);
        //这里可继续增加你消息的属性
    }
}
