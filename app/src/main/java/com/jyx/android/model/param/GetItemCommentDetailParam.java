package com.jyx.android.model.param;

/**
 * Author : zfang
 * Date : 2016-01-24
 */
public class GetItemCommentDetailParam {
    private String function = null;
    private String itemid = null;
    private int pagenumber = -1;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public int getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(int pagenumber) {
        this.pagenumber = pagenumber;
    }
}
