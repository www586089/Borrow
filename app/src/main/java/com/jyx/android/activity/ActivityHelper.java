package com.jyx.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jyx.android.activity.buy.AfterPaySuccessActivity;
import com.jyx.android.activity.buy.BuyAddressEditActivity;
import com.jyx.android.activity.buy.BuyAddressSelectActivity;
import com.jyx.android.activity.buy.BuyProcedureActivity;
import com.jyx.android.activity.buy.BuyRightNowActivity;
import com.jyx.android.activity.buy.LoveItemUserActivity;
import com.jyx.android.activity.chat.NewSubGroupActivity;
import com.jyx.android.activity.chat.OtherInfoActivity;
import com.jyx.android.activity.chat.redenvelope.PayThePasswordActivity;
import com.jyx.android.activity.chat.redenvelope.RealFlowersActivity;
import com.jyx.android.activity.chat.redenvelope.RedEnvelopeActivity;
import com.jyx.android.activity.chat.redenvelope.SelectFrinedActivity;
import com.jyx.android.activity.discovery.event.EnrollEventActivity;
import com.jyx.android.activity.discovery.event.EnrollSuccessActivity;
import com.jyx.android.activity.discovery.event.EventDetailActivity;
import com.jyx.android.activity.me.BabyManagementActivity;
import com.jyx.android.activity.me.ChangePriceActivity;
import com.jyx.android.activity.me.MyBorrowActivity;
import com.jyx.android.activity.me.MyLoveActivity;
import com.jyx.android.activity.me.MyRentalActivity;
import com.jyx.android.activity.me.MyWalletActivity;
import com.jyx.android.activity.me.PhotoviewActivity;
import com.jyx.android.activity.me.UserInfoActivity;
import com.jyx.android.activity.me.bankcard.AddCardActivity;
import com.jyx.android.activity.me.mypersonalcenter.mysettings.AboutActivity;
import com.jyx.android.activity.me.mypersonalcenter.mysettings.ChangePasswordActivity;
import com.jyx.android.activity.me.mypersonalcenter.mysettings.SettingActivity;
import com.jyx.android.activity.me.mypersonalcenter.mysettings.SuggestionActivity;
import com.jyx.android.activity.me.mypersonalcenter.personalinfo.MyPersonalInfoActivity;
import com.jyx.android.activity.publish.PictureSelectActivity;
import com.jyx.android.activity.publish.PublicItemActivity;
import com.jyx.android.activity.publish.PublishActivity;
import com.jyx.android.activity.publish.PublishDynamicActivity;
import com.jyx.android.activity.publish.PublishItemAddAddressActivity;
import com.jyx.android.activity.publish.PublishItemAreaSelectActivity;
import com.jyx.android.activity.publish.PublishItemTypeSelectActivity;
import com.jyx.android.activity.publish.PublishItemWaySelectActivity;
import com.jyx.android.activity.publish.PublishSuccessActivity;
import com.jyx.android.activity.publish.publishcowry.CowryQualityActivity;
import com.jyx.android.activity.purchase.CommentListsActivity;
import com.jyx.android.activity.purchase.LikeListsActivity;
import com.jyx.android.activity.purchase.WeixinPayActivity;
import com.jyx.android.activity.sign.MobileResetActivity;
import com.jyx.android.activity.sign.MobileVerificationActivity;
import com.jyx.android.activity.sign.SignInActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.fragment.contact.FriendFragment;
import com.jyx.android.fragment.me.MeFragment;

/**
 * Created by Tonlin on 2015/10/22.
 * <p/>
 * ACTIVITY 界面跳转辅助类 ，尽量所有 界面跳转通过该类进行
 * 方便以后如果改动的 Activity 的跳转，只需改动此类即可。
 */
public final class ActivityHelper {

    private static void simpleGo(Context context, Class clz) {
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
    }


    private static void goWithArgs(Context context, Class clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转到程序主界面
     *
     * @param context
     */
    public static void goMain(Context context) {
        simpleGo(context, MainActivity.class);
    }



    /**
     * 签到页面
     * @param context
     */
    public static void goSignIn(Context context) {
        simpleGo(context, SignInActivity.class);
    }

    public static void goSignInForResult(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getContext(),
                SignInActivity.class), requestCode);
    }

    public static void goSignInForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity,
                SignInActivity.class), requestCode);
    }

    /**
     * 跳转到立即购买
     * @param context
     */
    public static void goBuyRightNow(Context context) {
        simpleGo(context, BuyRightNowActivity.class);
    }

    /**
     * 跳转到立即购买
     * @param context
     */
    public static void goBuyRightNowExt(Context context, Bundle bundle) {
        goWithArgs(context, BuyRightNowActivity.class, bundle);
    }

    /**
     * 跳转到购买成功
     * @param context
     */
    public static void goBuyPaySuccess(Context context) {
        simpleGo(context, AfterPaySuccessActivity.class);
    }

    /**
     * 跳转到编辑收货地址
     * @param context
     */
    public static void goBuyEditAddress(Context context) {
        simpleGo(context, BuyAddressEditActivity.class);
    }
    /**
     * 跳转到编辑收货地址
     * @param context
     */
    public static void goBuyEditAddress(Context context, Bundle bundle) {
        goWithArgs(context, BuyAddressEditActivity.class, bundle);
    }
    public static void goSelectBuyAddressActivity(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, BuyAddressSelectActivity.class), requestCode);
    }

    /**
     * 跳转到购买流程
     * @param context
     */
    public static void goBuyProcedure(Context context) {
        simpleGo(context, BuyProcedureActivity.class);
    }

    /**
     * 跳转到购买流程
     * @param context
     */
    public static void goBuyProcedureExt(Context context, Bundle bundle) {
        goWithArgs(context, BuyProcedureActivity.class, bundle);
    }

    /**
     * 跳转到手机验证
     *
     * @param context context
     */
    public static void goMobileVerification(Context context) {
        simpleGo(context, MobileVerificationActivity.class);
    }


    /**
     * 跳转到添加银行卡页面
     *
     * @param context
     */
    public static void goAddCard(Context context) {
        simpleGo(context, AddCardActivity.class);
    }

    /**
     * 发红包
     *
     * @param context
     */
    public static void goRedEnvelope(Context context) {
        simpleGo(context, RedEnvelopeActivity.class);
    }

    /**
     * 修改密码
     *
     * @param context
     */
    public static void goChangePassword(Context context) {
        simpleGo(context, ChangePasswordActivity.class);
    }

    /**
     * 跳转到宝贝发布 成色选择界面
     * @param context
     */
    public static void goCowryQuality(Context context) {
        simpleGo(context, CowryQualityActivity.class);
    }
    public static void goCowryQuality(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity,
                CowryQualityActivity.class), requestCode);
    }
    public static void goSelectItemType(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PublishItemTypeSelectActivity.class), requestCode);
    }

    //取物方式
    public static void goSelectItemWay(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PublishItemWaySelectActivity.class), requestCode);
    }

    //物品地址
    public static void goSelectItemArea(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, PublishItemAreaSelectActivity.class), requestCode);
    }

    //添加地址
    public static void goAddAddress(Activity activity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, PublishItemAddAddressActivity.class);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }
    /**
     * 支付密码
     *
     * @param context
     */
    public static void goPayThePassword(Context context) {
        simpleGo(context, PayThePasswordActivity.class);
    }

    /**
     * 支付密码
     *
     * @param activity
     */
    public static void goPayThePasswordForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, PayThePasswordActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到改价页面
     * @param context
     * by: qice
     */
    public static void goChangePrice(Context context){
        simpleGo(context, ChangePriceActivity.class);
    }


    /**
     * 跳转到发布成功页面
     * @param context
     * by: qice
     */
    public static void goPublishSuccess(Context context){
        simpleGo(context, PublishSuccessActivity.class);
    }

    /**
     * 实物鲜花
     *
     * @param context
     */
    public static void goRealFlowers(Context context) {
        simpleGo(context, RealFlowersActivity.class);
    }

    /**
     * 喜欢人列表
     *
     * @param context
     */
    public static void goLikeLists(Context context) {
        simpleGo(context, LikeListsActivity.class);
    }

    /**
     * 喜欢人列表
     *
     * @param context
     */
    public static void goLikeListsExt(Context context, Bundle bundle) {
        //goWithArgs(context, LikeListsActivity.class, bundle);
        goWithArgs(context, LoveItemUserActivity.class, bundle);
    }

    /**
     * 评论列表
     *
     * @param context
     */
    public static void goCommentLists(Context context) {
        simpleGo(context, CommentListsActivity.class);
    }
    /**
     * 评论列表
     *
     * @param context
     */
    public static void goCommentListsExt(Context context, Bundle bundle) {
        goWithArgs(context, CommentListsActivity.class, bundle);
    }
    /**
     * 跳转到我的个人信息
     *
     * @param context context
     */
    public static void goMyPersonalInfo(Context context) {
        simpleGo(context, MyPersonalInfoActivity.class);
    }



    /**
     * 发布入口
     *
     * @param context context
     */
    public static void goPublishPage(Context context) {
        simpleGo(context, PublishActivity.class);
    }

    /**
     * 发布产品
     *
     * @param context context
     */
    public static void goPublishItem(Context context) {
        simpleGo(context, PublicItemActivity.class);
    }

    /**
     * 发布产品
     *
     * @param context context
     */
    public static void goPublishItemExt(Context context, Bundle bundle) {
        goWithArgs(context, PublicItemActivity.class, bundle);
    }

    /**
     * 图片选择
     *
     * @param context context
     */
    public static void goSelectPic(Context context) {
        simpleGo(context, PictureSelectActivity.class);
    }

    /**
     * 图片选择
     *
     * @param context context
     */
    public static void goSelectPicExt(Context context, Bundle bundle) {
        goWithArgs(context, PictureSelectActivity.class, bundle);
    }

    /**
     * 发布动态
     *
     * @param context context
     */
    public static void goPublishDynamic(Context context) {
        simpleGo(context, PublishDynamicActivity.class);
    }

    /**
     * 发布动态
     *
     * @param context context
     */
    public static void goPublishDynamicExt(Context context, Bundle bundle) {
        goWithArgs(context, PublishDynamicActivity.class, bundle);
    }
    //MyWalletActivity
    /**
     * 发布产品
     *
     * @param context context
     */
    public static void goMyWallet(Context context, String balance) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        intent.putExtra("balance", balance);
        context.startActivity(intent);
    }

    /**
     * 我的银行卡,到时需要修改回来
     *
     * @param context context
     */
    public static void goMyBank(Context context, String user_Id) {
        Intent intent = new Intent(context, AddCardActivity.class);
        intent.putExtra("user_Id",user_Id);
        context.startActivity(intent);
    }


    /**
     * 宝贝管理
     *
     * @param context context
     */
    public static void goBabyManagement(Context context) {
        simpleGo(context, BabyManagementActivity.class);
    }

    //
    /**
     * 借入
     *
     * @param context context
     */
    public static void goBarrowHaving(Context context) {
        simpleGo(context, MyBorrowActivity.class);
    }
    /**
     * 多人列表
     *
     * @param context
     */
    public static void goSelectFriend(Context context) {
        simpleGo(context, SelectFrinedActivity.class);
    }


    /**
     * 我的借出
     *
     * @param context
     */
    public static void goMyRental(Context context) {
        simpleGo(context, MyRentalActivity.class);
    }


    /**
     * 建子群
     * @param context
     */
    public static void goNewSubGroup(Context context){

        simpleGo(context, NewSubGroupActivity.class);

    }

    /**
     * 微信支付
     * @param context
     */
    public static void goWeixinPay(Context context){
        simpleGo(context, WeixinPayActivity.class);

    }


    /**
     * 忘记密码了
     *
     * @param context context
     */
    public static void goForgetPWD(Context context) {
        simpleGo(context, MobileResetActivity.class);
    }

    /**
     * 他人的个人中心
     *
     * @param context context
     */
    public static void goOtherInfoExt(Context context, Bundle bundle) {
        goWithArgs(context, OtherInfoActivity.class, bundle);
    }

    /**
     * 图片放大
     *
     * @param context context
     */
    public static void goPhotoView(Context context, String photouri) {
        Intent intent = new Intent(context, PhotoviewActivity.class);
        intent.putExtra("photoUri", photouri);
        context.startActivity(intent);
    }


    /**
     * 提交建议
     *
     * @param context context
     */
    public static void goSuggestion(Context context) {
        simpleGo(context, SuggestionActivity.class);
    }

    /**
     * 提交建议
     *
     * @param context context
     */
    public static void goSetting(Context context) {
        simpleGo(context, SettingActivity.class);
    }

    /**
     * 关于
     *
     * @param context context
     */
    public static void goAbout(Context context) {
        simpleGo(context, AboutActivity.class);
    }

    public static void goActivityDetail(Context context, Bundle bundle) {
        goWithArgs(context, EventDetailActivity.class, bundle);
    }

    public static void goEnrollActivity(Context context) {
        simpleGo(context, EnrollEventActivity.class);
    }


    public static void goEnrollSuccessActivity(Context context) {
        simpleGo(context, EnrollSuccessActivity.class);
    }

    /**
     * 我的收藏
     *
     * @param context context
     */
    public static void goMyLove(Context context) {
        simpleGo(context, MyLoveActivity.class);
    }

    public static void goUserInfoActivity(Context context, String userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(FriendFragment.KEY_PAGE_TYPE, ContactListAdapter.PAGE_CONTACT);
        bundle.putString(MeFragment.KEY_USER_ID, userId);
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


}
