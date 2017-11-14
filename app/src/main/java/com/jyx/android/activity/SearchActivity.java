package com.jyx.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.adapter.purchase.SearchLisetAdapter;
import com.jyx.android.base.UserRecord;
import com.jyx.android.fragment.chat.AbsBaseRentalFragment;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.DicDataParam;
import com.jyx.android.model.GetProListParam;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.SysDataItem;
import com.jyx.android.model.SystemDicData;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 搜索界面
 * Created by Dell on 2016/4/16.
 */
public class SearchActivity extends Activity {
    public static String GOUPRENTAL="GroupRentalFragment";//消息群租借
    public static String RENTAL="RentalFragment";//消息租借
    public static String RENTALGROUP="RentalGroupFragment";//陌生群租借
    public static String RENTALDISCOVERY="RentalDiscoveryFragment";//陌生租借
    private SearchLisetAdapter serachAdaoter;
    private List<ItemBean> itemList = null;
    private ArrayList<ItemBean> tmpList;
    private List<SysDataItem> mDate;
    private String keyWord;
    private EditText et_item_search;
    private ListView lv_item_search;
    private TextView tb_search;
    private TextView tv_search_name;
    private int mCurrentPage;
    private  String type;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_search_bar);
        init();
    }

    protected void init() {
        ArrayList<ItemBean> tmpList =new ArrayList<ItemBean>();
        intent = new Intent();
        RelativeLayout rl_title= (RelativeLayout) findViewById(R.id.toolbar);
        TextView tv_back= (TextView) rl_title.findViewById(R.id.tv_back1);
        TextView tv_title=(TextView) rl_title.findViewById(R.id.tv_title);
        tv_title.setText(R.string.toolbar_title_me_search);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_item_search= (ListView) findViewById(R.id.lv_item_search);
        et_item_search= (EditText) findViewById(R.id.et_item_search);
        tv_search_name= (TextView) findViewById(R.id.tv_search_name);
        tb_search= (TextView) findViewById(R.id.bt_search);
        Intent intent1=getIntent();
        type = intent1.getStringExtra("type");
        mCurrentPage=intent1.getIntExtra("page", 1);
        itemList=intent1.getParcelableArrayListExtra("item");
        dictdata();
        addText();
        tv_search_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord = et_item_search.getText().toString().trim();
                search(type, keyWord);
            }
        });
        tb_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord = et_item_search.getText().toString().trim();
                search(type, keyWord);
            }
        });
        lv_item_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                search(type, mDate.get(position).getName());
            }
        });
    }

    private void addText() {
        et_item_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyWor = s.toString();
                if (null != keyWor && keyWor.trim().length() > 0) {
                    tv_search_name.setText(keyWor);
                    matching(keyWor);
                } else
                    tv_search_name.setText(keyWor);
            }
        });
    }

    /**
     * 遍历所有分类
     * @param keyWor
     */
    private void matching(String keyWor) {
        List<SysDataItem> _date = new ArrayList<SysDataItem>();
        if (mDate==null)
            dictdata();
        for (SysDataItem item:mDate){
            //判断是否有此字段
            if (item.getName().indexOf(keyWor) != -1){
                _date.add(item);
            }
        }
        serachAdaoter.UpDate(_date);
    }

    /**
     * 获取分类列表
     */
    private void dictdata() {
        DicDataParam params = new DicDataParam();
        params.setFunction("getdictdata");
        ApiManager.getApi()
                .getDicDatta(new Gson().toJson(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseEntry<SystemDicData>, SystemDicData>() {
                    @Override
                    public SystemDicData call(BaseEntry<SystemDicData> baseData) {
                        Log.e("zfang", "call");
                        if (baseData == null) {
                            throw new BizException(-1, getString(R.string.tip_load_data_error));
                        }

                        if (baseData.getResult() != 0) {
                            throw new BizException(baseData.getResult(), baseData.getMsg());
                        }
//                        DoComHandleTask(baseData.getData().getClassification(), false);
                        return baseData.getData();
                    }
                })
                .subscribe(new Subscriber<SystemDicData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(SystemDicData data) {
                        mDate = data.getClassification();
                        if (serachAdaoter == null) {
                            serachAdaoter = new SearchLisetAdapter(mDate, getBaseContext());
                        }
                        lv_item_search.setAdapter(serachAdaoter);
                    }
                });

    }

    /**
     * 点击搜索查询
     * @param type
     */
    private void search(final String type,String keyWord) {
                String param = null;
                if (null != keyWord && keyWord.trim().length() > 0) {

                    if (type.equals("1")) {
                        param = "{\"function\":\"getItemlist\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                                + "\",\"pagenumber\":\"" + mCurrentPage + "\",\"searchkey\":\"" + keyWord + "\"" +
                                ",\"querytype\":\"all\"}";
                        intent.putExtra("title", RENTALDISCOVERY);
                        SearchTiem(param);
                    }
                    else if (type.equals("3") || type.equals("4")) {
                        for (ItemBean item : itemList) {
                            if (item.getDiscribe().contains(keyWord)) {
                                tmpList.add(item);
                            }
                        }
                        if (type.equals("3")) {
                            intent.putExtra("title", GOUPRENTAL);
                            dismss();
                        }
                        else {
                            intent.putExtra("title", RENTAL);
                            dismss();
                        }
                    } else {
                        param = "{\"function\":\"getItemlist\",\"userid\":\"" + UserRecord.getInstance().getUserId()
                                + "\",\"pagenumber\":\"" + mCurrentPage + "\",\"searchkey\":\"" + keyWord + "\"" +
                                ",\"querytype\":\"group\"}";
                        intent.putExtra("title",RENTALGROUP);
                        SearchTiem(param);
                    }

                }
    }
    private void SearchTiem(String param){
        GetProListParam xml = new GetProListParam();
            tmpList = new ArrayList<ItemBean>();
            xml.setUserid(UserRecord.getInstance().getUserId());
            xml.setPagenumber(1 + "");
            xml.setSearchkey(keyWord);
            Call<BaseEntry<List<ItemBean>>> result = ApiManager.getApi().getItemList(param);
            result.enqueue(new Callback<BaseEntry<List<ItemBean>>>() {
                @Override
                public void onResponse(Response<BaseEntry<List<ItemBean>>> response) {
                    if (response.isSuccess()) {
                        BaseEntry<List<ItemBean>> body = response.body();
                        Log.e("aa",body.getResult()+"");
                        Log.e("aa",body.getMsg()+"");
                        if (0 == body.getResult()) {
                            List<ItemBean> item = body.getData();
                            tmpList.addAll(item);
                            if (tmpList.size() == 0) {
                                Toast.makeText(getApplication(), "没有相关产品", Toast.LENGTH_SHORT).show();
                            }else
                                dismss();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
//                                DoComHandleTask(null, false);
                    Toast.makeText(getApplication(), "没有相关产品", Toast.LENGTH_SHORT).show();
                }
            });
        }
    private void dismss(){
        intent.setAction("android.intent.action.test");
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("item", tmpList);
        bundle.putBoolean("sear", true);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        finish();
    }
}
