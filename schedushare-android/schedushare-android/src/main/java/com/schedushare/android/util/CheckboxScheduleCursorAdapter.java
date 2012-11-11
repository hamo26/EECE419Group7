package com.schedushare.android.util;

import java.util.ArrayList;

import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesSQLiteHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;


public class CheckboxScheduleCursorAdapter extends SimpleCursorAdapter {
	private ArrayList<Integer> selection = new ArrayList<Integer>();
	
	public CheckboxScheduleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
	
	@Override 
	public View getView(final int position, View convertView, ViewGroup parent) {  
		View view = super.getView(position, convertView, parent);
		
		CheckBox checkbox = (CheckBox)view.findViewById(R.id.checkable_schedule_list_checkbox);
		checkbox.setOnClickListener(new OnClickListener() {  
			public void onClick(View v) {  
			    Cursor cursor = getCursor();  
			    cursor.moveToPosition(position);
			    
			    int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(SchedulesSQLiteHelper.COLUMN_ID));  
			    int index = selection.indexOf(rowId); 
			    if (index != -1) {    
			    	selection.remove(index);    
			    } else {    
			    	selection.add(rowId);    
			    }    
			}  
		});
		
		Cursor cursor = getCursor();  
		cursor.moveToPosition(position);
		
		int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(SchedulesSQLiteHelper.COLUMN_ID));  
		if (selection.indexOf(rowId)!= -1) {    
			checkbox.setChecked(true);    
		} else {    
			checkbox.setChecked(false);    
		}    
		return view;  
	}  
      
}