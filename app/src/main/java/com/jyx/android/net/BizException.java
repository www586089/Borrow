package com.jyx.android.net;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public class BizException extends RuntimeException{
    private int code;

    public BizException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
