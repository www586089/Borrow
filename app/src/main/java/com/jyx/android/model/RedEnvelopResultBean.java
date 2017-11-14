package com.jyx.android.model;

import java.io.Serializable;

/**
 * Created by Administrator on 3/24/2016.
 */
public class RedEnvelopResultBean implements Serializable{
    /**
     *   {"result":"0","msg":"操作成功", "datas":[{"isreceive":"0",
     *   "portraituri" :"http://demo.erongchuang
     *   .com:8888/JYX/ImageShow?image=154718280492286976.png",
     *   "nickname":"cai","overtime":"0", "userid":"137520969395766272"}]}
     */
    String isreceive;
    String overtime;
    String receivetime;
    String userid;
    String portraituri;
    String nickname;
    double sum;


    public double getSum() {
        return sum;
    }


    public void setSum(double sum) {
        this.sum = sum;
    }


    public String getIsreceive() {
        return isreceive;
    }


    public void setIsreceive(String isreceive) {
        this.isreceive = isreceive;
    }


    public String getOvertime() {
        return overtime;
    }


    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }


    public String getReceivetime() {
        return receivetime;
    }


    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime;
    }


    public String getUserid() {
        return userid;
    }


    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getPortraituri() {
        return portraituri;
    }


    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
