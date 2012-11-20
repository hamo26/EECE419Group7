package com.schedushare.android.fragments;

import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.MainMenuActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesDataSource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	private void setListViewAdapter() {
		// Get shared preferences.
		SharedPreferences p = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		Cursor cursor = dataSource.getAllOwnerSchedulesCursor(p.getLong(getString(R.string.settings_owner_id), 1));
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
		        startActivity(intent);
				
			}  
		});
		this.listView.setAdapter(adapter);
	}
	
	public void swapCursor() {
		// Get preferences.
		SharedPreferences p = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		Cursor cursor = dataSource.getAllOwnerSchedulesCursor(p.getLong(getString(R.string.settings_owner_id), 1));
		dataSource.close();
		
		// Swap existing cursor with new one.
		((SimpleCursorAdapter)this.listView.getAdapter()).changeCursor(cursor);
	}
}