package com.jyx.android.fragment.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.jyx.android.R;
import com.jyx.android.activity.me.UserInfoActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.event.ContactEvent;
import com.jyx.android.fragment.me.MeFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.PhoneFriend;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.ACache;
import com.jyx.android.utils.FriendPinyinComparator;
import com.jyx.android.utils.PinyinUtils;
import com.jyx.android.widget.view.BladeView;
import com.jyx.android.widget.view.PinnedSectionListView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class FriendFragment extends BaseFragment implements TextWatcher, ContactListAdapter.onContactItemClickListener {
    public static final String TAG = FriendFragment.class.getSimpleName();



    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.lv_friend)
    PinnedSectionListView mLvFriend;
    @Bind(R.id.blade_view)
    BladeView mBladeView;

    @Bind(R.id.pb_loading)
    ProgressWheel mPbLoading;

    private ContactListAdapter mAdapter;
    private ACache mAcache;

    private ArrayMap<String, Integer> maps = new ArrayMap<>();

    public static final String KEY_PAGE_TYPE = "page_type";

    private int mPageType;


    private Set<String> mSelectedUserIds = new HashSet<>();

    public static FriendFragment newInstance(int pageType){
        FriendFragment f = new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_TYPE, pageType);
        f.setArguments(bundle);
        return f;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fg_friend;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments() != null){
            mPageType = getArguments().getInt(KEY_PAGE_TYPE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchRemoteData();
    }

    private void fetchRemoteData() {
        final String functionName = mPageType == ContactListAdapter.PAGE_FOLLOW ? "getmyattention" :
                (mPageType == ContactListAdapter.PAGE_FAN ? "getmyfans" : "getmaillist");
        String param = "{\"function\":\"" + functionName + "\",\"userid\":\"" + UserRecord.getInstance().getUserId() + "\"}";
        ApiManager.getApi()
                .queryContact(param)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .doOnNext(new Action1<BaseEntry<List<FriendInfo>>>() {
                    @Override
                    public void call(BaseEntry<List<FriendInfo>> listBaseEntry) {
                        if (listBaseEntry != null && listBaseEntry.getData() != null) {
                            for (FriendInfo friendInfo : listBaseEntry.getData()) {
                                friendInfo.setPinyin(PinyinUtils.haizi2Pinyin(friendInfo.getNickName()));
                                if (!TextUtils.isEmpty(friendInfo.getPinyin())) {
                                    char firstSpell = friendInfo.getPinyin().charAt(0);
                                    if (firstSpell <= 'Z' && firstSpell >= 'A') {
                                        friendInfo.setFirstSpell(String.valueOf(firstSpell));
                                    } else {
                                        friendInfo.setFirstSpell("#");
                                    }
                                }
                            }
                            Collections.sort(listBaseEntry.getData(), new FriendPinyinComparator());
                            List<FriendInfo> newList = new ArrayList<>();
                            for (int i = 0; i < listBaseEntry.getData().size(); i++) {
                                FriendInfo friendInfo = listBaseEntry.getData().get(i);
                                if (i == 0) {
                                    FriendInfo sectionInfo = new FriendInfo();
                                    sectionInfo.setFirstSpell(friendInfo.getFirstSpell());
                                    sectionInfo.setType(FriendInfo.SECTION);
                                    newList.add(sectionInfo);
                                    newList.add(friendInfo);
                                    maps.put(friendInfo.getFirstSpell(), 0);
                                }else {
                                    if(!listBaseEntry.getData().get(i - 1).getFirstSpell().equals(friendInfo.getFirstSpell())){
                                        FriendInfo sectionInfo = new FriendInfo();
                                        sectionInfo.setFirstSpell(friendInfo.getFirstSpell());
                                        sectionInfo.setType(FriendInfo.SECTION);
                                        newList.add(sectionInfo);

                                        maps.put(friendInfo.getFirstSpell(), newList.size() - 1);
                                    }
                                    newList.add(friendInfo);
                                }
                            }
                            listBaseEntry.setData(newList);
                        }
                    }
                })
                .map(new ResultConvertFunc<List<FriendInfo>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FriendInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        hideWaitDialog();
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
                        initView(friendInfos);
                    }
                });
    }

    private void initView(List<FriendInfo> friendInfos){
        mAcache=ACache.get(getActivity());
        ArrayList<PhoneFriend> friend=new ArrayList<PhoneFriend>();

//        friend= (ArrayList<PhoneFriend>) mAcache.getAsObject(Constants.KEY_MAIL_LIST);
        mAdapter = new ContactListAdapter(friendInfos, mPageType,getContext(),friend);
        mLvFriend.setAdapter(mAdapter);
        mBladeView.setOnItemClickListener(new BladeView.OnItemClickListener() {
            @Override
            public void onItemClick(String s) {
                if (s != null) {
                    Integer position = maps.get(s);
                    if (position != null) {
                        mLvFriend.setSelection(position);
                    }
                }
            }
        });
        mEtSearch.addTextChangedListener(this);
        mAdapter.setClickListener(this);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mBladeView.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
        mAdapter.setMode(s.length() > 0 ? ContactListAdapter.SEARCH_MODE : ContactListAdapter.NORMAL_MODE);
        mAdapter.getFilter().filter(s.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onClick(View view, FriendInfo friendInfo, int position) {
        switch (view.getId()){
            case R.id.ll_item:
                if(mPageType == ContactListAdapter.PAGE_ESTABLISH_GROUP){
                    toggleSelect(friendInfo, position);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(KEY_PAGE_TYPE, mPageType);
                    bundle.putString(MeFragment.KEY_USER_ID, friendInfo.getUserId());
                    Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.tv_action:
                followOrUnfollow(friendInfo, position);
                break;
        }
    }

    private void toggleSelect(FriendInfo friendInfo, int position){
        if(friendInfo.isSelected()){
            mSelectedUserIds.remove(friendInfo.getUserId());
        }else {
            mSelectedUserIds.add(friendInfo.getUserId());
        }
        mAdapter.updateSelectState(position, !friendInfo.isSelected());
    }

    private void followOrUnfollow(final FriendInfo friendInfo, final int position){
        final String action = mPageType == ContactListAdapter.PAGE_FOLLOW ? "removeattention"
                : (friendInfo.getAttention() == 0 ? "addattention" : "removeattention");
        String param = "{\"function\":\"" + action
                + "\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                + "\",\"followid\":\"" + friendInfo.getUserId() + "\"}";
        showWaitDialog();
        ApiManager.getApi()
                .followOrUnfollow(param)
                .subscribeOn(Schedulers.io())
                .map(new ResultConvertFunc<List<Void>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<Void> voidBaseEntry) {
                        fetchRemoteData();
                        if (mPageType == ContactListAdapter.PAGE_FAN) {
                            EventBus.getDefault().post(new ContactEvent());
                        }
                    }
                });

    }


    public void onEventMainThread(ContactEvent event) {
        if(mPageType == ContactListAdapter.PAGE_FOLLOW || mPageType == ContactListAdapter.PAGE_CONTACT){
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
    public Set<String> getSelectedUserIds() {
        return mSelectedUserIds;
    }
}
