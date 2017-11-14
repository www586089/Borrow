package com.jyx.android.activity.chat.redenvelope;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.adapter.chat.Friend;
import com.jyx.android.adapter.chat.SelectFriendAdapter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.CharacterParser;
import com.jyx.android.base.MyLetterView;
import com.jyx.android.base.PinyinComparator;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.param.ContactParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.FriendPinyinComparator;
import com.jyx.android.utils.PinyinUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yiyi on 2015/11/9.
 */
public class SelectFrinedActivity extends BaseActivity {
    @Bind(R.id.et_selectfriend_search)
    EditText mEtSearch;
    @Bind(R.id.lv_selectfriend_list)
    ListView list_friends;
    @Bind(R.id.lv_selectfriend_right_letter)
    MyLetterView right_letter;
    @Bind(R.id.pb_loading)
    ProgressWheel mPbLoading;
    @Bind(R.id.select_friends_content_ll)
    LinearLayout select_friends_content_ll;
    List<Friend> friends = new ArrayList<Friend>();
    private ArrayMap<String, Integer> maps = new ArrayMap<>();
    private SelectFriendAdapter userAdapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private void filledDatas(List<Friend> user) {
        friends.clear();
        for (Friend sortModel : user) {
            String pinyin = characterParser.getSelling(sortModel.getUserName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            friends.add(sortModel);
        }

        Collections.sort(friends, pinyinComparator);
    }

    private void initFriendList(){
        if(userAdapter==null){
            userAdapter = new SelectFriendAdapter(this, friends);
            list_friends.setAdapter(userAdapter);
        }else{
            userAdapter.notifyDataSetChanged();
        }
    }

    private class LetterListViewListener implements
            MyLetterView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(String s) {
            // 该字母首次出现的位置
            int position = userAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                list_friends.setSelection(position);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectfriend;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_selectfriend;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected void onActionRightClick(View view) {
        super.onActionRightClick(view);
        ArrayList<Friend> listFriends = userAdapter.getSelected();
        if (0 == listFriends.size()) {
            Application.showToastShort("没有选中任何记录");
        } else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("friends", listFriends);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("确定");
        friends = new ArrayList<Friend>();
        pinyinComparator = new PinyinComparator();
        characterParser = CharacterParser.getInstance();

        //initFriendList();
        getFriends();
        right_letter.setOnTouchingLetterChangedListener(new LetterListViewListener());
        setListener();
    }

    private void getFriends() {
        select_friends_content_ll.setVisibility(View.GONE);
        mPbLoading.setVisibility(View.VISIBLE);
        ContactParam xml = new ContactParam();
        xml.setFunction("getmaillist");
        xml.setUserid(UserRecord.getInstance().getUserId());
        ApiManager.getApi()
                .queryContact(new Gson().toJson(xml))
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
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<FriendInfo> friendInfos) {
                        hideWaitDialog();
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
                        select_friends_content_ll.setVisibility(View.VISIBLE);
                        initView(friendInfos);
                    }
                });
    }

    private void initView(List<FriendInfo> friendInfos){
        List<Friend> listTmp = new ArrayList<Friend>();
        for (FriendInfo info : friendInfos) {
            Friend friend = new Friend();
            friend.setSelected(false);
            friend.setSortLetters(info.getFirstSpell());
            friend.setUserName(info.getNickName());
            friend.setUserId(info.getUserId());
            friend.setPortraituri(info.getPortraitUri());
            listTmp.add(friend);
        }
        filledDatas(listTmp);
        if(userAdapter==null){
            userAdapter = new SelectFriendAdapter(this, friends);
            list_friends.setAdapter(userAdapter);
        }else{
            userAdapter.notifyDataSetChanged();
        }
    }
    private void setListener() {
        mEtSearch.addTextChangedListener(mTextWatcher);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String key = s.toString().trim();
            if (!TextUtils.isEmpty(key)) {
                List<Friend> tmpFriends = new ArrayList<Friend>() ;
                for (Friend item : friends) {
                    if (item.getUserName().contains(key)) {
                        tmpFriends.add(item);
                    }
                }
                userAdapter.updateListView(tmpFriends);
            } else {
                userAdapter.updateListView(friends);
            }
        }
    };
}
