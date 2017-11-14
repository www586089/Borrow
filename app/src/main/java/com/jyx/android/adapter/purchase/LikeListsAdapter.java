package com.jyx.android.adapter.purchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;

import java.util.ArrayList;

/**
 * Created by yiyi on 2015/11/6.
 */
public class LikeListsAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView tv_name;
        TextView tv_note;
        ImageView iv_user;
        Button btn_attention;
    };

    Context mContext;
    public ArrayList<LikeListsItem> mData;
    LayoutInflater mInflater;
    ViewHolder holder = null;

    public LikeListsAdapter(Context context)
    {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecords(ArrayList<LikeListsItem> ra)
    {
        mData = ra;
        notifyDataSetChanged();
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.likelists_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_likelists_name);
            holder.tv_note = (TextView) convertView
                    .findViewById(R.id.tv_likelists_note);
            holder.iv_user = (ImageView) convertView
                    .findViewById(R.id.iv_likelists_img);
            holder.btn_attention = (Button) convertView
                    .findViewById(R.id.btn_likelists_attention);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mData!=null)
        {
            LikeListsItem infor = null;
            if(position>=0 && mData.size()>position)
            {
                infor = mData.get(position);
            }
            if(infor!=null)
            {
                holder.tv_name.setText(infor.name);
                holder.tv_note.setText(infor.note);
                if (!infor.attention)
                {
                    holder.btn_attention.setText("+关注");
                }
                else
                {
                    holder.btn_attention.setText("已关注");
                }
                holder.btn_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击按钮
                    }
                });
                //设置图片
            }
        }

        return convertView;
    }
}
