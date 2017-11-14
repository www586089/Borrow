package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class GroupNoticeList implements ListEntity {
    private List<GroupNoticeItemBean> itemlist = new ArrayList<GroupNoticeItemBean>();

    public List<GroupNoticeItemBean> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<GroupNoticeItemBean> itemlist) {
        this.itemlist = itemlist;
    }

    @Override
    public List<?> getList() {
        return itemlist;
    }
}
