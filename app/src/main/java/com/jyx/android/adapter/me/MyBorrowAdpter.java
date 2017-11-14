package com.jyx.android.adapter.me;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.fragment.me.MyBorrowFragment;
import com.jyx.android.model.BorrowInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2015/11/9.
 */
public class MyBorrowAdpter extends ListBaseAdapter {
    private MyBorrowFragment context = null;

    private int mType;

    public MyBorrowAdpter(MyBorrowFragment context, int type) {
        this.context = context;
        mType = type;
    }



    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        BorrowInfo borrowInfo = (BorrowInfo)_data.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_my_borrow, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            if (holder == null)
            {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_my_borrow, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
        }

        switch (mType)
        {
            case MyBorrowFragment.TYPE_PENDING_PAYMENT:
                holder.mBtnStop.setVisibility(View.VISIBLE);
                holder.mBtnPay.setVisibility(View.VISIBLE);
                break;
            case MyBorrowFragment.TYPE_BORROWED:
                switch (borrowInfo.getOrderstatus())
                {
                    case "2":
                    case "3":
                        holder.mBtnConfirmReturn.setVisibility(View.GONE);
                        break;
                    case "4":
                        holder.mBtnConfirmReturn.setText("确认收货");
                        holder.mBtnConfirmReturn.setVisibility(View.VISIBLE);
                        break;
                    case "5":
                        holder.mBtnConfirmReturn.setText("归还");
                        holder.mBtnConfirmReturn.setVisibility(View.VISIBLE);
                        break;
                    default:
                        holder.mBtnConfirmReturn.setVisibility(View.GONE);
                }

                break;
            case MyBorrowFragment.TYPE_RETURNED:
                break;
        }

        if(!TextUtils.isEmpty(borrowInfo.getImagejson())){
            holder.mSdvBorrowGood.setImageURI(Uri.parse(borrowInfo.getImagejson()));
        }
        holder.mTvName.setText(borrowInfo.getName());
        holder.mTvOrderid.setText(borrowInfo.getOrder_id());
        holder.mTvOrdertime.setText(borrowInfo.getOrdertime());
        if (!borrowInfo.getOperatype().trim().equals("1"))
        {
            holder.mLlRent.setVisibility(View.GONE);
        }
        else
        {
            holder.mLlRent.setVisibility(View.VISIBLE);
            holder.mTvDeposit.setText(borrowInfo.getDeposit());
            holder.mTvRent.setText(borrowInfo.getRent());
        }
        holder.mTvDescription.setText(borrowInfo.getDiscribe());
        holder.mTvPrice.setText(borrowInfo.getAmount());
        holder.mTvStatus.setText(borrowInfo.getStatusname());
        if(!TextUtils.isEmpty(borrowInfo.getPortraituri())){
            holder.mSdvPerson.setImageURI(Uri.parse(borrowInfo.getPortraituri()));
        }
        holder.mTvBorrowName.setText(borrowInfo.getNickname());

        OnListItemClickListener clickListener = new OnListItemClickListener(context.getContext(), borrowInfo);
        holder.mSdvPerson.setOnClickListener(clickListener);
        holder.mTvBorrowName.setOnClickListener(clickListener);
        holder.mTvChat.setOnClickListener(clickListener);
        holder.mBtnStop.setOnClickListener(clickListener);
        holder.mBtnPay.setOnClickListener(clickListener);
        holder.mBtnConfirmReturn.setOnClickListener(clickListener);
        convertView.setOnClickListener(clickListener);
        return convertView;
    }

    private class OnListItemClickListener implements View.OnClickListener{
        private BorrowInfo borrowInfo;
        private Context itemcontext;

        public OnListItemClickListener(Context context, BorrowInfo borrowInfo) {
            this.borrowInfo = borrowInfo;
            this.itemcontext = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_my_borrow:
                    context.ViewOrder(borrowInfo.getOrder_id());
                    break;
                case R.id.sdv_my_borrow_person:
                case R.id.tv_my_borrow_name:
                    context.ViewOtherUser(borrowInfo.getUser_id());
                    break;
                case R.id.tv_my_borrow_chat:
                    context.Chat(borrowInfo.getUser_id());
                    break;
                case R.id.btn_my_borrow_stop:
                    context.StopOrder(borrowInfo.getOrder_id());
                    break;
                case R.id.btn_my_borrow_pay:
                    context.PayOrder(borrowInfo.getOrder_id());
                    break;
                case R.id.btn_my_borrow_confirm_return:
                    switch (borrowInfo.getOrderstatus())
                    {
                        case "4":
                            context.ReceiveOrder(borrowInfo.getOrder_id());
                            break;
                        case "5":
                            context.ReturnOrder(borrowInfo.getOrder_id());
                            break;
                    }
                    break;
                default:
                    break;
            }

        }
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_cell_my_rental.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_my_borrow_orderid)
        TextView mTvOrderid;
        @Bind(R.id.tv_my_borrow_ordertime)
        TextView mTvOrdertime;
        @Bind(R.id.tv_my_borrow_itemname)
        TextView mTvName;
        @Bind(R.id.tv_my_borrow_deposit)
        TextView mTvDeposit;
        @Bind(R.id.tv_my_borrow_rent)
        TextView mTvRent;
        @Bind(R.id.tv_my_borrow_renttype)
        TextView mTvRenttype;
        @Bind(R.id.ll_my_borrow_rent)
        LinearLayout mLlRent;
        @Bind(R.id.sdv_my_borrow_good)
        SimpleDraweeView mSdvBorrowGood;
        @Bind(R.id.tv_my_borrow_description)
        TextView mTvDescription;
        @Bind(R.id.tv_my_borrow_price)
        TextView mTvPrice;
        @Bind(R.id.tv_my_borrow_status)
        TextView mTvStatus;
        @Bind(R.id.sdv_my_borrow_person)
        SimpleDraweeView mSdvPerson;
        @Bind(R.id.tv_my_borrow_name)
        TextView mTvBorrowName;
        @Bind(R.id.tv_my_borrow_chat)
        TextView mTvChat;
        @Bind(R.id.btn_my_borrow_stop)
        Button mBtnStop;
        @Bind(R.id.btn_my_borrow_pay)
        Button mBtnPay;
        @Bind(R.id.btn_my_borrow_confirm_return)
        Button mBtnConfirmReturn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

