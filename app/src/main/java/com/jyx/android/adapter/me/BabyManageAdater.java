package com.jyx.android.adapter.me;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.fragment.me.BabyManageFragment;
import com.jyx.android.model.ItemListEntity;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 2015/11/9.
 */
public class BabyManageAdater extends ListBaseAdapter {

    private BabyManageFragment context = null;

    static class ViewHolder {
        @Bind(R.id.ll_itemmanage_item)
        LinearLayout mLlItem;
        @Bind(R.id.tv_itemmanage_name)
        TextView mTvName;
        @Bind(R.id.tv_itemmanage_description)
        TextView mTvDescription;
        @Bind(R.id.sdv_itemmanage_1)
        SimpleDraweeView mSdv1;
        @Bind(R.id.sdv_itemmanage_2)
        SimpleDraweeView mSdv2;
        @Bind(R.id.sdv_itemmanage_3)
        SimpleDraweeView mSdv3;
        @Bind(R.id.sdv_itemmanage_4)
        SimpleDraweeView mSdv4;
        @Bind(R.id.sdv_itemmanage_5)
        SimpleDraweeView mSdv5;
        @Bind(R.id.tv_itemmanage_price)
        TextView mTvPrice;
        @Bind(R.id.tv_itemmanage_delete)
        TextView mTvDelete;
        @Bind(R.id.tv_itemmanage_edit)
        TextView mTvEdit;
        @Bind(R.id.tv_turnto)
        TextView mTvTurnto;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public BabyManageAdater(BabyManageFragment context) {
        this.context = context;
    }

    public void delete(int position){
        _data.remove(position);
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_itemmanage);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(_data!=null)
        {
            ItemListEntity infor = null;
            if(position>=0 && _data.size()>position)
            {
                infor = (ItemListEntity)_data.get(position);
            }
            if(infor!=null)
            {
                vh.mTvName.setText(infor.getName());
                vh.mTvDescription.setText(infor.getDiscribe());
                double ddeposit = Double.parseDouble(infor.getPrice());
                ddeposit = ddeposit / 100.00;
                vh.mTvPrice.setText(String.format("¥%1$.2f", ddeposit));
                vh.mTvTurnto.setText(infor.getOperatype_name());
                vh.mSdv1.setVisibility(View.GONE);
                vh.mSdv2.setVisibility(View.GONE);
                vh.mSdv3.setVisibility(View.GONE);
                vh.mSdv4.setVisibility(View.GONE);
                vh.mSdv5.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(infor.getImagejson())){
                    String imageuri;
                    try {
                        JSONArray jsonArray = new JSONArray(infor.getImagejson());
                        if (null != jsonArray && jsonArray.length() > 0) {
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                imageuri = jsonArray.optString(i);
//                                new BabyManageFragment().MyThread(imageuri,infor,i);
                                switch (i)
                                {
                                    case 0:
                                        vh.mSdv1.setImageURI(Uri.parse(imageuri));
                                        vh.mSdv1.setVisibility(View.VISIBLE);
                                        break;
                                    case 1:
                                        vh.mSdv2.setImageURI(Uri.parse(imageuri));
                                        vh.mSdv2.setVisibility(View.VISIBLE);
                                        break;
                                    case 2:
                                        vh.mSdv3.setImageURI(Uri.parse(imageuri));
                                        vh.mSdv3.setVisibility(View.VISIBLE);
                                        break;
                                    case 3:
                                        vh.mSdv4.setImageURI(Uri.parse(imageuri));
                                        vh.mSdv4.setVisibility(View.VISIBLE);
                                        break;
                                    case 4:
                                        vh.mSdv5.setImageURI(Uri.parse(imageuri));
                                        vh.mSdv5.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                OnListItemClickListener clickListener = new OnListItemClickListener(context.getContext(), infor,position);
                vh.mLlItem.setOnClickListener(clickListener);
                vh.mTvDelete.setOnClickListener(clickListener);
                vh.mTvEdit.setOnClickListener(clickListener);
            }
        }


        return convertView;
    }

    private class OnListItemClickListener implements View.OnClickListener{
        private ItemListEntity Info;
        private Context itemcontext;
        private int position;
        public OnListItemClickListener(Context context, ItemListEntity myItemInfo,int position) {
            this.Info = myItemInfo;
            this.itemcontext = context;
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_itemmanage_delete:
                    context.DeleteItem(Info.getItem_id(),position);
                    Log.e("111",111+"");
                    break;
                case R.id.tv_itemmanage_edit:
                    context.EditItem(Info.getItem_id(),Info);
                    break;
                case R.id.ll_itemmanage_item:
                    context.xiangqItem(Info.getItem_id());
                    break;
                default:
                    break;
            }
        }
    }
}

