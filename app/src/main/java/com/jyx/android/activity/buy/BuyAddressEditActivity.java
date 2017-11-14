package com.jyx.android.activity.buy;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AddAddressParam;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.ApiManager;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zfang on 2015/10/31.
 */

//这里是中文部分
public class BuyAddressEditActivity extends BaseActivity {

    private String TAG = "BuyAddressEditActivity";
    @Bind(R.id.et_addcard_username)
    EditText et_addcard_username;
    @Bind(R.id.et_buy_address_number)
    EditText et_buy_address_number;
    @Bind(R.id.et_buy_address_info_detail)
    EditText et_buy_address_info_detail;
    private boolean isAdd = true;
    private String addressId = null;

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_address_edit;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.buy_address_edit_title_center;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText(R.string.buy_address_add_item);
        et_addcard_username.setText("");
        et_buy_address_number.setText("");
        et_buy_address_info_detail.setText("");
        getBundleData();
        //添加收货人地址默认为空

    }
    private void getBundleData() {
        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            addressId = bundle.getString("address_id");
            if (!TextUtils.isEmpty(addressId)) {
                isAdd = false;
                setActionRightText(R.string.buy_address_edit);
                et_addcard_username.setText(bundle.getString("name"));
                et_buy_address_number.setText(bundle.getString("phone"));
                et_buy_address_info_detail.setText(bundle.getString("address"));
                Log.e("修改地址",bundle.getString("name")+bundle.getString("phone")+bundle.getString("address"));
            }

        }
    }
    @Override
    protected void onActionRightClick(View view) {
        if (TextUtils.isEmpty(et_buy_address_info_detail.getText())) {
            Application.showToast("要添加的地址不能为空");
            return ;
        }
        AddAddressParam xml = new AddAddressParam();
        xml.setFunction("addmyaddress");
        xml.setUserid(UserRecord.getInstance().getUserId());
        if (!TextUtils.isEmpty(addressId)) {
            xml.setAddressid(addressId);
        } else {
            xml.setAddressid("");
        }
        xml.setAddress(et_buy_address_info_detail.getText().toString());
//        xml.setMobile(UserRecord.getInstance().getUserEntity().getMobilePhoneNumber());
        xml.setMobile(et_buy_address_number.getText().toString());
        xml.setType("2");
        xml.setSar(et_addcard_username.getText().toString());
        String usr=new Gson().toJson(xml).toString();
        Log.e("add",usr);
        Call<BaseEntry<List<Void>>> result = ApiManager.getApi().addMyAddress(new Gson().toJson(xml).toString());
        result.enqueue(new Callback<BaseEntry<List<Void>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<Void>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<Void>> body = response.body();
                    if (0 == body.getResult()) {
                        setResult(Activity.RESULT_OK);
                        finish();
                        String ActionRightTex=getActionRightText();
                        if (ActionRightTex.equalsIgnoreCase("添加"))
                            Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
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
