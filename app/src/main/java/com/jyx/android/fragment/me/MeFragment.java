package com.jyx.android.fragment.me;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.MySqlistHelpe;
import com.jyx.android.activity.QrCodeActivity;
import com.jyx.android.activity.contact.ApplyFriendActivity;
import com.jyx.android.activity.me.ChangeBackground;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.contact.FriendFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.UserCenterEntity;
import com.jyx.android.model.UserEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.ACache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 我的界面
 * Author : Tree
 * Date : 2015-11-06
 */
public class MeFragment extends BaseFragment {
    public static final String KEY_USER_ID = "user_id";

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
    @Bind(R.id.tv_balance)
    TextView mTvBalance;
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
    @Bind(R.id.rl_me_bg)
    RelativeLayout vRlMeBg;
    @Bind(R.id.btn_delete_friend)
    Button mBtnDeleteFriend;
    @Bind(R.id.btn_chat)
    Button mBtnChat;
    @Bind(R.id.ll_relation)
    LinearLayout mLlRelation;
    @Bind(R.id.ll_money)
    LinearLayout mLlMoney;
    @Bind(R.id.btn_follow)
    Button mFollow;
    @Bind(R.id.ll_rent)
    LinearLayout mLlrental;



    private String[] mTabTitles;
    private MeAdapter mAdapter;
    private String mUserId = "";
    private String Userid="";
    private String mUserAvatarUri = "";
    private int mRelation;
    private ImageLoader imageLoad;
    private DisplayImageOptions options;
    private String user_background_uri;

    private SQLiteDatabase db;
    private ACache mAcache;
    private UserCenterEntity CenterEntity;

    public static MeFragment newInstance(Bundle bundle){
        MeFragment fragment = new MeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void queryUserInfo(final String userId) {
//        showWaitDialog();
        String xmlString = "{\"function\":\"getusercenter\",\"userid\":\"" + userId + "\"}";
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
//                        hideWaitDialog();
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(final UserCenterEntity userCenterEntity) {
                        CenterEntity=userCenterEntity;
//                        hideWaitDialog();
                        mTvFollow.setText(userCenterEntity.getFollow());
                        mTvFans.setText(userCenterEntity.getFans());
                        mTvBorrowing.setText(userCenterEntity.getBorrow());
                        mTvRental.setText(userCenterEntity.getRental());
                        mTvNickName.setText(userCenterEntity.getNickname());
                        mTvMyDiscrib.setText(userCenterEntity.getNamediscrib());
                        mTvSignature.setText(userCenterEntity.getSignature());
                        int b = Integer.parseInt(userCenterEntity.getBalance());
                        double balance = ((double) b) / 100;
                        mTvBalance.setText(String.format("%.2f", balance));
                        Userid = userCenterEntity.getUser_id();
                        if (!TextUtils.isEmpty(userCenterEntity.getPortraituri())) {
                            mUserAvatarUri = userCenterEntity.getPortraituri();
                            mIvUserAvatar.setImageURI(Uri.parse(mUserAvatarUri));
                        }
                        user_background_uri = userCenterEntity.getBackgrounduri();
                        if (!TextUtils.isEmpty(user_background_uri)) {
                            imageLoad.loadImage(user_background_uri, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    vRlMeBg.setBackgroundDrawable(new BitmapDrawable(loadedImage));
                                }
                            });
                        }
                        db = new MySqlistHelpe(getActivity()).getReadableDatabase();


                        String friends = null;
                        ArrayList<UserEntity> arrayList = new ArrayList
                                <UserEntity>();
                        arrayList = (ArrayList<UserEntity>) mAcache.getAsObject(Constants.KEY_USER);
                        if (arrayList!=null&&arrayList.size() != 0) {
                            for (UserEntity userEntity : arrayList) {
                                if (userEntity.getUserId().equals(userCenterEntity.getUser_id())) {
                                    mBtnDeleteFriend.setText("删除好友");
                                }
                            }
                        }
                        Cursor cursor = db.rawQuery("select * from follow where userid=?", new String[]{Userid});
                        while (cursor.moveToNext()) {
                                friends = cursor.getString(cursor.getColumnIndex("userid"));
                            }
                            if (friends.equals(userCenterEntity.getUser_id())) {
                                mFollow.setText("取消关注");
                            }
                            cursor.close();

                    }
                });
    }


    private void initView() {
        if(getArguments() != null){
            mUserId = TextUtils.isEmpty(getArguments().getString(KEY_USER_ID)) ?
                    UserRecord.getInstance().getUserId() : getArguments().getString(KEY_USER_ID);
            mRelation = getArguments().getInt(FriendFragment.KEY_PAGE_TYPE);
        }else {
            mUserId = UserRecord.getInstance().getUserId();
        }
        if(UserRecord.getInstance().getUserId().equals(mUserId)){
            mLlMoney.setVisibility(View.VISIBLE);
            mLlRelation.setVisibility(View.GONE);
            mLlrental.setVisibility(View.VISIBLE);
            mTabTitles = getResources().getStringArray(R.array.me_tab);
            vRlMeBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getContext(), ChangeBackground.class);
                    startActivity(intent);
                }
            });
        }else {
            mLlMoney.setVisibility(View.GONE);
            mLlRelation.setVisibility(View.VISIBLE);
            mLlrental.setVisibility(View.GONE);
            mTabTitles = getResources().getStringArray(R.array.other_tab);

        }
         mAcache=ACache.get(getActivity());

        mVpMyDisplay.setOffscreenPageLimit(3);
        mAdapter = new MeAdapter(getChildFragmentManager());
        mVpMyDisplay.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVpMyDisplay);

        this.imageLoad = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .displayer(new SimpleBitmapDisplayer())
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        mTvFollow.setText("0");
        mTvFans.setText("0");
        mTvBorrowing.setText("0");
        mTvRental.setText("0");
        mTvNickName.setText("昵称：");
        mTvMyDiscrib.setText("描述：");
        mTvSignature.setText("个人签名：");
        mTvBalance.setText("0.00");
        mTvMyJieyixiaNo.setText(getString(R.string.my_jieyixia_no, ""));
        mIvQrCode.setImageResource(R.drawable.ic_qr_code);
        //二维码
        queryUserInfo(mUserId);
    }

    @OnClick({R.id.iv_user_avatar, R.id.ll_balance, R.id.ll_mybank, R.id.btn_delete_friend, R.id.btn_chat,R.id.btn_follow,
            R.id.ll_borrowing,R.id.ll_rental,R.id.iv_qr_code})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_user_avatar:
                if(!TextUtils.isEmpty(mUserAvatarUri)){
                    ActivityHelper.goPhotoView(getActivity(), mUserAvatarUri);
                }
                break;
            case R.id.ll_balance:
                ActivityHelper.goMyWallet(getActivity(), mTvBalance.getText().toString().trim());
                break;
            case R.id.ll_mybank:
                ActivityHelper.goMyBank(getActivity(), UserRecord.getInstance().getUserId());
                break;
            case R.id.btn_chat:
                if (RongIM.getInstance() != null){
                    //跳转至私聊
                    RongIM.getInstance().startPrivateChat(getActivity(), mUserId, mTvNickName.getText().toString());
                }
                break;
            case R.id.btn_delete_friend:
                //如果为加好友跳转至 申请好友界面
                if (mBtnDeleteFriend.getText().toString().equalsIgnoreCase("加好友"))
                {
                    // Cursor cursor = db.rawQuery("select * from friends where _id=?", new String[]{"1"});
                    //db.execSQL("insert into friends(userid,isfriends) values(?,?)", new String[]{friend.getUserId(), "1"});
                    Intent intent = new Intent(getActivity(), ApplyFriendActivity.class);
                    intent.putExtra("friend_id", mUserId);
                    startActivity(intent);
                } else if (mBtnDeleteFriend.getText().toString().equalsIgnoreCase("删除好友")){
                    mBtnDeleteFriend.setText("加好友");
                    mRelation = ContactListAdapter.PAGE_FRIENDS;
                    updateRelation(mRelation);
                    break;
                }
                break;
            case R.id.btn_follow:
                //否则 执行关注
                if ((mFollow.getText().toString()).equals("关注")){
                    if (db==null)
                        db=new MySqlistHelpe(getActivity()).getReadableDatabase();
                    db.execSQL("insert into follow(userid) values(?)",new String[]{Userid});
                    mFollow.setText("取消关注");
                    mRelation = ContactListAdapter.PAGE_FAN;
                    updateRelation(mRelation);
                    break;
                }
                else if ((mFollow.getText().toString()).equals("取消关注")) {
                    if (db==null)
                        db=new MySqlistHelpe(getActivity()).getReadableDatabase();
                    db.execSQL("delete from follow where userid=?",new String[]{Userid});
                    mFollow.setText("关注");
                    mRelation = ContactListAdapter.PAGE_FOLLOW;
                    updateRelation(mRelation);
                    break;
                }
                break;
            case R.id.ll_borrowing:
                ActivityHelper.goBarrowHaving(getContext());
                break;
            case R.id.ll_rental:
                ActivityHelper.goMyRental(getContext());
                break;
            case R.id.iv_qr_code:
                Intent intent = new Intent(getContext(), QrCodeActivity.class);
                Bundle bundle= new Bundle();
                bundle.putString("userid",mUserId);
                bundle.putString("img",CenterEntity.getPortraituri());
                bundle.putString("username",CenterEntity.getNickname());
                intent.putExtra("qrcode", bundle);
                startActivity(intent);
                break;
        }

    }

    private void updateRelation(int relation){
        String param;
        if(relation == ContactListAdapter.PAGE_FOLLOW){
            //删除关注
            param = "{\"function\":\"removeattention\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"followid\":\"" + mUserId + "\"}";
        }else if (relation == ContactListAdapter.PAGE_FRIENDS){
            param = "{\"function\":\"removefriend\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"friendid\":\"" + mUserId + "\"}";
        }else {
            //添加关注
            param = "{\"function\":\"addattention\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"followid\":\"" + mUserId + "\"}";
        }
        ApiManager.getApi()
                .commonQuery(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<Void>>())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        if(mRelation == ContactListAdapter.PAGE_FAN){
                            mRelation = ContactListAdapter.PAGE_FOLLOW;
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
