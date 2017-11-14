package com.jyx.android.utils;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class PinyinUtils {

    public static String haizi2Pinyin(String src){
        if(TextUtils.isEmpty(src)){
            return "";
        }
        StringBuilder result = new StringBuilder();
        if(!TextUtils.isEmpty(src)){
            for(char c : src.toCharArray()){
                result.append(Pinyin.toPinyin(c).toUpperCase());
            }
        }
        return result.toString();
    }
}
