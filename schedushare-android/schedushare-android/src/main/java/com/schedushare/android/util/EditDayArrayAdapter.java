package com.schedushare.android.util;

import com.schedushare.android.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EditDayArrayAdapter extends ArrayAdapter<String>
{
    Context context;
    int layoutResourceId;   
    String data[] = null;
   
    public EditDayArrayAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view = convertView;
    	TextView time;
    	
        if(view == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(this.layoutResourceId, parent, false);
           
            time = (TextView)view.findViewById(R.id.edit_day_time_entry);
           
            view.setTag(time);
        }
        else
        {
            time = (TextView)view.getTag();
        }
       
        time.setText(this.data[position]);
       
        return view;
    }
}