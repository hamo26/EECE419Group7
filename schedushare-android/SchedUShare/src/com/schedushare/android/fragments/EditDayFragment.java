package com.schedushare.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.EditTimeBlockActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.BlockTypeData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.util.EditDayArrayAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class EditDayFragment extends Fragment {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	// Called on edit time block return.
    	if (requestCode == 0) {
    		if (resultCode != Activity.RESULT_CANCELED) {
    			System.out.println("Recreate edit day list view.");
    			
    			// Create new adapter with new time blocks, attach to listView, and refresh screen.
    			SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
    			dataSource.open();
    			this.timeBlocks = dataSource.getSchdeduleDayTimeBlocks(((EditScheduleActivity)this.getActivity()).scheduleId, this.day);
    			dataSource.close();
    			
    			EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
    					R.layout.list_item_time_block, EditScheduleActivity.timeData, this.timeBlocks, this.blockTypes);
    			this.listView.setAdapter(adapter);
    			
    			((EditDayArrayAdapter)this.listView.getAdapter()).notifyDataSetChanged();
    		}
    	}
    		
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
		this.timeBlocks = dataSource.getSchdeduleDayTimeBlocks(((EditScheduleActivity)this.getActivity()).scheduleId, this.day);
		this.blockTypes = dataSource.getAllBlockTypes();
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, EditScheduleActivity.timeData, this.timeBlocks, this.blockTypes);
		
		this.listView = new ListView(getActivity());
		this.listView.setOnItemClickListener(new OnItemClickListener() {  
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Start EditScheduleActivity with the selected schedule.
				Intent intent = new Intent(getActivity(), EditTimeBlockActivity.class);
				intent.putExtra("scheduleId", ((EditScheduleActivity)EditDayFragment.this.getActivity()).scheduleId);
				intent.putExtra("startTime", EditScheduleActivity.timeData[position]);
				intent.putExtra("day", EditDayFragment.this.day);
		        startActivityForResult(intent, 0);
			}  
		});
		this.listView.setAdapter(adapter);
	}
}