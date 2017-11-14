package com.jyx.android.adapter.me;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.ItemEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/10/26.
 */
public class ShowMyItemAdapter   extends RecycleBaseAdapter {

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(
                R.layout.list_cell_type, null);
    }

    @Override
    protected RecycleBaseAdapter.ViewHolder onCreateItemViewHolder(View view, int viewType) {
        return new ViewHolder(viewType, view);
    }


    @Override
    protected void onBindItemViewHolder(RecycleBaseAdapter.ViewHolder holder, int position) {
        super.onBindItemViewHolder(holder, position);
        ViewHolder vh = (ViewHolder) holder;

        final ItemEntity item = (ItemEntity) _data.get(position);
        vh.title.setText(item.getName());

    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.tv_name)
        TextView title;
//        public TextView tv_title;
//        public ImageView gender;
//        public AvatarView avatar;
//        public ProgressBar progressBar;//进度条
//        public Button install;//安装

        public ViewHolder(int viewType,View view) {
            super(viewType, view);
            ButterKnife.bind(this, view);
//            tv_title = (TextView) view.findViewById(R.id.tv_title);
//            desc = (TextView) view.findViewById(R.id.tv_desc);
//            gender = (ImageView) view.findViewById(R.id.iv_gender);
//            avatar = (AvatarView) view.findViewById(R.id.iv_avatar);
//            progressBar = (ProgressBar) view.findViewById(R.id.pb_main);
//            install = (Button) view.findViewById(R.id.install_game);
        }
    }



}
