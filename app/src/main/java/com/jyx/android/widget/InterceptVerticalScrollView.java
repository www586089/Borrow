package com.jyx.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 拦截竖直方向的触摸事件,用于解决内嵌ViewPager时的滑动冲突
 * Author : Tree
 * Date : 2015-11-06
 */
public class InterceptVerticalScrollView extends ScrollView{
    private GestureDetector mGestureDetector;


    public InterceptVerticalScrollView(Context context) {
        this(context, null);
    }

    public InterceptVerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new ScrollGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果此时滑动方向为水平,则不拦截触摸事件
        if(mGestureDetector.onTouchEvent(ev)){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private class ScrollGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) < Math.abs(distanceX);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return Math.abs(velocityY) < Math.abs(velocityX);
        }
    }
}
