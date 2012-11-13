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
    private Context context;
    private int layoutResourceId;   
    private String time[] = null;
    private ArrayList<TimeBlockData> data = null;
    private HashMap<Long, BlockTypeData> blockTypes = null;
    private SimpleDateFormat listTimeFormat;
   
    public EditDayArrayAdapter(Context context, int layoutResourceId, String[] time, ArrayList<TimeBlockData> data, HashMap<Long, BlockTypeData> blockTypes) {
        super(context, layoutResourceId, time);
        this.layoutResourceId = layoutResourceId;
        this.time = time;
        this.data = data;
        this.context = context;
        this.blockTypes = blockTypes;
        this.listTimeFormat = new SimpleDateFormat("hh:mm:ss aa");        
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View view = convertView;
    	TextView timeView;
    	TextView nameView;
    	TextView typeView;
    	
        if (view == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(this.layoutResourceId, parent, false);
        }
        
       	timeView = (TextView)view.findViewById(R.id.time_block_time_entry);
       	nameView = (TextView)view.findViewById(R.id.time_block_name_entry);
        typeView = (TextView)view.findViewById(R.id.time_block_type_entry);
        
        // Parse current time and change it to 12-hour format on list view.
        // Format so to vertically align numbers.
        Calendar currentTime = Calendar.getInstance();
        try {
			currentTime.setTime(this.listTimeFormat.parse(this.time[position]));
			
	        Formatter formatter = new Formatter();
	        String hour = Integer.toString(currentTime.get(Calendar.HOUR));
	        if (hour.equals("0")) hour = "12";
        	if (currentTime.get(Calendar.AM_PM) == Calendar.AM) {
        		timeView.setText(formatter.format("%2s:%02d AM", hour, currentTime.get(Calendar.MINUTE)).toString());
        	} else {
        		timeView.setText(formatter.format("%2s:%02d PM", hour, currentTime.get(Calendar.MINUTE)).toString());
        	}
        		
        	formatter.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        // Time blocks are free by default.
        nameView.setText("");
        typeView.setText("");
        
        // Look through all time blocks and find first time blocks in which the
        // current time block belongs to.
        try {
        	currentTime.setTime(this.listTimeFormat.parse(this.time[position]));
			
			Calendar startTime = Calendar.getInstance();
	        Calendar endTime = Calendar.getInstance();
	        
	        for (TimeBlockData timeBlock : this.data) {
//	        	System.out.println("ArrayAdapter: start " + timeBlock.startTime + " end " + timeBlock.endTime);
	        	
	        	// Create calendar objects from the time block's start and end times.
	        	startTime.setTime(this.listTimeFormat.parse(timeBlock.startTime));
	        	endTime.setTime(this.listTimeFormat.parse(timeBlock.endTime));
	        	
	        	// Check for whether current time block falls into the start and end times.
	        	if ((currentTime.getTime().getTime() >= startTime.getTime().getTime()) &&
	        		(currentTime.getTime().getTime() < endTime.getTime().getTime())) {
	        		nameView.setText(timeBlock.name);
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