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
 * Created by Administrator on 2/18/2016.
 */
@MessageTag(value = "RC:TransferMsg", flag = MessageTag.ISCOUNTED | MessageTag
        .ISPERSISTED)
public class TransferMessage extends MessageContent {

    //描述
    String description;
    //金额
    double sum;


    public String getDescription() {
        return description;
    }


    public double getSum() {
        return sum;

    }


    public TransferMessage(String description, double sum) {
        this.description = description;
        this.sum = sum;
    }

    public TransferMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            description = jsonObj.optString("description");
            sum = jsonObj.optDouble("sum");

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("description", description);
            jsonObj.put("sum", sum);
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
    public TransferMessage(Parcel in) {
        description = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        sum = ParcelUtils.readDoubleFromParcel(in);
        //这里可继续增加你消息的属性
    }


    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<TransferMessage> CREATOR = new Creator<TransferMessage>
            () {

        @Override
        public TransferMessage createFromParcel(Parcel source) {
            return new TransferMessage(source);
        }

        @Override
        public TransferMessage[] newArray(int size) {
            return new TransferMessage[size];
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
        ParcelUtils.writeToParcel(dest, sum);
        //这里可继续增加你消息的属性
    }
}