package com.schedushare.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.MainMenuActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.BlockTypeData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.util.EditDayArrayAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ViewDayFragment extends Fragment {
	public ListView listView;
	private int day;
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
			setListViewAdapter();
		}
		
		return this.listView;
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void setListViewAdapter() {
		// Get all time blocks of the current schedule and day of the week.
		// As well, get hash of all block types.
		SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
		dataSource.open();
		this.timeBlocks = dataSource.getSchdeduleDayTimeBlocks(((MainMenuActivity)this.getActivity()).activeScheduleId, this.day);
		this.blockTypes = dataSource.getAllBlockTypes();
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, EditScheduleActivity.timeData, this.timeBlocks, this.blockTypes);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
	
	public void refreshListView() {
		System.out.println("Recreate edit day list view.");
		
		// Create new adapter with new time blocks, attach to listView, and refresh screen.
		SchedulesDataSource dataSource = new SchedulesDataSource(getActivity());
		dataSource.open();
		this.timeBlocks = dataSource.getSchdeduleDayTimeBlocks(((MainMenuActivity)getActivity()).activeScheduleId, this.day);
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, EditScheduleActivity.timeData, this.timeBlocks, this.blockTypes);
		this.listView.setAdapter(adapter);
		
		((EditDayArrayAdapter)this.listView.getAdapter()).notifyDataSetChanged();
	}
}