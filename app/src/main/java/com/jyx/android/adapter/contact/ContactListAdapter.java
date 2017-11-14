package com.jyx.android.adapter.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
public class ContactListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, Filterable, View.OnClickListener {

    private List<FriendInfo> mDataList;
    private ArrayList<FriendInfo> mOriginalValues;
    private  ArrayList<PhoneFriend> friend;

    private int mode = NORMAL_MODE;

    private int mPageType;

    public static final int NORMAL_MODE = 0;

    public static final int SEARCH_MODE = 1;

    public static final int PAGE_CONTACT = 0;

    public static final int PAGE_FRIENDS = 4;

    public static final int PAGE_FOLLOW = 1;

    public static final int PAGE_FAN = 2;

    public static final int PAGE_ESTABLISH_GROUP = 3;

    private DisplayImageOptions mOptions;
    private final Object mLock = new Object();
    private ArrayFilter mFilter;
    Context context;
    private onContactItemClickListener mListener;

    public ContactListAdapter(List<FriendInfo> mDataList, int pageType,Context context,ArrayList<PhoneFriend> friend) {
        this.mDataList = mDataList;
        this.mPageType = pageType;
        mOptions = ImageOptions.getContactOptions();
        this.context=context;
        this.friend=friend;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        FriendInfo friendInfo = mDataList.get(position);
        int viewType = getItemViewType(position);
        if (viewType == FriendInfo.SECTION) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_pinned, null);
            }
            ((TextView) convertView).setText(friendInfo.getFirstSpell());
        } else {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_friend, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(friendInfo.getPortraitUri(), holder.mIvAvatar, mOptions);
            holder.mTvNickName.setText(friendInfo.getNickName());
            if ((position < mDataList.size() - 1)
                    && (mDataList.get(position + 1).getType() == FriendInfo.SECTION)) {
                holder.mVDivider.setVisibility(View.GONE);
            } else {
                holder.mVDivider.setVisibility(View.VISIBLE);
            }

            if (mPageType == PAGE_FOLLOW) {
                holder.mTvAction.setVisibility(View.VISIBLE);
                holder.mTv_phone.setVisibility(View.GONE);

            }else if(mPageType == PAGE_ESTABLISH_GROUP)
            {
                holder.mTv_phone.setVisibility(View.GONE);
                holder.mCbContact.setVisibility(View.VISIBLE);
                holder.mCbContact.setChecked(friendInfo.isSelected());
            }else if (mPageType == PAGE_FAN){
                holder.mTv_phone.setVisibility(View.GONE);
            }
            holder.mLlItem.setOnClickListener(this);
            holder.mTvAction.setOnClickListener(this);
            holder.mLlItem.setTag(position);
            holder.mTvAction.setTag(position);
            holder.mTv_phone.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("message","拨打电话 == "+mDataList.get(position)
                            .getMobilephonenumberr());
                    Uri uri = Uri.parse("tel:"+ mDataList.get(position)
                            .getMobilephonenumberr());//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
                    Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                    context.startActivity(intent);
                }
            });
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
        if(mListener != null){
            int position = (int) v.getTag();
            mListener.onClick(v, mDataList.get(position), position);
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
                ArrayList<FriendInfo> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString();

                ArrayList<FriendInfo> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<FriendInfo> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final FriendInfo value = values.get(i);

                    if (value.getType() == FriendInfo.ITEM) {
                        if (value.getNickName().startsWith(prefixString)
                                || value.getPinyin().startsWith(prefixString.toUpperCase())) {
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
            mDataList = (List<FriendInfo>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_cell_friend.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_avatar)
        ImageView mIvAvatar;
        @Bind(R.id.tv_nick_name)
        TextView mTvNickName;
        @Bind(R.id.v_divider)
        View mVDivider;
        @Bind(R.id.tv_action)
        TextView mTvAction;
        @Bind(R.id.ll_item)
        LinearLayout mLlItem;
        @Bind(R.id.cb_contact)
        CheckBox mCbContact;
        @Bind(R.id.tv_myphone)
        ImageView mTv_phone;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface onContactItemClickListener {
        void onClick(View view, FriendInfo friendInfo, int position);
    }

    public void setClickListener(onContactItemClickListener listener) {
        this.mListener = listener;
    }

    public void removeItem(int position){
        FriendInfo friendInfo = mDataList.remove(position);
        if(mOriginalValues != null){
            mOriginalValues.remove(friendInfo);
        }
        notifyDataSetChanged();
    }

    public void updateSelectState(int position, boolean select){
        FriendInfo friendInfo = mDataList.get(position);
        friendInfo.setIsSelected(select);
        int index;
        if(mOriginalValues != null && ((index = mOriginalValues.indexOf(friendInfo)) != -1)){
            mOriginalValues.get(index).setIsSelected(select);
        }
        notifyDataSetChanged();
    }

}
