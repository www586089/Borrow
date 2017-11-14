package com.jyx.android.activity.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.BaseActivity;
import com.umeng.social.controller.UmengSocialController;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import butterknife.OnClick;

/**
 * Created by Tonlin on 2015/10/27.
 */
public class PublishSuccessActivity extends BaseActivity{
    private UmengSocialController mController;

    @OnClick(R.id.btn_continue_publish)
    void clickContinuePublish() {
        ActivityHelper.goPublishPage(this);
        finish();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_discovery_publish_success;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_discovery_publish_success;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText(R.string.toolbar_title_action_done);

        mController = new UmengSocialController(this);

        setShareContent();

        findViewById(R.id.tv_share_qq).setOnClickListener(this);
        findViewById(R.id.tv_share_wechat).setOnClickListener(this);
        findViewById(R.id.tv_share_weibo).setOnClickListener(this);
    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }


    @Override
    protected void onActionRightClick(View view) {
        //ActivityHelper.goCowryQuality(PublishSuccessActivity.this);
       ActivityHelper.goMain(PublishSuccessActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share_qq:
                performShare(SHARE_MEDIA.QQ);
                break;
            case R.id.tv_share_wechat:
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_share_weibo:
                performShare(SHARE_MEDIA.SINA);
                break;
        }
    }

    private void setShareContent(){
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent("借一下");
        weixinContent.setTitle("借一下");
        weixinContent.setTargetUrl("WWW.JIEYIXIA.CN");
        mController.getService().setShareMedia(weixinContent);
    }

    private void performShare(SHARE_MEDIA platform) {
        if(mController == null){
            return;
        }
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
                Toast.makeText(PublishSuccessActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mController != null) {
            mController.onSinaSSOReturn(requestCode, resultCode, data);
        }
    }
}
