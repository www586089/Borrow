package com.jyx.android.activity.publish;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.chat.ImagePagerActivity;
import com.jyx.android.activity.chat.redenvelope.SelectFrinedActivity;
import com.jyx.android.adapter.chat.Friend;
import com.jyx.android.base.Application;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.PicDataInfo;
import com.jyx.android.model.PublishItemResultBean;
import com.jyx.android.model.PublishNewsParam;
import com.jyx.android.net.ApiManager;
import com.jyx.android.utils.Bimp;
import com.jyx.android.utils.FileUtils;

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

/**
 * Created by zfang on 2015/12/22.
 */
public class PublishDynamicActivity extends BaseActivity implements AdapterView.OnItemClickListener{


    private String TAG = PublishDynamicActivity.class.getSimpleName();
    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private static final int SELECTIMG_SEARCH = 3;
    private static final int SELECT_Quality = 4;
    private static final int SELECT_featureName = 5;
    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    private GridAdapter adapter;
    PublicItemTask  mParserTask;
    private float dp;
    private String path = "";
    private Uri photoUri;

    @Bind(R.id.selectimg_horizontalScrollView)
    HorizontalScrollView selectimg_horizontalScrollView;
    @Bind(R.id.tv_item_theme)
    EditText tv_item_theme;
    @Bind(R.id.tv_item_discrib)
    EditText tv_item_discrib;
    @Bind(R.id.grid_image)
    GridView gridview;
    @Bind(R.id.tv_remind)
    TextView tv_remind;
    @Bind(R.id.tv_public)
    TextView tv_public;
    private boolean isSelectedPhoto = false;
    private ArrayList<PicDataInfo> picDataInfoList = new ArrayList<PicDataInfo>();
    private ParsePicTask parsePicTask = null;
    private int flag = 0;
    private String seemanjson = "";
    private String remindmanjson = "";
    private List<String> urlImags=new ArrayList<>();
    private boolean ImgEnd=false;
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_dynamic;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.publish_item_title_center;
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
        setActionRightText("");
        dp = getResources().getDimension(R.dimen.dp);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        getBundleData();
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        parsePicTask = new ParsePicTask();
        parsePicTask.execute();
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
        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            this.picDataInfoList = bundle.getParcelableArrayList("selectedItem");
            this.flag = bundle.getInt("flag");
        }
    }
    @OnClick(R.id.ll_publish_authorization)
    void clickAuthorization() {
        isSelectedPhoto = false;
        new PopupWindows(PublishDynamicActivity.this, gridview);
    }

    @OnClick(R.id.btn_publish_item)
    void clickPublishItem() {
        publishitem();
    }

    @OnClick(R.id.rl_email_someone)
    void clickEmailSomeOne() {
        Intent intent = new Intent(this, SelectFrinedActivity.class);
        startActivityForResult(intent, 5);
    }
    /**
     * 表格里面的图片，点击后，浏览照片
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PublishDynamicActivity.this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        isSelectedPhoto = true;
        if (position == bmp.size()) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("lastSelected", this.picDataInfoList);
            bundle.putInt("flag", flag);
            ActivityHelper.goSelectPicExt(this,bundle);
        } else {
            List<String> urlsList = new ArrayList<String>();
            for (PicDataInfo data : picDataInfoList) {
                urlsList.add("file://" + data.getDataPath());
            }
            String[] urls = new String[urlsList.size()];
            urlsList.toArray(urls);
            Intent intent = new Intent(this, ImagePagerActivity.class);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
            startActivity(intent);
        }
    }

    /**
     * 初始化 表格
     */
    public void gridviewInit() {
        adapter = new GridAdapter(this);
        adapter.setSelectedPosition(0);
        int size = 0;
        if (bmp.size() < 6) {
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

        final ViewTreeObserver ob = selectimg_horizontalScrollView.getViewTreeObserver();
        ob.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                selectimg_horizontalScrollView.scrollTo(width, 0);
                ob.removeOnPreDrawListener(this);
                return false;
            }
        });
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
            if (bmp.size() < 6) {
                return bmp.size() + 1;
            } else {
                return bmp.size();
            }
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
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
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(bmp.get(position));
                holder.bt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        PhotoActivity.bitmap.remove(sign);
                        bmp.get(sign).recycle();
                        bmp.remove(sign);
                        gridviewInit();
                    }
                });
            }
            return convertView;
        }
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.activity_publish_item_select_pupup, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);

            if (isSelectedPhoto) {
                bt1.setText("拍照");
                bt2.setText("从相册中选择");
            } else {
                bt1.setText("公开");
                bt2.setText("私密");
            }
            Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_close);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (isSelectedPhoto) {
                        photo();
                    } else {
                        /*选择公开，取消跳转;
                        Intent intent = new Intent(PublishDynamicActivity.this, SelectFrinedActivity.class);
                        startActivityForResult(intent, 4);
                        */
                        tv_public.setText("公开");
                        seemanjson = "";
                    }

                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (isSelectedPhoto) {
                        Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    } else {
                        tv_public.setText("私密");
                        seemanjson = "";
                    }
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

    /**
     * 拍照程序，保存的相关路径下
     */
    public void photo() {
        try {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            //外部存储的路径
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = Environment
                    .getExternalStorageDirectory().getPath() + Constants.CACHE_DIR_pho+"/photo/";
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (drr.size() < 6 && resultCode == -1) {// 拍照
                    startPhotoZoom(photoUri);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (drr.size() < 6 && resultCode == RESULT_OK && null != data) {// 相册返回
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
                    bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
                            (int) (dp * 1.6f));
                    bmp.add(bitmap);
                    gridviewInit();
                }

            case 4:
                if (resultCode == Activity.RESULT_OK && null != data) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        ArrayList<Friend> listFriend = bundle.getParcelableArrayList("friends");
                        List<String> listUserIds = new ArrayList<String>();
                        List<String> nickNameList = new ArrayList<String>();
                        for(Friend friend : listFriend) {
                            listUserIds.add(friend.getUserId());
                            nickNameList.add(friend.getUserName());
                        }
                        seemanjson = new Gson().toJson(listUserIds);
                        tv_public.setText(new Gson().toJson(nickNameList));
                    }
                }
                break;
            case 5:
                if (resultCode == Activity.RESULT_OK && null != data) {
                    Bundle bundle = data.getExtras();
                    if (null != bundle) {
                        ArrayList<Friend> listFriend = bundle.getParcelableArrayList("friends");
                        List<String> listUserIds = new ArrayList<String>();
                        List<String> nickNameList = new ArrayList<String>();
                        for(Friend friend : listFriend) {
                            listUserIds.add(friend.getUserId());
                            nickNameList.add(friend.getUserName());
                        }
                        remindmanjson = new Gson().toJson(listUserIds);
                        tv_remind.setText(new Gson().toJson(nickNameList));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startPhotoZoom(Uri uri) {
        try {
            /****判断目录是否已存在****/
            String sdcardPathDir = Environment
                    .getExternalStorageDirectory().getPath() + Constants.CACHE_DIR_pho+"/photo/";
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
            Uri imageUri = Uri.parse("file:///sdcard/"+Constants.CACHE_DIR_pho+"/photo/thumb" + address
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

    /**
     * 发布产品
     */
    public void publishitem() {
        if (!checkData()) {
            return;
        }
        PublishNewsParam xml = new PublishNewsParam();
        xml.setFunction("publishnews");
        xml.setUserid(UserRecord.getInstance().getUserId());
        xml.setTheme("");
        xml.setDiscribe(tv_item_discrib.getText().toString());
        xml.setNewstypeid("1");
        xml.setSeemanjson(seemanjson);
        xml.setRemindmanjson(remindmanjson);
        mParserTask = new PublicItemTask(this, xml);
        mParserTask.execute();
    }

    private boolean checkData() {
        String strTmp = null;
        /*strTmp = tv_item_theme.getText().toString();
        if (null == strTmp || (null != strTmp && 0 == strTmp.trim().length())) {
            Application.showToast(R.string.publish_dynamic_theme_notice);
            return false;
        }*/
        strTmp = tv_item_discrib.getText().toString();
        if (null == strTmp || (null != strTmp && 0 == strTmp.trim().length())) {
            Application.showToast(R.string.publish_dynamic_description_notice);
            return false;
        }
        return true;
    }
    //这个就只能用线程发布了，无法

    /**
     * 保存成功后，调用此方法
     */
    public void executeOnLoadDataSuccess()
    {
        //关闭本页面，跳转到分享页面
//        Application.showToast("存入成功");
        ActivityHelper.goPublishSuccess(this);
        finish();
    }

    protected void onDestroy() {

        FileUtils.deleteDir(FileUtils.SDPATH);
        FileUtils.deleteDir(FileUtils.SDPATH1);
        // 清理图片缓存
        for (int i = 0; i < bmp.size(); i++) {
            bmp.get(i).recycle();
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
    private void cancelParserTask() {
        if (mParserTask != null) {
            mParserTask.cancel(true);
            mParserTask = null;
        }
    }

    //采用异步任务，进行产品发布
    private class PublicItemTask extends AsyncTask<Void, Void, String> {
        private WeakReference<PublishDynamicActivity> mInstance;
        List<String> imagList;//图片的绝对路径
        boolean parserError;
        private SweetAlertDialog pDialogQuery = null;
        private PublishNewsParam xml;
        //产品对象
        public PublicItemTask(PublishDynamicActivity instance, PublishNewsParam xml) {
            this.mInstance = new WeakReference<>(instance);
            this.xml = xml;
            parserError=false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogQuery = new SweetAlertDialog(PublishDynamicActivity.this,SweetAlertDialog.PROGRESS_TYPE) .setTitleText("正在发布，请稍候.....");
            pDialogQuery.show();
            pDialogQuery.setCancelable(false);
        }
        @Override
        protected String doInBackground(Void... params) {
            PublishDynamicActivity instance = mInstance.get();
            if (instance == null)
                return null;
            while (true){
                if (ImgEnd) {
                    try {
                        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                        String json = gson.toJson(urlImags);

                        xml.setImagejson(json);
                        Call<BaseEntry<List<PublishItemResultBean>>> result = ApiManager.getApi().publishNews(gson.toJson(xml));
                        Response<BaseEntry<List<PublishItemResultBean>>> response = result.execute();
                        if (null != response && response.isSuccess()) {
                            BaseEntry<List<PublishItemResultBean>> body = response.body();
                        } else {
                            parserError = true;
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

        private String getFileName(String path) {
            String fileName = System.currentTimeMillis() + "";
            int fileNamePartIndex = path.lastIndexOf("/");
            if (fileNamePartIndex > 0) {
                fileName = path.substring(fileNamePartIndex + 1);
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialogQuery.dismissWithAnimation();
            PublishDynamicActivity instance = mInstance.get();
            if (instance != null) {
                if (parserError) {
                    Application.showToast("保存失败!");
                } else {
                    instance.executeOnLoadDataSuccess();
                }
            }
        }
    }
}
