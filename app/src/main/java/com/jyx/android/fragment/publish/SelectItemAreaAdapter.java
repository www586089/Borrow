package com.jyx.android.fragment.publish;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.AddressItemBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zfang on 2015/12/29.
 */
public class SelectItemAreaAdapter extends RecycleBaseAdapter {
    private Context context = null;
    private OnCheckClickListener listener = null;
    public SelectItemAreaAdapter(Context context) {
        this.context = context;
    }

    public OnCheckClickListener getListener() {
        return listener;
    }

    public void setListener(OnCheckClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(R.layout.list_cell_select_item_area, null);
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

        final AddressItemBean item = (AddressItemBean) _data.get(position);
        vh.tv_publish_select_item_area.setText(item.getAddress());
        setClickListener(vh, item);
    }

    private void setClickListener(final ViewHolder vh, final AddressItemBean item) {
        vh.rdoBtn_publish_area.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && null != listener) {
                    listener.onCheckClick(item);
                }
            }
        });
    }



    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {
        @Bind(R.id.rdoBtn_publish_area)
        RadioButton rdoBtn_publish_area;
        @Bind(R.id.tv_publish_select_item_area)
        TextView tv_publish_select_item_area;
        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnCheckClickListener {
        public void onCheckClick(AddressItemBean item);
    }
}
