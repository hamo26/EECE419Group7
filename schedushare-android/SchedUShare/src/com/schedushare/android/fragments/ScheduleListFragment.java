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
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;

public class ScheduleListFragment extends Fragment {
	private SchedulesDataSource dataSource;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.dataSource = new SchedulesDataSource(getActivity());
		this.dataSource.open();
		
		Cursor cursor = this.dataSource.getAllSchedulesCursor();
		int[] toViews = new int[] {R.id.owner_entry, R.id.name_entry};
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), 
				R.layout.list_item_schedule, cursor, SchedulesDataSource.menuScheduleColumns, toViews, 0);
		
		ListView listView = new ListView(getActivity());
		listView.setAdapter(adapter);
		
		return listView;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.dataSource.close();
	}
}