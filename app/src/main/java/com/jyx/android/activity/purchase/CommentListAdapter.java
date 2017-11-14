package com.jyx.android.activity.purchase;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.ItemCommentDetailBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zfang on 2016-01-30.
 */
public class CommentListAdapter extends RecycleBaseAdapter {
    private Context context = null;
    public CommentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.commentlists_item, null);
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        if (null == _data || (null != _data && 0 == _data.size())) {
            return;
        }
        if (_data != null) {
            final ViewHolder vh = (ViewHolder) holder;
            final ItemCommentDetailBean infor = (ItemCommentDetailBean) _data.get(position);
            if (infor != null) {
                vh.tv_name.setText(infor.getNickname());
                vh.tv_comment.setText(infor.getContexts());
                String dateStr = null;
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = sdf.parse(infor.getCreatedat());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dateStr = sdf.format(date);
                vh.tv_time.setText(dateStr);
                if (!TextUtils.isEmpty(infor.getPortraituri())) {
                    vh.iv_user.setImageURI(Uri.parse(infor.getPortraituri()));
//                    ImageLoader.getInstance().displayImage(infor.getPortraituri(), vh.iv_user, ImageOptions.get_portrait_Options() );
                } else {
                    vh.iv_user.setImageResource(R.mipmap.me_photo_2x_81);
                }
                vh.iv_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityHelper.goUserInfoActivity(context, infor.getUser_id());
                    }
                });
            }
        }
    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {
        @Bind(R.id.tv_commentlists_name)
        TextView tv_name;
        @Bind(R.id.tv_commentlists_comment)
        TextView tv_comment;
        @Bind(R.id.iv_commentlists_img)
        SimpleDraweeView iv_user;
        @Bind(R.id.tv_commentlists_time)
        TextView tv_time;
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }
}
