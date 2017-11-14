package com.jyx.android.fragment.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jyx.android.R;
import com.jyx.android.activity.contact.ApplyGroupActivity;
import com.jyx.android.activity.contact.EditGroupInfoActivity;
import com.jyx.android.activity.contact.EstablishGroupActivity;
import com.jyx.android.activity.contact.GroupActivity;
import com.jyx.android.activity.contact.PublishGroupNoticeActivity;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.ImageOptions;
import com.jyx.android.widget.view.NoScrollGridView;
import com.jyx.android.widget.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-26
 */
public class GroupDetailFragment extends BaseFragment {

    @Bind(R.id.grid_view)
    NoScrollGridView mGridView;
    @Bind(R.id.tv_group_name)
    TextView mTvGroupName;
    @Bind(R.id.ll_group_name)
    LinearLayout mLlGroupName;
    @Bind(R.id.tv_group_announce)
    TextView mTvGroupAnnounce;
    @Bind(R.id.btn_delete)
    Button mBtnDelete;
    @Bind(R.id.pb_loading)
    ProgressWheel mPbLoading;
    @Bind(R.id.ll_group_container)
    LinearLayout mLlGroupContainer;
    @Bind(R.id.cb_do_not_disturb)
    ToggleButton mCbDoNotDisturb;
    @Bind(R.id.tv_clear_history)
    TextView tvClearHistory;

    private String mGroupId;
    private String mGroupName;

    private GroupAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fg_group_detail;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(GroupActivity.KEY_GROUP_ID);
            mGroupName = getArguments().getString(GroupActivity.KEY_GROUP_NAME);

            mTvGroupName.setText(mGroupName);
            GetMyGroupList();
            fetchRemoteData();

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    /*
    获取群内成员
     */
    private void fetchRemoteData() {
        String param = "{\"function\":\"getgroupuser\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"groupid\":\"" + mGroupId + "\"}";
        ApiManager.getApi()
                .queryContact(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<FriendInfo>>())
                .subscribe(new Subscriber<List<FriendInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showLoading(false);
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        showLoading(false);
                        mAdapter = new GroupAdapter(friendInfos);
                        mGridView.setAdapter(mAdapter);
                    }
                });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvClearHistory.setVisibility(View.VISIBLE);
                    mBtnDelete.setText("解散此群");
                    break;
                case 0:
                    tvClearHistory.setVisibility(View.GONE);
                    mBtnDelete.setText("退出此群");
                    break;
            }
        }
    };
    private void GetMyGroupList(){
        String params = "{\"function\":\"getmygrouplist\",\"userid\":\"" + UserRecord.getInstance().getUserId() +"\"}";
        ApiManager.getApi()
                .queryGroups(params)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new ResultConvertFunc<List<GroupInfo>>())
                .subscribe(new Subscriber<List<GroupInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<GroupInfo> groupInfos) {
                        Message msg=new Message();
                        for (GroupInfo info :groupInfos){
                            if (info.getIsManagement().equals("1")){
                                if (info.getGroupId().equals(mGroupId)){
                                    msg.what=1;
                                    handler.sendMessage(msg);
                                    return;
                                }
                            }
                        }
                        msg.what=0;
                        handler.sendMessage(msg);
                    }
                });
    }

    private void deleteGroup(int type) {
        showWaitDialog();
//        退群{"function":"quitgroup","userid":"","groupid":""}
        String param="";
        if (type==1){
            param = "{\"function\":\"quitgroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"groupid\":\"" + mGroupId + "\"}";
        }
        else {
            param = "{\"function\":\"removegroup\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                    + "\",\"groupid\":\"" + mGroupId + "\"}";
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
                    public void onNext(List<Void> friendInfos) {
                        hideWaitDialog();
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
    }

    private void showLoading(boolean show) {
        if (mPbLoading != null) {
            if(show){
                mPbLoading.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.INVISIBLE);
                mLlGroupContainer.setVisibility(View.INVISIBLE);
                mBtnDelete.setVisibility(View.INVISIBLE);
            }else {
                mPbLoading.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
                mLlGroupContainer.setVisibility(View.VISIBLE);
                mBtnDelete.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class GroupAdapter extends BaseAdapter {
        private List<FriendInfo> friendInfoList;

        public GroupAdapter(List<FriendInfo> friendInfoList) {
            this.friendInfoList = friendInfoList;
        }

        @Override
        public int getCount() {
            return friendInfoList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == (getCount() - 1)) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_add_group_member, null);
                convertView.findViewById(R.id.iv_avatar).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EstablishGroupActivity.class);
                        intent.putExtra(GroupActivity.KEY_GROUP_ID, mGroupId);
                        startActivityForResult(intent, 2);

                    }
                });
                return convertView;
            }

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_group_member, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            FriendInfo friendInfo = friendInfoList.get(position);
            if(!(friendInfo.getPortraitUri()).equals("")){
                String url=friendInfo.getPortraitUri();
                Log.e("imgURl",friendInfo.getPortraitUri());
                ImageLoader.getInstance().displayImage(url,  holder.mroundImageView, ImageOptions.get_gushi_Options());
//                holder.mroundImageView.setImageURI(Uri.parse(friendInfo.getPortraitUri()));
            }
            holder.mTvName.setText(friendInfo.getNickName());
            return convertView;
        }

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_cell_group_member.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.sdv_avatar)
        RoundImageView mroundImageView;
        @Bind(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            String stringExtra = data.getStringExtra(GroupActivity.KEY_GROUP_NAME);
            mTvGroupName.setText(stringExtra);
        }
        if(resultCode == Activity.RESULT_OK && requestCode == 2){
            showLoading(true);
            fetchRemoteData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.ll_group_name, R.id.tv_group_announce, R.id.tv_clear_history, R.id.btn_delete})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_group_name:{
                Intent intent = new Intent(getActivity(), EditGroupInfoActivity.class);
                intent.putExtra(GroupActivity.KEY_GROUP_ID, mGroupId);
                intent.putExtra(GroupActivity.KEY_GROUP_NAME, mTvGroupName.getText().toString());
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.tv_group_announce:{
                Intent intent = new Intent(getActivity(), PublishGroupNoticeActivity.class);
                intent.putExtra(GroupActivity.KEY_GROUP_ID, mGroupId);
                startActivity(intent);
                break;
            }
            case R.id.tv_clear_history:
                Intent intent=new Intent(getActivity(), ApplyGroupActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_delete:
                if (mBtnDelete.getText().toString().trim().equals("解散此群")) {
                    deleteGroup(0);
                }
                else if (mBtnDelete.getText().toString().trim().equals("退出此群")) {
                    deleteGroup(1);
                }
                break;
            case R.id.cb_do_not_disturb:
                break;
        }
    }




    public void onEventMainThread(String groupName){
        mTvGroupName.setText(groupName);
    }
}
