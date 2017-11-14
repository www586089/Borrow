package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2016-01-17
 */
public class ItemBeanList implements ListEntity{
    private List<ItemBean> itemList = new ArrayList<ItemBean>();
    @Override
    public List<?> getList() {
        return itemList;
    }

    public List<ItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemBean> itemList) {
        this.itemList = itemList;
    }
}
