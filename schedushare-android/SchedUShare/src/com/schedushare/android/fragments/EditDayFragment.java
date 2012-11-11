package com.schedushare.android.fragments;

import java.util.ArrayList;

import com.schedushare.android.R;
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
	
	String[] timeData = {"6:00:00", "7:00:00", "8:00:00", "9:00:00",
			"10:00:00", "11:00:00", "12:00:00", "13:00:00",
			"14:00:00", "15:00:00", "16:00:00", "17:00:00",
			"18:00:00", "19:00:00", "20:00:00", "21:00:00",
			"22:00:00", "23:00:00", "0:00:00", "1:00:00",
			"2:00:00", "3:00:00", "4:00:00", "5:00:00"};
	
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
		SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
		dataSource.open();
		ArrayList<TimeBlockData> timeBlocks = dataSource.getSchdeduleTimeBlocks(1);
		dataSource.close();
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(), R.layout.list_item_time_block, this.timeData, timeBlocks);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
}