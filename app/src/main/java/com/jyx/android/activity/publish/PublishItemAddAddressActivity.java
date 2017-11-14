package com.jyx.android.activity.publish;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.AddAddressParam;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.net.ApiManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zfang on 2015-12-30.
 */
public class PublishItemAddAddressActivity extends BaseActivity {

    private String TAG = "PublishItemAreaSelectActivity";
    @Bind(R.id.edit_add_address)
    EditText edit_add_address;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_add_address;
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
        setActionBarTitle("添加地址");
        setActionRightText("保存");
        getIntentData();
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            String locationAddress = bundle.getString("locationAddress");
            if (null != locationAddress) {
                edit_add_address.setText(locationAddress);
            }
        }
    }
    @Override
    protected void onActionRightClick(View view) {
        if (TextUtils.isEmpty(edit_add_address.getText())) {
            Application.showToast("要添加的地址不能为空");
            return ;
        }
        AddAddressParam xml = new AddAddressParam();
        xml.setFunction("addmyaddress");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setAddressid("");
        xml.setAddress(edit_add_address.getText().toString());
        xml.setMobile(UserRecord.getInstance().getUserEntity().getMobilePhoneNumber());
        xml.setType("1");
        String xmlStr = new Gson().toJson(xml).toString();
        String xmlUtf8 = null;
        try {
            xmlUtf8 = new String(xmlStr.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Application.showToast("不支付的编码转换");
            return;
        }
        Call<BaseEntry<List<Void>>> result = ApiManager.getApi().addMyAddress(xmlUtf8);
        result.enqueue(new Callback<BaseEntry<List<Void>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<Void>>> response) {
                Log.e(TAG, "onResponse");
                if (response.isSuccess()) {
                    BaseEntry<List<Void>> body = response.body();
                    if (0 == body.getResult()) {
                        setResult(Activity.RESULT_OK);
                        finish();
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
