package com.schedushare.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.schedushare.android.EditScheduleActivity;
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

public class EditDayFragment extends Fragment {
	public ListView listView;
	private int day;
	
	String[] timeData = {"6:00", "7:00", "8:00", "9:00",
			"10:00", "11:00", "12:00", "13:00",
			"14:00", "15:00", "16:00", "17:00",
			"18:00", "19:00", "20:00", "21:00",
			"22:00", "23:00", "0:00", "1:00",
			"2:00", "3:00", "4:00", "5:00"};
	
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
		SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
		dataSource.open();
		ArrayList<TimeBlockData> timeBlocks = 
				dataSource.getSchdeduleDayTimeBlocks(((EditScheduleActivity)this.getActivity()).scheduleId,
				this.day);
		HashMap<Long, BlockTypeData> blockTypes = dataSource.getAllBlockTypes();
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(),
				R.layout.list_item_time_block, this.timeData, timeBlocks, blockTypes);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
}