package com.jyx.android.activity.publish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jyx.android.R;
import com.jyx.android.activity.ActivityHelper;
import com.jyx.android.activity.sign.AutomaticLogon;
import com.jyx.android.base.BaseActivity;
import com.jyx.android.base.Constants;
import com.jyx.android.base.UserRecord;
import com.jyx.android.model.BaseEntry;
import com.jyx.android.model.PicDataInfo;
import com.jyx.android.net.ApiManager;
import com.jyx.android.net.BizException;
import com.jyx.android.rx.CommonExceptionHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * created by zfang at 2016-01-07
 */
public class PictureSelectActivity extends BaseActivity {

    private String TAG = "PictureSelectActivity";
    private GridView mPictureGrid;
    private PicSelectGridAdapter gridAdapter = null;
    private GetPicTask getPicAsyncTask = null;
    private ArrayList<PicDataInfo> selectedPicInfoList = new ArrayList<PicDataInfo>();
    private ArrayList<PicDataInfo> selectedPicPreList = new ArrayList<PicDataInfo>();
    private int flag = 0;
    private String path = "";
    private Uri photoUri;
    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    private String user_id="";
    private File imgFile;
    private String imgFileName = "";
    private SweetAlertDialog mLoadingDialog;
    private Uri imageUri;
    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_picture_select;
    }

    private PicSelectGridAdapter.OnCheckClistener checkClistener = new PicSelectGridAdapter.OnCheckClistener() {
        @Override
        public void onCheckClick(CheckBox checkBox, int position, PicDataInfo dataInfo) {
            if (0 == position) {
                if (null == selectedPicPreList) {
                    selectedPicPreList = new ArrayList<PicDataInfo>();
                }
                selectedPicPreList.addAll(selectedPicInfoList);
                doTakePhoto();
                return;
            }
            if (checkBox.isChecked()) {
                selectedPicInfoList.add(dataInfo);
            } else {
                selectedPicInfoList.remove(dataInfo);
            }
            if (selectedPicInfoList.size() > 0) {
                setRlCount(selectedPicInfoList.size());
            } else {
                hideRlCount();
            }
        }
    };
    @Override
    protected int getActionBarCustomView() {
        return R.layout.toolbar_simple_title_with_right_text;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        //setActionBarTitle("选择相片");
        initDiskCache();
        showRlTittleCenter();
        setRlTitle("选择相片");
        hideRlCount();
        setActionRightText("下一步");
        initView();
        getIntentData();
        getData();
        user_id = UserRecord.getInstance().getUserId();
        mLoadingDialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在保存数据，请稍候.....");
        mLoadingDialog.setCancelable(false);
    }

    private void getIntentData() {
        Bundle bundle = this.getIntent().getExtras();
        if (null != bundle) {
            this.selectedPicPreList = bundle.getParcelableArrayList("lastSelected");
            this.flag = bundle.getInt("flag");
        }
    }



    @Override
    protected void onActionRightClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("selectedItem", selectedPicInfoList);
        bundle.putInt("flag", flag);

        switch (flag)
        {
            case 1:
                ActivityHelper.goPublishItemExt(this, bundle);
                break;
            case 2:
                ActivityHelper.goPublishDynamicExt(this, bundle);
                break;
            case 3:
                //设置背景
                init();
                uploadBackgroundImage();
                break;
            case 4:
                //设置头像
                init();
                uploadPortraitImage();
                break;
        }


    }
    private void init(){
        if (selectedPicInfoList.size() > 0)
        {
            PicDataInfo pdata = selectedPicInfoList.get(0);
            imgFileName = pdata.getDataPath();
            imageUri = Uri.parse("file://" + imgFileName);
        }
    }
    private void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
    private void savePortrait(final String uristr)
    {
        Map<String, Object> jm = null;
        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();

            jm.put("function", "updateportrait");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("portraituri", uristr);
            xmlString = new Gson().toJson(jm);

            showLoading();

            ApiManager.getApi()
                    .updatePortrait(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<String>>, String>() {
                        @Override
                        public String call(BaseEntry<List<String>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.save_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getBaseContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                            }
                            return "";
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(String da) {
                                    finish();
                                    mLoadingDialog.dismiss();
                        }
                    });
        }
    }

    private void saveBackground(final String uristr)
    {
        Log.e("xx","xx-----xx-----xx");
        Map<String, Object> jm = null;
        String xmlString = "";

        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("function", "updateuserbg");
            jm.put("userid", UserRecord.getInstance().getUserId());
            jm.put("backgrounduri", uristr);
            xmlString = new Gson().toJson(jm);

            showLoading();
            Log.e("xx", "xxxxxxxxxxxxxx");
            ApiManager.getApi()
                    .updateBackground(xmlString)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<BaseEntry<List<String>>, String>() {
                        @Override
                        public String call(BaseEntry<List<String>> listBaseEntry) {
                            if (listBaseEntry == null) {
                                throw new BizException(-1, getString(R.string.save_data_error));
                            }

                            if (listBaseEntry.getResult() != 0) {
                                if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                    new AutomaticLogon(getBaseContext()).login();
                                else
                                throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                            }
                            return "";
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
                            CommonExceptionHandler.handleBizException(e);
                        }

                        @Override
                        public void onNext(String da) {
                                    finish();
                                    mLoadingDialog.dismiss();
                        }
                    });
        }
    }
    private void uploadBackgroundImage()
    {
        Map<String, Object> jm = null;
        String xmlString = "";
        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("projectid", "item");
            jm.put("userid", UserRecord.getInstance().getUserId());
            xmlString = new Gson().toJson(jm);
            showLoading();

            //imgFileName = "/storage/sdcard1/test.png";
            try {
                RequestBody xml = RequestBody.create(MediaType.parse("text/plain"), xmlString);
                Map<String, RequestBody> map = new HashMap<>();
                map.put("xml", xml);

                imgFile = new File(imgFileName);
                if (imgFile != null && imgFile.exists()) {
                    Bitmap BitmapOrg = BitmapFactory.decodeFile(imgFileName);
                    int width = BitmapOrg.getWidth();
                    int height = BitmapOrg.getHeight();
                    int newWidth = 290;
                    int newHeight = 290;

                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);

                    Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                            height, matrix, true);

                    String newImgFilePath = Environment.getExternalStorageDirectory().getPath() + "/background.png";
                    FileOutputStream fOut = null;
                    File nf = new File(newImgFilePath);
                    fOut = new FileOutputStream(nf);
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    resizedBitmap.recycle();
                    BitmapOrg.recycle();

                    Log.d("image path:", newImgFilePath);
                    imgFile = new File(newImgFilePath);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
                    map.put("image\"; filename=\"" + imgFile.getName() + "", fileBody);
                    ApiManager.getApi()
                            .MyUpload(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Func1<BaseEntry<List<String>>, String>() {
                                @Override
                                public String call(BaseEntry<List<String>> listBaseEntry) {
                                    if (listBaseEntry == null) {
                                        throw new BizException(-1, getString(R.string.save_data_error));
                                    }

                                    if (listBaseEntry.getResult() != 0) {
                                        if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                            new AutomaticLogon(getBaseContext()).login();
                                        else
                                        throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                                    }

                                    if (listBaseEntry.getData().size() > 0)
                                    {
                                        return listBaseEntry.getData().get(0);
                                    }

                                    return "";
                                }
                            })
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    dismissLoading();
                                    CommonExceptionHandler.handleBizException(e);
                                    if (imgFile != null)
                                    {
                                        if (imgFile.exists())
                                            imgFile.delete();
                                    }
                                }

                                @Override
                                public void onNext(final String da) {
                                    if (imgFile != null)
                                    {
                                        if (imgFile.exists())
                                            imgFile.delete();
                                    }
                                    saveBackground(da);
                                }

                            });
                }
                else
                {
                    mLoadingDialog.setTitleText("打开图片文件失败!")
                            .setConfirmText("确定")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            mLoadingDialog.dismiss();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void uploadPortraitImage()
    {
        Map<String, Object> jm = null;
        String xmlString = "";
        if (!user_id.equals("")) {
            jm = new HashMap<>();
            jm.put("projectid", "item");
            jm.put("userid", UserRecord.getInstance().getUserId());
            xmlString = new Gson().toJson(jm);
            showLoading();

            //imgFileName = "/storage/sdcard1/test.png";
            try {
                RequestBody xml = RequestBody.create(MediaType.parse("text/plain"), xmlString);
                Map<String, RequestBody> map = new HashMap<>();
                map.put("xml", xml);

                imgFile = new File(imgFileName);
                if (imgFile != null && imgFile.exists()) {
                    Bitmap BitmapOrg = BitmapFactory.decodeFile(imgFileName);
                    int width = BitmapOrg.getWidth();
                    int height = BitmapOrg.getHeight();
                    int newWidth = 290;
                    int newHeight = 290;

                    float scaleWidth = ((float) newWidth) / width;
                    float scaleHeight = ((float) newHeight) / height;

                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);

                    Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                            height, matrix, true);

                    String newImgFilePath = Environment.getExternalStorageDirectory().getPath() + "/background.png";
                    FileOutputStream fOut = null;
                    File nf = new File(newImgFilePath);
                    fOut = new FileOutputStream(nf);
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    resizedBitmap.recycle();
                    BitmapOrg.recycle();

                    Log.d("image path:", newImgFilePath);
                    imgFile = new File(newImgFilePath);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), imgFile);
                    map.put("image\"; filename=\"" + imgFile.getName() + "", fileBody);
                    ApiManager.getApi()
                            .MyUpload(map)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Func1<BaseEntry<List<String>>, String>() {
                                @Override
                                public String call(BaseEntry<List<String>> listBaseEntry) {
                                    if (listBaseEntry == null) {
                                        throw new BizException(-1, getString(R.string.save_data_error));
                                    }

                                    if (listBaseEntry.getResult() != 0) {
                                        if (listBaseEntry.getMsg().equals(getString(R.string.session_data_error)))
                                            new AutomaticLogon(getBaseContext()).login();
                                        else
                                        throw new BizException(listBaseEntry.getResult(), listBaseEntry.getMsg());
                                    }

                                    if (listBaseEntry.getData().size() > 0)
                                    {
                                        return listBaseEntry.getData().get(0);
                                    }

                                    return "";
                                }
                            })
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    dismissLoading();
                                    CommonExceptionHandler.handleBizException(e);
                                    if (imgFile != null)
                                    {
                                        if (imgFile.exists())
                                            imgFile.delete();
                                    }
                                }

                                @Override
                                public void onNext(final String da) {
                                    if (imgFile != null)
                                    {
                                        if (imgFile.exists())
                                            imgFile.delete();
                                    }
                                    savePortrait(da);
                                }

                            });
                }
                else
                {
                    mLoadingDialog.setTitleText("打开图片文件失败!")
                            .setConfirmText("确定")
                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    mLoadingDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            mLoadingDialog.dismiss();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


    private void initView() {
        mPictureGrid = (GridView) findViewById(R.id.picture_grid);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.pic_select_empty_layout, null, false);
        ImageView img_empty_view = (ImageView) emptyView.findViewById(R.id.img_empty_view);
        img_empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //photo();
                doTakePhoto();
            }
        });
        addContentView(emptyView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mPictureGrid.setEmptyView(emptyView);
    }

    public void photo() {
        try {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String sdcardState = Environment.getExternalStorageState();
            String sdcardPathDir = android.os.Environment.getExternalStorageDirectory().getPath() + Constants.CACHE_DIR_pho+"/photo/";
            File file = null;
            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file = new File(sdcardPathDir + System.currentTimeMillis() + ".jpg");
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
    private String cachePath = null;
    private void initDiskCache() {
        File cacheDir = getDiskCacheDir(this, "uploadfile");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        cachePath = cacheDir.getAbsolutePath();
    }
    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            Log.v("zfang", "cache path = " + context.getExternalCacheDir());
            cachePath = Environment.getExternalStorageDirectory().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    private void doTakePhoto() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = File.separator + System.currentTimeMillis() + ".jpg";
        String filePath = cachePath;
        path = filePath + fileName;
        Uri imageUri = Uri.fromFile(new File(filePath + fileName));
        Log.e(TAG, "path = " + imageUri);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    private void getData() {
        if (null == getPicAsyncTask) {
            getPicAsyncTask = new GetPicTask(this);
        }
        getPicAsyncTask.execute();
    }

    class GetPicTask extends AsyncTask<Void, Void, Boolean> {
        private Context mContext  = null;
        private List<PicDataInfo> picPathList = new ArrayList<PicDataInfo>();
        public GetPicTask(Context context) {
            super();
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mPictureGrid.setAdapter(null);
                gridAdapter = new PicSelectGridAdapter(PictureSelectActivity.this, picPathList, mPictureGrid, checkClistener);
                mPictureGrid.setAdapter(gridAdapter);
                if (null != selectedPicInfoList && selectedPicInfoList.size() > 0) {
                    setRlCount(selectedPicInfoList.size());
                    gridAdapter.addSelectedPicInfoList(selectedPicInfoList);
                }
            }
            getPicAsyncTask = null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Uri uri = MediaStore.Images.Media.getContentUri("external");
            Cursor cursor = null;
            try {
                String[] projection = new String[] {
                        MediaStore.MediaColumns._ID,
                        MediaStore.MediaColumns.DATA,
                        MediaStore.MediaColumns.DATE_MODIFIED
                };
                cursor = mContext.getContentResolver().query(uri, projection,null, null, projection[2] + " DESC");
                if (null != cursor && cursor.getCount() > 0) {
                    selectedPicInfoList.clear();
                    picPathList.clear();
                    int dataId = cursor.getColumnIndex(projection[0]);
                    int dataIndex = cursor.getColumnIndex(projection[1]);
                    cursor.moveToFirst();
                    do {
                        PicDataInfo item = new PicDataInfo(cursor.getLong(dataId), cursor.getString(dataIndex));
                        if (null != selectedPicPreList && selectedPicPreList.size() > 0) {
                            for (int i = 0 ; i < selectedPicPreList.size(); i++) {
                                if (selectedPicPreList.get(i).getMediaId() == item.getMediaId()) {
                                    item.setIsSelected(1);
                                    selectedPicInfoList.add(item);
                                    break;
                                }
                            }
                        }
                        cursor.moveToNext();
                        this.picPathList.add(item);
                    } while (!cursor.isAfterLast());
                    PicDataInfo itemCamera = new PicDataInfo();
                    itemCamera.setMediaId(-100);
                    picPathList.add(0, itemCamera);
                    return true;
                }
            }
            catch (IllegalStateException ex) {
                ex.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
            return false;
        }
    }
    private MediaScannerConnection.OnScanCompletedListener callback = new MediaScannerConnection.OnScanCompletedListener(){

        @Override
        public void onScanCompleted(String path, Uri uri) {
            Log.e("zfang", "path = " + path + ", uri = " + uri);
            getData();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case TAKE_PICTURE: {
                    if (!TextUtils.isEmpty(path)) {
                        String[] paths = { path };
                        Log.d("zfang", "scanPathforMediaStore,scan file .");
                        MediaScannerConnection.scanFile(this, paths, null, callback);
                    }
                }
            }
        }
    }
}
