package com.jyx.android.adapter.chat;

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
public class RealFlowersAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView tv_describe;
        TextView tv_price;
        ImageView iv_flower;
        Button btn_give;
    };

    Context mContext;
    public ArrayList<RealFlowersItem> mData;
    LayoutInflater mInflater;
    ViewHolder holder = null;

    public RealFlowersAdapter(Context context)
    {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecords(ArrayList<RealFlowersItem> ra)
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
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.realflowers_item, null);
            holder = new ViewHolder();
            holder.tv_describe = (TextView) convertView
                    .findViewById(R.id.tv_realflowers_describe);
            holder.tv_price = (TextView) convertView
                    .findViewById(R.id.tv_realflowers_price);
            holder.iv_flower = (ImageView) convertView
                    .findViewById(R.id.iv_realflowers_img);
            holder.btn_give = (Button) convertView
                    .findViewById(R.id.btn_realflowers_give);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(mData!=null)
        {
            RealFlowersItem infor = null;
            if(position>=0 && mData.size()>position)
            {
                infor = mData.get(position);
            }
            if(infor!=null)
            {
                holder.tv_describe.setText(infor.describe);
                holder.tv_price.setText(infor.price);
                holder.btn_give.setOnClickListener(new View.OnClickListener() {
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
