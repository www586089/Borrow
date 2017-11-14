package com.jyx.android.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zfang on 2015/12/24.
 */
public class NewsFriendsList implements ListEntity {

    private List<NewsFriendsBean> itemList = new ArrayList<NewsFriendsBean>();
    @Override
    public List<?> getList() {
        return itemList;
    }

    public List<NewsFriendsBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<NewsFriendsBean> itemList) {
        this.itemList = itemList;
    }
}
