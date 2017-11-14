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
import com.jyx.android.fragment.me.MyRentalFragment;
import com.jyx.android.model.BorrowInfo;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : Tree
 * Date : 2015-11-10
 */
public class MyRentalAdapter extends ListBaseAdapter {
    private MyRentalFragment context = null;

    private int mType;

    public MyRentalAdapter(MyRentalFragment context, int type) {
        this.context = context;
        mType = type;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        BorrowInfo borrowInfo = (BorrowInfo)_data.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_my_rental, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            if (holder == null)
            {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_my_rental, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
        }

        switch (mType)
        {
            case MyRentalFragment.TYPE_PENDING_PAYMENT:
                holder.mBtnCancel.setVisibility(View.VISIBLE);
                holder.mBtnChangePrice.setVisibility(View.VISIBLE);
                break;
            case MyRentalFragment.TYPE_RENTALED:
                switch (borrowInfo.getOrderstatus())
                {
                    case "2":
                    case "3":
                        holder.mBtnConfirmReturn.setText("发货");
                        holder.mBtnConfirmReturn.setVisibility(View.VISIBLE);
                        break;
                    case "4":
                    case "5":
                        holder.mBtnConfirmReturn.setVisibility(View.GONE);
                        break;
                    case "6":
                        holder.mBtnConfirmReturn.setText("确认归还");
                        holder.mBtnConfirmReturn.setVisibility(View.VISIBLE);
                        break;
                    default:
                        holder.mBtnConfirmReturn.setVisibility(View.GONE);
                }
                break;
            case MyRentalFragment.TYPE_RETURNED:
                break;
        }

        holder.mTvItemname.setText(borrowInfo.getName());
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
            holder.mTvRenttype.setText(borrowInfo.getRenttype_name());
        }
        if(!TextUtils.isEmpty(borrowInfo.getImagejson())){
            holder.mSdvRental.setImageURI(Uri.parse(borrowInfo.getPortraituri()));
        }
        holder.mTvDescription.setText(borrowInfo.getDiscribe());
        holder.mTvPrice.setText(borrowInfo.getAmount());
        holder.mTvStatus.setText(borrowInfo.getStatusname());
        if(!TextUtils.isEmpty(borrowInfo.getPortraituri())){
            holder.mSdvBorrowPerson.setImageURI(Uri.parse(borrowInfo.getPortraituri()));
        }
        holder.mTvBorrowName.setText(borrowInfo.getNickname());

        OnListItemClickListener clickListener = new OnListItemClickListener(context.getContext(), borrowInfo);
        holder.mSdvBorrowPerson.setOnClickListener(clickListener);
        holder.mTvBorrowName.setOnClickListener(clickListener);
        holder.mTvChat.setOnClickListener(clickListener);
        holder.mBtnCancel.setOnClickListener(clickListener);
        holder.mBtnChangePrice.setOnClickListener(clickListener);
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
                case R.id.rl_my_rental:
                    context.ViewOrder(borrowInfo.getOrder_id());
                    break;
                case R.id.sdv_my_rental_person:
                case R.id.tv_my_rental_name:
                    context.ViewOtherUser(borrowInfo.getUser_id());
                    break;
                case R.id.tv_my_rental_chat:
                    context.Chat(borrowInfo.getUser_id());
                    break;
                case R.id.btn_my_rental_cancel:
                case R.id.tv_itemmanage_edit:
                    context.StopOrder(borrowInfo.getOrder_id());
                    break;
                //改价
                case R.id.btn_my_rental_change_price:
                    context.ChangePrice(borrowInfo.getOrder_id(),borrowInfo.getDeposit());

                    break;
                case R.id.btn_my_rental_confirm_return:
                    switch (borrowInfo.getOrderstatus())
                    {
                        case "3":
                            context.SendOrder(borrowInfo.getOrder_id());
                            break;
                        case "6":
                            context.ConfirmReturnOrder(borrowInfo.getOrder_id());
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
        @Bind(R.id.tv_my_rental_orderid)
        TextView mTvOrderid;
        @Bind(R.id.tv_my_rental_ordertime)
        TextView mTvOrdertime;
        @Bind(R.id.tv_my_rental_itemname)
        TextView mTvItemname;
        @Bind(R.id.tv_my_rental_deposit)
        TextView mTvDeposit;
        @Bind(R.id.tv_my_rental_rent)
        TextView mTvRent;
        @Bind(R.id.tv_my_rental_renttype)
        TextView mTvRenttype;
        @Bind(R.id.ll_my_rental_rent)
        LinearLayout mLlRent;
        @Bind(R.id.sdv_rental)
        SimpleDraweeView mSdvRental;
        @Bind(R.id.tv_my_rental_description)
        TextView mTvDescription;
        @Bind(R.id.tv_my_rental_amount)
        TextView mTvPrice;
        @Bind(R.id.tv_my_rental_status)
        TextView mTvStatus;
        @Bind(R.id.sdv_my_rental_person)
        SimpleDraweeView mSdvBorrowPerson;
        @Bind(R.id.tv_my_rental_name)
        TextView mTvBorrowName;
        @Bind(R.id.tv_my_rental_chat)
        TextView mTvChat;
        @Bind(R.id.btn_my_rental_cancel)
        Button mBtnCancel;
        @Bind(R.id.btn_my_rental_change_price)
        Button mBtnChangePrice;
        @Bind(R.id.btn_my_rental_confirm_return)
        Button mBtnConfirmReturn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
