package com.jyx.android.activity.me;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.jyx.android.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by yiyi on 2015/12/27.
 */
public class PhotoviewActivity extends Activity {
    private String photoUri;
    private PhotoView photoView;
    private ImageLoader imageLoad;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        photoUri = getIntent().getStringExtra("photoUri");

        PhotoView photoView = (PhotoView) findViewById(R.id.pv_photo);

        this.imageLoad = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .displayer(new SimpleBitmapDisplayer())
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        this.imageLoad.getInstance().displayImage(photoUri, photoView, options);
        //ImageLoader.getInstance().displayImage(photoUri, photoView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
