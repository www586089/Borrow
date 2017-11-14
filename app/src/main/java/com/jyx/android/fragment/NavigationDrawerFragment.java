package com.jyx.android.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.UserCenterEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by user on 2015/10/23.
 */

public class NavigationDrawerFragment  extends BaseFragment {
    @Bind(R.id.iv_me_photo)
    SimpleDraweeView mIvPhoto;
    @Bind(R.id.tv_me_nickname)
    TextView mTvNickname;
    @Bind(R.id.tv_me_borrow)
    TextView mTvBorrow;
    @Bind(R.id.tv_me_leased)
    TextView mTvLeased;
    @Bind(R.id.tv_me_wallet)
    TextView mTvWallet;

    private String user_id = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        user_id = UserRecord.getInstance().getUserId();
        queryUserInfor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me_draw, container, false);

        ButterKnife.bind(this, view);
//        view.findViewById(R.id.tv_test).setOnClickListener(this);
//        view.findViewById(R.id.tv_bindcard_test).setOnClickListener(this);
//        view.findViewById(R.id.tv_redenvelope_send).setOnClickListener(this);
//        view.findViewById(R.id.tv_software).setOnClickListener(this);
//        view.findViewById(R.id.tv_event).setOnClickListener(this);
//        view.findViewById(R.id.tv_shake).setOnClickListener(this);
//        view.findViewById(R.id.tv_scan).setOnClickListener(this);
//        view.findViewById(R.id.order_purchase).setOnClickListener(this);
//        view.findViewById(R.id.bt_mymoney).setOnClickListener(this);
//        initViews(view);
        return view;
    }

    private void queryUserInfor()
    {
        String xmlString = "";
        if (!user_id.equals(""))
        {
            xmlString = "{\"function\":\"getusercenter\",\"userid\":\"" + user_id + "\"}";
            ApiManager.getApi()
                    .getUserCenter(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<UserCenterEntity>>, UserCenterEntity>() {
                        @Override
                        public UserCenterEntity call(BaseEntry<List<UserCenterEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.load_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                            }
                            return listBaseEntry.getData().get(0);
                        }
                    })
                    .subscribe(new Subscriber<UserCenterEntity>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(UserCenterEntity userCenterEntity) {
                            mTvBorrow.setText(userCenterEntity.getRental());
                            mTvLeased.setText(userCenterEntity.getBorrow());
                            mTvNickname.setText(userCenterEntity.getNickname());

                            int b = Integer.parseInt(userCenterEntity.getBalance());
                            double balance = ((double) b) / 100;
                            mTvWallet.setText(String.format("%.2f", balance));
                            String Portrait = userCenterEntity.getPortraituri();

                            if (Portrait != null) {
                                if (!Portrait.equals("")) {
                                    Uri imageUri = Uri.parse(Portrait);
                                    mIvPhoto.setImageURI(imageUri);
                                }
                            }

                        }
                    });
        }
    }

    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.tv_test:
//                Application.showToastShort("成色测试");
//                ActivityHelper.goTest(this.getContext());
//                break;
//            case R.id.tv_bindcard_test:
//                Application.showToastShort("添加银行卡");
//                ActivityHelper.goAddCard(this.getContext());
//                break;
//            case R.id.tv_redenvelope_send:
//                Application.showToastShort("支付密码");
//                //ActivityHelper.goRedEnvelope(this.getContext());
//                ActivityHelper.goPayThePassword(this.getContext());
//                break;
        }
    }

//    @OnClick(R.id.tv_test)
//    void clickTest() {
//        Application.showToastShort("成色测试23");
//        ActivityHelper.goTest(this.getContext());
//
//    }

    @OnClick(R.id.ly_borrow)
    void lvborrm(){
        ActivityHelper.goBarrowHaving(this.getContext());
    }

    @OnClick(R.id.ly_loan)
    void lvloan(){
        ActivityHelper.goMyRental(this.getContext());
    }

    @OnClick(R.id.ly_wallet)
    void lvwallet(){
        ActivityHelper.goMyWallet(this.getContext(), mTvWallet.getText().toString().trim());
    }

    //我的借出
  @OnClick(R.id.tv_my_loan)
    void loanTest() {
//        Application.showToastShort("成色测试23");    ActivityHelper.goMyRental(this.getContext());
        ActivityHelper.goMyRental(this.getContext());

    }

    //我的借入
    @OnClick(R.id.tv_my_barrow)
    void loanBarrow() {
//        Application.showToastShort("成色测试23");
        ActivityHelper.goBarrowHaving(this.getContext());

    }

    //我的收藏
    @OnClick(R.id.tv_my_love)
    void clickMyLove() {
        ActivityHelper.goMyLove(this.getContext());
    }

    //tv_setting
    @OnClick(R.id.tv_setting)
    void clickSetting() {
        ActivityHelper.goSetting(this.getContext());
    }
    //我的个人信息
    @OnClick(R.id.ll_myperson_infor)
    void clickMyPerson() {
        ActivityHelper.goMyPersonalInfo(this.getContext());

    }

    //我的钱包
    @OnClick(R.id.tv_my_wallent)
    void clickMyWallent() {
        ActivityHelper.goMyWallet(this.getContext(), mTvWallet.getText().toString().trim());

    }

    //我的宝贝管理
    @OnClick(R.id.tv_baby_management)
    void clickManagement() {
        ActivityHelper.goBabyManagement(this.getContext());

    }

    //退出登录
    @OnClick(R.id.btn_medraw_logout)
    void clickLogout() {
        ActivityHelper.goSignIn(this.getContext());
        getActivity().finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        queryUserInfor();
    }
}
