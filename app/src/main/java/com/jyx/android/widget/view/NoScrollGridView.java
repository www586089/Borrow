package com.jyx.android.widget.view;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by user on 2015/10/30.
 */
public class NoScrollGridView extends GridView {

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}