package com.jyx.android.adapter.me;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.model.PayDetailInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yiyi on 2015/12/23.
 */
public class MyPayDetailAdapter extends ListBaseAdapter{
    private Context context = null;

    static class ViewHolder {
        @Bind(R.id.tv_my_wallet_item_name)
        TextView tv_name;
        @Bind(R.id.tv_my_wallet_item_price)
        TextView tv_price;
        @Bind(R.id.tv_my_wallet_item_time)
        TextView tv_paytime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MyPayDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_my_wallet);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(_data!=null)
        {
            PayDetailInfo infor = null;
            if(position>=0 && _data.size()>position)
            {
                infor = (PayDetailInfo)_data.get(position);
            }
            if(infor!=null)
            {
                vh.tv_name.setText(infor.getRemark());
                vh.tv_paytime.setText(infor.getPaytime());
                if (infor.getInoutflag() == 1)
                {
                    vh.tv_price.setTextColor(Color.GREEN);
                    vh.tv_price.setText("+" + infor.getAmount());
                }
                else
                {
                    vh.tv_price.setTextColor(Color.RED);
                    vh.tv_price.setText("-" + infor.getAmount());
                }
            }
        }


        return convertView;
    }
}
