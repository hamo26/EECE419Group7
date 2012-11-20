package com.schedushare.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.BlockTypeData;
import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.util.EditDayArrayAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ViewDayFragment extends Fragment {
	public ListView listView;
	private int day;
	private ScheduleData schedule;
	private ArrayList<TimeBlockData> timeBlocks;
	private HashMap<Long, BlockTypeData> blockTypes;
	
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
			this.day = getArguments().getInt("day");
			SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
			dataSource.open();
			this.schedule = dataSource.getScheduleFromId(getArguments().getLong("scheduleId"));
			dataSource.close();
			setListViewAdapter();
		}
		
		return this.listView;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshListView();
	}
	
	private void setListViewAdapter() {
		// Get all time blocks of the current schedule and day of the week.
		// As well, get hash of all block types.
		SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
		dataSource.open();
		this.timeBlocks = dataSource.getScheduleDayTimeBlocks(this.schedule.id, this.day);
		this.blockTypes = dataSource.getAllBlockTypes();
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, EditScheduleActivity.TIME_DATA, this.timeBlocks, this.blockTypes);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
	
	public void refreshListView() {
		System.out.println("Recreate edit day list view.");
		
		// Create new adapter with new time blocks, attach to listView, and refresh screen.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		this.timeBlocks = dataSource.getScheduleDayTimeBlocks(this.schedule.id, this.day);
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, EditScheduleActivity.TIME_DATA, this.timeBlocks, this.blockTypes);
		this.listView.setAdapter(adapter);
		
		((EditDayArrayAdapter)this.listView.getAdapter()).notifyDataSetChanged();
	}
}