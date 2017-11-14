package com.jyx.android.model;

/**
 * Created by Administrator on 3/15/2016.
 */
public class WaitFriendEntity {

    /**
     * createdat : 2016-03-15 10:23:08.0
     * portraituri : http://demo.erongchuang
     * .com:8888/JYX/ImageShow?image=154718280492286976.png
     * user_id : 137520969395766272
     * signature : 签名
     * nickname : cai
     * apply_id : 159245044975436800
     * apply_msg : Caixiong
     * applytype : 等待审核
     */

    private String createdat;
    private String portraituri;
    private String user_id;
    private String signature;
    private String nickname;
    private String apply_id;
    private String apply_msg;
    private String applytype;
    private String applytypemsg;

    public String getApplytypemsg() {
        return applytypemsg;
    }

    public void setApplytypemsg(String applytypemsg) {
        this.applytypemsg = applytypemsg;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public void setPortraituri(String portraituri) {
        this.portraituri = portraituri;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public void setApply_msg(String apply_msg) {
        this.apply_msg = apply_msg;
    }

    public void setApplytype(String applytype) {
        this.applytype = applytype;
    }

    public String getCreatedat() {
        return createdat;
    }

    public String getPortraituri() {
        return portraituri;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getSignature() {
        return signature;
    }

    public String getNickname() {
        return nickname;
    }

    public String getApply_id() {
        return apply_id;
    }

    public String getApply_msg() {
        return apply_msg;
    }

    public String getApplytype() {
        return applytype;
    }
}
