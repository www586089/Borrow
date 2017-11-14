package com.jyx.android.base;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jyx.android.event.RongCloudEvent;
import com.jyx.android.utils.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import io.rong.imkit.RongIM;

/**
 * Created by Tonlin on 2015/10/22.
 */
public class Application extends BaseApplication {

    //leancloud
    String appId = "voi8VkO6QeGAehq9pxSxD7XE";// "vqoyzl9cx3krrex3g8kouzv5nl2y8h9ie0shtqkqoozg98hz";
    String appKey = "oPMmr6iJLgoY6oisWpPbJgna";// "739c9co8e233cnwn0yfgbknkj11es21m25pznszey0rtxf56";
    private String WECHAT_KEY = "";


    private SharedPreferences mPreferences;

    private static Application mDemoContext;
    public static IWXAPI iwxapi;
    public static Application getInstance() {
//        if (mDemoContext == null) {
//            mDemoContext = new Application();
//        }
        return mDemoContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //http初始化 用于登录、注册使用
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDemoContext=this;
        Fresco.initialize(this);

        //初始化融云
        initRongCloud();
        /**d
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);

        initImageLoader(this);
        SDKInitializer.initialize(this);
        regToWx();
    }
    private void regToWx(){
        iwxapi= WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        iwxapi.registerApp(Constants.WX_APP_ID);
    }
    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }




    /**
     * 初始化融云25wehl3uwkkaw
     */
    public void initRongCloud() {
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
//        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
//                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
//        }
    }



    public static void initImageLoader(Context context) {
        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .displayer(new CircleBitmapDisplayer())
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();


        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .defaultDisplayImageOptions(displayOptions).build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


}
