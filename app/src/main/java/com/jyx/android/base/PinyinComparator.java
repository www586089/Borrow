package com.jyx.android.base;

import com.jyx.android.adapter.chat.Friend;

import java.util.Comparator;

/**
 * Created by yiyi on 2015/11/9
 */
public class PinyinComparator implements Comparator<Friend> {

	public int compare(Friend o1, Friend o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
