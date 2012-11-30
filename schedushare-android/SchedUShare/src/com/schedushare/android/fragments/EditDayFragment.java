package com.schedushare.android.fragments;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.web.client.RestTemplate;

import roboguice.RoboGuice;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Session;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.EditScheduleActivity;
import com.schedushare.android.EditTimeBlockActivity;
import com.schedushare.android.R;
import com.schedushare.android.db.BlockTypeData;
import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.guice.GuiceConstants;
import com.schedushare.android.timeblock.task.UpdateScheduleDayTask;
import com.schedushare.android.util.EditDayArrayAdapter;
import com.schedushare.android.util.ResourceUriBuilder;
import com.schedushare.common.domain.dto.SchedushareExceptionEntity;
import com.schedushare.common.domain.dto.TimeBlockEntity;
import com.schedushare.common.domain.dto.TimeBlocksEntity;
import com.schedushare.common.domain.rest.RestResult;
import com.schedushare.common.domain.rest.RestResultHandler;
import com.schedushare.common.util.JSONUtil;

public class EditDayFragment extends Fragment {
	public ListView listView;
	private int day;
	private ScheduleData schedule;
	private ArrayList<TimeBlockData> timeBlocks;
	private HashMap<Long, BlockTypeData> blockTypes;
	
	@Inject
	private Provider<UpdateScheduleDayTask> updateScheduleDayTaskProvider;
	
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	SchedulesDataSource dataSource = new SchedulesDataSource(this.getActivity());
		dataSource.open();
		this.timeBlocks = dataSource.getScheduleDayTimeBlocks(this.schedule.id, this.day);
		dataSource.close();
    	
    	// Called on edit time block return.
    	if (requestCode == EditTimeBlockActivity.REQUEST_CODE) {
    		if (resultCode != Activity.RESULT_CANCELED) {
    			Collection<TimeBlockEntity> timeBlocksEntitites = new ArrayList<TimeBlockEntity>();
    			
    			Calendar t1 = Calendar.getInstance();
    			Calendar t2 = Calendar.getInstance();
    			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
    			
    			for (TimeBlockData timeBlock : this.timeBlocks) {
    				try {
						t1.setTime(timeFormat.parse(timeBlock.startTime));
						t2.setTime(timeFormat.parse(timeBlock.endTime));
						Time startTime = new Time(t1.getTime().getTime());
						Time endTime = new Time(t2.getTime().getTime());
						
	    				timeBlocksEntitites.add(new TimeBlockEntity ((int)timeBlock.sid,
	    						startTime, 
	    						endTime,
	    						timeBlock.getDayString(),
	    						timeBlock.name,
	    						timeBlock.getBlockTypeString(),
	    						timeBlock.latitude, 
	    						timeBlock.longitude,
	    						(int)schedule.sid));
					} catch (ParseException e) {
						e.printStackTrace();
					}
    			}
    			
    			TimeBlocksEntity timeBlocksEntity = new TimeBlocksEntity((int)this.schedule.sid, timeBlocksEntitites);
    			
    			try {
					UpdateScheduleDayTask updateScheduleDayTask = new UpdateScheduleDayTask(new RestTemplate(), new ResourceUriBuilder(GuiceConstants.HOST, GuiceConstants.HOST_PORT),
							GuiceConstants.SCHEDULE_TIME_BLOCKS_RESOURCE, 
							new RestResultHandler(new JSONUtil()));
					RestResult<TimeBlocksEntity> restResult = updateScheduleDayTask.execute(timeBlocksEntity)
																				   .get();
					if (restResult.isFailure()) {
						SchedushareExceptionEntity error = restResult.getError();
						
						Toast.makeText(getActivity(), "Update time blocks failed.", Toast.LENGTH_LONG).show();
					} else {
						TimeBlocksEntity timeBlocksEntities = restResult.getRestResult();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
    			
    		}
    	}
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
		this.listView.setOnItemClickListener(new OnItemClickListener() {  
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Session session = Session.getActiveSession();
				
				if (session != null && session.isOpened()) {
					// Start EditScheduleActivity with the selected schedule.
					Intent intent = new Intent(getActivity(), EditTimeBlockActivity.class);
					intent.putExtra("scheduleId", EditDayFragment.this.schedule.id);
					intent.putExtra("startTime", EditScheduleActivity.TIME_DATA[position]);
					intent.putExtra("day", EditDayFragment.this.day);
			        startActivityForResult(intent, EditTimeBlockActivity.REQUEST_CODE);
				} else {
					Toast.makeText(getActivity(), "You must log into Facebook!", Toast.LENGTH_LONG).show();
				}
			}  
		});
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