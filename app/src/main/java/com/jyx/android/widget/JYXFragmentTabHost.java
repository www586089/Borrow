package com.jyx.android.widget;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * Created by Tonlin on 2015/10/22.
 */
public class JYXFragmentTabHost extends FragmentTabHost {
    private String mCurrentTag;
    private String mNoTabChangedTag;

    public JYXFragmentTabHost(Context context) {
        super(context);
    }

    public JYXFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tag) {
        if (tag.equals(mNoTabChangedTag)) {
            setCurrentTabByTag(mCurrentTag);
        } else {
            super.onTabChanged(tag);
            mCurrentTag = tag;
        }
    }

    public void setNoTabChangedTag(String tag) {
        this.mNoTabChangedTag = tag;
    }
}
