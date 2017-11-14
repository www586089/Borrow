package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/29.
 */
public class ItemCommentDetailBeanList implements ListEntity{
    private List<ItemCommentDetailBean> itemList = new ArrayList<ItemCommentDetailBean>();
    @Override
    public List<?> getList() {
        return itemList;
    }

    public List<ItemCommentDetailBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemCommentDetailBean> itemList) {
        this.itemList = itemList;
    }
}
