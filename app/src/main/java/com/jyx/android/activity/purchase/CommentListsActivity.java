package com.jyx.android.activity.purchase;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.RecycleBaseAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.ItemCommentDetailBean;
import com.jyx.android.model.ItemCommentParam;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.net.ApiManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yiyi on 2015/11/6.
 */
public class CommentListsActivity extends BaseActivity {

    private String TAG = "CommentListsActivity";
    @Bind(R.id.et_commentlists_input)
    EditText inputEditText;
    private ItemBean itemBean = null;
    @Override
    protected int getActionBarTitle() {
        return R.string.toolbar_title_commentlists;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commentlists;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getBundleData();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionRightText("");
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            itemBean = bundle.getParcelable("item");
        }
    }

    public String getItemId() {
        return itemBean.getItem_id();
    }

    @OnClick(R.id.btn_commentlists_publish)
    void clickPublish() {
        final String inputText = inputEditText.getText().toString().trim();
        if (inputText != null && inputText.length() > 0) {
            ItemCommentParam xml = new ItemCommentParam();
            xml.setFunction("submititemcomment");
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setItemid(itemBean.getItem_id());
            xml.setTheme(itemBean.getName());
            xml.setContexts(inputText);
            Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().commentItem(new Gson().toJson(xml));
            result.enqueue(new Callback<BaseEntry<List<PublishItemResultBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<PublishItemResultBean>>> response) {
                    if (null != response && response.isSuccess()) {
                        BaseEntry<List<PublishItemResultBean>> body = response.body();
                        if (0 == body.getResult()) {
                            Application.showToast("提交成功");
                            ComentListFragment fragment = (ComentListFragment) getSupportFragmentManager().findFragmentByTag("ComentListFragment");
                            if (null != fragment) {
                                Date date = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
                                String defaultStartDate = sdf.format(calendar.getTime());

                                RecycleBaseAdapter adapter = fragment.getListAdapter();
                                ItemCommentDetailBean itemCommentDetailBean = new ItemCommentDetailBean();
                                itemCommentDetailBean.setNickname(UserRecord.getInstance().getNickName());
                                itemCommentDetailBean.setPortraituri(UserRecord.getInstance().getUserEntity().getPortraitUri());
                                itemCommentDetailBean.setContexts(inputText);
                                itemCommentDetailBean.setCreatedat(defaultStartDate);
                                adapter.addItem(adapter.getData().size(), itemCommentDetailBean);
                                fragment.getListAdapter().notifyDataSetChanged();
                                inputEditText.setText("");
                                closeInputMethod();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Error onItemSupport.");
                }
            });
        }
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
}
