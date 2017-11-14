package com.jyx.android.adapter.discovery;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.model.PraisenItemInfoBean;
import com.jyx.android.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by user on 2015/10/30.
 * 
 */
public class LoveMyAdapter extends ListBaseAdapter {

    private Context context;
    public LoveMyAdapter(Context context){
        this.context=context;
    }
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_grid_imag_love);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final PraisenItemInfoBean item = (PraisenItemInfoBean) _data.get(position);
        String url=item.getPortraituri();
         if(StringUtils.isBlank(url)==false)
         {
             if(url.length()>10) {
//                 Uri uri = Uri.parse(url);
                 vh.album_image.setImageURI(Uri.parse(url));
//                 ImageLoader.getInstance().displayImage(url, vh.album_image, ImageOptions.get_gushi_Options());
//                 vh.album_image.setLayoutParams(new GridLayout.LayoutParams(100, 100));
//                 vh.draweeView.setImageURI(uri);
             }
         }
        vh.album_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.goUserInfoActivity(context, item.getUser_Id());
            }
        });



    //    vh.album_image.setImageResource(R.mipmap.me_photo_2x_81);
        ///  fill data ...
//        vh.txt.setText(" Position :" + position);


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.album_image)
        SimpleDraweeView album_image;

//        @Bind(R.id.album_image)
//        SimpleDraweeView draweeView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


