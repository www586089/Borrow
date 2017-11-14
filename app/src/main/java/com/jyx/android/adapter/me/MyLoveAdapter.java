package com.jyx.android.adapter.me;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.fragment.me.MyLoveFragment;
import com.jyx.android.model.MyLoveItem;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyLoveAdapter extends ListBaseAdapter {
    private MyLoveFragment context = null;
    private MyLoveItem infor = null;
    static class ViewHolder {
        @Bind(R.id.ll_my_love_item)
        LinearLayout mLlLoveItem;
        @Bind(R.id.sdv_my_love_person)
        SimpleDraweeView mSdvUser;
        @Bind(R.id.tv_my_love_username)
        TextView mTvUsername;
        @Bind(R.id.tv_my_love_describe)
        TextView mTvDescribe;
        @Bind(R.id.sdv_my_loveitem1)
        SimpleDraweeView mSdvItem1;
        @Bind(R.id.sdv_my_loveitem2)
        SimpleDraweeView mSdvItem2;
        @Bind(R.id.sdv_my_loveitem3)
        SimpleDraweeView mSdvItem3;
        @Bind(R.id.sdv_my_loveitem4)
        SimpleDraweeView mSdvItem4;
        @Bind(R.id.sdv_my_loveitem5)
        SimpleDraweeView mSdvItem5;
        @Bind(R.id.tv_itemmanage_delete)
        TextView mTvDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public MyLoveAdapter(MyLoveFragment context) {
        this.context = context;
    }

    public void delete(int position){
            _data.remove(position);
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_my_love);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(_data!=null)
        {
            if(position>=0 && _data.size()>position)
            {
                infor = (MyLoveItem)_data.get(position);
            }
            if(infor!=null)
            {
                if(!TextUtils.isEmpty(infor.getPortraituri())){
                    vh.mSdvUser.setImageURI(Uri.parse(infor.getPortraituri()));
                }
                vh.mTvUsername.setText(infor.getNickname());
                vh.mTvDescribe.setText(infor.getDiscribe());
                vh.mSdvItem1.setVisibility(View.GONE);
                vh.mSdvItem2.setVisibility(View.GONE);
                vh.mSdvItem3.setVisibility(View.GONE);
                vh.mSdvItem4.setVisibility(View.GONE);
                vh.mSdvItem5.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(infor.getImagejson())){
                    String imageuri;
                    try {
                        JSONArray jsonArray = new JSONArray(infor.getImagejson());
                        if (null != jsonArray && jsonArray.length() > 0) {
                            for (int i=0; i<jsonArray.length() && i<5; i++)
                            {
                                imageuri = jsonArray.optString(i);
                                switch (i)
                                {
                                    case 0:
                                        vh.mSdvItem1.setImageURI(Uri.parse(imageuri));
                                        vh.mSdvItem1.setVisibility(View.VISIBLE);
                                        break;
                                    case 1:
                                        vh.mSdvItem2.setImageURI(Uri.parse(imageuri));
                                        vh.mSdvItem2.setVisibility(View.VISIBLE);
                                        break;
                                    case 2:
                                        vh.mSdvItem3.setImageURI(Uri.parse(imageuri));
                                        vh.mSdvItem3.setVisibility(View.VISIBLE);
                                        break;
                                    case 3:
                                        vh.mSdvItem4.setImageURI(Uri.parse(imageuri));
                                        vh.mSdvItem4.setVisibility(View.VISIBLE);
                                        break;
                                    case 4:
                                        vh.mSdvItem5.setImageURI(Uri.parse(imageuri));
                                        vh.mSdvItem5.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                OnListItemClickListener clickListener = new OnListItemClickListener(context.getContext(), infor,position);
                vh.mLlLoveItem.setOnClickListener(clickListener);
                vh.mTvDelete.setOnClickListener(clickListener);
            }
        }


        return convertView;
    }

    private class OnListItemClickListener implements View.OnClickListener{
        private MyLoveItem Info;
        private Context itemcontext;
        private int position;
        public OnListItemClickListener(Context context, MyLoveItem myLoveInfo,int position) {
            this.Info = myLoveInfo;
            this.itemcontext = context;
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_my_love_item:
                    context.ViewMyLove(Info.getLtype(), Info.getId());
                    break;
                case R.id.tv_itemmanage_delete:
                    context.DeleteItem(Info.getId(),position);
                    break;
                default:
                    break;
            }

        }
    }
}
