package com.jyx.android.fragment.chat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.SearchActivity;
import com.jyx.android.adapter.chat.AbsBaseRentalAdapter;
import com.jyx.android.adapter.purchase.SearchLisetAdapter;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.GetProListParam;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemBeanList;
import com.jyx.android.model.ItemCommentBean;
import com.jyx.android.model.ItemCommentParam;
import com.jyx.android.model.ListEntity;
import com.jyx.android.model.LoveItemParam;
import com.jyx.android.model.PraisenItemInfoBean;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.model.SupportItemParam;
import com.jyx.android.net.ApiManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class AbsBaseRentalFragment extends BaseRecycleViewFragment {

    protected static final String TAG = AbsBaseRentalFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "AbsBaseRentalFragment";
    private static final String FRIEND_SCREEN = "AbsBaseRentalFragment";
    public static final String BUNDLE_KEY_UID = "AbsBaseRentalFragment";
    private int mUid;
    protected AbsBaseRentalAdapter rentalAdapter = null;
    protected FragmentActivity mActivity = null;
    protected Context mContext = null;
    private RelativeLayout mSearchView = null;
    protected Dialog inputDialog;
    protected View inputView;
    protected EditText inputEditText;
    protected Button sendButton;
    protected  ItemBean itemData;
    private TextView et_item_search;
    private ListView lv_item_search;
    private List<ItemBean> itemList = null;
    private boolean isReply = false;
    private ItemCommentBean commentBean;
    private LinearLayout commentReplyLayout;
    private LayoutInflater inflater = null;
    private SearchLisetAdapter serachAdaoter;
    protected AbsBaseRentalAdapter.OnItemOpListener opListener = new AbsBaseRentalAdapter.OnItemOpListener() {
        @Override
        public void onItemSupport(final ItemBean item) {
            SupportItemParam xml = new SupportItemParam();
            xml.setFunction("praiseitem");
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setItemid(item.getItem_id());
            Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().praiseItem(new Gson().toJson(xml));
            result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<PublishItemResultBean>> body = response.body();
                        if (0 == body.getResult()) {
                            List<PraisenItemInfoBean>  loveList = item.getPraisenitem();
                            if (null == loveList) {
                                loveList = new ArrayList<PraisenItemInfoBean>();
                            }

                            List<PublishItemResultBean> resultList = body.getData();
                            if (null == resultList || (null != resultList && 0 == resultList.size())) {
                                int index = -1;
                                for (int i = 0; i < loveList.size(); i++) {
                                    if (UserRecord.getInstance().getUserId().equals(loveList.get(i).getUser_Id())) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (-1 != index) {
                                    loveList.remove(index);
                                }
                            } else {
                                PraisenItemInfoBean bean = new PraisenItemInfoBean();
                                bean.setNickname(UserRecord.getInstance().getNickName());
                                bean.setUser_Id(UserRecord.getInstance().getUserId());
                                bean.setPraise_item_id(body.getData().get(0).getId());
                                loveList.add(bean);
                            }
                            item.setPraisenitem(loveList);
                            item.setPraisenum(loveList.size());
                            getListAdapter().notifyDataSetChanged();
                            Application.showToast("提交成功");
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Error onItemSupport.");
                }
            });
        }

        @Override
        public void onItemComment(ItemBean item, LinearLayout commentLayout) {
            itemData = item;
            commentReplyLayout = commentLayout;
            inputEditText.setText("");
            showInputDialog(false, "");
        }

        @Override
        public void onReplyComment(ItemBean item, ItemCommentBean itemCommentBean, LinearLayout commentLayout) {
            itemData = item;
            commentBean = itemCommentBean;
            commentReplyLayout = commentLayout;
            inputEditText.setText("");
            showInputDialog(true, itemCommentBean.getNickname());
        }

        @Override
        public void onItemCollect(ItemBean item) {
            LoveItemParam xml = new LoveItemParam();
            xml.setFunction("loveitem");
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setItemid(item.getItem_id());
            Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().loveItem(new Gson().toJson(xml));
            result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<PublishItemResultBean>> body = response.body();
                        if (0 == body.getResult()) {
                            Application.showToast("提交成功");
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Error onItemSupport.");
                }
            });
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mUid = args.getInt(BUNDLE_KEY_UID);
        }
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        initFragment();
        initCommentDialog();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        initHeaderView(1);
    }

    private void showInputDialog(boolean flag, String replyer) {
        isReply = flag;
        if (flag) {
            inputEditText.setHint("回复" + replyer);
        } else {
            inputEditText.setHint("评论");
        }

        inputDialog.show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) inputEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }
    protected void initCommentDialog() {
        inputView = mActivity.getLayoutInflater().inflate(R.layout.common_input_popup, null);
        inputDialog = new Dialog(mActivity, R.style.transparentFrameWindowStyle);
        inputDialog.setContentView(inputView);
        Window window = inputDialog.getWindow();
        // 设置显示动画
        // window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        wl.width = (int) (display.getWidth()); // 设置宽度
        // dialog.getWindow().setAttributes(wl);
        // 设置显示位置
        window.setGravity(Gravity.BOTTOM);
        inputDialog.onWindowAttributesChanged(wl);
        // 设置点击外围消散
        // inputDialog.setCanceledOnTouchOutside(true);
        inputEditText = (EditText) inputView.findViewById(R.id.common_input_popup_edittext);
        sendButton = (Button) inputView.findViewById(R.id.common_input_popup_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputText = inputEditText.getText().toString().trim();
                if (inputText != null && inputText.length() > 0) {
                    ItemCommentParam xml = new ItemCommentParam();
                    xml.setFunction("submititemcomment");
                    if (isReply) {//userId replay for commentBean's user
                        xml.setUserid(UserRecord.getInstance().getUserId());
                        xml.setItemid(itemData.getItem_id());
                        xml.setTheme(itemData.getName());
                        xml.setContexts(inputText);
                        xml.setCommentfor(commentBean.getUser_id());
                    } else {
                        xml.setUserid(UserRecord.getInstance().getUserId());
                        xml.setItemid(itemData.getItem_id());
                        xml.setTheme(itemData.getName());
                        xml.setContexts(inputText);
                    }
                    Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().commentItem(new Gson().toJson(xml));
                    result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                        @Override
                        public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                            if (null != response && response.isSuccess()) {
                                BaseEntry<List<PublishItemResultBean>> body = response.body();
                                if (null != getActivity() && 0 == body.getResult()) {
                                    Application.showToast("提交成功");
                                    addCommentItemBean(inputText);
                                    getListAdapter().notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e(TAG, "Error onItemSupport.");
                        }
                    });
                    inputDialog.dismiss();
                    closeInputMethod();
                } else  {
                    Application.showToast("请输入评价内容！");
                }
            }
        });
    }

    private void addCommentItemBean(String inputText) {
        ItemCommentBean itemCommentBean = new ItemCommentBean();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String displayTime = sdf.format(calendar.getTime());
        if (isReply) {
            itemCommentBean.setUser_id(UserRecord.getInstance().getUserId());
            itemCommentBean.setNickname(UserRecord.getInstance().getNickName());
            itemCommentBean.setItem_comment_id(itemData.getItem_id());
            itemCommentBean.setContexts(inputText);
            itemCommentBean.setCreatedat(displayTime);
            itemCommentBean.setCommentfor(commentBean.getUser_id());
            itemCommentBean.setCommentfor_name(commentBean.getNickname());
        }  else {
            itemCommentBean.setUser_id(UserRecord.getInstance().getUserId());
            itemCommentBean.setNickname(UserRecord.getInstance().getNickName());
            itemCommentBean.setItem_comment_id(itemData.getItem_id());
            itemCommentBean.setContexts(inputText);
            itemCommentBean.setCreatedat(displayTime);
        }
        for (ItemBean item : itemList) {
            if (item.getItem_id().equals(itemCommentBean.getItem_comment_id())) {
                item.getItemcomment().add(itemCommentBean);
                break;
            }
        }
    }

    private void addCommentItemView(String inputText) {
        View commentView = null;
        commentView = inflater.inflate(R.layout.comment_item_cell, null);
        TextView commenterTextView = (TextView) commentView.findViewById(R.id.comment_item_textview);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String displayTime = sdf.format(calendar.getTime());
        if (isReply) {
            commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + UserRecord.getInstance().getNickName() + " 回复 " +
                    commentBean.getNickname() + ":" + "</font>" +
                    inputText + "<font color=\"#8194AA\">" +
                    "(" + displayTime + ")" + "</font>"));
        } else {
            commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + UserRecord.getInstance().getNickName() + ":" + "</font>" +
                    inputText + "<font color=\"#8194AA\">" +
                    "(" + displayTime + ")" + "</font>"));
        }
        View parent = (View) commentReplyLayout.getParent();
        if (View.GONE == parent.getVisibility()) {
            parent.setVisibility(View.VISIBLE);
            commentReplyLayout.removeAllViews();
        }
        commentReplyLayout.addView(commentView);
    }
    private void closeInputMethod() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) inputEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
    }

    private void initFragment() {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
    }
    public void initHeaderView(final int type) {

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.list_header_search_bar, null);
        mSearchView = (RelativeLayout) headView.findViewById(R.id.rl_search_view);
        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("page",mCurrentPage);
                intent.putExtra("type",type+"");
                itemList = rentalAdapter.getData();

                intent.putParcelableArrayListExtra("item", (ArrayList<? extends Parcelable>) itemList);
                startActivity(intent);
//                getRootFragment().startActivityForResult(intent, 10);
            }
        });

//        et_item_search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String keyWord = s.toString();
//                if (null != keyWord && keyWord.trim().length() > 0) {
//                    if (null == itemList) {
//                        itemList = rentalAdapter.getData();
//                    }
//                    final ArrayList<ItemBean> tmpList = new ArrayList<ItemBean>();
//                    if (type==3){
//                        for (ItemBean item : itemList) {
//                            if (item.getDiscribe().contains(keyWord)) {
//                                tmpList.add(item);
//                            }
//                        }
//                    }else {
//                        GetProListParam xml = new GetProListParam();
//                        if (type == 1) {
//                            xml.setFunction("getitemlist");
//                        } else {
//                            xml.setFunction("getgroupitemlist");
//                        }
//                        xml.setUserid(UserRecord.getInstance().getUserId());
//                        getQueryType(xml);
//                        xml.setPagenumber(mCurrentPage + "");
//                        xml.setSearchkey(keyWord);
//                        Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(new Gson().toJson(xml));
//                        result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
//                            @Override
//                            public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
//                                Log.e(TAG, "onResponse");
//                                if (response.isSuccess()) {
//                                    BaseEntry<List<ItemBean>> body = response.body();
//                                    if (0 == body.getResult()) {
//                                        List<ItemBean> item = body.getData();
//                                        tmpList.addAll(item);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Throwable t) {
//                                Log.e(TAG, "onFailure");
//                                DoComHandleTask(null, false);
//                            }
//                        });
//                    }
//                    rentalAdapter.setData(tmpList);
//                } else {
//                    rentalAdapter.setData(itemList);
//                }
//            }
//        });
        rentalAdapter.setHeaderView(headView);
    }

    /**
     * 得到根Fragment
     *
     * @return
     */
    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.rl_search_view:
//                //FIXME
//                Toast.makeText(getActivity(), "go to search ", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    @Override
    protected RecycleBaseAdapter getListAdapter() {
        initAdapter();
        return rentalAdapter;
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }

    protected ListEntity parseComAvoList(List<?> listavos ) throws Exception {

        itemList = (List<ItemBean>) listavos;
        List<ItemBean> listobjs = (List<ItemBean>) listavos;
        ItemBeanList itemBeanList = new ItemBeanList();
        List<ItemBean> listItem = new ArrayList<ItemBean>();
        for (ItemBean object : listobjs) {
            listItem.add(object);
        }
        itemBeanList.setItemList(listItem);
        return itemBeanList;
    }

    @Override
    protected void sendRequestData() {
        if (0 == mCurrentPage) {
            mCurrentPage = 1;
        }
        getData();
    }


    @Override
    public void onItemClick(View view, int position) {
        final ItemBean item = (ItemBean) rentalAdapter.getData().get(position - 1);
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", item);
        ActivityHelper.goBuyProcedureExt(getActivity(), bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    abstract protected void getData();
    protected abstract void initAdapter();
    protected abstract void getQueryType(GetProListParam param);
}
