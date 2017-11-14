package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/29.
 */
public class AddressItemList implements ListEntity{
    private List<AddressItemBean> itemList = new ArrayList<AddressItemBean>();
    @Override
    public List<?> getList() {
        return itemList;
    }

    public List<AddressItemBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<AddressItemBean> itemList) {
        this.itemList = itemList;
    }
}
