package com.jyx.android.model;

/**
 * Created by caixiong on 15/12/18.
 */


import java.util.ArrayList;
import java.util.List;

public class UserRelationList implements ListEntity {

    public List<UserRelation> getItemlist() {
        return userrelationlist;
    }

    public void setItemlist(List<UserRelation> userrelationlist) {
        this.userrelationlist = userrelationlist;
    }

    private List<UserRelation> userrelationlist = new ArrayList<UserRelation>();

    @Override
    public List<?> getList() {
        return userrelationlist;
    }
}
