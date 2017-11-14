package com.umeng.social.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.OauthUserInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.util.Map;
import java.util.Set;

/**
 * Author : Tree
 * Date : 2015-12-30
 */
public class UmengSocialController {
    private static final String QQ_APP_ID = "100424468";
    private static final String QQ_APP_KEY = "c7394704798a158208a74ab60104f0ba";
    private static final String WECHAT_APP_ID = "wxa30c410979816d12";
    private static final String WECHAT_APP_KEY = "c174cd684e02c923c03fb4f07d181eec";

    private UMSocialService mService;
    private Context mContext;

    public UmengSocialController(Activity act){
        mContext = act;
        mService = UMServiceFactory.getUMSocialService("com.umeng.login");

        //设置新浪SSO handler
        mService.getConfig().setSsoHandler(new SinaSsoHandler());

        //添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(act, QQ_APP_ID, QQ_APP_KEY);
//        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(act, QQ_APP_ID, QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(act, WECHAT_APP_ID, WECHAT_APP_KEY);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(act, WECHAT_APP_ID, WECHAT_APP_KEY);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }


    public void onSinaSSOReturn(int requestCode, int resultCode, Intent data){
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mService.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void doOauthVerify(SHARE_MEDIA shareMedia, final OauthLoginListener listener){
        mService.doOauthVerify(mContext, shareMedia, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if(listener != null){
                    listener.onStart(share_media);
                }
            }

            @Override
            public void onComplete(Bundle bundle, final SHARE_MEDIA share_media) {
                if (bundle != null && !TextUtils.isEmpty(bundle.getString("uid")) && listener != null) {
                    OauthUserInfo userInfo = new OauthUserInfo();
                    userInfo.setUid(bundle.getString("uid"));
                    userInfo.setExpiresTime(bundle.getString("expires_in"));
                    userInfo.setToken(bundle.getString(share_media == SHARE_MEDIA.SINA ? "access_key" : "access_token"));
                    listener.onComplete(share_media, userInfo);
                } else {
                    Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA share_media) {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                if(listener != null){
                    listener.onError(share_media);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();
                if(listener != null){
                    listener.onCancel(share_media);
                }
            }
        });
    }

    public UMSocialService getService() {
        return mService;
    }

    public interface OauthLoginListener{
        void onStart(SHARE_MEDIA platform);

        void onComplete(SHARE_MEDIA platform, OauthUserInfo userInfo);

        void onError(SHARE_MEDIA platform);

        void onCancel(SHARE_MEDIA platform);
    }


}
