package com.jyx.android.adapter.chat;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.base.ListBaseAdapter;
import com.jyx.android.fragment.contact.GroupNoticeFragment;
import com.jyx.android.model.GroupNoticeItemBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 群通知adapter
 * Created by px96004@qq.com on 2015/10/31.
 */
public class GrouNoticepAdapter extends ListBaseAdapter {

    private GroupNoticeFragment context = null;

    static class ViewHolder {
        @Bind(R.id.tv_notive_cell_title)
        TextView mLitle;
        @Bind(R.id.tv_notive_cell_content)
        TextView mContent;
        @Bind(R.id.tv_notive_cell_time)
        TextView mTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public GrouNoticepAdapter(GroupNoticeFragment context) {
        this.context=context;
    }

    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null || convertView.getTag() == null) {
            convertView = getConvertView(parent, R.layout.list_cell_group_notice);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(_data!=null)
        {
            GroupNoticeItemBean infor = null;
            if(position>=0 && _data.size()>position)
            {
                infor = (GroupNoticeItemBean)_data.get(position);
            }
            if(infor!=null)
            {
                Date date = null;
                String createTimeStr = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date = sdf.parse(infor.getCreatedat());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                createTimeStr = sdf.format(date);
                vh.mTime.setText(createTimeStr);
                vh.mLitle.setText(infor.getTitle());
                vh.mContent.setText(infor.getContent());
                Log.e("cont",infor.getContent());
            }
        }


        return convertView;
    }

}
