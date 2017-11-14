package com.jyx.android.adapter.contact;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.model.FriendInfo;
import com.jyx.android.model.PhoneFriend;
import com.jyx.android.utils.ImageOptions;
import com.jyx.android.widget.view.PinnedSectionListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : Tree
 * Date : 2016-01-10
 */
public class PhoneContactListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, Filterable, View.OnClickListener {

    private List<PhoneFriend> mDataList;
    private ArrayList<PhoneFriend> mOriginalValues;

    private int mode = NORMAL_MODE;

    public static final int NORMAL_MODE = 0;

    public static final int SEARCH_MODE = 1;

    public static final int PAGE_CONTACT = 0;

    public static final int PAGE_FOLLOW = 1;

    public static final int PAGE_FAN = 2;
    private DisplayImageOptions mOptions;
    private ContentValues values;
    private final Object mLock = new Object();
    private ArrayFilter mFilter;

    private onPhoneContactItemClickListener mListener;

    public PhoneContactListAdapter(List<PhoneFriend> mDataList) {
        this.mDataList = mDataList;
        mOptions = ImageOptions.getContactOptions();
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return (mode == NORMAL_MODE) && (viewType == FriendInfo.SECTION);
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhoneFriend friendInfo = mDataList.get(position);
        int viewType = getItemViewType(position);
        if (viewType == FriendInfo.SECTION) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_pinned, null);
            }
            ((TextView) convertView).setText(friendInfo.getFirstSpell());
        } else {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_phone_friend, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(friendInfo.getPortraituri(), holder.mIvAvatar, mOptions);
            holder.mTvNickName.setText(friendInfo.getName());
            if ((position < mDataList.size() - 1)
                    && (mDataList.get(position + 1).getType() == FriendInfo.SECTION)) {
                holder.mVDivider.setVisibility(View.GONE);
            } else {
                holder.mVDivider.setVisibility(View.VISIBLE);
            }
            String status = friendInfo.getStatus();
            if("1".equals(status)){
                holder.mTvState.setVisibility(View.VISIBLE);
                holder.mBtnAddFriend.setVisibility(View.GONE);
                holder.mTvState.setText("已添加");
            }else if("2".equals(status)){
                holder.mTvState.setVisibility(View.VISIBLE);
                holder.mBtnAddFriend.setVisibility(View.GONE);
                holder.mTvState.setText("等待验证");

            }else if("3".equals(status)){
                holder.mTvState.setVisibility(View.GONE);
                holder.mBtnAddFriend.setVisibility(View.VISIBLE);
                holder.mBtnAddFriend.setText("加好友");
            }else if("4".equals(status)){
                holder.mTvState.setVisibility(View.GONE);
                holder.mBtnAddFriend.setVisibility(View.VISIBLE);
                holder.mBtnAddFriend.setText("邀请加入");
            }else if("5".equals(status)){
                holder.mTvState.setVisibility(View.GONE);
                holder.mBtnAddFriend.setVisibility(View.VISIBLE);
                holder.mBtnAddFriend.setText("接受");
            }else {
                holder.mTvState.setVisibility(View.GONE);
                holder.mBtnAddFriend.setVisibility(View.GONE);
            }


            holder.mBtnAddFriend.setOnClickListener(this);
            holder.mBtnAddFriend.setTag(position);
        }
        return convertView;
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            int position = (int) v.getTag();
            mListener.onClick(v, mDataList.get(position), position);
            notifyDataSetChanged();
        }
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mDataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<PhoneFriend> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString();

                ArrayList<PhoneFriend> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<PhoneFriend> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final PhoneFriend value = values.get(i);

                    if (value.getType() == FriendInfo.ITEM) {
                        if ((value.getName() != null && value.getName().contains(prefixString))
                                || (value.getPinyin() != null && value.getPinyin().contains(prefixString.toUpperCase()))
                                || (value.getMobile() != null && value.getMobile().contains(prefixString))
                                || (value.getNickname() != null && value.getNickname().contains(prefixString))
                                ) {
                            newValues.add(value);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mDataList = (List<PhoneFriend>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_cell_phone_friend.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        ImageView mIvAvatar;
        @Bind(R.id.tv_nick_name)
        TextView mTvNickName;
        @Bind(R.id.tv_state)
        TextView mTvState;
        @Bind(R.id.btn_add_friend)
        Button mBtnAddFriend;
        @Bind(R.id.ll_item)
        LinearLayout mLlItem;
        @Bind(R.id.v_divider)
        View mVDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public interface onPhoneContactItemClickListener {
        void onClick(View view, PhoneFriend friendInfo, int position);
    }

    public void setClickListener(onPhoneContactItemClickListener listener) {
        this.mListener = listener;
    }

    public void removeItem(int position) {
        PhoneFriend friendInfo = mDataList.remove(position);
        if (mOriginalValues != null) {
            mOriginalValues.remove(friendInfo);
        }
        notifyDataSetChanged();
    }

}
