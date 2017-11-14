package com.jyx.android.fragment.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jyx.android.R;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.adapter.chat.TreeViewAdapter;
import com.jyx.android.base.BaseFragment;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.RelationEntity;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.List;

import butterknife.Bind;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 消息-关系界面
 * Author : yiyi
 * Date : 2015-11-01
 */
public class RelationFragment extends BaseFragment{

    @Bind(R.id.et_treeview_search)
    EditText metSearch;

    public TreeViewAdapter ta = null;

    private String user_id = "";
    private SweetAlertDialog mLoadingDialog;

    private void showLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView(this.getView());
    }

    private void queryUserRelation()
    {
        String xmlString = "";
        if (!user_id.equals(""))
        {
            showLoading();
            xmlString = "{\"function\":\"getmyrelation\",\"userid\":\"" + user_id + "\"}";
            ApiManager.getApi()
                    .getMyRelation(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<RelationEntity>>, List<RelationEntity>>() {
                        @Override
                        public List<RelationEntity> call(BaseEntry<List<RelationEntity>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.load_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                            }
                            return listBaseEntry.getData();
                        }
                    })
                    .subscribe(new Subscriber<List<RelationEntity>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(List<RelationEntity> relationEntities) {
                            RelationEntity re;
                            for (int i=0; i<relationEntities.size(); i++)
                            {
                                boolean attention,isgroup;

                                re = relationEntities.get(i);
                                if (re.getAttention().equals("1"))
                                    attention = true;
                                else
                                    attention = false;
                                if (re.getIsgroup().equals("1"))
                                    isgroup = true;
                                else
                                    isgroup = false;

                                ta.addATreeNode(re.getId(),re.getParentid(),re.getName(),re.getImageurl(),attention,isgroup);
                            }

                            dismissLoading();
                            ta.NotifyDataChange();
                        }
                    });
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        user_id = UserRecord.getInstance().getUserId();




        metSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    //mivDeleteText.setVisibility(View.GONE);
                } else {
                   // mivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });

        ta = new TreeViewAdapter();
        ta.addATreeNode("0", "", "关系网", "", false, true);
        /*
        ta.addATreeNode("01", "0", "test01", "", false, true);
        ta.addATreeNode("02", "0", "test02", "", false, true);
        ta.addATreeNode("010", "01", "test010", "", false, true);
        ta.addATreeNode("0100", "010", "测试测试测试测试测试测试测试测试测试测试测试测试", "", true, false);
        ta.addATreeNode("0101", "010", "测试", "", true, false);
        ta.addATreeNode("011", "01", "test011", "", false, false);
        ta.addATreeNode("020", "02", "test020", "", false, false);
        ta.addATreeNode("021", "02", "test021", "", false, false);
        ta.addATreeNode("022", "02", "test022", "", false, false);
        ta.addATreeNode("023", "02", "test023", "", false, false);
        ta.addATreeNode("024", "02", "test024", "", false, false);
        ta.addATreeNode("025", "02", "test025", "", false, false);
        ta.addATreeNode("026", "02", "test026", "", false, false);
        ta.addATreeNode("027", "02", "test027", "", false, false);
        ta.addATreeNode("028", "02", "test028", "", false, false);
        ta.addATreeNode("029", "02", "test029", "", false, false);
        ta.addATreeNode("090", "02", "test090", "", false, false);
        ta.addATreeNode("091", "02", "test091", "", false, false);
        ta.addATreeNode("092", "02", "test092", "", false, false);
        ta.addATreeNode("093", "02", "test093", "", false, false);
        ta.addATreeNode("094", "02", "test094", "", false, false);
        ta.addATreeNode("095", "02", "test095", "", false, false);
        ta.addATreeNode("096", "02", "test096", "", false, false);
        ta.addATreeNode("097", "02", "test097", "", false, false);
        ta.addATreeNode("098", "02", "test098", "", false, false);
        ta.addATreeNode("099", "02", "test099", "", false, false);*/

        final LinearLayout treeLayout = (LinearLayout)view.findViewById(R.id.lv_relation_tree);

        ta.LoadTreeView(view.getContext(), treeLayout);

    }

    @Override
    public void onResume() {
        super.onResume();
        queryUserRelation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_relation;
    }

}
