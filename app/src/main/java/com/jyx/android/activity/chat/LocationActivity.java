package com.jyx.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jyx.android.R;
import com.jyx.android.adapter.chat.CommonAdapter;
import com.jyx.android.adapter.chat.ViewHolder;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.provider.BDLocationInfo;
import com.jyx.android.utils.ACache;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 1/28/2016.
 */
public class LocationActivity extends BaseActivity{

    @Bind(R.id.bmapView)
    MapView mMapView;

    BaiduMap mBaiduMap;
    // 定位相关
    LocationClient mLocClient;

    @Bind(R.id.listView)
    ListView listView;

    ACache mCache;

    String KEY_CACHE_LOCATION = "key_cache_location";

    ArrayList<BDLocationInfo> mDataSet;

    public MyLocationListenner myListener = new MyLocationListenner();
    private boolean isFirstLoc = true;
    BDLocationInfo info;
    BDLocationInfo chooseInfo;
    CommonAdapter<BDLocationInfo> mAdapter;

    //选中列表中的某个位置
    int choosePosition;



    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        KEY_CACHE_LOCATION = KEY_CACHE_LOCATION + UserRecord.getInstance().getUserId();
        mCache = ACache.get(this);
        mDataSet = new ArrayList<>();
        mAdapter = new CommonAdapter<BDLocationInfo>(this,mDataSet,R.layout.list_cell_location) {
            @Override
            public void convert(ViewHolder holder, BDLocationInfo bdLocationInfo,int
                    position) {
                holder.setText(R.id.tv_location,bdLocationInfo.getLocation());
                if(position == 0)
                    holder.setText(R.id.tv_description,"[位置]");
                else
                    holder.setText(R.id.tv_description,bdLocationInfo.getDescription());
                if(choosePosition == position){
                    holder.setVisible(R.id.iv_ok,true);
                }else{
                    holder.setVisible(R.id.iv_ok,false);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosePosition =position;
                mAdapter.notifyDataSetChanged();
                //改变地图
                BDLocationInfo location = mDataSet.get(position);

                LatLng point = new LatLng(location.getLatitude(),location.getLongitude());
                mBaiduMap.clear();
                mBaiduMap.addOverlay(new MarkerOptions().position(point)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.mipmap.icon_mark_location)));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
                chooseInfo = location;

            }
        });
        listView.setAdapter(mAdapter);

        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);

        mLocClient.setLocOption(option);
        mLocClient.start();
        setActionRightText(R.string.send);
    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.location;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
//        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onActionRightClick(View view) {
        //写入缓存
        //判断是否选择其他位置
        boolean delete = false;
        if(choosePosition != 0)
            mDataSet.remove(0);
        else{
            for(BDLocationInfo info:mDataSet){
                if(info.getLocation().equals(mDataSet.get(0).getLocation()))
                    delete = true;
            }
        }
        if(delete)
            mDataSet.remove(0);

        mCache.put(KEY_CACHE_LOCATION,mDataSet);
        setResult(RESULT_OK,new Intent().putExtra("location",chooseInfo));
        finish();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (isFirstLoc) {
                info = new BDLocationInfo(location.getAddrStr(),location
                        .getLocationDescribe(),location.getLongitude(),location.getLatitude
                        ());
                List<BDLocationInfo> cacheList = (List<BDLocationInfo>)mCache.getAsObject
                        (KEY_CACHE_LOCATION);
                if(cacheList == null) {
                    cacheList = new ArrayList<>();
                }
                mDataSet.clear();
                mDataSet.add(info);
                mDataSet.addAll(cacheList);
                mAdapter.notifyDataSetChanged();

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();

                mBaiduMap.setMyLocationData(locData);
                isFirstLoc = false;
                chooseInfo = info;

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_mark_location);
                OverlayOptions options = new MarkerOptions().icon(icon).position
                        (ll);
                mBaiduMap.addOverlay(options);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
