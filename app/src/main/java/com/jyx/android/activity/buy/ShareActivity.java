package com.jyx.android.activity.buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jyx.android.R;
import com.jyx.android.model.ItemBean;
import com.umeng.social.controller.UmengSocialController;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Dell on 2016/4/15.
 */
public class ShareActivity extends FragmentActivity implements View.OnClickListener{

    private UmengSocialController mController;
    private ItemBean item = null;
    private String image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share);
        mController = new UmengSocialController(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            item = bundle.getParcelable("item");
            String imageJson = item.getImagejson();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(imageJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (null != jsonArray && jsonArray.length() > 0) {
                image = jsonArray.optString(0);
            }

            setShareContent();

            TextView wx = (TextView) findViewById(R.id.share_wechat);
            TextView wb = (TextView) findViewById(R.id.share_weibo);
            TextView qq = (TextView) findViewById(R.id.hare_qq);
            ImageView dissmis = (ImageView) findViewById(R.id.dissmis);
            wx.setOnClickListener(this);
            wb.setOnClickListener(this);
            qq.setOnClickListener(this);
            dissmis.setOnClickListener(this);
        }
    }
    private void setShareContent(){
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent(item.getDiscribe());
        weixinContent.setTitle(item.getNickname());
        weixinContent.setTargetUrl("http://120.76.131.75/JYX/iteminfo?itemid="+item.getItem_id());
        weixinContent.setShareMedia(new UMImage(getBaseContext(),image));
        mController.getService().setShareMedia(weixinContent);
//        http://120.76.131.75/JYX/iteminfo?itemid=179240996989600768
        SinaShareContent sinaContext = new SinaShareContent();
        sinaContext.setShareContent(item.getDiscribe());
        sinaContext.setTargetUrl(" http://120.76.131.75/JYX/iteminfo?itemid=" + item.getItem_id());
        sinaContext.setTitle(item.getNickname());
        sinaContext.setShareMedia(new UMImage(this,image));
        mController.getService().setShareMedia(sinaContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_wechat:
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_weibo:
                performShare(SHARE_MEDIA.SINA);
                break;
            case R.id.hare_qq:
                performShare(SHARE_MEDIA.QQ);
                break;
            case R.id.dissmis:
                finish();
                break;
            default:
                break;

        }
    }

    private void performShare(SHARE_MEDIA platform) {
        if(mController == null){
            return;
        }
        try {
        mController.getService().postShare(this, platform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = platform.toString();
                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
                    showText += "平台分享成功";
                } else {
                    showText += "平台分享失败";
                }
                Toast.makeText(getApplicationContext(), showText, Toast.LENGTH_SHORT).show();
            }
        });
        }catch (Exception e){

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mController != null) {
            mController.onSinaSSOReturn(requestCode, resultCode, data);
        }
    }

}
