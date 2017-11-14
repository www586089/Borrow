package com.jyx.android.adapter.me;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.model.UserRelation;
import com.jyx.android.utils.MySectionIndexer;
import com.jyx.android.widget.view.PinnedHeaderListView;
import com.jyx.android.widget.view.PinnedHeaderListView.PinnedHeaderAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class UserRelationListAdapter extends BaseAdapter implements
		PinnedHeaderAdapter, OnScrollListener {
	private List<UserRelation> mList;
	private MySectionIndexer mIndexer;
	private Context mContext;
	private int mLocationPosition = -1;
	private LayoutInflater mInflater;

	public UserRelationListAdapter(List<UserRelation> mList, MySectionIndexer mIndexer, Context mContext) {
		this.mList = mList;
		this.mIndexer = mIndexer;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(List<UserRelation> listData) {
		this.mList = listData;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.user_relation_cell_item, null);

			holder = new ViewHolder();
			holder.group_title = (TextView) view.findViewById(R.id.group_title);
			holder.user_name = (TextView) view.findViewById(R.id.user_name);
			holder.obj_id= (TextView)  view.findViewById(R.id.obj_id);
			holder.headPortrait = (ImageView) view.findViewById(R.id.iv_headPortrait);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		UserRelation userrelation = mList.get(position);
		
		int section = mIndexer.getSectionForPosition(position);
		if (mIndexer.getPositionForSection(section) == position) {
			holder.group_title.setVisibility(View.VISIBLE);
			holder.group_title.setText(userrelation.getSortKey());
		} else {
			holder.group_title.setVisibility(View.GONE);
		}
		
		holder.user_name.setText(userrelation.getUserFriendName());
		holder.obj_id.setText(userrelation.getObjId());

		ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
		//imageLoader.displayImage(userrelation.getImageUrl(), holder.headPortrait);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				//.showImageOnLoading(R.drawable.ic_stub) // resource or drawable
				.showImageForEmptyUri(R.mipmap.icon_launcher) // resource or drawable
				//

				.showImageOnFail(R.mipmap.icon_launcher) // resource or drawable
				//.resetViewBeforeLoading(false)  // default
				//.delayBeforeLoading(1000)
				//.cacheInMemory(false) // default
				//.cacheOnDisk(false) // default
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.RGB_565) // default
				.displayer(new SimpleBitmapDisplayer()) // default
				//.handler(new Handler()) // default
				.build();
		ImageSize targetSize = new ImageSize(40, 40); // result Bitmap will be fit to this size
		Bitmap bmp = imageLoader.loadImageSync(userrelation.getImageUrl(), targetSize, options);
		holder.headPortrait.setImageBitmap(bmp);
		return view;
	}

	public static class ViewHolder {
		public TextView group_title;
		public TextView user_name;
		public TextView obj_id;
		public ImageView headPortrait;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || (mLocationPosition != -1 && mLocationPosition == realPosition)) {
			return PINNED_HEADER_GONE;
		}
		mLocationPosition = -1;
		int section = mIndexer.getSectionForPosition(realPosition);
		int nextSectionPosition = mIndexer.getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;
		}
		return PINNED_HEADER_VISIBLE;
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		if (position >= 0 && position < mList.size()) {
			int section = mList.get(position).getSortKey().charAt(0) - (int) 'A' + 1;
			String title = (String) mIndexer.getSections()[section];
			((TextView) header.findViewById(R.id.group_title)).setText(title);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (view instanceof PinnedHeaderListView) {
			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
		}

	}
}
