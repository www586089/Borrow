package com.jyx.android.fragment.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.MySqlistHelpe;
import com.jyx.android.activity.contact.ApplyFriendActivity;
import com.jyx.android.adapter.contact.ContactListAdapter;
import com.jyx.android.adapter.contact.PhoneContactListAdapter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.event.ContactEvent;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.PhoneFriend;
import com.jyx.android.model.param.ContactMobileEntity;
import com.jyx.android.model.param.ContactUploadParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.rx.ResultConvertFunc;
import com.jyx.android.utils.PhoneFriendPinyinComparator;
import com.jyx.android.utils.PinyinUtils;
import com.jyx.android.widget.view.BladeView;
import com.jyx.android.widget.view.PinnedSectionListView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class AddContactFriendFragment extends BaseFragment implements TextWatcher, PhoneContactListAdapter.onPhoneContactItemClickListener {

    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.lv_friend)
    PinnedSectionListView mLvFriend;
    @Bind(R.id.blade_view)
    BladeView mBladeView;

    @Bind(R.id.pb_loading)
    ProgressWheel mPbLoading;

    private PhoneContactListAdapter mAdapter;

    private ArrayMap<String, Integer> maps = new ArrayMap<>();

    private String md5Str;

    private SQLiteDatabase db;
    private ContentValues values;


    @Override
    protected int getLayoutId() {
        return R.layout.fg_friend;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db=new MySqlistHelpe(getActivity()).getReadableDatabase();
        fetchRemoteData();
    }

    private void fetchRemoteData() {
        Observable.create(new Observable.OnSubscribe<ContactUploadParam>() {
            @Override
            public void call(Subscriber<? super ContactUploadParam> subscriber) {
                try {
                    List<ContactMobileEntity> datas = new ArrayList<>();
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                            null, null, null, null);
                    if (cur != null && cur.getCount() > 0) {
                        while (cur.moveToNext()) {

                            String id = cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cur.getString(
                                    cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                                Cursor pCur = cr.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[]{id}, null);
                                if (pCur != null) {
                                    if (pCur.moveToFirst()) {
                                        ContactMobileEntity entity = new ContactMobileEntity();
                                        entity.setName(name);
                                        entity.setMobile(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                                        datas.add(entity);
                                    }
                                    pCur.close();
                                }

                            }
                        }
                    }
                    if(cur != null){
                        cur.close();
                    }
                    ContactUploadParam param = new ContactUploadParam();
                    param.setFunction("addcontacts");
                    param.setUserId(UserRecord.getInstance().getUserId());
                    param.setDatas(datas);

                    subscriber.onNext(param);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new BizException(-1, "上传通讯录失败"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .flatMap(new Func1<ContactUploadParam, Observable<BaseEntry<List<Void>>>>() {
                    @Override
                    public Observable<BaseEntry<List<Void>>> call(ContactUploadParam s) {
                        String lastMd5 = Application.getInstance().getSharedPreferences().getString("last_md5", "");
                        md5Str = MD5Tools.toMD5(new Gson().toJson(s));
                        if(md5Str.equals(lastMd5)){
                            return Observable.create(new Observable.OnSubscribe<BaseEntry<List<Void>>>() {
                                @Override
                                public void call(Subscriber<? super BaseEntry<List<Void>>> subscriber) {
                                    BaseEntry<List<Void>> result = new BaseEntry<>();
                                    result.setResult(304);
                                    subscriber.onNext(result);
                                }
                            });
                        }
                        return ApiManager.getApi().uploadContact(s);
                    }
                })
                .flatMap(new Func1<BaseEntry<List<Void>>, Observable<BaseEntry<List<PhoneFriend>>>>() {
                    @Override
                    public Observable<BaseEntry<List<PhoneFriend>>> call(BaseEntry<List<Void>> s) {
                        if (s != null && s.getResult() == 0) {
                            Log.d("upload contact", "contact change");
                            Application.getInstance().getSharedPreferences().edit().putString("last_md5", md5Str).apply();
                        }
                        //TODO 判断通讯录上传是否成功
                        String param = "{\"function\":\"getcontacts\",\"userid\":\""
                                + UserRecord.getInstance().getUserId() + "\"}";
                        return ApiManager.getApi()
                                .queryPhoneFriend(param);
                    }
                }).doOnNext(new Action1<BaseEntry<List<PhoneFriend>>>() {
            @Override
            public void call(BaseEntry<List<PhoneFriend>> listBaseEntry) {
                if (listBaseEntry != null && listBaseEntry.getData() != null) {
                    for (PhoneFriend friendInfo : listBaseEntry.getData()) {
                        friendInfo.setPinyin(PinyinUtils.haizi2Pinyin(friendInfo.getName()));
                        if (!TextUtils.isEmpty(friendInfo.getPinyin())) {
                            char firstSpell = friendInfo.getPinyin().charAt(0);
                            if (firstSpell <= 'Z' && firstSpell >= 'A') {
                                friendInfo.setFirstSpell(String.valueOf(firstSpell));
                            } else {
                                friendInfo.setFirstSpell("#");
                            }
                            if (TextUtils.isEmpty(friendInfo.getFirstSpell())) {
                                friendInfo.setFirstSpell("#");
                            }
                        }
                    }
                    Collections.sort(listBaseEntry.getData(), new PhoneFriendPinyinComparator());
                    List<PhoneFriend> newList = new ArrayList<>();
                    for (int i = 0; i < listBaseEntry.getData().size(); i++) {
                        PhoneFriend friendInfo = listBaseEntry.getData().get(i);
                        if (i == 0) {
                            PhoneFriend sectionInfo = new PhoneFriend();
                            sectionInfo.setFirstSpell(friendInfo.getFirstSpell());
                            sectionInfo.setType(PhoneFriend.SECTION);
                            newList.add(sectionInfo);
                            newList.add(friendInfo);
                            maps.put(friendInfo.getFirstSpell(), 0);
                        } else {
                            if (!listBaseEntry.getData().get(i - 1).getFirstSpell().equals(friendInfo.getFirstSpell())) {
                                PhoneFriend sectionInfo = new PhoneFriend();
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
                .map(new ResultConvertFunc<List<PhoneFriend>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PhoneFriend>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideWaitDialog();
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
//                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(List<PhoneFriend> friendInfos) {
                        hideWaitDialog();
                        if (mPbLoading != null) {
                            mPbLoading.setVisibility(View.GONE);
                        }
                        initView(friendInfos);
                    }
                });
    }

    private void initView(final List<PhoneFriend> friendInfos){
        mAdapter = new PhoneContactListAdapter(friendInfos);
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
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < friendInfos.size(); i++) {
                    PhoneFriend friend = friendInfos.get(i);
                    values = new ContentValues();
                    values.put("userid",friend.getUserid());
                    values.put("phone",friend.getMobile());
                    db.insert("user",null,values);
                }
            }
        };
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
    public void onClick(View view, PhoneFriend friendInfo, int position) {
        switch (view.getId()){
            case R.id.btn_add_friend:
                if("3".equals(friendInfo.getStatus())){
                    Intent intent = new Intent(getActivity(), ApplyFriendActivity.class);
                    intent.putExtra(ApplyFriendActivity.KEY_FRIEND_ID, friendInfo.getUserid());
                    startActivityForResult(intent, 0);
                }else if("4".equals(friendInfo.getStatus())){
                    Uri uri = Uri.parse("smsto:" + friendInfo.getMobile());
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", getString(R.string.invite_sms_content));
                    if(it.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(it);
                    }
                }else if("5".equals(friendInfo.getStatus())){
                    acceptFriend(friendInfo);
                }
                break;
        }
    }

    private void acceptFriend(final PhoneFriend friendInfo){
        String param = "{\"function\":\"managefriendapply\",\"applyid\":\"" + friendInfo.getUserid() + "\",\"applytype\":\"2\"}";
        showWaitDialog();
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
                        friendInfo.setStatus("1");
                        if(mAdapter != null){
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }


    public void onEventMainThread(ContactEvent event) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            fetchRemoteData();
        }
    }
}
