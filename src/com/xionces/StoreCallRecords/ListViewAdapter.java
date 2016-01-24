package com.xionces.StoreCallRecords;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tayfun CESUR on 24.01.2016.
 */
public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<String> ds;

    public ListViewAdapter(Context context,ArrayList<String> dataSource) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.ds = dataSource;
    }

    public class ViewHolder {
        TextView name;
        TextView date;
        TextView time;
    }

    @Override
    public int getCount() {
        return ds.size();
    }

    @Override
    public Object getItem(int position) {
        return ds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_row, null);
            holder.name = (TextView) view.findViewById(R.id.textView2);
            holder.date = (TextView) view.findViewById(R.id.textView3);
            holder.time = (TextView) view.findViewById(R.id.textView4);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String[] array = ds.get(position).split("\\|");
        holder.name.setText(array[0]);
        String[] datetime = array[1].split("_");
        holder.date.setText(datetime[0]);
        String[] timearray = datetime[1].split("\\.");
        String[] timex = timearray[0].split("-");
        holder.time.setText(timex[0]+":"+timex[1]);


        return view;
    }

}