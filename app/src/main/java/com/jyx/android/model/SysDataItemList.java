package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : zfang
 * Date : 2016-01-009
 */
public class SysDataItemList implements ListEntity {
    private List<SysDataItem> itemList = new ArrayList<SysDataItem>();

    public List<SysDataItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SysDataItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public List<?> getList() {
        return itemList;
    }
}
