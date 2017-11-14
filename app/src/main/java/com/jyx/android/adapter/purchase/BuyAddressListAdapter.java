package com.jyx.android.adapter.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zfang on 2016-02-23
 */
public class BuyAddressListAdapter extends BaseAdapter {
    Context mContext;
    public ArrayList<CommentListsItem> mData;
    LayoutInflater mInflater;
    ViewHolder holder = null;

    public BuyAddressListAdapter(Context context)
    {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecords(ArrayList<CommentListsItem> ra)
    {
        mData = ra;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        if(mData==null)
        {
            return 0;
        }

        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {/*
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.commentlists_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_commentlists_name);
            holder.tv_comment = (TextView) convertView
                    .findViewById(R.id.tv_commentlists_comment);
            holder.iv_user = (ImageView) convertView
                    .findViewById(R.id.iv_commentlists_img);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_commentlists_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mData != null) {
            CommentListsItem infor = null;
            if (position >= 0 && mData.size() > position) {
                infor = mData.get(position);
            }
            if (infor != null) {
                holder.tv_name.setText(infor.name);
                holder.tv_comment.setText(infor.comment);
                holder.tv_time.setText(infor.time_str);
                //设置图片
            }
        }*/
        return convertView;
    }

    private class ViewHolder {
        TextView receiver_name;
        TextView receiver_phone;
        TextView receiver_address;
        RadioButton rdoBtn_select_address;
    };

}
