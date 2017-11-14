package com.jyx.android.fragment.publish;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
public class CrowyQualityAdapter extends RecycleBaseAdapter {
    private Context context = null;
    private OnCheckClistener listener = null;
    public CrowyQualityAdapter(Context context) {
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
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_cowryquality_item, null);
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
        vh.tv_publish_title.setText(item.getName());
        if (!TextUtils.isEmpty(item.getRemark())) {
            vh.tv_publish_summary.setVisibility(View.VISIBLE);
            vh.tv_publish_summary.setText(item.getRemark());
        } else {
            vh.tv_publish_summary.setVisibility(View.GONE);
        }
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
        @Bind(R.id.tv_publish_title)
        TextView tv_publish_title;
        @Bind(R.id.tv_publish_summary)
        TextView tv_publish_summary;
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCheckClistener {
        public void onCheckClick(SysDataItem item);
    }
}
