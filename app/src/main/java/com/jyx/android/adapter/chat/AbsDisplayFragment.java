package com.jyx.android.adapter.chat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseRecycleViewFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemList;
import com.jyx.android.model.NewsCommentBean;
import com.jyx.android.model.NewsEntity;
import com.jyx.android.model.NewsFriendsBean;
import com.jyx.android.model.NewsFriendsList;
import com.jyx.android.model.PraisenNewsInfoBean;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.model.param.CollectNewsParam;
import com.jyx.android.model.param.NewsCommentParam;
import com.jyx.android.model.param.PraiseNewsParam;
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
 * Created by zfang on 2015/12/24.
 */
public abstract class AbsDisplayFragment extends BaseRecycleViewFragment {
    protected static final String TAG = AbsDisplayFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "AbsDisplayFragment";
    private static final String FRIEND_SCREEN = "AbsDisplayFragment";
    public static final String BUNDLE_KEY_UID = "AbsDisplayFragment";
    private int mUid;
    protected AbsBaseDisplayAdapter displayAdapter = null;
    protected FragmentActivity mActivity = null;
    protected Context mContext = null;
    protected Dialog inputDialog;
    protected View inputView;
    protected EditText inputEditText;
    protected Button sendButton;
    protected NewsEntity newsItem = null;

    protected NewsFriendsBean itemData;
    private boolean isReply = false;
    private NewsCommentBean commentBean;
    private LinearLayout commentReplyLayout;
    private LayoutInflater inflater = null;
    private List<NewsFriendsBean> itemList;

    protected AbsBaseDisplayAdapter.OnItemOpListener commentClickListener = new AbsBaseDisplayAdapter.OnItemOpListener() {
        @Override
        public void onItemSupport(final NewsFriendsBean item) {
            PraiseNewsParam xml = new PraiseNewsParam();
            xml.setFunction("praisenews");
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setNewsid(item.getNews_id());
            Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().praiseNews(new Gson().toJson(xml));
            result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<PublishItemResultBean>> body = response.body();
                        if (null != getActivity() && 0 == body.getResult()) {
                            List<PraisenNewsInfoBean> loveList = item.getPraisenews();
                            if (null == loveList) {
                                loveList = new ArrayList<PraisenNewsInfoBean>();
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
                                PraisenNewsInfoBean bean = new PraisenNewsInfoBean();
                                bean.setNickname(UserRecord.getInstance().getNickName());
                                bean.setUser_Id(UserRecord.getInstance().getUserId());
                                bean.setPraise_item_id(body.getData().get(0).getId());
                                loveList.add(bean);
                            }
                            item.setPraisenews(loveList);
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
        public void onItemComment(NewsFriendsBean item, LinearLayout commentLayout) {
            itemData = item;
            commentReplyLayout = commentLayout;
            inputEditText.setText("");
            showInputDialog(false, "");
        }

        @Override
        public void onReplyComment(NewsFriendsBean item, NewsCommentBean itemCommentBean, LinearLayout commentLayout) {
            itemData = item;
            commentBean = itemCommentBean;
            commentReplyLayout = commentLayout;
            inputEditText.setText("");
            showInputDialog(true, itemCommentBean.getNickname());
        }

        @Override
        public void onItemCollect(NewsFriendsBean item) {
            CollectNewsParam xml = new CollectNewsParam();
            xml.setFunction("lovenews");
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setNewsid(item.getNews_id());
            Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().loveNews(new Gson().toJson(xml));
            result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<PublishItemResultBean>> body = response.body();
                        if (null != getActivity() && 0 == body.getResult()) {
                            Application.showToast("提交成功");
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Error onItemCollect.");
                }
            });
        }
    };

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
                final String inputText = inputEditText.getText().toString();
                if (inputText != null && inputText.length() > 0) {
                    NewsCommentParam xml = new NewsCommentParam();
                    xml.setFunction("submitnewscomment");
                    if (isReply) {
                        xml.setUserid(UserRecord.getInstance().getUserId());
                        xml.setNewsid(itemData.getNews_id());
                        xml.setTheme(itemData.getTheme());
                        xml.setContexts(inputText);
                        xml.setCommentfor(commentBean.getUser_id());
                    } else {
                        xml.setUserid(UserRecord.getInstance().getUserId());
                        xml.setNewsid(itemData.getNews_id());
                        xml.setTheme(itemData.getTheme());
                        xml.setContexts(inputText);
                    }
                    Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().commentNews(new Gson().toJson(xml));
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

    private void addCommentItemBean(String inputText) {
        NewsCommentBean itemCommentBean = new NewsCommentBean();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String displayTime = sdf.format(calendar.getTime());
        if (isReply) {
            itemCommentBean.setUser_id(UserRecord.getInstance().getUserId());
            itemCommentBean.setNickname(UserRecord.getInstance().getNickName());
            itemCommentBean.setNews_comment_id(itemData.getNews_id());
            itemCommentBean.setContexts(inputText);
            itemCommentBean.setCreatedat(displayTime);
            itemCommentBean.setCommentfor(commentBean.getUser_id());
            itemCommentBean.setCommentfor_name(commentBean.getNickname());
        }  else {
            itemCommentBean.setUser_id(UserRecord.getInstance().getUserId());
            itemCommentBean.setNickname(UserRecord.getInstance().getNickName());
            itemCommentBean.setNews_comment_id(itemData.getNews_id());
            itemCommentBean.setContexts(inputText);
            itemCommentBean.setCreatedat(displayTime);
        }
        for (NewsFriendsBean item : itemList) {
            if (item.getNews_id().equals(itemCommentBean.getNews_comment_id())) {
                item.getNewscomment().add(itemCommentBean);
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
                    inputText
//                            + "<font color=\"#8194AA\">"
//                            + "(" + displayTime + ")" + "</font>"
            ));
        } else {
            commenterTextView.setText(Html.fromHtml("<font color=\"#353535\">" + UserRecord.getInstance().getNickName() + ":" + "</font>" +
                    inputText
//                            + "<font color=\"#8194AA\">"
//                    + "(" + displayTime + ")" + "</font>"
            ));
        }
        View parent = (View) commentReplyLayout.getParent();
        if (View.GONE == parent.getVisibility()) {
            parent.setVisibility(View.VISIBLE);
            commentReplyLayout.removeAllViews();
        }
        commentReplyLayout.addView(commentView);
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

    private void initFragment() {
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }


    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mUid;
    }


    protected NewsFriendsList parseComAvoList(List<?> listavos ) throws Exception {
        ItemList d;
        itemList = (List<NewsFriendsBean>) listavos;
        NewsFriendsList newsList = new NewsFriendsList();
        List<NewsFriendsBean> listItem = new ArrayList<NewsFriendsBean>();
        for (NewsFriendsBean object : itemList) {
            listItem.add(object);
        }
        newsList.setItemList(listItem);
        return newsList;
    }

    @Override
    protected void sendRequestData() {
        int e=  mCurrentPage ;
        int index=mCurrentPage*20;
        getData();
    }


    @Override
    public void onItemClick(View view, int position) {
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
}
