package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class BabyManageList implements ListEntity {
    private List<ItemListEntity> itemlist = new ArrayList<ItemListEntity>();

    public List<ItemListEntity> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<ItemListEntity> itemlist) {
        this.itemlist = itemlist;
    }

    @Override
    public List<?> getList() {
        return itemlist;
    }
}
