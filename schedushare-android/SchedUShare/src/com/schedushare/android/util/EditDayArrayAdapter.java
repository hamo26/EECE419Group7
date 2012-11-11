package com.schedushare.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;

import com.schedushare.android.R;
import com.schedushare.android.db.BlockTypeData;
import com.schedushare.android.db.TimeBlockData;

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
    String time[] = null;
    ArrayList<TimeBlockData> data = null;
    HashMap<Long, BlockTypeData> blockTypes = null;
    SimpleDateFormat listTimeFormat;
   
    public EditDayArrayAdapter(Context context, int layoutResourceId, String[] time, ArrayList<TimeBlockData> data, HashMap<Long, BlockTypeData> blockTypes) {
        super(context, layoutResourceId, time);
        this.layoutResourceId = layoutResourceId;
        this.time = time;
        this.data = data;
        this.context = context;
        this.blockTypes = blockTypes;
        this.listTimeFormat = new SimpleDateFormat("kk:mm");        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view = convertView;
    	TextView timeView;
    	TextView typeView;
    	
        if (view == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(this.layoutResourceId, parent, false);
        }
        
       	timeView = (TextView)view.findViewById(R.id.time_block_time_entry);
        typeView = (TextView)view.findViewById(R.id.time_block_type_entry);
        
        Calendar currentTime = Calendar.getInstance();
        try {
			currentTime.setTime(this.listTimeFormat.parse(this.time[position]));
			
	        Formatter formatter = new Formatter();
	        
        	if (currentTime.get(Calendar.AM_PM) == Calendar.AM)
        		timeView.setText(formatter.format("%2s:%02d AM", Integer.toString(currentTime.get(Calendar.HOUR)), currentTime.get(Calendar.MINUTE)).toString());
        	else
        		timeView.setText(formatter.format("%2s:%02d PM", Integer.toString(currentTime.get(Calendar.HOUR)), currentTime.get(Calendar.MINUTE)).toString());

        	formatter.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        typeView.setText("Free");
        
        try {
        	currentTime.setTime(this.listTimeFormat.parse(this.time[position]));
			
			Calendar startTime = Calendar.getInstance();
	        Calendar endTime = Calendar.getInstance();
	        
	        for (TimeBlockData timeBlock : this.data) {
	        	startTime.setTime(this.listTimeFormat.parse(timeBlock.startTime));
	        	endTime.setTime(this.listTimeFormat.parse(timeBlock.endTime));
	        	
	        	if ((currentTime.get(Calendar.HOUR_OF_DAY) >= startTime.get(Calendar.HOUR_OF_DAY)) &&
	        		(currentTime.get(Calendar.HOUR_OF_DAY) <= endTime.get(Calendar.HOUR_OF_DAY))) {
	        		typeView.setText(this.blockTypes.get(timeBlock.blockTypeId).name);
	        		break;
	        	}
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}

        return view;
    }
}