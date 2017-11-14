package com.jyx.android.activity.buy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.adapter.discovery.LoveMyAdapter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemCommentInfoBean;
import com.jyx.android.model.ItemInfoBean;
import com.jyx.android.model.PraisenItemInfoBean;
import com.jyx.android.model.param.GetItemInfoParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.utils.ImageOptions;
import com.jyx.android.utils.StringUtils;
import com.jyx.android.widget.view.NoScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by zfang on 2015/11/07.
 *
 */
public class BuyProcedureActivity extends BaseActivity{

    @Bind(R.id.slv_buy_procedure)
    ScrollView slv_buy_procedure;
    @Bind(R.id.img_buy_procedure_item_photo)
    SimpleDraweeView img_buy_procedure_item_photo;
    @Bind(R.id.tv_buy_procedure_user_name)
    TextView tvBuyProcedureUserName;
    @Bind(R.id.tv_buy_procedure_theme)
    TextView tv_buy_procedure_theme;

    @Bind(R.id.rl_buy_procedure_price)
    RelativeLayout rl_buy_procedure_price;
    @Bind(R.id.tv_buy_procedure_current_price)
    TextView tv_buy_procedure_current_price;
    @Bind(R.id.tv_buy_procedure_price)
    TextView tv_buy_procedure_price;
    @Bind(R.id.tv_buy_procedure_economy)
    TextView tv_buy_procedure_economy;
    @Bind(R.id.tv_buy_procedure_opera_type)
    TextView tvBuyProcedureOperaType;

    @Bind(R.id.tv_buy_procedure_item_des)
    TextView tvBuyProcedureItemDes;
    @Bind(R.id.rl_container_vp)
    RelativeLayout rl_container_vp;
    @Bind(R.id.vp_buy_procedure)
    ViewPager vpBuyProcedure;
    @Bind(R.id.tv_buy_procedure_ratio)
    TextView tvBuyProcedureRatio;
    @Bind(R.id.ll_buy_procedure_op)
    LinearLayout llBuyProcedureOp;
    @Bind(R.id.tv_buy_procedure_like_ll)
    LinearLayout tv_buy_procedure_like_ll;
    @Bind(R.id.tv_buy_procedure_like_also)
    TextView tv_buy_procedure_like_also;

    @Bind(R.id.gd_buy_procedure_photos)
    NoScrollGridView bdBuyProcedurePhotos;

    @Bind(R.id.tv_buy_procedure_comment_ll)
    LinearLayout tv_buy_procedure_comment_ll;
    @Bind(R.id.tv_buy_procedure_comment_num)
    TextView tv_buy_procedure_comment_num;

    @Bind(R.id.ll_buy_procedure_comment)
    LinearLayout ll_buy_procedure_comment;
    @Bind(R.id.iv_buy_procedure_photo)
    ImageView iv_buy_procedure_photo;
    @Bind(R.id.tv_buy_procedure_name)
    TextView tv_buy_procedure_name;
    @Bind(R.id.tv_buy_procedure_comment_item_content)
    TextView tv_buy_procedure_comment_item_content;
    @Bind(R.id.tv_buy_procedure_comment_item_time)
    TextView tv_buy_procedure_comment_item_time;

    @Bind(R.id.rl_buy_procedure_bottom)
    RelativeLayout rl_buy_procedure_bottom;

    @Bind(R.id.pb_loading)
    ProgressWheel pb_loading;
    private PhotosAdapter photosAdapter = null;
    private List<String> listImages = new ArrayList<String>();
    private int currentSelected = 1;
    private PhotosPageChangeListener pageChangeListener = null;
    private LoveMyAdapter photosGridAdapter = null;
    private Animation animRightIn,animRightOut;//按钮伸缩动画


    private String userName = null;
    private String itemName = null;
    private String itemOperaType = null;
    private String itemDescrible = null;
    private String itemPrice = null;
    private String discountPrice = null;
    private String likeNum = null;
    private String commentNum = null;
    private Bundle bundle = null;
    private String itemUserId = null;
    private ItemBean item = null;
    private LayoutInflater infalter = null;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @OnClick(R.id.img_buy_procedure_item_photo)
    void clickItemUserPortrait() {
        ActivityHelper.goUserInfoActivity(this, item.getUser_id());
    }
    @OnClick(R.id.iv_buy_procedure_photo)
    void clickCommentUserPortrait() {
        ActivityHelper.goUserInfoActivity(this, item.getUser_id());
    }
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_procedure;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle(getActionBarTitle());
        setActionRightText("");
        setActionRightImg(R.mipmap.shar_top);
        getBundleData2();
        initWidget();
        getData();
    }
    @Override
    protected void onActionRightClick(View view) {
        //ActivityHelper.goCowryQuality(PublishSuccessActivity.this);
        Intent intent =new Intent(this,ShareActivity.class);
        startActivity(intent);
    }

    private void prevGetData() {
        pb_loading.setVisibility(View.VISIBLE);
        if (item.getUser_id().equals(UserRecord.getInstance().getUserId())) {
            rl_buy_procedure_bottom.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) slv_buy_procedure.getLayoutParams();
            mlp.bottomMargin = 0;
            slv_buy_procedure.setLayoutParams(mlp);
        }
        slv_buy_procedure.setVisibility(View.GONE);
    }

    private void afterGetData(boolean success) {
        if (success) {
            pb_loading.setVisibility(View.GONE);
            rl_buy_procedure_bottom.setVisibility(View.VISIBLE);
            slv_buy_procedure.setVisibility(View.VISIBLE);
        } else {
            pb_loading.setVisibility(View.VISIBLE);
            if (item.getUser_id().equals(UserRecord.getInstance().getUserId())) {
                rl_buy_procedure_bottom.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) slv_buy_procedure.getLayoutParams();
                mlp.bottomMargin = 0;
                slv_buy_procedure.setLayoutParams(mlp);
            }
            slv_buy_procedure.setVisibility(View.GONE);
        }
    }

    private void getData() {
        GetItemInfoParam xml = new GetItemInfoParam();
        xml.setFunction("getiteminfor");
        xml.setItemid(item.getItem_id());
        xml.setUserid(UserRecord.getInstance().getUserId());
        Call<BaseEntry<List<ItemInfoBean>>> result = ApiManager.getApi().getItemInformation(new Gson().toJson(xml));
        result.enqueue(new Callback<BaseEntry<List<ItemInfoBean>>>() {
            @Override
            public void onResponse(Response<BaseEntry<List<ItemInfoBean>>> response) {
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<ItemInfoBean>> body = response.body();
                    if (null != body) {
                        List<ItemInfoBean> listInfo = body.getData();
                        if (null != listInfo && listInfo.size() > 0) {
                            initUINet(body.getData().get(0));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void initUINet(ItemInfoBean itemDetail) {
        List<PraisenItemInfoBean> praiseItemList = itemDetail.getPraisenitem();
        if (null != praiseItemList && praiseItemList.size() > 0) {

            photosGridAdapter.setData(praiseItemList);
        }

        int likeNum = 0;
        int commentNum = 0;
        try {
            likeNum = Integer.valueOf(itemDetail.getLikenum());
            commentNum = Integer.valueOf(itemDetail.getCommentnum());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (likeNum > 0) {
            tv_buy_procedure_like_ll.setVisibility(View.VISIBLE);
            String resultsTextFormat = getResources().getString(R.string.buy_procedure_like_num);
            String resultsText = String.format(resultsTextFormat, likeNum);
            CharSequence styledResults = Html.fromHtml(resultsText);
            tv_buy_procedure_like_also.setText(styledResults);
        } else {
            tv_buy_procedure_like_ll.setVisibility(View.GONE);
        }
        if (commentNum > 0) {
            tv_buy_procedure_comment_num.setText(Html.fromHtml(String.format(getString(R.string.buy_procedure_comment_num), commentNum)));
        } else {
            tv_buy_procedure_comment_ll.setVisibility(View.GONE);
        }
        List<ItemCommentInfoBean> commentItemList = itemDetail.getItemcomment();
        if (null != commentItemList && commentItemList.size() > 0) {
            for (int i = 0; i < commentItemList.size(); i++) {
                if (i > 9) {
                    break;
                }
                View view = infalter.inflate(R.layout.commentlists_item, null, false);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_commentlists_name);
                TextView tv_comment = (TextView) view.findViewById(R.id.tv_commentlists_comment);
                ImageView iv_user = (ImageView) view.findViewById(R.id.iv_commentlists_img);
                TextView tv_time = (TextView) view.findViewById(R.id.tv_commentlists_time);
                final ItemCommentInfoBean commentBean = commentItemList.get(i);
                if (!TextUtils.isEmpty(commentBean.getPortraituri())) {
                    iv_user.setImageURI(Uri.parse(commentBean.getPortraituri()));
                    ImageLoader.getInstance().displayImage(commentBean.getPortraituri(), iv_user, ImageOptions.get_portrait_Options());
                }
                iv_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityHelper.goUserInfoActivity(BuyProcedureActivity.this, commentBean.getUser_id());
                    }
                });
                tv_name.setText(commentBean.getNickname());
                tv_comment.setText(commentBean.getContexts());
                tv_time.setText(getFormatDateString(commentBean.getCreatedat()));
                ll_buy_procedure_comment.addView(view);
            }
           /* ItemCommentInfoBean commentBean = commentItemList.get(0);
            if (!TextUtils.isEmpty(commentBean.getPortraituri())) {
                ImageLoader.getInstance().displayImage(commentBean.getPortraituri(), iv_buy_procedure_photo, ImageOptions.get_portrait_Options());
            }
            tv_buy_procedure_name.setText(commentBean.getNickname());
            tv_buy_procedure_comment_item_content.setText(commentBean.getContexts());
            tv_buy_procedure_comment_item_time.setText(getFormatDateString(commentBean.getCreatedat()));*/
        }
    }

    private String getFormatDateString(String dateStr) {
        Date date = null;
        String timeStr = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeStr = sdf.format(date);
        return timeStr;
    }
    private void getBundleData2() {
        bundle = getIntent().getExtras();
        if (null != bundle) {
            item = bundle.getParcelable("item");
            String url = null;
            url = item.getPortraituri();
            //设置头像与昵称
            if (null != url && url.trim().length() > 0) {
                img_buy_procedure_item_photo.setImageURI(Uri.parse(url));
//                ImageLoader.getInstance().displayImage(url, img_buy_procedure_item_photo, ImageOptions.get_portrait_Options() );
            } else {
                img_buy_procedure_item_photo.setImageResource(R.mipmap.me_photo_2x_81);
            }
            String imageJson = item.getImagejson();
            if (null != imageJson) {
                try {
                    JSONArray jsonArray = new JSONArray(imageJson);
                    if (null != jsonArray && jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            this.listImages.add(jsonArray.optString(i));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            itemUserId = item.getUser_id();
            itemName = item.getName();
            userName = item.getNickname();
            likeNum = item.getLikenum();
            commentNum = item.getCommentnum();
            int type = 1000;
            type = item.getOperatype();
            switch (type) {
                case 1:
                    tv_buy_procedure_economy.setVisibility(View.GONE);
                    tv_buy_procedure_price.setVisibility(View.GONE);
                    itemOperaType = "租借";
                    tv_buy_procedure_current_price.setText("租金：￥" + StringUtils.toMoneyYuan(item.getRent()));
                    break;
                case 2:
                    itemOperaType = "转让";
                    itemPrice = item.getPrice() + "";
                    tv_buy_procedure_current_price.setText(StringUtils.toMoneyYuan(item.getDiscountprice()) + "");
                    tv_buy_procedure_price.setText(StringUtils.toMoneyYuan(item.getPrice()) + "");
                    double economyPrice = StringUtils.toMoneyYuan(item.getPrice() - item.getDiscountprice());
                    tv_buy_procedure_economy.setText("节省￥" + economyPrice);
                    tv_buy_procedure_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    break;
                case 3:
                    itemOperaType = "赠送";
                    tv_buy_procedure_economy.setVisibility(View.GONE);
                    tv_buy_procedure_price.setVisibility(View.GONE);
                    tv_buy_procedure_current_price.setText(item.getRemarks());
                    break;
                default:
                    rl_buy_procedure_price.setVisibility(View.GONE);
                    itemOperaType = "其它";
                    break;
            }
            itemOperaType = item.getOperatype_name();
            itemDescrible = item.getDiscribe();

            if (item.getUser_id().equals(UserRecord.getInstance().getUserId())) {
                rl_buy_procedure_bottom.setVisibility(View.GONE);
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) slv_buy_procedure.getLayoutParams();
                mlp.bottomMargin = 0;
                slv_buy_procedure.setLayoutParams(mlp);
            }
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.buy_procedure_title_center;
    }

    private void initWidget() {
        infalter = LayoutInflater.from(this);
        tvBuyProcedureUserName.setText(userName);
        tv_buy_procedure_theme.setText(itemName);
        tvBuyProcedureOperaType.setText(itemOperaType);

        tvBuyProcedureItemDes.setText(itemDescrible);
        tvBuyProcedureRatio.setText(currentSelected + "/" + this.listImages.size());
//        ArrayList<String> arrs=new ArrayList<String>();
//        arrs.add("33");
//        arrs.add("323");
//        arrs.add("331");
//        arrs.add("323");
        photosGridAdapter = new LoveMyAdapter(this);
//        photosGridAdapter.setData(arrs);
        bdBuyProcedurePhotos.setAdapter(photosGridAdapter);
        setGridListener();
        initAnimation();
        initVp();
    }

    private void initAnimation() {
        animRightIn = AnimationUtils.loadAnimation(this, R.anim.scale_in_from_right);
        animRightOut = AnimationUtils.loadAnimation(this, R.anim.scale_out_to_right);

    }

    private void setGridListener() {
        this.bdBuyProcedurePhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private void initVp() {
        if (null != listImages && listImages.size() > 0) {
            pageChangeListener = new PhotosPageChangeListener(this.listImages.size());
            photosAdapter = new PhotosAdapter(this, this.listImages);
            vpBuyProcedure.setAdapter(photosAdapter);
            vpBuyProcedure.addOnPageChangeListener(pageChangeListener);
        } else {
            rl_container_vp.setVisibility(View.GONE);
            tvBuyProcedureRatio.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ll_buy_procedure_talk)
    void clickTalk() {
        if (null != itemUserId && itemUserId.trim().length() > 0) {
            if (RongIM.getInstance() != null)
                RongIM.getInstance().startPrivateChat(this, itemUserId, "title");
        } else {
            Application.showToast("未找到与产品关联的用户");
        }
    }

    @OnClick(R.id.tv_buy_procedure_want)
     void clickGoBuy() {
        ActivityHelper.goBuyRightNowExt(this, bundle);
    }


    @OnClick(R.id.tv_buy_procedure_share)
    void clickGoShare() {

        Intent intent =new Intent(this,ShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //更多评论
    @OnClick(R.id.tv_buy_procedure_more_people_comment)
    void clickMoreComment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        ActivityHelper.goCommentListsExt(this, bundle);
    }


    @OnClick(R.id.tv_buy_procedure_more_people_love)
    void clickMoreLover() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        ActivityHelper.goLikeListsExt(this, bundle);
    }

    @OnClick(R.id.ll_buy_procedure_operate)
    void clickOp() {
        if (View.VISIBLE == llBuyProcedureOp.getVisibility()) {
            switchDisplay(llBuyProcedureOp, false);
        } else {
            switchDisplay(llBuyProcedureOp, true);
        }
    }

    /**切换View的显示状态（带有动画效果）
     * @param view
     * @param isShow 为true则将该view设置为VISIBLE状态，否则设置为GONE状态*/
    private void switchDisplay(final View view, boolean isShow){
        if(isShow){
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animRightIn);
        }else{
            view.setVisibility(View.GONE);
            view.startAnimation(animRightOut);
        }
    }
    private class PhotosAdapter extends PagerAdapter {
        private List<String> listImages = null;
        private Context context = null;
        private LayoutInflater layoutInflater;
        public PhotosAdapter(Context context, List<String> images) {
            this.listImages = images;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return listImages.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = layoutInflater.inflate(R.layout.list_cell_vp_image_cell, container, false);
            SimpleDraweeView mDraweeView = (SimpleDraweeView) view.findViewById(R.id.album_image);
            mDraweeView.setImageURI(Uri.parse(listImages.get(position)));
            setClickListener(view, position);
            container.addView(view);
            return view;
        }
    }

    private void setClickListener(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private class PhotosPageChangeListener implements ViewPager.OnPageChangeListener {
        private int count = 0;
        public PhotosPageChangeListener(int count) {
            this.count = count;
        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            tvBuyProcedureRatio.setText((position + 1) + "/" + count);
        }
    }
}
