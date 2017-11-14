package com.jyx.android.utils;

import com.jyx.android.model.PhoneFriend;

import java.util.Comparator;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class PhoneFriendPinyinComparator implements Comparator<PhoneFriend> {

    @Override
    public int compare(PhoneFriend lhs, PhoneFriend rhs) {
        if(lhs.getFirstSpell() == null){
            lhs.setFirstSpell("#");
        }
        if(rhs.getFirstSpell() == null){
            rhs.setFirstSpell("#");
        }
        if(lhs.getFirstSpell().equals("#") && rhs.getFirstSpell().equals("#")){
            return 0;
        }

        if (lhs.getFirstSpell().equals("#")) {
            return -1;
        }
        if (rhs.getFirstSpell().equals("#")) {
            return 1;
        }
        return lhs.getFirstSpell().charAt(0) - rhs.getFirstSpell().charAt(0);
    }
}
