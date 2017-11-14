package com.jyx.android.adapter.event;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.fragment.discovery.EventFragment;
import com.jyx.android.model.ActivityItemBean;
import com.jyx.android.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动adapter
 * Created by px96004@qq.com on 2015/10/31.
 */
public class EventAdapter extends RecycleBaseAdapter {


    private LayoutInflater inflater = null;
    public EventAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }
    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        if (EventFragment.ITEM_TYPE_SPECIAL == viewType) {
            return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_event_special, null);
        } else {
            return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_event, null);
        }
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        if (EventFragment.ITEM_TYPE_SPECIAL == viewType) {
            return new ViewHolderSpe(viewType, view);
        } else {
            return new ViewHolder(viewType, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ActivityItemBean item = (ActivityItemBean) _data.get(position);
        if (EventFragment.ITEM_TYPE_SPECIAL == item.getItemType()) {
            return EventFragment.ITEM_TYPE_SPECIAL;
        } else {
            return EventFragment.ITEM_TYPE_NORMAL;
        }
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        if (EventFragment.ITEM_TYPE_SPECIAL == getItemViewType(position)) {
            ViewHolderSpe vh = (ViewHolderSpe) holder;
            final ActivityItemBean item = (ActivityItemBean) _data.get(position);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            vh.list_cell_event_item_ll.setLayoutParams(lp);
            vh.list_cell_event_item_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityHelper.goEnrollActivity(v.getContext());
                }
            });
        } else {
            ViewHolder vh = (ViewHolder) holder;
            final ActivityItemBean item = (ActivityItemBean) _data.get(position);
            Date date = null;
            String createTimeStr = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(item.getExpirydate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            createTimeStr = sdf.format(date);
            vh.tv_event_cell_time.setText(createTimeStr);
            String url = item.getImagejson();
            if (!TextUtils.isEmpty(url)) {
                ImageLoader.getInstance().displayImage(url, vh.iv_event_cell_img, ImageOptions.get_gushi_Options());
            }

            vh.rl_event_cell_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("itemActivityBean", item);
                    ActivityHelper.goActivityDetail(v.getContext(), bundle);
                }
            });
        }
    }

    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.tv_event_cell_time)
        TextView tv_event_cell_time;
        @Bind(R.id.iv_event_cell_img)
        ImageView iv_event_cell_img;
        @Bind(R.id.rl_event_cell_detail)
        RelativeLayout rl_event_cell_detail;


        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderSpe extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.list_cell_event_item_ll)
        LinearLayout list_cell_event_item_ll;
        @Bind(R.id.tv_event_special_tv)
        TextView tv_event_special_tv;


        public ViewHolderSpe(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }
}
