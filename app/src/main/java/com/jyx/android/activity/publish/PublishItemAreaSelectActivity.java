package com.jyx.android.activity.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.fragment.publish.SelectItemAreaFragment;
import com.jyx.android.model.AddressItemBean;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zfang on 2016-01-12.
 */
public class PublishItemAreaSelectActivity extends BaseActivity {

    private String TAG = "PublishItemAreaSelectActivity";
    private SelectItemAreaFragment fragment = null;
    private LocationClient mLocClient;
    private String locationAddress = null;

    @Bind(R.id.tv_publish_select_item_location)
    TextView tv_publish_select_item_location;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_select_item_area;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle("地址管理");
        setActionRightText("");
        fragment = (SelectItemAreaFragment) getSupportFragmentManager().findFragmentByTag("SelectItemAreaFragment");
        initLocation();
    }

    private void initLocation() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                locationAddress = bdLocation.getAddrStr();
                tv_publish_select_item_location.setText(bdLocation.getAddrStr());
                mLocClient.stop();
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }
    @OnClick(R.id.ll_publish_add_address)
    void clickAddress() {
        Bundle bundle = new Bundle();
        bundle.putString("locationAddress", locationAddress);
        ActivityHelper.goAddAddress(this, bundle, 1000);
    }

    @OnClick(R.id.ll_publish_location_address)
    void clickLocationAddress() {
        AddressItemBean item = new AddressItemBean();
        item.setAddress(locationAddress);
        doFinish(item);
    }
    private void doFinish(AddressItemBean item) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("code", item.getAddress_id());
        bundle.putString("address", item.getAddress());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 1000:
                    fragment.refresh();
                    break;
            }
        }
    }
}
