package com.jyx.android.utils;

import com.jyx.android.model.FriendInfo;

import java.util.Comparator;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class FriendPinyinComparator implements Comparator<FriendInfo> {

    @Override
    public int compare(FriendInfo lhs, FriendInfo rhs) {
        if (lhs.getFirstSpell().equals("#")) {
            return -1;
        }
        if (rhs.getFirstSpell().equals("#")) {
            return 1;
        }
        return lhs.getFirstSpell().charAt(0) - rhs.getFirstSpell().charAt(0);
    }
}
