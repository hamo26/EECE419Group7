package com.schedushare.android.fragments;

import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesDataSource;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ScheduleListFragment extends Fragment {
	private SchedulesDataSource dataSource;
	private ListView listView;
	
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
		
		if (savedInstanceState == null) {
			this.dataSource = new SchedulesDataSource(getActivity());
			this.dataSource.open();
			
			Cursor cursor = this.dataSource.getAllSchedulesCursor();
			int[] toViews = new int[] {R.id.owner_entry, R.id.name_entry};
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), 
					R.layout.list_item_schedule, cursor, SchedulesDataSource.menuScheduleColumns, toViews, 0);
			
			this.listView = new ListView(getActivity());
			this.listView.setAdapter(adapter);
		}
		
		return this.listView;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.dataSource.close();
	}
}