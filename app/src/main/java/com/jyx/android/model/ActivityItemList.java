package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2016-03-12
 */
public class ActivityItemList implements ListEntity{
    private List<ActivityItemBean> itemList = new ArrayList<ActivityItemBean>();
    @Override
    public List<?> getList() {
        return itemList;
    }

    public List<ActivityItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ActivityItemBean> itemList) {
        this.itemList = itemList;
    }
}
