package com.jyx.android.adapter.chat;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.adapter.discovery.MyGridAdapter;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemCommentBean;
import com.jyx.android.model.PraisenItemInfoBean;
import com.jyx.android.utils.StringUtils;
import com.jyx.android.widget.view.NoScrollGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zfang on 2015/12/24.
 */
public abstract class AbsBaseRentalAdapter extends RecycleBaseAdapter {

    private Animation animRightIn,animRightOut;//按钮伸缩动画
    private Context context = null;
    private OnItemOpListener itemOpListner = null;
    private boolean lastShow = false;
    private View lastView = null;
    private int lastShowPosition = -1;
    private LayoutInflater inflater = null;
    public AbsBaseRentalAdapter(Context context) {
        this.context = context;
        init();
    }

    public OnItemOpListener getItemOpListner() {
        return itemOpListner;
    }

    public void setItemOpListner(OnItemOpListener itemOpListner) {
        this.itemOpListner = itemOpListner;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_group_rental_message, null);
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        animRightIn = AnimationUtils.loadAnimation(context, R.anim.scale_in_from_right);
        animRightOut = AnimationUtils.loadAnimation(context, R.anim.scale_out_to_right);
    }

    /**切换View的显示状态（带有动画效果）
     * @param view
     * @param isShow 为true则将该view设置为VISIBLE状态，否则设置为GONE状态*/
    private void switchDisplay(final View view, boolean isShow){
        if(isShow){
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animRightIn);
        }else{
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animRightOut);
        }
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }

    @Override
    protected void onBindHeaderViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        if (hasHeader()) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHeaderView.setLayoutParams(lp);
            if (0 == position) {
                TextView et_item_search = (TextView) mHeaderView.findViewById(R.id.et_item_search);
                et_item_search.requestFocus();
            }
        }
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        final ViewHolder vh = (ViewHolder) holder;
        final ItemBean item = (ItemBean) _data.get(position);
        /*if (lastShow) {
            if (position == lastShowPosition) {
                vh.ll_Rental_op.setVisibility(View.VISIBLE);
            } else {
                vh.ll_Rental_op.setVisibility(View.GONE);
            }
        }*/
        String url = null;
        String userName = null;
        String objectId = null;
        String displayTime = null;
        if (null != item) {
            url = item.getPortraituri();
            userName = item.getNickname();
        }
        if (null != url && url.trim().length() > 0) {
            vh.imgRentalAvator.setImageURI(Uri.parse(url));
//            ImageLoader.getInstance().displayImage(url, vh.imgRentalAvator, ImageOptions.get_portrait_Options());
        } else {
            vh.imgRentalAvator.setImageResource(R.mipmap.me_photo_2x_81);
        }
        vh.tvRentalName.setText(userName);
        vh.tv_rental_current_position.setText(item.getAddress());
        String operaType = item.getOperatype_name();
        if (1 == item.getOperatype()) {
            vh.tvRentalOriginalPrice.setVisibility(View.GONE);
            vh.tvRentalCurrentPrice.setVisibility(View.VISIBLE);
            vh.tvRentalEconomizePrice.setVisibility(View.GONE);
            vh.tvRentalCurrentPrice.setText("租金:￥" + StringUtils.toMoneyYuan(item.getRent()));
        } else if (2 == item.getOperatype()) {
            vh.tvRentalOriginalPrice.setVisibility(View.VISIBLE);
            vh.tvRentalCurrentPrice.setVisibility(View.VISIBLE);
            vh.tvRentalEconomizePrice.setVisibility(View.VISIBLE);
            vh.tvRentalCurrentPrice.setText("￥" + StringUtils.toMoneyYuan(item.getDiscountprice()));
            vh.tvRentalEconomizePrice.setText("节省:￥" + StringUtils.toMoneyYuan((item.getPrice() - item.getDiscountprice())));
            vh.tvRentalOriginalPrice.setText("￥" + StringUtils.toMoneyYuan(item.getPrice()));
            vh.tvRentalOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else if (3 == item.getOperatype()) {
            vh.tvRentalOriginalPrice.setVisibility(View.GONE);
            vh.tvRentalCurrentPrice.setVisibility(View.VISIBLE);
            vh.tvRentalEconomizePrice.setVisibility(View.GONE);
            vh.tvRentalCurrentPrice.setText(item.getRemarks());
        } else {
            vh.tvRentalOriginalPrice.setVisibility(View.GONE);
            vh.tvRentalCurrentPrice.setVisibility(View.GONE);
            vh.tvRentalEconomizePrice.setVisibility(View.GONE);
            operaType = "其它";
        }
        vh.tvRentalOperaType.setText(operaType);

        vh.tvRentalDisCripe.setText(item.getDiscribe());

        boolean hasPic = false;
        String strArray =  item.getImagejson();
        ArrayList<String> arrs = new  ArrayList<String>();
        try {
            if (StringUtils.isBlank(strArray) == false && strArray.length() > 10) {
                Gson gson = new Gson();
                arrs = gson.fromJson(strArray, new TypeToken<ArrayList<String>>() {}.getType());
                if (null != arrs && arrs.size() > 0) {
                    if (arrs.size() > 9) {
                        for (int i = 9; i < arrs.size();) {
                            arrs.remove(i);
                        }
                    }
                    MyGridAdapter myGridAdapter = new MyGridAdapter();
                    vh.gdRentalPhotos.setAdapter(myGridAdapter);
                    myGridAdapter.setData(arrs);
                    hasPic = true;
                    vh.gdRentalPhotos.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (hasPic) {
                vh.gdRentalPhotos.setVisibility(View.VISIBLE);
            } else {
                vh.gdRentalPhotos.setVisibility(View.GONE);
            }
        }

        List<ItemCommentBean> listComment = item.getItemcomment();
        List<PraisenItemInfoBean> listLove = item.getPraisenitem();
        if ((null != listComment && listComment.size() > 0) || null != listLove && listLove.size() > 0) {
            vh.ll_comment_reply.setVisibility(View.VISIBLE);
            initCommentContent(vh, item, listComment, listLove);
        } else {
            vh.ll_comment_reply.setVisibility(View.GONE);
        }
        setClickListener(vh, arrs, objectId, item, position);
    }

    private void initCommentContent(final ViewHolder vh, final ItemBean item, List<ItemCommentBean> listComment, List<PraisenItemInfoBean> listLove) {
        vh.ll_comment_reply_content.removeAllViews();
        if (null != listLove && listLove.size() > 0) {
            View loveView  = inflater.inflate(R.layout.love_item_cell, null);
            TextView comment_item_textview = (TextView) loveView.findViewById(R.id.love_item_textview);
            StringBuilder sb = new StringBuilder("♥");
            for (PraisenItemInfoBean bean : listLove) {
                sb.append(bean.getNickname() + ", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            comment_item_textview.setText(sb.toString());
            vh.ll_comment_reply_content.addView(loveView);
        }
        if (null != listComment && listComment.size() > 0) {
            View commentView = null;
            for(final ItemCommentBean comment:listComment) {
                commentView = inflater.inflate(R.layout.comment_item_cell, null);
                TextView commenterTextView = (TextView) commentView.findViewById(R.id.comment_item_textview);
                commentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != itemOpListner) {
                            itemOpListner.onReplyComment(item, comment, vh.ll_comment_reply_content);
                        }
                    }
                });
                String displayTime = getFormatDateString(comment.getCreatedat());
                if (!TextUtils.isEmpty(comment.getCommentfor())) {
                    commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + comment.getNickname() + " 回复 " +
                                    comment.getCommentfor_name() + ":" + "</font>" +
                                    comment.getContexts()
//                            + "<font color=\"#8194AA\">" +
//                            "(" + displayTime + ")" + "</font>"
                    ));
                } else {
                    commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + comment.getNickname() + ":" + "</font>" +
                                    comment.getContexts()
//                            + "<font color=\"#8194AA\">" +
//                            "(" + displayTime + ")" + "</font>"
                    ));
                }

                vh.ll_comment_reply_content.addView(commentView);
            }
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
    private void setClickListener(final ViewHolder vh, final ArrayList<String> urlsList, final String objectId, final ItemBean item, final int position) {
        vh.gdRentalPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("item", item);
                ActivityHelper.goBuyProcedureExt(context, bundle);
//                List<String> list = new ArrayList<String>();
//                String[] urls = new String[urlsList.size()];
//                urlsList.toArray(urls);
//                Intent intent = new Intent(context, ImagePagerActivity.class);
//                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
//                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
//                context.startActivity(intent);
            }
        });
        vh.imgRentalAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.goUserInfoActivity(context, item.getUser_id());
            }
        });
        vh.llOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastView == vh.ll_Rental_op) {
                    if (View.VISIBLE == vh.ll_Rental_op.getVisibility()) {
                        switchDisplay(vh.ll_Rental_op, false);
                        lastShow = false;
                        lastShowPosition = -1;
                    } else {
                        switchDisplay(vh.ll_Rental_op, true);
                        lastShow = true;
                        lastShowPosition = position;
                        lastView = vh.ll_Rental_op;
                    }
                } else {
                    if (lastShow) {
                        switchDisplay(lastView, false);
                    }
                    switchDisplay(vh.ll_Rental_op, true);
                    lastShow = true;
                    lastShowPosition = position;
                    lastView = vh.ll_Rental_op;
                }
            }
        });

        vh.tvRentalPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != itemOpListner) {
                    itemOpListner.onItemSupport(item);
                }
            }
        });
        vh.tvRentalComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != itemOpListner) {
                    itemOpListner.onItemComment(item, vh.ll_comment_reply_content);
                }
            }
        });
        vh.tvRentalCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != itemOpListner) {
                    itemOpListner.onItemCollect(item);
                }
            }
        });
    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.iv_group_img)
        SimpleDraweeView iv_group_img;
        @Bind(R.id.tv_group_name)
        TextView tv_group_name;
        @Bind(R.id.img_rental_avator)
        SimpleDraweeView imgRentalAvator;
        @Bind(R.id.rl_group_rental_owner)
        RelativeLayout rlGroupRentalOwner;
        @Bind(R.id.ll_group_rental_arrow)
        LinearLayout llGroupRentalArrow;
        @Bind(R.id.img_group_rental_arrow)
        ImageView imgGroupRentalArrow;
        @Bind(R.id.tv_rental_name)
        TextView tvRentalName;
        @Bind(R.id.tv_rental_current_price)
        TextView tvRentalCurrentPrice;//当前价格
        @Bind(R.id.tv_rental_original_price)
        TextView tvRentalOriginalPrice;//原价
        @Bind(R.id.tv_rental_opera_type)
        TextView tvRentalOperaType;//买卖类型
        @Bind(R.id.tv_rental_economize_price)
        TextView tvRentalEconomizePrice;//节省价格
        @Bind(R.id.tv_rental_discripe)
        TextView tvRentalDisCripe;//描述
        @Bind(R.id.gd_rental_photos)
        NoScrollGridView gdRentalPhotos;
        @Bind(R.id.ll_rental_op)
        LinearLayout ll_Rental_op;
        @Bind(R.id.ll_rental_operate)
        LinearLayout llOperate;
        @Bind(R.id.tv_rental_praise)
        TextView tvRentalPraise;
        @Bind(R.id.tv_rental_comment)
        TextView tvRentalComment;
        @Bind(R.id.tv_rental_collect)
        TextView tvRentalCollect;
        @Bind(R.id.ll_comment_reply)
        LinearLayout ll_comment_reply;
        @Bind(R.id.ll_comment_reply_content)
        LinearLayout ll_comment_reply_content;
        @Bind(R.id.tv_rental_current_position)
        TextView tv_rental_current_position;

        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemOpListener {
        public void onItemSupport(ItemBean item);
        public void onItemComment(ItemBean item, LinearLayout commentLayout);
        public void onReplyComment(ItemBean item, ItemCommentBean itemCommentBean, LinearLayout commentLayout);
        public void onItemCollect(ItemBean item);
    }
}
