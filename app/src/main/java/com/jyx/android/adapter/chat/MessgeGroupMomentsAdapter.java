package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.fragment.chat.MessgeGroupMomentsFragment;
import com.jyx.android.model.NewsFriendsBean;

import io.rong.imkit.RongIM;

/**
 * 群动态
 * Created by gaobo on 2015/11/4.
 */
public class MessgeGroupMomentsAdapter extends AbsBaseDisplayAdapter {

    private Context context;
    private boolean isOpen = true;
    private  MessgeGroupMomentsFragment contant;
    public MessgeGroupMomentsAdapter(Context context, MessgeGroupMomentsFragment contant) {
        super(context);
        this.context=context;
        this.contant=contant;
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }


    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        if (null == _data || (null != _data && 0 ==  _data.size())) {
            return;
        }
        final ViewHolder vh = (ViewHolder) holder;
        final NewsFriendsBean item = (NewsFriendsBean) _data.get(position);
        String operaType = null;
        if (0 != position) {
            if (isOpen) {
                vh.rlGroupRentalOwner.setVisibility(View.VISIBLE);
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_down);
            } else {
                vh.rlGroupRentalOwner.setVisibility(View.GONE);
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_up);
            }
        }
        else {
            vh.rlGroupRentalOwner.setVisibility(View.VISIBLE);
            if (isOpen) {
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_down);
            } else {
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_up);
            }
        }
        setClickListener(vh,item);
        if (!TextUtils.isEmpty(item.getGroupPortraitUri())) {
            vh.iv_group_img.setImageURI(Uri.parse(item.getGroupPortraitUri()));
//            ImageLoader.getInstance().displayImage(item.getGroupPortraitUri(), vh.iv_group_img, ImageOptions.get_portrait_Options() );
        } else {
            vh.imgRentalAvator.setImageResource(R.mipmap.me_photo_2x_81);
        }
        vh.tv_group_name.setText(item.getGroupName());
        vh.iv_group_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RongIM.getInstance() != null) {
                    SharedPreferences sp = context.getSharedPreferences("user", 0);
                    sp.edit().putString("groupid", item.getGroup_id()).commit();
                    RongIM.getInstance().startGroupChat(context, item.getGroup_id(), item.getGroupName());
                }
            }
        });
    }

    private void setClickListener(final ViewHolder vh, final NewsFriendsBean item) {
        vh.llGroupRentalArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_down);
                    isOpen = false;
                    contant.getGroupInfo(item);
                } else {
                    vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_up);
                    isOpen = true;

                    contant.getGroupList();
                }
                notifyDataSetChanged();
            }
        });
    }
}
