package com.jyx.android.activity.buy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.buy.adapter.BuyAddressSelectAdapter;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AddressItemBean;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetAddressParam;
import com.jyx.android.model.UserEntity;
import com.jyx.android.net.ApiManager;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by zfang on 2016-02-23.
 */
public class BuyAddressSelectActivity extends BaseActivity {

    private String TAG = "BuyAddressSelectActivity";
    @Bind(R.id.buy_address_listview)
    ListView buy_address_listview;
    private BuyAddressSelectAdapter selectAddressAdapter = null;
    private List<AddressItemBean> addressList = null;
    private UserEntity userEntity=new UserEntity();
    SharedPreferences sp;
    private BuyAddressSelectAdapter.OnCheckClistener onCheckClickListener = new BuyAddressSelectAdapter.OnCheckClistener() {
        @Override
        public void onCheckClick(RadioButton radioButton, int position, AddressItemBean addressItemBean) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("address_id", addressItemBean.getAddress_id());
            bundle.putString("address", addressItemBean.getAddress());
            bundle.putString("phone", addressItemBean.getMobilephonenumber());
//            bundle.putString("name", UserRecord.getInstance().getNickName());
            bundle.putString("name", addressItemBean.gets_a_r());
            intent.putExtras(bundle);
            sp=getSharedPreferences("address",0);
            sp.edit().putString("name", addressItemBean.gets_a_r())
                    .putString("address", addressItemBean.getAddress())
                    .putString("phone", addressItemBean.getMobilephonenumber())
                    .putString("address_id", addressItemBean.getAddress_id()).commit();
            //收货人地址
            Log.e("添加地", "name" + UserRecord.getInstance().getNickName() + "..."
                    + addressItemBean.getMobilephonenumber() + "..."
                    + addressItemBean.getAddress() + "...." + addressItemBean.getAddress_id());
            setResult(RESULT_OK, intent);

            finish();

        }
    };
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_address_select;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.buy_address_select_title_center;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    //添加地址
    protected void onActionRightClick(View view) {
        super.onActionRightClick(view);
        if (null != addressList && addressList.size() > 0) {
            AddressItemBean addressItemBean = (AddressItemBean) selectAddressAdapter.getItem(0);
            Bundle bundle = new Bundle();
            bundle.putString("address", addressItemBean.getAddress());
            bundle.putString("phone", UserRecord.getInstance().getUserEntity().getMobilePhoneNumber());
            bundle.putString("name", addressItemBean.gets_a_r());
            Log.e("添加地址0", "name" + addressItemBean.getAddress() + "..."
                    + UserRecord.getInstance().getUserEntity().getMobilePhoneNumber() + "..."
                    + UserRecord.getInstance().getUserEntity().getNickName());
            ActivityHelper.goBuyEditAddress(BuyAddressSelectActivity.this, bundle);
        } else {
            ActivityHelper.goBuyEditAddress(this);
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText(R.string.buy_address_add);
        initListView();
    }
        //修改地址
    private void initListView() {
        buy_address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressItemBean addressItemBean = (AddressItemBean) selectAddressAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("address_id", addressItemBean.getAddress_id());
                bundle.putString("address", addressItemBean.getAddress());
                bundle.putString("phone", addressItemBean.getMobilephonenumber());
                bundle.putString("name", addressItemBean.gets_a_r());
                Log.e("添加地址1", "name" + UserRecord.getInstance().getNickName() + "..."
                        + addressItemBean.getMobilephonenumber() + "..."
                        + addressItemBean.getAddress());
                ActivityHelper.goBuyEditAddress(BuyAddressSelectActivity.this, bundle);
            }
        });
    }

    private void initAdapter(List<AddressItemBean> addressList) {
        this.addressList = addressList;
        if (null == selectAddressAdapter) {
            selectAddressAdapter = new BuyAddressSelectAdapter(this, addressList, onCheckClickListener);
            buy_address_listview.setAdapter(selectAddressAdapter);
        } else {
            selectAddressAdapter.setData(addressList);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }


    private void getData() {
        GetAddressParam xml = new GetAddressParam();
        xml.setFunction("getmyaddress");
        xml.setType("2");
        xml.setUserid(UserRecord.getInstance().getUserId());
        String xmlStr = new Gson().toJson(xml).toString();
        Call<BaseEntry<List<AddressItemBean>>> result = ApiManager.getApi().getMyAddress(xmlStr);
        result.enqueue(new Callback<BaseEntry<List<AddressItemBean>>>() {
            @Override
            public void onResponse(retrofit2.Response<BaseEntry<List<AddressItemBean>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<AddressItemBean>> body = response.body();
                    if (0 == body.getResult()) {
                        initAdapter(body.getData());
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
            }
        });
    }
 }
