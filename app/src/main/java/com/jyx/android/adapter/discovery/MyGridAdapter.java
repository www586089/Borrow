package com.jyx.android.adapter.discovery;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.utils.ImageOptions;
import com.jyx.android.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by user on 2015/10/30.
 * 
 */
public class MyGridAdapter extends ListBaseAdapter {



    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_grid_imag_cell);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final String  url = (String) _data.get(position);

         if(StringUtils.isBlank(url)==false)
         {
             if(url.length()>10) {
//                 Uri uri = Uri.parse(url);
                 ImageLoader.getInstance().displayImage(url, vh.album_image, ImageOptions.get_gushi_Options());
//                 vh.album_image.setLayoutParams(new GridLayout.LayoutParams(100, 100));
//                 vh.draweeView.setImageURI(uri);
             }
         }




    //    vh.album_image.setImageResource(R.mipmap.me_photo_2x_81);
        ///  fill data ...
//        vh.txt.setText(" Position :" + position);


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.album_image)
        ImageView album_image;

//        @Bind(R.id.album_image)
//        SimpleDraweeView draweeView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}


