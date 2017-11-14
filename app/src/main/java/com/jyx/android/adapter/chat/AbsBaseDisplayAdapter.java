package com.jyx.android.adapter.chat;

import android.content.Context;
import android.content.Intent;
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
import com.jyx.android.activity.chat.ImagePagerActivity;
import com.jyx.android.activity.me.UserInfoActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.adapter.discovery.MyGridAdapter;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.fragment.contact.FriendFragment;
import com.jyx.android.fragment.me.MeFragment;
import com.jyx.android.model.NewsCommentBean;
import com.jyx.android.model.NewsFriendsBean;
import com.jyx.android.model.PraisenNewsInfoBean;
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
public class AbsBaseDisplayAdapter extends RecycleBaseAdapter {
    private Animation animRightIn,animRightOut;//按钮伸缩动画
    private Context context = null;
    private LayoutInflater inflater = null;
    protected OnItemOpListener commentClickListener = null;
    public AbsBaseDisplayAdapter(Context context) {
        this.context = context;
        init();
    }
    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_group_moments, null);
    }

    public OnItemOpListener getCommentClickListener() {
        return commentClickListener;
    }

    public void setCommentClickListener(OnItemOpListener commentClickListener) {
        this.commentClickListener = commentClickListener;
    }

    private void init(){
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
            view.setVisibility(View.GONE);
            view.startAnimation(animRightOut);
        }
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }


    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        if (null == _data || (null != _data && 0 ==  _data.size())) {
            return;
        }
        final ViewHolder vh = (ViewHolder) holder;
        final NewsFriendsBean item = (NewsFriendsBean) _data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvRentalDisCripe.setText(item.getDiscribe());
        String url = item.getPortraituri();
        String userName = item.getNickname();
        viewHolder.tvDisplayTime.setText(getFormatDateString(item.getCreatedat()));

        if (!TextUtils.isEmpty(url)) {
            vh.imgRentalAvator.setImageURI(Uri.parse(url));
//            ImageLoader.getInstance().displayImage(url, viewHolder.imgRentalAvator, ImageOptions.get_portrait_Options() );
        } else {
            vh.imgRentalAvator.setImageResource(R.mipmap.me_photo_2x_81);
        }
        vh.tvUserName.setText(userName);
        ArrayList<String> urlList = new Gson().fromJson(item.getImagejson(), new TypeToken<ArrayList<String>>() {}.getType());
        if (null != urlList && urlList.size() > 0) {
            MyGridAdapter myGridAdapter = new MyGridAdapter();
            vh.gdRentalPhotos.setAdapter(myGridAdapter);
            myGridAdapter.setData(urlList);
            vh.gdRentalPhotos.setVisibility(View.VISIBLE);
        } else {
            vh.gdRentalPhotos.setVisibility(View.GONE);
        }
        List<NewsCommentBean> listComment = item.getNewscomment();
        List<PraisenNewsInfoBean> listLove = item.getPraisenews();
        if ((null != listComment && listComment.size() > 0) || (null != listLove && listLove.size() > 0)) {
            vh.ll_comment_reply.setVisibility(View.VISIBLE);
            initCommentContent(vh, item, listComment, listLove);
        } else {
            vh.ll_comment_reply.setVisibility(View.GONE);
        }
        setClickListener(vh, urlList, item);
    }
    private void initCommentContent(final ViewHolder vh, final NewsFriendsBean item, List<NewsCommentBean> listComment, List<PraisenNewsInfoBean> listLove) {
        vh.ll_comment_reply_content.removeAllViews();
        if (null != listLove && listLove.size() > 0) {
            View loveView  = inflater.inflate(R.layout.love_item_cell, null);
            TextView comment_item_textview = (TextView) loveView.findViewById(R.id.love_item_textview);
            StringBuilder sb = new StringBuilder("♥");
            for (PraisenNewsInfoBean bean : listLove) {
                sb.append(bean.getNickname() + ", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.length());
            comment_item_textview.setText(sb.toString());
            vh.ll_comment_reply_content.addView(loveView);
        }
        if (null != listComment && listComment.size() > 0) {
            View commentView = null;
            for(final NewsCommentBean comment:listComment) {
                commentView = inflater.inflate(R.layout.comment_item_cell, null);
                TextView commenterTextView = (TextView) commentView.findViewById(R.id.comment_item_textview);
                commentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != commentClickListener) {
                            commentClickListener.onReplyComment(item, comment, vh.ll_comment_reply_content);
                        }
                    }
                });
                String displayTime = getFormatDateString(comment.getCreatedat());
                if (!TextUtils.isEmpty(comment.getCommentfor())) {
                    commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + comment.getNickname() + " 回复 " +
                            comment.getCommentfor_name() + ":" + "</font>" +
                            comment.getContexts()
//                                    + "<font color=\"#8194AA\">"
//                            + "(" + displayTime + ")" + "</font>"
                    ));
                }else {
                    commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + comment.getNickname() + ":" + "</font>" +
                            comment.getContexts()
//                                    + "<font color=\"#8194AA\">"
//                            + "(" + displayTime + ")" + "</font>"
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
    private void setClickListener(final ViewHolder vh, final List<String> urlsList, final NewsFriendsBean item) {
        vh.gdRentalPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> list = new ArrayList<String>();
                String[] urls = new String[urlsList.size()];
                urlsList.toArray(urls);
                Intent intent = new Intent(context, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                context.startActivity(intent);
            }
        });
        vh.llOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.VISIBLE == vh.ll_Rental_op.getVisibility()) {
                    switchDisplay(vh.ll_Rental_op, false);
                } else {
                    switchDisplay(vh.ll_Rental_op, true);
                }
            }
        });
        vh.llOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.VISIBLE == vh.ll_Rental_op.getVisibility()) {
                    switchDisplay(vh.ll_Rental_op, false);
                } else {
                    switchDisplay(vh.ll_Rental_op, true);
                }
            }
        });

        vh.imgRentalAvator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(FriendFragment.KEY_PAGE_TYPE, ContactListAdapter.PAGE_CONTACT);
                bundle.putString(MeFragment.KEY_USER_ID, item.getUser_id());
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        vh.tvRentalPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != commentClickListener) {
                    commentClickListener.onItemSupport(item);
                }
            }
        });
        vh.tvRentalComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != commentClickListener) {
                    commentClickListener.onItemComment(item, vh.ll_comment_reply_content);
                }
            }
        });
        vh.tvRentalCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchDisplay(vh.ll_Rental_op, false);
                if (null != commentClickListener) {
                    commentClickListener.onItemCollect(item);
                }
            }
        });
    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.iv_group_img)
        SimpleDraweeView iv_group_img;
        @Bind(R.id.tv_group_name)
        TextView tv_group_name;
        @Bind(R.id.rl_group_rental_owner)
        RelativeLayout rlGroupRentalOwner;
        @Bind(R.id.ll_group_rental_arrow)
        LinearLayout llGroupRentalArrow;
        @Bind(R.id.img_group_rental_arrow)
        ImageView imgGroupRentalArrow;
        @Bind(R.id.img_rental_avator)
        SimpleDraweeView imgRentalAvator;
        @Bind(R.id.tv_rental_name)
        TextView tvUserName;
        @Bind(R.id.tv_rental_discripe)
        TextView tvRentalDisCripe;
        @Bind(R.id.gd_rental_photos)
        NoScrollGridView gdRentalPhotos;
        @Bind(R.id.tv_display_time)
        TextView tvDisplayTime;
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
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }
    public interface OnItemOpListener {
        public void onItemSupport(NewsFriendsBean item);
        public void onItemComment(NewsFriendsBean item, LinearLayout commentLayout);
        public void onReplyComment(NewsFriendsBean item, NewsCommentBean itemCommentBean, LinearLayout commentLayout);
        public void onItemCollect(NewsFriendsBean item);
    }
}
