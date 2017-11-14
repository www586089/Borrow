package com.jyx.android.fragment.chat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.me.MyDisplayFragment;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的界面
 * Author : Tree
 * Date : 2015-11-06
 */
public class OtherFragment extends BaseFragment {
    @Bind(R.id.iv_user_avatar)
    SimpleDraweeView mIvUserAvatar;
    @Bind(R.id.tv_nick_name)
    TextView mTvNickName;
    @Bind(R.id.tv_my_jieyixia_no)
    TextView mTvMyJieyixiaNo;
    @Bind(R.id.tv_my_discrib)
    TextView mTvMyDiscrib;
    @Bind(R.id.tv_my_signature)
    TextView mTvSignature;
    @Bind(R.id.iv_qr_code)
    ImageView mIvQrCode;
    @Bind(R.id.tv_follow)
    TextView mTvFollow;
    @Bind(R.id.tv_fans)
    TextView mTvFans;
    @Bind(R.id.tv_borrowing)
    TextView mTvBorrowing;
    @Bind(R.id.tv_rental)
    TextView mTvRental;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.vp_my_display)
    ViewPager mVpMyDisplay;

    private String[] mTabTitles;
    private MeAdapter mAdapter;
    private String user_id = "";


    @Override
    protected int getLayoutId() {
        return R.layout.other_fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        user_id = UserRecord.getInstance().getUserId();
        Bundle bundle = getActivity().getIntent().getExtras();
        if (null != bundle) {
            user_id = bundle.getString("userid");
        }
    }
    private void queryUserInfor()
    {
//        if (!TextUtils.isEmpty(object_Id)) {
//            AVQuery.doCloudQueryInBackground("select count(*) from userRelation where user_MyId='" + object_Id + "' and type='3'", new CloudQueryCallback<AVCloudQueryResult>() {
//                @Override
//                public void done(AVCloudQueryResult result, AVException e) {
//                    if (e == null) {
//                        Log.d("成功", "查询到" + result.getCount() + "条关注的数据");
//                        mTvFollow.setText(String.valueOf(result.getCount()));
//                    } else {
//                        Log.d("失败", "查询错误: " + e.getMessage());
//                        mTvFollow.setText("0");
//                    }
//                }
//            });
//
//            AVQuery.doCloudQueryInBackground("select count(*) from userRelation where user_FrienId='" + object_Id + "' and type='3'", new CloudQueryCallback<AVCloudQueryResult>() {
//                @Override
//                public void done(AVCloudQueryResult result, AVException e) {
//                    if (e == null) {
//                        Log.d("成功", "查询到" + result.getCount() + "条粉丝的数据");
//                        mTvFans.setText(String.valueOf(result.getCount()));
//                    } else {
//                        Log.d("失败", "查询错误: " + e.getMessage());
//                        mTvFans.setText("0");
//                    }
//                }
//            });
//
//            AVQuery.doCloudQueryInBackground("select count(*) from Order where user_Id='" + object_Id + "' and orderType='1' and status=10", new CloudQueryCallback<AVCloudQueryResult>() {
//                @Override
//                public void done(AVCloudQueryResult result, AVException e) {
//                    if (e == null) {
//                        Log.d("成功", "查询到" + result.getCount() + "条租出的数据");
//                        mTvBorrowing.setText(String.valueOf(result.getCount()));
//                    } else {
//                        Log.d("失败", "查询错误: " + e.getMessage());
//                        mTvBorrowing.setText("0");
//                    }
//                }
//            });
//
//            AVQuery.doCloudQueryInBackground("select count(*) from Order where user_buyer_Id='" + object_Id + "' and orderType='1' and status=10", new CloudQueryCallback<AVCloudQueryResult>() {
//                @Override
//                public void done(AVCloudQueryResult result, AVException e) {
//                    if (e == null) {
//                        Log.d("成功", "查询到" + result.getCount() + "条借入的数据");
//                        mTvRental.setText(String.valueOf(result.getCount()));
//                    } else {
//                        Log.d("失败", "查询错误: " + e.getMessage());
//                        mTvRental.setText("0");
//                    }
//                }
//            });
//
//            AVQuery.doCloudQueryInBackground("select * from _User where objectId='" + object_Id + "'", new CloudQueryCallback<AVCloudQueryResult>() {
//                @Override
//                public void done(AVCloudQueryResult result, AVException e) {
//                    if (e == null) {
//                        Log.d("成功", "查询到" + result.getResults().size() + " 条符合条件的数据");
//                        if (result.getResults().size() > 0) {
//                            Log.d("成功", "查询到user_Id:" + result.getResults().get(0).getObjectId());
//                            String tmp = "";
//                            tmp = result.getResults().get(0).getString("Nickname");
//                            if (tmp == null)
//                            {
//                                tmp = "";
//                            }
//                            if (tmp.trim().equals(""))
//                            {
//                                tmp = "昵称：";
//                            }
//                            mTvNickName.setText(tmp.trim());
//                            tmp = result.getResults().get(0).getString("nameDiscrib");
//                            if (tmp == null)
//                            {
//                                tmp = "";
//                            }
//                            mTvMyDiscrib.setText("描述：" + tmp.trim());
//                            tmp = result.getResults().get(0).getString("signature");
//                            if (tmp == null)
//                            {
//                                tmp = "";
//                            }
//                            mTvSignature.setText("签名：" + tmp.trim());
//
//                            int b = result.getResults().get(0).getInt("balance");
//                            double balance = ((double)b) / 100;
//                            //mTvBalance.setText(String.format("%.2f",balance));
//                            String Portrait = result.getResults().get(0).getString("portraitUri");
//
//                            if (Portrait != null)
//                            {
//                                if (!Portrait.equals(""))
//                                {
//                                    Uri imageUri = Uri.parse(Portrait);
//                                    mIvUserAvatar.setImageURI(imageUri);
//                                }
//                            }
//                        } else {
//                            Log.d("成功", "查询不到数据");
//                            object_Id = "";
//                            mTvNickName.setText("");
//                            mTvMyDiscrib.setText("描述：" + "");
//                            mTvSignature.setText("签名：" + "");
//                            //mTvBalance.setText("0.00");
//                        }
//                    } else {
//                        Log.d("失败", "查询错误: " + e.getMessage());
//                        object_Id = "";
//                        mTvNickName.setText("");
//                        mTvMyDiscrib.setText("");
//                        mTvSignature.setText("");
//                        //mTvBalance.setText("0.00");
//                    }
//                }
//            });
//        }
    }

    private void initView() {
        mVpMyDisplay.setOffscreenPageLimit(3);
        mTabTitles = getResources().getStringArray(R.array.other_tab);
        mAdapter = new MeAdapter(getChildFragmentManager());
        mVpMyDisplay.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpMyDisplay);

        //FIXME
        //mIvUserAvatar.setBackgroundResource(R.mipmap.me_photo_2x_81);
        mTvNickName.setText("老皮匠");
        mTvMyJieyixiaNo.setText(getString(R.string.my_jieyixia_no, "002488"));
        mTvMyDiscrib.setText(getString(R.string.my_remark, "老哥"));
        mTvSignature.setText("微雪刚刚开始融化，天上的月亮倒映水中，撒满半边水池。");
        mIvQrCode.setImageResource(R.drawable.ic_qr_code);
        //mTvBalance.setText(getString(R.string.balance_format, 30.52));

        queryUserInfor();
    }

    /**
     * 添加跳转到我的个人信息页面，Added By 吴益文 (2015-11-07)
     */
    @OnClick(R.id.iv_user_avatar)
    void onClickAvatar() {
        ActivityHelper.goMyPersonalInfo(getActivity());
    }

    @OnClick(R.id.ll_other_info_back)
    void onClickBack() {
        getActivity().finish();
    }

    private class MeAdapter extends FragmentStatePagerAdapter {

        public MeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyDisplayFragment.newInstance(position, UserRecord.getInstance().getUserId());
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }
    }


}
