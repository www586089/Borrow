package com.jyx.android.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.ItemBean;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Dell on 2016/5/31.
 * 申请加入群界面
 */
public class AddGroupActivity extends BaseActivity{
    private ItemBean item;
    @Bind(R.id.iv_group_logo)
    ImageView iv_group_logo;
    @Bind(R.id.tv_group_name)
    TextView tv_group_name;
    @Bind(R.id.btn_addgroup)
    Button btn_addgroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_group;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_addgroup;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        final Bundle bundle =intent.getExtras();
        Log.e("img",bundle.getString("img"));
        ImageLoader.getInstance().displayImage(bundle.getString("img"),iv_group_logo, ImageOptions.get_portrait_Options());
        tv_group_name.setText(bundle.getString("name"));
        final String groupid =bundle.getString("groupid");
        final String applymsg="申请加入"+bundle.getString("name")+"群";
        btn_addgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String  param = "{\"function\":\"applyjoingroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                        + "\",\"groupid\":\"" + groupid + "\",\"applymsg\":\""+applymsg+"\"}";
            ApiManager.getApi()
                    .commonQuery(param)
            .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new ResultConvertFunc<List<Void>>())
                    .subscribe(new Subscriber<List<Void>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    CommonExceptionHandler.handleBizException(e);
                }

                @Override
                public void onNext(List<Void> voids) {
                    showToast("申请成功");
                }
            });
            }
        });
    }
}
