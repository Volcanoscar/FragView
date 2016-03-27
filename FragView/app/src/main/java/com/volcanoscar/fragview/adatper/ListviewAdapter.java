package com.volcanoscar.fragview.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.volcanoscar.fragview.R;
import com.volcanoscar.fragview.dummy.DummyContent;

import java.util.List;

/**
 * Created by volcanoscar on 16/3/27.
 */
public class ListviewAdapter extends BaseAdapter {

    private Context mContext;
    private List<DummyContent.DummyItem> mDummyItems;

    public ListviewAdapter(Context mContext, List<DummyContent.DummyItem> mDummyItems) {
        this.mContext = mContext;
        this.mDummyItems = mDummyItems;
    }

    @Override
    public int getCount() {
        return mDummyItems.size();
    }

    @Override
    public DummyContent.DummyItem getItem(int position) {
        return mDummyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_item, parent, false);
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText(getItem(position).id);
        holder.content.setText(getItem(position).content);
        return convertView;
    }

    class ViewHolder {
        TextView id;
        TextView content;
    }
}
