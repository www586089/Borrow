package com.jyx.android.adapter.purchase;

/**
 * Created by yiyi on 2015/11/6.
 */
public class LikeListsItem {
    public String usrimg;
    public String name;
    public String note;
    public boolean attention;

    public LikeListsItem()
    {
        usrimg = "";
        name = "";
        note = "";
        attention = false;
    }

    public LikeListsItem(String img, String user, String unote, boolean atten)
    {
        usrimg = img;
        name = user;
        note = unote;
        attention = atten;
    }
}
