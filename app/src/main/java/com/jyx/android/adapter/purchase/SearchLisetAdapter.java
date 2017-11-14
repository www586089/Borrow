package com.jyx.android.adapter.purchase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jyx.android.R;
import com.jyx.android.model.SysDataItem;

import java.util.List;

/**
 * Created by Dell on 2016/5/25.
 */
public class SearchLisetAdapter extends BaseAdapter{
    private List<SysDataItem> classification;
    private Context context;
    public SearchLisetAdapter(List<SysDataItem> classification,Context context){
        this.classification=classification;
        this.context=context;
    }
    public void UpDate(List<SysDataItem> classification){
        this.classification=classification;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return classification.size();
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
        View view;
//        if (convertView==null){
        view=View.inflate(context, R.layout.list_cell_item_search,null);
        TextView textView= (TextView) view.findViewById(R.id.tv_search_name);
        textView.setText(classification.get(position).getName());
//        }else {
//            view=convertView;
//        }
        return view;
    }
}
