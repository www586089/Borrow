package com.jyx.android.model;

/**
 * Created by yiyi on 2015/12/23.
 */
public class PayDetailInfo {
    private String remark;
    private String paytime;
    private String amount;
    private int inoutflag;
    private String pay_id;

    public void setRemark(String r)
    {
        remark = r;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setPaytime(String t)
    {
        paytime = t;
    }

    public String getPaytime()
    {
        return paytime;
    }

    public void setAmount(String a) {
        this.amount = a;
    }

    public void setInoutflag(int inoutflag) {
        this.inoutflag = inoutflag;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getAmount() {
        double da = Double.parseDouble(amount);
        da = da / 100.00;

        return String.format("%.2f", da);
    }

    public int getInoutflag() {
        return inoutflag;
    }

    public String getPay_id() {
        return pay_id;
    }
}
