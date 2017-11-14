package com.jyx.android.activity.chat.redenvelope;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.utils.PreferenceHelper;
import com.sea_monster.common.Md5;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yiyi on 2015/10/30.
 * 支付密码
 */
public class PayThePasswordActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.btn_paythepassword_one)
    Button mBtnOne;
    @Bind(R.id.btn_paythepassword_two)
    Button mBtnTwo;
    @Bind(R.id.btn_paythepassword_three)
    Button mBtnThree;
    @Bind(R.id.btn_paythepassword_four)
    Button mBtnFour;
    @Bind(R.id.btn_paythepassword_five)
    Button mBtnFive;
    @Bind(R.id.btn_paythepassword_six)
    Button mBtnSix;
    @Bind(R.id.btn_paythepassword_seven)
    Button mBtnSeven;
    @Bind(R.id.btn_paythepassword_eight)
    Button mBtnEight;
    @Bind(R.id.btn_paythepassword_nine)
    Button mBtnNine;
    @Bind(R.id.btn_paythepassword_zero)
    Button mBtnZero;
    @Bind(R.id.btn_paythepassword_del)
    Button mBtnDel;
    @Bind(R.id.btn_paythepassword_clear)
    Button mBtnClear;

    private LinearLayout layout_input;

    private Context ctx;

    //6位密码输入框
    private TextView[] textViews = new TextView[6];

    @Override
    protected int getLayoutId() {
        return R.layout.activity_paythepassword;
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
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
        ctx = this;

        layout_input = (LinearLayout)findViewById(R.id.lv_paythepassword_input);

        initTextViews();

        mBtnOne.setOnClickListener(this);
        mBtnTwo.setOnClickListener(this);
        mBtnThree.setOnClickListener(this);
        mBtnFour.setOnClickListener(this);
        mBtnFive.setOnClickListener(this);
        mBtnSix.setOnClickListener(this);
        mBtnSeven.setOnClickListener(this);
        mBtnEight.setOnClickListener(this);
        mBtnNine.setOnClickListener(this);
        mBtnZero.setOnClickListener(this);
        mBtnDel.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);
    }

    /**
     * @description:初始化密码输入框
     * @user yiyi
     */
    private void initTextViews() {
        for (int i = 0; i < textViews.length; i++) {
            textViews[i] = new TextView(ctx);
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
            params.weight = 1;
            textViews[i].setLayoutParams(params);
            layout_input.addView(textViews[i]);
            textViews[i].setTransformationMethod(PasswordTransformationMethod.getInstance());
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(30);
            textViews[i].setTextColor(Color.BLACK);
            if (i < textViews.length - 1) {
                View view = new View(ctx);
                LinearLayout.LayoutParams viewParams = new LayoutParams((int) ctx.getResources().getDimension(R.dimen.width_input_tv_cutline), LayoutParams.FILL_PARENT);
                view.setLayoutParams(viewParams);
                view.setBackgroundColor(Color.parseColor("#e8e8e8"));
                layout_input.addView(view);
            }
        }
    }

    /**
     * @description:输入密码，如果密码长度超过6位，超出部分会被忽略
     * @user yiyi
     * @param code
     */
    private void inputTextView(String code) {
        for (int i = 0; i < textViews.length; i++) {
            TextView tv = textViews[i];
            if (tv.getText().toString().equals("")) {
                tv.setText(code);
                return;
            }
        }
    }

    /**
     * @description:删除最后一位密码
     * @user yiyi
     */
    private void deleteTextView() {
        for (int i = textViews.length - 1; i >= 0; i--) {
            TextView tv = textViews[i];
            if (!tv.getText().toString().equals("")) {
                tv.setText("");
                return;
            }
        }
    }

    /**
     * @description:清空输入的密码
     * @user yiyi
     */
    private void clearTextView() {
        for (int i = textViews.length - 1; i >= 0; i--) {
            TextView tv = textViews[i];
            tv.setText("");
        }
    }

    /**
     * @description:获取输入的密码
     * @user yiyi
     */
    private String getPassword()
    {
        String password = "";
        for (int i = 0; i < textViews.length; i++) {
            TextView tv = textViews[i];
            if (tv.getText().toString().equals("")) {
                break;
            }
            password = password + tv.getText().toString();
        }

        return password;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String code = "";
        int IType = 0;

        switch (v.getId()) {
            case R.id.btn_paythepassword_one:
                IType = 1;
                code = "1";
                break;
            case R.id.btn_paythepassword_two:
                IType = 1;
                code = "2";
                break;
            case R.id.btn_paythepassword_three:
                IType = 1;
                code = "3";
                break;
            case R.id.btn_paythepassword_four:
                IType = 1;
                code = "4";
                break;
            case R.id.btn_paythepassword_five:
                IType = 1;
                code = "5";
                break;
            case R.id.btn_paythepassword_six:
                IType = 1;
                code = "6";
                break;
            case R.id.btn_paythepassword_seven:
                IType = 1;
                code = "7";
                break;
            case R.id.btn_paythepassword_eight:
                IType = 1;
                code = "8";
                break;
            case R.id.btn_paythepassword_nine:
                IType = 1;
                code = "9";
                break;
            case R.id.btn_paythepassword_zero:
                IType = 1;
                code = "0";
                break;
            case R.id.btn_paythepassword_del:
                IType = 2;
                break;
            case R.id.btn_paythepassword_clear:
                IType = 3;
                break;
        }

        switch(IType)
        {
            case 1:
                inputTextView(code);
                break;
            case 2:
                deleteTextView();
                break;
            case 3:
                clearTextView();
                break;
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_paythepassword;
    }

    @OnClick(R.id.btn_paythepassword_ok)
    void clickOk() {
        String password = getPassword();
        if (password.length() < 6) {
            SweetAlertDialog sd;
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("输入支付密码长度不够，请输入6位支付密码");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            return;
        }else if(!Md5.encode(password).equals(PreferenceHelper.readString(this,
                Constants.FILE_KEY_PASSWORD,Constants.KEY_PASSWORD))){
            SweetAlertDialog sd;
            sd = new SweetAlertDialog(this);
            sd.setTitleText("提示信息");
            sd.setConfirmText("确定");
            sd.setContentText("输入支付密码错误，请重新输入");
            sd.changeAlertType(SweetAlertDialog.WARNING_TYPE);
            sd.setCancelable(true);
            sd.setCanceledOnTouchOutside(true);
            sd.show();
            clearTextView();
            return;
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("password", password);
        bundle.putBoolean("isCancelPay", false);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);// 放入回传的值,并添加一个Code,方便区分返回的数据
        finish();
    }

    @OnClick(R.id.btn_paythepassword_cancel)
    void clickcancel() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isCancelPay", true);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
