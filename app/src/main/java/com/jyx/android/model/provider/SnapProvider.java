package com.jyx.android.model.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.jyx.android.R;
import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;

/**
 * Created by Administrator on 1/25/2016.
 */

public class SnapProvider extends InputProvider.ExtendProvider {

    boolean mBurnAfterRead;

    public SnapProvider(RongContext context) {
        super(context);
    }

    /**
     * 设置展示的图标
     * @param context
     * @return
     */
    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(R.mipmap.icon_snap);
    }

    /**
     * 设置图标下的title
     * @param context
     * @return
     */
    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.snap);
    }

    public boolean ismBurnAfterRead() {
        return mBurnAfterRead;
    }

    public void setmBurnAfterRead(boolean mBurnAfterRead) {
        this.mBurnAfterRead = mBurnAfterRead;
    }

    /**
     * click 事件
     * @param view
     */
    @Override
    public void onPluginClick(View view) {
        mBurnAfterRead = true;
    }

}
