package com.jyx.android.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.activity.me.mypersonalcenter.WithdrawActivity;
import com.jyx.android.base.AskForResult;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Tonlin on 2015/10/27.
 */
public class MyWalletActivity extends BaseActivity {
    @Bind(R.id.tv_header_my_wallent_balance)
    TextView mTvBalance;

    private String Balance;
    private String user_id;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_my_wallet;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_back_title;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_me_my_wallet;
    }

    @OnClick(R.id.btn_header_my_wallet_cash)
    void OnCashClick()
    {
        //提现
        Intent intent = new Intent(this, WithdrawActivity.class);
        intent.putExtra("balance", Balance);
        startActivityForResult(intent, AskForResult.ASK_WITHDRAW);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Balance = getIntent().getStringExtra("balance");

        mTvBalance.setText(Balance);
        user_id = UserRecord.getInstance().getUserId();

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        MyPayDetailFragment myPayDetailFragment = new MyPayDetailFragment();
//
//        fragmentTransaction.add(R.id.fragment_paydetail, myPayDetailFragment);
//        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String ret_string = "";

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AskForResult.ASK_WITHDRAW:
                if (resultCode == AskForResult.ASK_RET_OK) {
                    if (data != null)
                    {
                        ret_string = data.getStringExtra("balance");
                        if (ret_string != null) {
                            double b = Double.parseDouble(ret_string);
                            b = b / 100.00;
                            Balance = String.format("%.2f", b);
                            mTvBalance.setText(Balance);
                        }
                    }
                }
                break;
        }
    }

}
