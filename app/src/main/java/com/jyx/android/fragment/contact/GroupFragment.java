package com.jyx.android.fragment.contact;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jyx.android.R;
import com.jyx.android.activity.chat.NewSubGroupActivity;
import com.jyx.android.adapter.contact.GroupListAdapter;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.PinyinUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class GroupFragment extends BaseFragment implements TextWatcher, AdapterView.OnItemClickListener {
    public static final String TAG = GroupFragment.class.getSimpleName();
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.rl_search_view)
    RelativeLayout mRlSearchView;
    @Bind(R.id.lv_group)
    ListView mLvGroup;
    @Bind(R.id.pb_loading)
    ProgressWheel mPbLoading;

    private GroupListAdapter mAdapter;

    public static final String KEY_MODE = "mode";

    public static final int MODE_NORMAL = 0;
    public static final int MODE_SINGLE_CHOICE = 1;
    private int mode;
    private String mParentGroupId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null){
            mode = getArguments().getInt(KEY_MODE, MODE_NORMAL);
            mParentGroupId = getArguments().getString(NewSubGroupActivity.KEY_PARENT_GROUP_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        fetchRemoteData();
    }

    private void fetchRemoteData(){
        String params = "{\"function\":\"getmygrouplist\",\"userid\":\"" + UserRecord.getInstance().getUserId() +"\"}";
        ApiManager.getApi()
                .queryGroups(params)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Action1<BaseEntry<List<GroupInfo>>>() {
                    @Override
                    public void call(BaseEntry<List<GroupInfo>> listBaseEntry) {
                        if (listBaseEntry != null && listBaseEntry.getData() != null) {
                            for (GroupInfo groupInfo : listBaseEntry.getData()) {
                                groupInfo.setPinyin(PinyinUtils.haizi2Pinyin(groupInfo.getGroupName()));
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResultConvertFunc<List<GroupInfo>>())
                .subscribe(new Subscriber<List<GroupInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<GroupInfo> groupInfos) {
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
                        initView(groupInfos);
                    }
                });
    }

    private void initView(List<GroupInfo> groupInfos) {
        if(mode != MODE_NORMAL && !TextUtils.isEmpty(mParentGroupId)){
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setGroupId(mParentGroupId);
            groupInfos.remove(groupInfo);
        }
        mAdapter = new GroupListAdapter(groupInfos, mode);
        mLvGroup.setAdapter(mAdapter);
        mEtSearch.addTextChangedListener(this);
        mLvGroup.setOnItemClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mode == MODE_SINGLE_CHOICE){
            mAdapter.updateSelectItems(mAdapter.getDataList().get(position), true);
            return;
        }
//        Intent intent = new Intent(getActivity(), GroupActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(GroupActivity.KEY_GROUP_ID, mAdapter.getDataList().get(position).getGroupId());
//        bundle.putString(GroupActivity.KEY_GROUP_NAME, mAdapter.getDataList().get(position).getGroupName());
//        intent.putExtras(bundle);
//        startActivityForResult(intent, 0);
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().startGroupChat(getActivity(),
                    mAdapter.getDataList().get(position).getGroupId(),
                    mAdapter.getDataList().get(position).getGroupName());
            SharedPreferences sp = getActivity().getSharedPreferences("user", 0);
            sp.edit().putString("groupid", mAdapter.getDataList().get(position).getGroupId()).commit();
        }
    }


    public List<GroupInfo> getSelectGroupItems(){
        return mAdapter.getSelectedItems();
    }
}
