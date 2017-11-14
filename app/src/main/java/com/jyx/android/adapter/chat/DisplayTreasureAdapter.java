package com.jyx.android.adapter.chat;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.jyx.android.base.RecycleBaseAdapter;

/**
 * Created by cooldemo on 2015/11/05.
 */
public class DisplayTreasureAdapter extends AbsBaseDisplayAdapter {

    private Animation animRightIn,animRightOut;//按钮伸缩动画
    private Context context = null;
    public DisplayTreasureAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        final ViewHolder vh = (ViewHolder) holder;
        vh.rlGroupRentalOwner.setVisibility(View.GONE);
    }
}
