package com.jyx.android.activity.publish;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;

import com.jyx.android.R;
import com.jyx.android.base.Application;
import com.jyx.android.model.PicDataInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zfang on 2015/12/29.
 */
public class PicSelectGridAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private String TAG = "PicSelectGridAdapter";
    private Context mContext = null;
    private OnCheckClistener listener = null;
    private List<PicDataInfo> picPathList;
    private LayoutInflater mInflater = null;
    private LruCache<PicDataInfo, Bitmap> mMemoryCache;
    private Set<BitmapWorkerTask> taskCollection;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private boolean isFirstEnter = true;
    private GridView mPictureGv = null;
    private OnCheckClistener checkClistener = null;
    private final int MAX_COUNT = 9;
    private  ArrayList<PicDataInfo> selectedPicInfoList= new ArrayList<PicDataInfo>();
    public PicSelectGridAdapter(Context context, List<PicDataInfo> picPathList, GridView grdivew, OnCheckClistener listener) {
        this.mContext = context;
        this.picPathList = picPathList;
        this.mInflater = LayoutInflater.from(context);
        this.mPictureGv = grdivew;
        this.checkClistener = listener;
        init(this.mPictureGv);
    }
    @TargetApi(12)
    private void init(GridView pictureGv) {
        taskCollection = new HashSet<BitmapWorkerTask>();
        mPictureGv = pictureGv;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<PicDataInfo, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(PicDataInfo key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        mPictureGv.setOnScrollListener(this);

    }

    public boolean isFirstEnter() {
        return isFirstEnter;
    }

    public void setIsFirstEnter(boolean isFirstEnter) {
        this.isFirstEnter = isFirstEnter;
    }

    public void addSelectedPicInfoList(ArrayList<PicDataInfo> selectedPicInfoList) {
        this.selectedPicInfoList.addAll(selectedPicInfoList);
    }
    public OnCheckClistener getListener() {
        return listener;
    }

    public void setListener(OnCheckClistener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return this.picPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.picPathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PicViewHolder viewHolder ;
        PicDataInfo currentPic = this.picPathList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_pic_select_gd_cell, null);
            viewHolder = new PicViewHolder(
                    (ImageView) convertView.findViewById(R.id.picture),
                    (CheckBox) convertView.findViewById(R.id.checkbox));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PicViewHolder) convertView.getTag();
        }
        if (currentPic != null) {
            if (0 == position && -100 == currentPic.getMediaId()) {
                viewHolder.mCheckBox.setVisibility(View.GONE);
                viewHolder.mIcon.setTag(currentPic);
                viewHolder.mIcon.setImageResource(R.drawable.icon_pic_select_empty);
                setOnCheckClick(viewHolder.mIcon, viewHolder.mCheckBox, position);
            } else {
                viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                viewHolder.mIcon.setTag(currentPic);
                setImageView(currentPic, viewHolder.mIcon);

                if (1 == currentPic.getIsSelected()) {
                    //viewHolder.mCheckBox.setChecked(true);
                    viewHolder.mCheckBox.setButtonDrawable(R.mipmap.icon_check_on);
                } else {
                    //viewHolder.mCheckBox.setChecked(false);
                    viewHolder.mCheckBox.setButtonDrawable(R.mipmap.icon_check_off);
                }
                setOnCheckClick(viewHolder.mIcon, viewHolder.mCheckBox, position);
            }
        }
        return convertView;
    }

    private void setOnCheckClick(ImageView imageView, final CheckBox checkBox, final int position) {
        final PicDataInfo dataInfo = this.picPathList.get(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicDataInfo currentPic = picPathList.get(position);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PicDataInfo currentPic = picPathList.get(position);
                if (isChecked) {
                    if (MAX_COUNT == selectedPicInfoList.size()) {
                        Application.showToast(R.string.picture_select_max_notice);
                        checkBox.setChecked(false);
                        return;
                    } else {
                        if (1 != currentPic.getIsSelected()) {
                            checkBox.setButtonDrawable(R.mipmap.icon_check_on);
                            currentPic.setIsSelected(1);
                            selectedPicInfoList.add(currentPic);
                        } else {
                            return;
                        }
                    }
                } else {
                    checkBox.setButtonDrawable(R.mipmap.icon_check_off);
                    currentPic.setIsSelected(0);
                    selectedPicInfoList.remove(currentPic);
                }
                if (null != checkClistener) {
                    checkClistener.onCheckClick(checkBox, position, dataInfo);
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEnter && visibleItemCount > 0) {
            loadBitmaps(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
                PicDataInfo currentFile = this.picPathList.get(i);
                Bitmap bitmap = getBitmapFromMemoryCache(currentFile);
                if (0 == i) {
                    ImageView imageView = (ImageView) mPictureGv.findViewWithTag(this.picPathList.get(firstVisibleItem));
                    if (imageView != null) {
                        imageView.setImageResource(R.drawable.icon_pic_select_empty);
                    }
                    continue;
                }
                if (bitmap == null) {
                    BitmapWorkerTask task = new BitmapWorkerTask();
                    taskCollection.add(task);
                    task.execute(currentFile);
                } else {
                    ImageView imageView = (ImageView) mPictureGv.findViewWithTag(currentFile);
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setImageView(PicDataInfo fileInfo, ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemoryCache(fileInfo);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.icon_loading);
        }
    }

    @TargetApi(12)
    public Bitmap getBitmapFromMemoryCache(PicDataInfo fileInfo) {
        return mMemoryCache.get(fileInfo);
    }

    @TargetApi(12)
    public void addBitmapToMemoryCache(PicDataInfo fileInfo, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(fileInfo) == null) {
            mMemoryCache.put(fileInfo, bitmap);
        }
    }
    private class  PicViewHolder {
        private ImageView mIcon = null;
        private CheckBox mCheckBox = null;
        PicViewHolder(ImageView picture, CheckBox checkBox) {
            this.mIcon = picture;
            this.mCheckBox = checkBox;
        }
    }
    class BitmapWorkerTask extends AsyncTask<PicDataInfo, Void, Bitmap> {

        private PicDataInfo mFileInfo;

        @Override
        protected Bitmap doInBackground(PicDataInfo... params) {
            // TODO Auto-generated method stub
            mFileInfo = params[0];
            Bitmap bitmap = getThumbnails(mFileInfo);
            if (bitmap != null) {
                addBitmapToMemoryCache(mFileInfo, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mPictureGv.findViewWithTag(mFileInfo);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            taskCollection.remove(this);
        }
    }
    public Bitmap getThumbnails(PicDataInfo fileInfo) {
        long originId = fileInfo.getMediaId();
        ContentResolver cr = mContext.getContentResolver();
        int kind = MediaStore.Images.Thumbnails.MICRO_KIND;
        return MediaStore.Images.Thumbnails.getThumbnail(cr, originId, kind, null);
    }
    public interface OnCheckClistener {
        public void onCheckClick(CheckBox checkBox, int position, PicDataInfo dataInfo);
    }
}
