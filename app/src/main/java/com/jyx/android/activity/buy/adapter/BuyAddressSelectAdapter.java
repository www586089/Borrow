package com.jyx.android.activity.buy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AddressItemBean;

import java.util.List;

/**
 * Created by zfang on 2016-02-27
 */
public class BuyAddressSelectAdapter extends BaseAdapter {

    private String TAG = "PicSelectGridAdapter";
    private OnCheckClistener listener = null;
    private List<AddressItemBean> addressList = null;
    private LayoutInflater mInflater = null;
    private OnCheckClistener checkClistener = null;
    public BuyAddressSelectAdapter(Context context, List<AddressItemBean> addressList, OnCheckClistener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.addressList = addressList;
        this.checkClistener = listener;
    }
    public void setData(List<AddressItemBean> addressList) {
        this.addressList = addressList;
        this.notifyDataSetChanged();
    }
    public OnCheckClistener getListener() {
        return listener;
    }

    public void setListener(OnCheckClistener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return this.addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.list_cell_select_buy_address, parent, false);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.receiver_name),
                    (TextView) convertView.findViewById(R.id.receiver_phone),
                    (TextView) convertView.findViewById(R.id.receiver_address),
                    (RadioButton) convertView.findViewById(R.id.rdoBtn_select_address));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AddressItemBean item = this.addressList.get(position);
        holder.receiver_name.setText(item.gets_a_r());
//        holder.receiver_name.setText(UserRecord.getInstance().getUserEntity().getUserName());//名称
        holder.receiver_phone.setText(item.getMobilephonenumber());//电话
        holder.receiver_address.setText(item.getAddress());//地址

        //选择收货地址
        Log.e("添加地址2", "name" + UserRecord.getInstance().getUserEntity().getUserName()+"..."
        +item.getMobilephonenumber()+"..."+item.getAddress()+"...."+ item.getAddress_id());
        setOnCheckClick(holder.rdoBtn_select_address, position);
        return convertView;
    }

    private void setOnCheckClick(final RadioButton radioButton, final int position) {
        final AddressItemBean addressItemBean = this.addressList.get(position);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (null != checkClistener) {
                        checkClistener.onCheckClick(radioButton, position, addressItemBean);
                    }
                }
            }
        });
    }

    public class ViewHolder{
        public TextView receiver_name;
        public TextView receiver_phone;
        public TextView receiver_address;
        private RadioButton rdoBtn_select_address;
        public ViewHolder(TextView name, TextView phone, TextView address, RadioButton radioButton) {
            this.receiver_address = address;
            this.receiver_phone = phone;
            this.receiver_name = name;
            this.rdoBtn_select_address = radioButton;
        }
    }
    public interface OnCheckClistener {
        public void onCheckClick(RadioButton radioButton, int position, AddressItemBean addressItemBean);
    }
}
