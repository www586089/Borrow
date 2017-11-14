package com.jyx.android.activity.publish;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.chat.ImagePagerActivity;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.DicDataParam;
import com.jyx.android.model.ItemBean;
import com.jyx.android.model.PicDataInfo;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.model.PublishProParam;
import com.jyx.android.model.SysDataItem;
import com.jyx.android.model.SystemDicData;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;
import com.jyx.android.utils.Bimp;
import com.jyx.android.utils.FileUtils;
import com.jyx.android.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by gaobo on 2015/11/1.
 */

public class PublicItemActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    private String TAG = "PublishItemActivity";
    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private static final int SELECTIMG_SEARCH = 3;
    private static final int SELECT_Quality = 4;
    private static final int SELECT_featureName = 5;//成色
    private static final int PIC_MAX_COUNT = 9;
    private SystemDicData mSystemDicData = null;
    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    private GridAdapter adapter;
    PublicItemTask mParserTask;
    private float dp;
    private String path = "";
    private Uri photoUri;
    int operaType = 1;//租借类型
    private String operaTypeStr = "租借";
    //  租 1 租借 2 转让 3 赠送 备注如果是租，那么就有租金，单位
    private int rentType = 1;
    //用来存放图片地址
    private List<String> urlImags = new ArrayList<>();

    @Bind(R.id.scrollview)
    ScrollView scrollview;
    //    @Bind(R.id.tv_item_name)
//    EditText tv_item_name;
    @Bind(R.id.selectimg_horizontalScrollView)
    HorizontalScrollView selectimg_horizontalScrollView;
    @Bind(R.id.tv_item_discrib)
    EditText tv_item_discrib;
    @Bind(R.id.grid_image)
    GridView gridview;

    //取物方式
    @Bind(R.id.ll_item_area)
    LinearLayout ll_item_area;
    @Bind(R.id.tv_item_area)
    TextView tv_item_area;
    private String fetchObjAreaCode;
    //宝贝成色，点击弹出窗体
    @Bind(R.id.rl_item_featureName)
    RelativeLayout rl_item_featureName;

    //宝贝成色字体
    @Bind(R.id.tv_item_featureName)
    TextView tv_item_featureName;
    private String featureCode;
    @Bind(R.id.et_item_stocknumber)
    EditText et_item_stocknumber;
    //类别显示
    @Bind(R.id.tv_item_type_name)
    TextView tv_item_type_name;
    private String itemTypeCode = null;
    @Bind(R.id.ll_item_fetch_way)
    LinearLayout ll_item_fetch_way;
    @Bind(R.id.tv_item_fetch_way)
    TextView tv_item_fetch_way;
    String itemFetchTypeCoe = null;

    //原价
    @Bind(R.id.et_item_price)
    EditText et_item_price;
    @Bind(R.id.rb_rent)
    RadioButton rb_rent;
    @Bind(R.id.rd_transfer)
    RadioButton rd_transfer;
    @Bind(R.id.rg_rent_select)
    RadioGroup rg_rent_select;
    @Bind(R.id.rb_handset)
    RadioButton rb_handset;
    @Bind(R.id.et_item_deposit)
    EditText et_item_deposit;
    @Bind(R.id.et_item_rentmoney)
    EditText et_item_rentmoney;
    @Bind(R.id.et_preferential)
    EditText et_preferential;
    @Bind(R.id.ll_contain_transfer)
    LinearLayout ll_contain_transfer;
    @Bind(R.id.ll_contain_preferential)
    LinearLayout ll_contain_preferential;
    @Bind(R.id.ll_contain_handsel)
    LinearLayout ll_contain_handsel;
    @Bind(R.id.publish_item_present)
    TextView publish_item_present;




    private ArrayList<PicDataInfo> picDataInfoList = new ArrayList<PicDataInfo>();
    private ParsePicTask parsePicTask = null;
    private int flag = 0;
    private List<SysDataItem> listRentType = new ArrayList<SysDataItem>();
    private String rentTypeItem[];
    private int currentSelectRentType = -1;
    private AlertDialog dialog = null;
    private String imagejson;
    private ItemBean item;
    private int type;
    private boolean ImgEnd=false;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_item;
    }

    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }

    @OnClick(R.id.rl_item_featureName)
    void rlClick() {
        ActivityHelper.goCowryQuality(PublicItemActivity.this, 5);
    }

    @OnClick(R.id.rl_item_type)
    void onClickItemType() {
        ActivityHelper.goSelectItemType(PublicItemActivity.this, 6);
    }

    @OnClick(R.id.ll_item_fetch_way)
//取物方式
    void onClickItemWay() {
        ActivityHelper.goSelectItemWay(PublicItemActivity.this, 7);
    }

    @OnClick(R.id.ll_item_area)
//物品所在地
    void onClickAreaSelect() {
        ActivityHelper.goSelectItemArea(PublicItemActivity.this, 8);
    }

    private class ParsePicTask extends AsyncTask<ArrayList<PicDataInfo>, Void, Boolean> {
        public ParsePicTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                gridviewInit();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @TargetApi(12)
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
        }

        @Override
        protected Boolean doInBackground(ArrayList<PicDataInfo>... params) {
            PhotoActivity.bitmap.clear();
            bmp.clear();
                    for (PicDataInfo item : picDataInfoList) {
                        Bitmap bitmap = getThumbnails(item);
                        PhotoActivity.bitmap.add(bitmap);
                        bmp.add(bitmap);
                    }

            return true;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public Bitmap getThumbnails(PicDataInfo fileInfo) {
        long originId = fileInfo.getMediaId();
        ContentResolver cr = getContentResolver();
        int kind = MediaStore.Images.Thumbnails.MICRO_KIND;
        return MediaStore.Images.Thumbnails.getThumbnail(cr, originId, kind, null);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setActionBarTitle("产品发布");
        setActionRightText("");
        dp = getResources().getDimension(R.dimen.dp);

        getBundleData();

        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        parsePicTask = new ParsePicTask();
        parsePicTask.execute();
        //给RadioGroup设置事件监听
        rg_rent_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == rb_rent.getId()) {//租借
                    ll_contain_transfer.setVisibility(View.VISIBLE);
                    ll_contain_preferential.setVisibility(View.GONE);
                    ll_contain_handsel.setVisibility(View.GONE);
                    operaType = 1;
                    operaTypeStr = "租借";
                } else if (checkedId == rd_transfer.getId()) {//转让
                    ll_contain_transfer.setVisibility(View.GONE);
                    ll_contain_preferential.setVisibility(View.VISIBLE);
                    ll_contain_handsel.setVisibility(View.GONE);
                    operaType = 2;
                    operaTypeStr = "转让";
                } else {//赠送
                    operaType = 3;
                    operaTypeStr = "赠送";
                    ll_contain_transfer.setVisibility(View.GONE);
                    ll_contain_preferential.setVisibility(View.GONE);
                    ll_contain_handsel.setVisibility(View.VISIBLE);
                }
            }
        });
        new updataImage().execute();
    }

    private class updataImage extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            final List<String> urlList = new ArrayList<String>();//图片的绝对路径
            for (int i = 0; i < picDataInfoList.size(); i++) {
                urlList.add(picDataInfoList.get(i).getDataPath());
            }
            Map<String, Object> jm = null;
            String xmlString = "";
            for (String filePath : urlList) {
                jm = new HashMap<>();
                jm.put("projectid", "item");
                jm.put("userid", UserRecord.getInstance().getUserId());
                xmlString = new Gson().toJson(jm);
                RequestBody xml = RequestBody.create(MediaType.parse("text/plain"), xmlString);
                Map<String, RequestBody> map = new HashMap<>();
                map.put("xml", xml);
                File imgFile = new File(filePath);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
                map.put("image\"; filename=\""+imgFile.getName()+"", fileBody);
                Call<BaseEntry<List<String>>> result = ApiManager.getApi().uploadPic(map);
                Response<BaseEntry<List<String>>> response = null;
                try {
                    response = result.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null != response && response.isSuccess()) {
                    BaseEntry<List<String>> body = response.body();
                    if (0 == body.getResult()) {
                        urlImags.add(body.getData().get(0));
                        Log.e(TAG, "publishItem, msg = " + body.getMsg());
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            ImgEnd=true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (null != bundle) {
            this.picDataInfoList = bundle.getParcelableArrayList("selectedItem");
            this.flag = bundle.getInt("flag");
            parsePicTask.cancel(true);
            parsePicTask = new ParsePicTask();
            parsePicTask.execute();
        }
    }

    private void getBundleData() {
        getDicData();
        if (-1 == currentSelectRentType) {
            currentSelectRentType = 0;
        }
        Bundle bundle = this.getIntent().getExtras();

        if (null != bundle) {
//            type= bundle.getInt("type");
//           if (bundle.getInt("type")==1){
//                this.picDataInfoList = bundle.getParcelableArrayList("selectedItem");
//                this.flag = bundle.getInt("flag");
//                item=bundle.getParcelable("item");
//                imagejson=item.getImagejson();
//                type=1;
//                set(item);
//            }else
//           {
               this.picDataInfoList = bundle.getParcelableArrayList("selectedItem");
               this.flag = bundle.getInt("flag");
               type=2;
        }
    }


    /**
     * 初始化 表格
     */
    public void gridviewInit() {
        adapter = new GridAdapter(this);
        adapter.setSelectedPosition(0);
        int size = 0;
        if (bmp.size() < PIC_MAX_COUNT) {
            size = bmp.size() + 1;
        } else {
            size = bmp.size();
        }
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        final int width = size * (int) (dp * 9.4f);
        params.width = width;
        gridview.setLayoutParams(params);
        gridview.setColumnWidth((int) (dp * 9.4f));
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);

        selectimg_horizontalScrollView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {// 绘制完毕
            public boolean onPreDraw() {
                selectimg_horizontalScrollView.scrollTo(width, 0);
                selectimg_horizontalScrollView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }


    public void set(ItemBean item){
        tv_item_discrib.setText(item.getDiscribe());
        tv_item_area.setText(item.getAddress());
        tv_item_featureName.setText(item.getFeature_name());
        et_item_stocknumber.setText(item.getStocknumber());
        tv_item_fetch_way.setText(item.getGettype_name());
        tv_item_type_name.setText(item.getType_name());
        et_item_price.setText(String.valueOf(item.getPrice()/100));
        String opname= item.getOperatype_name();
        if (opname.equals("租借")){
            rb_rent.setChecked(true);
            ll_contain_transfer.setVisibility(View.VISIBLE);
            ll_contain_preferential.setVisibility(View.GONE);
            ll_contain_handsel.setVisibility(View.GONE);
            operaType = 1;
            operaTypeStr = "租借";
            et_item_deposit.setText(String.valueOf(item.getDeposit() / 100));
            et_item_rentmoney.setText(String.valueOf(item.getRent()/100));
        }else if (opname.equals("转让")){
            rd_transfer.setChecked(true);
            ll_contain_transfer.setVisibility(View.GONE);
            ll_contain_preferential.setVisibility(View.VISIBLE);
            ll_contain_handsel.setVisibility(View.GONE);
            operaType = 2;
            operaTypeStr = "转让";
            et_preferential.setText(String.valueOf(item.getDiscountprice() / 100));
        }else if (opname.equals("赠送")){
            rb_handset.setChecked(true);
            operaType = 3;
            operaTypeStr = "赠送";
            ll_contain_transfer.setVisibility(View.GONE);
            ll_contain_preferential.setVisibility(View.GONE);
            ll_contain_handsel.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 拍照程序，保存的相关路径下
     */
    public void photo() {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //外部存储的路径
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = android.os.Environment
                    .getExternalStorageDirectory().getPath() + Constants.CACHE_DIR_pho + "/photo/";
            //String sdcardPathDir = FileUtils.SDPATH1;
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                // 有sd卡，是否有myImage文件夹
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                // 是否有headImg文件
                file = new File(sdcardPathDir + System.currentTimeMillis()
                        + ".jpg");
            }
            if (file != null) {
                path = file.getPath();
                photoUri = Uri.fromFile(file);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 表格里面的图片，点击后，浏览照片
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PublicItemActivity.this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        if (position == bmp.size()) {
            /*String sdcardState = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                new PopupWindows(PublicItemActivity.this, gridview);
            } else {
                Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
            }*/
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("lastSelected", this.picDataInfoList);
            bundle.putInt("flag", flag);
            ActivityHelper.goSelectPicExt(this, bundle);
        } else {
            /*Intent intent = new Intent(PublicItemActivity.this,PhotoActivity.class);
            intent.putExtra("ID", position);
            startActivity(intent);*/


            List<String> urlsList = new ArrayList<String>();
            for (PicDataInfo data : picDataInfoList) {
                urlsList.add("file://" + data.getDataPath());
            }
            String[] urls = new String[urlsList.size()];
            urlsList.toArray(urls);
            Intent intent = new Intent(this, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            startActivity(intent);
        }
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public class ViewHolder {
            public ImageView image;
            public Button bt;
        }

        public GridAdapter(Context context) {
            listContainer = LayoutInflater.from(context);
        }

        public int getCount() {
            if (bmp.size() < PIC_MAX_COUNT) {
                return bmp.size() + 1;
            } else {
                return bmp.size();
            }
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
            // 自定义视图
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = listContainer.inflate(R.layout.list_cell_publish_item_grid, null);
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.bt = (Button) convertView.findViewById(R.id.item_grida_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_addpic_unfocused));
                holder.bt.setVisibility(View.GONE);
                if (position == PIC_MAX_COUNT) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(bmp.get(position));
                holder.bt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        PhotoActivity.bitmap.remove(sign);
                        bmp.get(sign).recycle();
                        bmp.remove(sign);
                        picDataInfoList.remove(sign);
                        //drr.remove(sign);
                        //gridviewInit();
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            return convertView;
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.activity_publish_item_select_pupup, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_close);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(
                            // 相册
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Application.showToast("接受到回调：" + requestCode + "_" + resultCode);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (drr.size() < PIC_MAX_COUNT && resultCode == -1) {// 拍照
                    startPhotoZoom(photoUri);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (drr.size() < PIC_MAX_COUNT && resultCode == RESULT_OK && null != data) {// 相册返回
                    Uri uri = data.getData();
                    if (uri != null) {
                        startPhotoZoom(uri);
                    }
                }
                break;
            case CUT_PHOTO_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {// 裁剪返回
                    Bitmap bitmap = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
                    PhotoActivity.bitmap.add(bitmap);
                    bitmap = Bimp.createFramedPhoto(480, 480, bitmap, (int) (dp * 1.6f));
                    bmp.add(bitmap);
                    gridviewInit();
                }
                break;
            case SELECT_featureName:
                if (Activity.RESULT_OK == resultCode) {
                    Bundle bundle = data.getExtras();
                    featureCode = bundle.getString("code");
                    tv_item_featureName.setText(bundle.getString("name"));
                }
                break;

            case 6:
                if (Activity.RESULT_OK == resultCode) {
                    Bundle bundle = data.getExtras();
                    itemTypeCode = bundle.getString("code");
                    tv_item_type_name.setText(bundle.getString("name"));
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }
                break;

            case 7:
                if (Activity.RESULT_OK == resultCode) {
                    Bundle bundle = data.getExtras();
                    itemFetchTypeCoe = bundle.getString("code");
                    tv_item_fetch_way.setText(bundle.getString("name"));
                }
                break;

            case 8:
                if (Activity.RESULT_OK == resultCode) {
                    Bundle bundle = data.getExtras();
                    fetchObjAreaCode = bundle.getString("code");
                    tv_item_area.setText(bundle.getString("address"));
                }
                break;
            default:
                Log.e(TAG, "unknow request code.");
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            /****判断目录是否已存在****/
            String sdcardPathDir = android.os.Environment
                    .getExternalStorageDirectory().getPath() + Constants.CACHE_DIR_pho + "/photo/";
            //String sdcardPathDir = FileUtils.SDPATH1;
            File file = null;
            // 有sd卡，是否有myImage文件夹
            File fileDir = new File(sdcardPathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            /****判断目录是否已存在end****/

            // 获取系统时间 然后将裁剪后的图片保存至指定的文件夹
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyyMMddhhmmss");
            String address = sDateFormat.format(new java.util.Date());
            if (!FileUtils.isFileExist("")) {
                FileUtils.createSDDir("");

            }
            drr.add(FileUtils.SDPATH + address + ".jpg");

            //Uri imageUri = Uri.parse("file:///sdcard/formats/" + address
            //		+ ".jpg");
            //这里需要和FileUtils.SDPATH 一致，而且在之前要创建文件夹
            Uri imageUri = Uri.parse("file:///sdcard/" + Constants.CACHE_DIR_pho + "/photo/thumb" + address
                    + ".jpg");
//            System.out.println("uri===="+ FileUtils.SDPATH + address + ".jpg");
            final Intent intent = new Intent("com.android.camera.action.CROP");

            // 照片URL地址
            intent.setDataAndType(uri, "image/*");

            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 480);
            intent.putExtra("outputY", 480);
            // 输出路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 输出格式
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            // 不启用人脸识别
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onDestroy() {

        FileUtils.deleteDir(FileUtils.SDPATH);
        FileUtils.deleteDir(FileUtils.SDPATH1);
        // 清理图片缓存
        if (null != bmp && bmp.size() > 0) {
            for (int i = 0; i < bmp.size(); i++) {
                bmp.get(i).recycle();
            }
        }

        for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
            PhotoActivity.bitmap.get(i).recycle();
        }
        PhotoActivity.bitmap.clear();
        bmp.clear();
        drr.clear();
        cancelParserTask();
        super.onDestroy();
    }


    //这个就只能用线程发布了，无法

    /**
     * 保存成功后，调用此方法
     */
    public void executeOnLoadDataSuccess() {
        //关闭本页面，跳转到分享页面
//        Application.showToast("存入成功");
        ActivityHelper.goPublishSuccess(this);
        finish();
    }

    @OnClick(R.id.btn_publish_item)
    void clickSignIn() {
        publishitem();
        //发布成功后，需要跳转到分享界面
        //uploadFile(this.picDataInfoList.get(0).getDataPath());
        //uploadFileTest(this.picDataInfoList.get(0).getDataPath());
    }

    private void getDicData() {
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
                        return baseData.getData();
                    }
                })
                .subscribe(new Subscriber<SystemDicData>() {
                    @Override
                    public void onCompleted() {
                        Log.e("zfang", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zfang", "onError");
                        CommonExceptionHandler.handleBizException(e);
                    }

                    @Override
                    public void onNext(SystemDicData data) {
                        mSystemDicData = data;
                        listRentType = data.getRenttype();
                        if (null != listRentType && listRentType.size() > 0) {
                            rentTypeItem = new String[listRentType.size()];
                            for (int i = 0; i < listRentType.size(); i++) {
                                rentTypeItem[i] = listRentType.get(i).getName();
                            }
                        }
                    }
                });
    }

    private boolean checkPublish() {
        String tmpStr = null;
//        tmpStr = tv_item_name.getText().toString();
//        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
//            Application.showToast(R.string.publish_item_name_notice);
//            return false;
//        }
        tmpStr = tv_item_discrib.getText().toString();
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_describe_notice);
            return false;
        }
        tmpStr = tv_item_area.getText().toString();
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_area);
            return false;
        }

        tmpStr = featureCode;
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_feature);
            return false;
        }

        tmpStr = et_item_stocknumber.getText().toString();
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_stockNumber);
            return false;
        }

        tmpStr = itemFetchTypeCoe;
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_fetch_type);
            return false;
        }

        tmpStr = itemTypeCode;
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_type);
            return false;
        }

        tmpStr = et_item_price.getText().toString();
        if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
            Application.showToast(R.string.publish_item_price_notice);
            return false;
        }

        if (1 == operaType) {
            tmpStr = et_item_deposit.getText().toString();
            if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
                Application.showToast(R.string.publish_item_deposit_notice);
                return false;
            }

            tmpStr = et_item_rentmoney.getText().toString();
            if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
                Application.showToast(R.string.publish_item_rent_notice);
                return false;
            }
        } else if (2 == operaType) {
            tmpStr = et_preferential.getText().toString();
            if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
                Application.showToast(R.string.publish_item_rent_discount);
                return false;
            }
        } else if (3 == operaType) {
            tmpStr = publish_item_present.getText().toString();
            if ((null == tmpStr) || (tmpStr != null && tmpStr.trim().length() == 0)) {
                Application.showToast(R.string.publish_item_rent_present);
                return false;
            }
        }

        return true;
    }

    /**
     * 发布产品
     */
    public void publishitem() {
        if (!checkPublish()) {
            return;
        }

        PublishProParam xml = new PublishProParam();
        if (type==1){
            xml.setFunction("edititem");
        }
        xml.setFunction("publishitem");
        xml.setName("");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setFeatureid(featureCode);
        xml.setFeaturename(tv_item_featureName.getText().toString().trim());
        xml.setDiscribe(tv_item_discrib.getText().toString());
        xml.setPrice(StringUtils.toMoneyFen(et_item_price.getText().toString()) + "");//原价
        if (1 == operaType) {
            xml.setDeposit(StringUtils.toMoneyFen(et_item_deposit.getText().toString()) + "");//押金
            xml.setRent(StringUtils.toMoneyFen(et_item_rentmoney.getText().toString()) + "");//租金
        } else if (2 == operaType) {
            xml.setDiscountPrice(StringUtils.toMoneyFen(et_preferential.getText().toString()) + "");
        } else if (3 == operaType) {
            xml.setRemarks(publish_item_present.getText().toString());
        }

        xml.setTypeid(itemTypeCode);
        xml.setTypename(tv_item_type_name.getText().toString().trim());
        xml.setRenttypeid(listRentType.get(currentSelectRentType).getType_id() + "");
        xml.setRenttypename(listRentType.get(currentSelectRentType).getName());
        xml.setAddress(tv_item_area.getText().toString());//物品所在地
        xml.setOperatype(operaType + "");
        xml.setOperatypename(operaTypeStr);
        xml.setGettypeid(itemFetchTypeCoe);//取物方式
        xml.setGettypename(tv_item_fetch_way.getText().toString());
        xml.setStockNumber(et_item_stocknumber.getText().toString());
        mParserTask = new PublicItemTask(this, xml);
        mParserTask.execute();
    }

    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    private String getFileName(String filePath) {
        String fileName = "";
        if (null != filePath && filePath.trim().length() > 0) {
            fileName = filePath.substring(filePath.lastIndexOf("/"));
        }
        return fileName;
    }

    //采用异步任务，进行产品发布
    private class PublicItemTask extends AsyncTask<Void, Void, String> {
        private WeakReference<PublicItemActivity> mInstance;
        private PublishProParam xml = null;
        boolean parserError;
        private SweetAlertDialog pDialogQuery = null;

        //产品对象
        public PublicItemTask(PublicItemActivity instance, PublishProParam xml) {
            this.mInstance = new WeakReference<>(instance);
            this.xml = xml;
            parserError = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogQuery = new SweetAlertDialog(PublicItemActivity.this, SweetAlertDialog.PROGRESS_TYPE).setTitleText("正在发布，请稍候.....");
            pDialogQuery.show();
            pDialogQuery.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            PublicItemActivity instance = mInstance.get();
            if (instance == null)
                return null;
           while (true){
               if (ImgEnd){
                       try {
                           Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                           String imageJson = gson.toJson(urlImags);
                           xml.setImagejson(imageJson);
                           Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().publishItem(gson.toJson(xml));
                           Response<BaseEntry<List<PublishItemResultBean>>> response = result.execute();
                           if (null != response && response.isSuccess()) {
                               BaseEntry<List<PublishItemResultBean>> body = response.body();
                               if (0 == body.getResult()) {
                                   Log.e(TAG, "publishItem, msg = " + body.getMsg());
                               }
                           }
                           break;
                       } catch (Exception e) {
                           e.printStackTrace();
                           parserError = true;
                           break;
                       }
               }
           }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialogQuery.dismissWithAnimation();
            PublicItemActivity instance = mInstance.get();
            if (instance != null) {
                if (parserError) {
                    //instance.readCacheData(instance.getCacheKey());
                    //  instance.executeOnLoadDataError(null);
                    //执行错误，弹出窗
                    Application.showToast("保存失败!");
                } else {
                    //执行保存成功，执行正确的方法，关闭按钮
                    instance.executeOnLoadDataSuccess();
//                    BaseApplication.showToast("保存失败!");

                }
            }
        }
    }
}