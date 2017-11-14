package com.jyx.android.widget.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 3/30/2016.
 */
public class MyViewPager extends ViewPager{

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}