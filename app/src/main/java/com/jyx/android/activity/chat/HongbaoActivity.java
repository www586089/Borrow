package com.jyx.android.activity.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2/17/2016.
 */
public class HongbaoActivity extends BaseActivity{


    @Bind(R.id.tv_send)
    TextView mTvSend;

    @Bind(R.id.et_sum)
    EditText mEtSum;

    @Bind(R.id.et_description)
    EditText mEtDesc;

    @Bind(R.id.tv_sum)
    TextView mTvSum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hongbao;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.send_hongbao;
    }


    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        mEtSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    mTvSum.setText("￥"+s.toString());
            }
        });

        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = mEtDesc.getText().toString();
                if(TextUtils.isEmpty(desc)){
                    desc="恭喜发财，大吉大利";
                }
                String sum = mEtSum.getText().toString();
                double money = Double.parseDouble(sum);
                if(TextUtils.isEmpty(sum)){
                    showToast("请输入金额");
                    return;
                }
                setResult(RESULT_OK,new Intent().putExtra("description",desc).putExtra
                        ("sum",money).putExtra("number",1).putExtra("isGroup",0));
                finish();
            }
        });
    }
}
