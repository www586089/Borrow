package com.jyx.android.model;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Author : Tree
 * Date : 2015-11-10
 */
public class BorrowInfo {
    /**
     * imagejson : ["http://localhost:9999/JYX/ImageShow?image=132274026293789696.jpg"]
     * amount : 0
     * portraituri :
     * orderstatus : 10
     * item_id : 153564652079449088
     * user_id : 153561152041812992
     * discribe : 赠送描述
     * name : 赠送测试
     * nickname :
     * statusname : 订单结束
     * ordertime : 2016-02-28 18:30:52.0
     * order_id : 153569583310080000
     */

    private String imagejson;
    private String amount;
    private String portraituri;
    private String orderstatus;
    private String item_id;
    private String user_id;
    private String discribe;
    private String name;
    private String nickname;
    private String statusname;
    private String ordertime;
    private String order_id;
    /**
     * rent : 0
     * operatype : 2
     * deposit : 0
     * renttype_name :
     */

    private String rent;
    private String operatype;
    private String deposit;
    private String renttype_name;

    public void setImagejson(String imagejson) {
        this.imagejson = imagejson;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getImagejson() {
        String imageuri = "";

        if (imagejson != null) {
            try {
                JSONArray jsonArray = new JSONArray(imagejson);
                if (null != jsonArray && jsonArray.length() > 0) {
                    imageuri = jsonArray.optString(0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return imageuri;
    }

    public String getAmount() {
        double damount = Double.parseDouble(this.amount);
        damount = damount / 100.00;

        return String.format("¥%1$.2f", damount);
    }

    public String getPortraituri() {
        return portraituri;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDiscribe() {
        return discribe;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getStatusname() {
        return statusname;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public void setOperatype(String operatype) {
        this.operatype = operatype;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public void setRenttype_name(String renttype_name) {
        this.renttype_name = renttype_name;
    }

    public String getRent() {
        double drent = Double.parseDouble(this.rent);
        drent = drent / 100.00;

        return String.format("¥%1$.2f", drent);
    }

    public String getOperatype() {
        return operatype;
    }

    public String getDeposit() {
        double ddeposit = Double.parseDouble(this.deposit);
        ddeposit = ddeposit / 100.00;

        return String.format("¥%1$.2f", ddeposit);
    }

    public String getRenttype_name() {
        return renttype_name;
    }
}
