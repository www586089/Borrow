package com.jyx.android.activity.buy;

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
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.LoveItemBean;
import com.jyx.android.model.SysDataItem;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zfang on 2016-02-29.
 */
public class LoveItemUserAdapter extends RecycleBaseAdapter {
    private Context context = null;
    private OnCheckClistener listener = null;
    private SweetAlertDialog pDialogQuery = null;
    private List<FriendInfo> friendInf =new ArrayList<FriendInfo>();
    public LoveItemUserAdapter(Context context,List<FriendInfo> friendInf) {
        this.context = context;
        this.friendInf=friendInf;
    }

    public OnCheckClistener getListener() {
        return listener;
    }

    public void setListener(OnCheckClistener listener) {
        this.listener = listener;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_love_item_user, null);
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        if (0 == _data.size()) {
            return;
        }
        final ViewHolder vh = (ViewHolder) holder;

        final LoveItemBean item = (LoveItemBean) _data.get(position);
        if (!TextUtils.isEmpty(item.getPortraituri())) {
            vh.iv_likelists_img.setImageURI(Uri.parse(item.getPortraituri()));
//            ImageLoader.getInstance().displayImage(item.getPortraituri(), vh.iv_likelists_img, ImageOptions.get_portrait_Options() );
        } else {
            vh.iv_likelists_img.setImageResource(R.mipmap.me_photo_2x_81);
        }
        vh.tv_likelists_name.setText(item.getNickname());
        vh.tv_likelists_note.setText(item.getSignature());
        setClickListener(vh, item);
        for (FriendInfo friend:friendInf){
            if (friend.getUserId().equals(item.getUser_id())){
                vh.btn_likelists_attention.setText("取消关注");
            }
        }
    }

    private void setClickListener(final ViewHolder vh, final LoveItemBean item) {
        vh.btn_likelists_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.btn_likelists_attention.getText().toString().equals("关注")){
                    updateRelation(1, item.getUser_id(), vh);
                }else {
                    updateRelation(0, item.getUser_id(), vh);
                }
            }
        });
        vh.iv_likelists_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.goUserInfoActivity(context, item.getUser_id());
            }
        });
    }

    private void showWaitDialog() {
        if (null == pDialogQuery) {
            pDialogQuery = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE).setTitleText("请稍候.....");
            pDialogQuery.show();
            pDialogQuery.setCancelable(false);
        }
    }
    private void hideWaitDialog() {
        if (null != pDialogQuery) {
            pDialogQuery.dismissWithAnimation();
        }
    }
    private void updateRelation(int relation, String mUserId, final ViewHolder vh){
        showWaitDialog();
        String param = null;
        if(relation == 0){
            param = "{\"function\":\"removeattention\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"followid\":\"" + mUserId + "\"}";
        }else if (relation==1){
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
                        hideWaitDialog();
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voids) {
                        hideWaitDialog();
                        if (vh.btn_likelists_attention.getText().toString().equals("关注"))
                        vh.btn_likelists_attention.setText("取消关注");
                        else
                            vh.btn_likelists_attention.setText("关注");
                    }
                });
    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {
        @Bind(R.id.iv_likelists_img)
        SimpleDraweeView iv_likelists_img;
        @Bind(R.id.tv_likelists_name)
        TextView tv_likelists_name;
        @Bind(R.id.tv_likelists_note)
        TextView tv_likelists_note;
        @Bind(R.id.tv_action)
        TextView btn_likelists_attention;
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCheckClistener {
        public void onCheckClick(SysDataItem item);
    }
}
