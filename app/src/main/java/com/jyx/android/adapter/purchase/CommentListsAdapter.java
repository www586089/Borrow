package com.jyx.android.adapter.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;

import java.util.ArrayList;

/**
 * Created by yiyi on 2015/11/6.
 */
public class CommentListsAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView tv_name;
        TextView tv_comment;
        ImageView iv_user;
        TextView tv_time;
    };

    Context mContext;
    public ArrayList<CommentListsItem> mData;
    LayoutInflater mInflater;
    ViewHolder holder = null;

    public CommentListsAdapter(Context context)
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        }
        return convertView;
    }

}
