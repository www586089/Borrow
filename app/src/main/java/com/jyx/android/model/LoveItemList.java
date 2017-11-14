package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : zfang
 * Date : 2016-01-009
 */
public class LoveItemList implements ListEntity {
    private List<LoveItemBean> itemList = new ArrayList<LoveItemBean>();

    public List<LoveItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<LoveItemBean> itemList) {
        this.itemList = itemList;
    }

    @Override
    public List<?> getList() {
        return itemList;
    }
}
