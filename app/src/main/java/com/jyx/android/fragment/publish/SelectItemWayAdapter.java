package com.jyx.android.fragment.publish;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.SysDataItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zfang on 2015/12/29.
 */
public class SelectItemWayAdapter extends RecycleBaseAdapter {
    private Animation animRightIn,animRightOut;//按钮伸缩动画
    private Context context = null;
    private OnCheckClistener listener = null;
    public SelectItemWayAdapter(Context context) {
        this.context = context;
    }

    public OnCheckClistener getListener() {
        return listener;
    }

    public void setListener(OnCheckClistener listener) {
        this.listener = listener;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_select_item_way, null);
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }

    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        final ViewHolder vh = (ViewHolder) holder;

        final SysDataItem item = (SysDataItem) _data.get(position);
        vh.tv_publish_select_item.setText(item.getName());
        setClickListener(vh, item);
    }

    private void setClickListener(final ViewHolder vh, final SysDataItem item) {
        vh.rdoBtn_publish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && null != listener) {
                    listener.onCheckClick(item);
                }
            }
        });
    }



    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {
        @Bind(R.id.rdoBtn_publish)
        RadioButton rdoBtn_publish;
        @Bind(R.id.tv_publish_select_item)
        TextView tv_publish_select_item;
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCheckClistener {
        public void onCheckClick(SysDataItem item);
    }
}
