package com.jyx.android.adapter.contact;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jyx.android.R;
import com.jyx.android.fragment.contact.GroupFragment;
import com.jyx.android.model.GroupInfo;
import com.jyx.android.utils.ImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author : Tree
 * Date : 2016-01-13
 */
public class GroupListAdapter extends BaseAdapter implements Filterable {
    private List<GroupInfo> mDataList;
    private ArrayList<GroupInfo> mOriginalValues;

    private final Object mLock = new Object();
    private ArrayFilter mFilter;
    private int mode;

    public List<GroupInfo> getSelectedItems() {
        return mSelectedItems;
    }

    private List<GroupInfo> mSelectedItems = new ArrayList<>();

    private DisplayImageOptions mOptions;

    public GroupListAdapter(List<GroupInfo> groupInfoList, int mode) {
        this.mDataList = groupInfoList;
        this.mode = mode;
        mOptions = ImageOptions.getContactOptions();
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

    public List<GroupInfo> getDataList() {
        return mDataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_group, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GroupInfo groupInfo = mDataList.get(position);
        viewHolder.mTvGroupName.setText(groupInfo.getGroupName());
        if (!TextUtils.isEmpty(groupInfo.getImageJson())) {
            ImageLoader.getInstance().displayImage(groupInfo.getImageJson(), viewHolder.mIvGroupLogo, mOptions);
        }
        viewHolder.mRbContact.setVisibility(mode == GroupFragment.MODE_NORMAL ? View.GONE : View.VISIBLE);
        viewHolder.mRbContact.setChecked(mSelectedItems.contains(groupInfo));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
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
                ArrayList<GroupInfo> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString();

                ArrayList<GroupInfo> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<GroupInfo> newValues = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    final GroupInfo value = values.get(i);

                    if (value.getGroupName().startsWith(prefixString)
                            || value.getPinyin().startsWith(prefixString.toUpperCase())) {
                        newValues.add(value);
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
            mDataList = (List<GroupInfo>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public void updateSelectItems(GroupInfo selectedGroup, boolean clear){
        if(mSelectedItems.contains(selectedGroup)){
            mSelectedItems.remove(selectedGroup);
        }else {
            if(clear){
                mSelectedItems.clear();
            }
            mSelectedItems.add(selectedGroup);
        }

        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'list_cell_group.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.rb_contact)
        RadioButton mRbContact;
        @Bind(R.id.iv_group_logo)
        ImageView mIvGroupLogo;
        @Bind(R.id.tv_group_name)
        TextView mTvGroupName;
        @Bind(R.id.ll_item)
        LinearLayout mLlItem;
        @Bind(R.id.v_divider)
        View mVDivider;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
