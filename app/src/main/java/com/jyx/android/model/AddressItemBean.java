package com.jyx.android.model;

public class AddressItemBean {
    private String mobilephonenumber;
    private String address;
    private String address_id;
    private String name;
    private int type_id;
    private  String s_a_r;


    public String gets_a_r() {
        return s_a_r;
    }

    public void sets_a_r(String s_a_r) {
        this.s_a_r = s_a_r;
    }


    public String getMobilephonenumber() {
        return mobilephonenumber;
    }

    public void setMobilephonenumber(String mobilephonenumber) {
        this.mobilephonenumber = mobilephonenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}
