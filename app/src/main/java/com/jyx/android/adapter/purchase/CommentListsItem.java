package com.jyx.android.adapter.purchase;

/**
 * Created by yiyi on 2015/11/6.
 */
public class CommentListsItem {
    public String usrimg;
    public String name;
    public String comment;
    public String time_str;

    public CommentListsItem()
    {
        usrimg = "";
        name = "";
        comment = "";
        time_str = "";
    }

    public CommentListsItem(String img, String user, String comm, String ti)
    {
        usrimg = img;
        name = user;
        comment = comm;
        time_str = ti;
    }
}
