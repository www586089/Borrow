package com.jyx.android.adapter.chat;

/**
 * Created by yiyi on 2015/11/6.
 */
public class RealFlowersItem {
    public String flowersimg;
    public String describe;
    public String price;

    public RealFlowersItem()
    {
        flowersimg = "";
        describe = "";
        price = "";
    }

    public RealFlowersItem(String img, String des, String pr)
    {
        flowersimg = img;
        describe = des;
        price = pr;
    }
}
