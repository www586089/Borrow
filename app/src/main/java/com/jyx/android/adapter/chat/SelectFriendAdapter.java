package com.jyx.android.adapter.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.utils.ImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择好友列表
 * Created by yiyi on 2015/11/9
 */
@SuppressLint("DefaultLocale")
public class SelectFriendAdapter extends BaseAdapter implements SectionIndexer {
	private Context ct;
	private List<Friend> data;

	public SelectFriendAdapter(Context ct, List<Friend> datas) {
		this.ct = ct;
		this.data = datas;
	}

	/** 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<Friend> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void remove(Friend user){
		this.data.remove(user);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(ct).inflate(
					R.layout.select_friend_item, null);
			viewHolder = new ViewHolder();
			viewHolder.alpha = (TextView) convertView.findViewById(R.id.tv_friend_alpha);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_friend_name);		
			viewHolder.selected = (CheckBox)convertView.findViewById(R.id.cb_friend_selected);
			viewHolder.img_friends_avatar = (ImageView) convertView.findViewById(R.id.img_friends_avatar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Friend friend = data.get(position);
		final String name = friend.getUserName();
		if (!TextUtils.isEmpty(friend.getPortraituri())) {
			ImageLoader.getInstance().displayImage(friend.getPortraituri(), viewHolder.img_friends_avatar, ImageOptions.get_gushi_Options() );
		} else {
			viewHolder.img_friends_avatar.setImageResource(R.mipmap.me_photo_2x_81);
		}
		viewHolder.selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				data.get(position).setSelected(isChecked);
				getSelected();
			}
		});
		
		viewHolder.name.setText(name);
		viewHolder.selected.setChecked(friend.getSelected());

		// 根据position获取分类的首字母的ASCII值
		int section = getSectionForPosition(position);
		// 如果当前位置等于该分类首字母的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.alpha.setVisibility(View.VISIBLE);
			viewHolder.alpha.setText(friend.getSortLetters());
		} else {
			viewHolder.alpha.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView alpha;//首字母提示
		TextView name;
		CheckBox selected;
		ImageView img_friends_avatar;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的ASCII值
	 */
	public int getSectionForPosition(int position) {
		return data.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的ASCII值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = data.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section){
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public ArrayList<Friend> getSelected() {
		ArrayList<Friend> listItem = new ArrayList<Friend>();
		
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getSelected()) {
				listItem.add(data.get(i));
			}
		}
		
		return listItem;
	}
}