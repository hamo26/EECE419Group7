package com.schedushare.android.fragments;

import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesDataSource;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ScheduleListFragment extends Fragment {
	public ListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set so that fragment does not get recreated every time configuration changes.
		// (e.g. orientation change)
		setRetainInstance(true);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		// Only create the cursor and adapter the first time fragment is created.
		if (savedInstanceState == null) {
			setListViewAdapter();
		}
		
		return this.listView;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	// Called on edit schedule return.
    	if (requestCode == 0) {
    		if (resultCode != Activity.RESULT_CANCELED) {
    			Bundle b = data.getExtras();
    			if (b.getBoolean("deleteSuccess")) {
    				swapCursor();
    			}
    		}
    	}
    }
	
	private void setListViewAdapter() {
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		Cursor cursor = dataSource.getAllSchedulesCursor();
		dataSource.close();
		
		// Create new adapter and attach to listView.
		int[] toViews = new int[] {R.id.schedule_list_name_entry, R.id.schedule_list_last_modified_entry};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), 
				R.layout.list_item_schedule, cursor, SchedulesDataSource.menuScheduleColumns, toViews, 0);
		
		// Create view with callback method when user selects an item from the list.
		this.listView = new ListView(getActivity());
		this.listView.setOnItemClickListener(new OnItemClickListener() {  
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Start EditScheduleActivity with the selected schedule.
				Intent intent = new Intent(getActivity(), EditScheduleActivity.class);
				intent.putExtra("scheduleId", id);
		        startActivityForResult(intent, 0);
				
			}  
		});
		this.listView.setAdapter(adapter);
	}
	
	public void swapCursor() {
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		Cursor cursor = dataSource.getAllSchedulesCursor();
		dataSource.close();
		
		// Swap existing cursor with new one.
		((SimpleCursorAdapter)this.listView.getAdapter()).changeCursor(cursor);
	}
}