package com.jyx.android.net;

import com.jyx.android.model.ActivityItemBean;
import com.jyx.android.model.AddressItemBean;
import com.jyx.android.model.AnOrderBean;
import com.jyx.android.model.BankCardEntity;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.BorrowInfo;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.GiftEntity;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.model.GroupInfoBean;
import com.jyx.android.model.GroupNoticeItemBean;
import com.jyx.android.model.GroupRedEnvelopResultBean;
import com.jyx.android.model.ImageListEntity;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemCommentDetailBean;
import com.jyx.android.model.ItemInfoBean;
import com.jyx.android.model.ItemListEntity;
import com.jyx.android.model.LoveItemBean;
import com.jyx.android.model.MyLoveItem;
import com.jyx.android.model.NewsFriendsBean;
import com.jyx.android.model.OrderEntity;
import com.jyx.android.model.PayDetailInfo;
import com.jyx.android.model.PhoneFriend;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.model.RedEnvelopBean;
import com.jyx.android.model.RedEnvelopResultBean;
import com.jyx.android.model.RelationEntity;
import com.jyx.android.model.ReturnGroupEntity;
import com.jyx.android.model.SystemDicData;
import com.jyx.android.model.UserCenterEntity;
import com.jyx.android.model.UserEntity;
import com.jyx.android.model.WXPrePayBean;
import com.jyx.android.model.WaitFriendEntity;
import com.jyx.android.model.WithdrawEntity;
import com.jyx.android.model.param.ContactUploadParam;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author : Tree
 * Date : 2016-01-07
 */
public interface JYXApi {

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<UserEntity>>> login(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<SystemDicData>> getDicDatta(@Field("xml")String signParam);

    @Multipart
    @POST("UploadFile")
    Observable<BaseEntry<List<String>>> uploadFile(@Part("xml") RequestBody xml, @Part("file") RequestBody...file);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<UserCenterEntity>>> getUserCenter(@Field("xml")String signParam);


    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<UserEntity>>> getMailList(@Field("xml")String  signParam);

    /**
     * 获取订单id
     * @param signParam
     * @return
     */
    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<OrderEntity>>> getOrderList(@Field("xml")String signParam);

    /**
     * 获取微信预支付id
     * @param signParam
     * @return
     */
    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<OrderEntity>>> getWXPrepayId(@Field("xml")String signParam);

    /**
     * 获取礼物列表
     * @param signParam
     * @return
     */
    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<GiftEntity>>> getGiftList(@Field("xml")String  signParam);

    /**
     * 获取等待我审核的好友请求
     * @param signParam
     * @return
     */
    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<WaitFriendEntity>>> getWaitFriendApply(@Field("xml")String signParam);

       /*
       发红包
        */
    @POST("JYXServer")
    Call<BaseEntry<List<RedEnvelopBean>>> sendRedEnvelop(@Query("xml")String xml);
    /*
       查询个人红包
        */
    @POST("JYXServer")
    Call<BaseEntry<List<RedEnvelopResultBean>>> queryRedEnvelop(@Query("xml")String  xml);

    /**
     * 查询群红包
     * @param xml
     * @return */
    @POST("JYXServer")
    Call<BaseEntry<List<GroupRedEnvelopResultBean>>> queryGroupRedEnvelop  (@Query("xml")String xml);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<ImageListEntity>>> getImageList(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<UserEntity>>> getUserInfor(@Field("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updateNickname(@Query("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updateGender(@Query("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updateSignature(@Query("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<FriendInfo>>> queryContact(@Field("xml")String contactParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<Void>>> followOrUnfollow(@Field("xml")String followParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<GroupInfo>>> queryGroups(@Field("xml")String groupPram);

    //发布 begin
    @POST("JYXServer")
    Call<BaseEntry<List<AddressItemBean>>> getMyAddress(@Query("xml")String xml);

    @POST("JYXServer")
    Call<BaseEntry<List<Void>>> addMyAddress(@Query("xml")String xml);

    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> publishItem(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> publishNews(@Query("xml")String xml);
    //发布 end

    @POST("JYXServer")
    Call<BaseEntry<List<ItemBean>>> getItemList(@Query("xml")String xml);

    @POST("JYXServer")
    Call<BaseEntry<List<GroupInfoBean>>> getMyGroupList(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<ItemBean>>> getGroupNews(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<NewsFriendsBean>>> getFriendGroupNews(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> praiseItem(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> commentItem(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> loveItem(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<ItemCommentDetailBean>>> getItemComment(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<ItemInfoBean>>> getItemInformation(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> praiseNews(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> loveNews(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<PublishItemResultBean>>> commentNews(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<AnOrderBean>>> placeAnOrder(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<AnOrderBean>>> WalletPay(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<WXPrePayBean>>> getWXPrePayId(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<Void>>> wxPayNotify(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<LoveItemBean>>> getLoveItemUser(@Query("xml")String xml);
    @POST("JYXServer")
    Call<BaseEntry<List<ActivityItemBean>>> getActivity(@Query("xml")String xml);
    @Multipart
    @POST("UploadFile")
    Call<BaseEntry<List<String>>> uploadPic(@PartMap Map<String, RequestBody> params);
    @POST("JYXServer")
    Call<BaseEntry<List<Void>>> activityEnroll(@Query("xml")String xml);


    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> addBankCard(@Query("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<BankCardEntity>>> getBankCard(@Field("xml")String groupPram);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<RelationEntity>>> getMyRelation(@Field("xml")String groupPram);


    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<PhoneFriend>>> queryPhoneFriend(@Field("xml")String contactParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> applyFriend(@Query("xml")String friendParam);

    @POST("UploadMobile")
    Observable<BaseEntry<List<Void>>> uploadContact(@Body ContactUploadParam friendParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> checkMobileExist(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> getMobileVerifyCode(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> verifyMobileCode(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> register(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<ReturnGroupEntity>>> createGroup(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> batchAdd2Group(@Query("xml")String param);

    @GET("JYXServer")
    Observable<BaseEntry<List<Void>>> commonQuery(@Query("xml")String param);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<PayDetailInfo>>> getPayList(@Field("xml")String signParam);

    @Multipart
    @POST("UploadFile")
    Observable<BaseEntry<List<String>>> MyUpload(@PartMap Map<String, RequestBody> params);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updateBackground(@Query("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updatePortrait(@Query("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updateBirthday(@Query("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<BorrowInfo>>> getMyBorrowList(@Field("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> updatePassword(@Query("xml")String signParam);

    @GET("JYXServer")
    Observable<BaseEntry<List<String>>> submitSuggestion(@Query("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<String>>> manageOrder(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<WithdrawEntity>>> Withdraw(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<MyLoveItem>>> getMyLoveList(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<ItemListEntity>>> getMyItemList(@Field("xml")String signParam);

    @POST("JYXServer")
    @FormUrlEncoded
    Observable<BaseEntry<List<GroupNoticeItemBean>>> getGroupnotice(@Field("xml")String signParam);
}
