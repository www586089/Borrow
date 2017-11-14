package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.jyx.android.R;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.fragment.discovery.RentalGroupFragment;
import com.jyx.android.model.ItemBean;

import io.rong.imkit.RongIM;

/**
 * Created by zfang on 2015/11/01.
 *消息群租借
 */
public class RentalGroupMessageAdapter extends AbsBaseRentalAdapter {

    private  boolean isOpen = true;
    private Context context;
    private RentalGroupFragment contant=null;
    public RentalGroupMessageAdapter(Context context, RentalGroupFragment contant) {
        super(context);
        this.context=context;
        this.contant=contant;
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        final ViewHolder vh = (ViewHolder) holder;
        final ItemBean item = (ItemBean) _data.get(position);
        String operaType = null;
        if (0 != position) {
            if (isOpen) {
                vh.rlGroupRentalOwner.setVisibility(View.VISIBLE);
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_down);
            } else {
                vh.rlGroupRentalOwner.setVisibility(View.GONE);
                vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_up);
            }
        } else {
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
                    SharedPreferences sp=context.getSharedPreferences("user", 0);
                    sp.edit().putString("groupid",item.getGroup_id()).commit();
                    RongIM.getInstance().startGroupChat(context, item.getGroup_id(), item.getGroupName());
                }
            }
        });
    }

    private void setClickListener(final ViewHolder vh, final ItemBean item) {
        vh.llGroupRentalArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_down);
                    isOpen = false;
                    contant.getGroupIdRental(item);
                } else {
                    vh.imgGroupRentalArrow.setImageResource(R.mipmap.icon_arrow_up);
                    isOpen = true;
                    contant.upData();
                }
                notifyDataSetChanged();
            }
        });
    }
}
