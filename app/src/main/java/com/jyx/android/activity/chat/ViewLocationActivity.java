package com.jyx.android.activity.chat;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 1/29/2016.
 */
public class ViewLocationActivity extends BaseActivity{


    @Bind(R.id.bmapView)
    MapView mMapView;

    BaiduMap mBaiduMap;

    double longitude ,latitude;
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        longitude = getIntent().getDoubleExtra("longitude",0);
        latitude = getIntent().getDoubleExtra("latitude",0);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        LatLng point = new LatLng(latitude,longitude);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_mark);
        OverlayOptions options = new MarkerOptions().position(point).icon(icon);
        mBaiduMap.addOverlay(options);
//        定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(12)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_location;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.location;
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
//        // 关闭定位图层
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }
}
