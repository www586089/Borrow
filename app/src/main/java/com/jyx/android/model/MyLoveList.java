package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyLoveList implements ListEntity {
    private List<MyLoveItem> itemlist = new ArrayList<MyLoveItem>();

    public List<MyLoveItem> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<MyLoveItem> itemlist) {
        this.itemlist = itemlist;
    }

    @Override
    public List<?> getList() {
        return itemlist;
    }
}
