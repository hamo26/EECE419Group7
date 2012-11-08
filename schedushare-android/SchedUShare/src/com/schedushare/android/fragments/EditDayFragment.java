package com.schedushare.android.fragments;

import com.schedushare.android.R;
import com.schedushare.android.util.EditDayArrayAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EditDayFragment extends Fragment {
	public ListView listView;
	String[] timeData = {"6:00AM", "7:00AM", "8:00AM", "9:00AM",
			"10:00AM", "11:00AM", "12:00PM", "1:00PM",
			"2:00PM", "3:00PM", "4:00PM", "5:00PM",
			"6:00PM", "7:00PM", "8:00PM", "9:00PM",
			"10:00PM", "11:00PM", "12:00AM", "1:00AM",
			"2:00AM", "3:00AM", "4:00AM", "5:00AM"};
	
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
		
		
		EditDayArrayAdapter adapter = new EditDayArrayAdapter(getActivity(), R.layout.list_item_time_block, this.timeData);
		
		this.listView = new ListView(getActivity());
		this.listView.setAdapter(adapter);
	}
}