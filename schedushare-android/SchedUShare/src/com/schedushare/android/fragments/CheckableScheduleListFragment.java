package com.schedushare.android.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.schedushare.android.MainMenuActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.util.CheckboxScheduleCursorAdapter;

public class CheckableScheduleListFragment extends ScheduleListFragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		// Only create cursor and adapter the first time fragment is created.
		if (savedInstanceState == null) {
			setListViewAdapter();
		}
		
		return this.listView;
    }
	
	private void setListViewAdapter() {
		SharedPreferences p = getActivity().getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		Cursor cursor = dataSource.getAllOwnerSchedulesCursor(p.getLong(getString(R.string.settings_owner_id), 1));
		dataSource.close();
		
		// Create new adapter and attach to listView.
		int[] toViews = new int[] {R.id.checkable_schedule_list_owner_entry, R.id.checkable_schedule_list_name_entry};
		CheckboxScheduleCursorAdapter adapter = new CheckboxScheduleCursorAdapter(getActivity(), 
				R.layout.list_item_checkable_schedule, cursor, SchedulesDataSource.menuScheduleColumns, toViews, 0);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
}