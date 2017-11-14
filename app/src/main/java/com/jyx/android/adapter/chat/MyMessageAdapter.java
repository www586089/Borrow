package com.jyx.android.adapter.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.adapter.discovery.MyGridAdapter;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.model.ItemEntity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 消息界面，一个图像+一行字，一个介绍+时间+建子群
 * Created by gaobo on 2015/10/30.
 */
public class MyMessageAdapter extends RecycleBaseAdapter {

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return getLayoutInflater(parent.getContext()).inflate(
                R.layout.list_cell_my_message, null);
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
        vh.content.setText(item.getName());

        ArrayList<String> arrs=new ArrayList<String>();
        arrs.add("33");
        arrs.add("3d3");
        MyGridAdapter  myGridAdapter=new MyGridAdapter();
        myGridAdapter.setData(arrs);

//
//        vh.gridView.setVisibility(View.VISIBLE);
//
//
//        vh.gridView.setAdapter(myGridAdapter);
//        vh.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Application.showToast("点击事件");
//            }
//        });

    }


    static class ViewHolder extends RecycleBaseAdapter.ViewHolder {

        @Bind(R.id.content)
        TextView content;




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
