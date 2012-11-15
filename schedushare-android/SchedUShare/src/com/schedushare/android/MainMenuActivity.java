package com.schedushare.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.EditDayFragment;
import com.schedushare.android.schedule.task.GetSchedulesTask;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.rest.RestResult;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@ContentView(R.layout.activity_main_menu)
public class MainMenuActivity extends RoboActivity {

    @Inject Provider<GetSchedulesTask> getGetSchedulesTaskProvider;
    
    @InjectView(R.id.active_schedule_day_button_scroller) private LinearLayout dayButtonScroller;

    public long activeScheduleId;
    private EditDayFragment[] dayFragments;
    private int lastViewedDay;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
        	
//        	RestResult<ScheduleListEntity> getSchedulesResult;
//			try {
//				getSchedulesResult = getGetSchedulesTaskProvider.get().execute("test@email.com").get();
//				
//				SchedulesDataSource dataSource = new SchedulesDataSource(this);
//				dataSource.open();
//				
//				if (getSchedulesResult.isFailure()) {
//					Toast.makeText(this, getSchedulesResult.getError().getException(), Toast.LENGTH_LONG).show();
//				} else {
//					for (ScheduleEntity schedule : getSchedulesResult.getRestResult().getScheduleList()) {
//						dataSource.createSchedule(schedule.getScheduleId(), schedule.getScheduleName(), false, 1, "whatever");
//					}
//				}
//				
//				dataSource.close();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
        	startLocationManager();
	        createTestData();
	        initializeCurrentScheduleLayout();
        }
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
    	
    	// Refresh list view.
    	FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        this.dayFragments[this.lastViewedDay] = new EditDayFragment();
        Bundle args = new Bundle();
        args.putInt("day", this.lastViewedDay);
        args.putLong("scheduleId", this.activeScheduleId);
        this.dayFragments[this.lastViewedDay].setArguments(args);
    	fragmentTransaction.replace(R.id.active_schedule_container, this.dayFragments[this.lastViewedDay]);
		fragmentTransaction.commit();
    }
    
    // Called when user clicks schedules button.
    public void startSchedulesMenu(View view) {
    	Intent intent = new Intent(this, SchedulesMenuActivity.class);
        startActivity(intent);
    }
    
    // Called when user clicks bump button.
    public void startBump(View view) {
    	
    }
    
    // Called when user clicks settings button.
    public void startSettingsMenu(View view) {
    	
    }
    
    // Creates test data.
    private void createTestData() {
    	Calendar dateTime = Calendar.getInstance();
    	
    	SchedulesDataSource dataSource = new SchedulesDataSource(this);
        dataSource.open();
        dataSource.dropAllTables();
        dataSource.createUser(1, "oscarlee");
        for (int i = 0; i < 100; i++) {
        	dataSource.createSchedule(i, "schedule" + i, true, 1, dateTime.getTime().toString());
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        dateTime.set(Calendar.HOUR, 10);
        dateTime.set(Calendar.MINUTE, 0);
        dateTime.set(Calendar.SECOND, 0);
        dateTime.set(Calendar.AM_PM, Calendar.AM);
        
        String startTime;
        String endTime;
		startTime = sdf.format(dateTime.getTime());
		dateTime.set(Calendar.HOUR, 15);
		endTime = sdf.format(dateTime.getTime());
		
		System.out.println("MainMenu: start: " + startTime.toString() + 
				" end: " + endTime.toString());
        
        dataSource.createBlockType(1, "School");
        dataSource.createBlockType(2, "Work");
        dataSource.createBlockType(3, "Leisure");
        dataSource.createTimeBlock(1, "EECE 419", startTime.toString(), endTime.toString(), 1, 1, 1, 5.55, 5.55);
        dataSource.close();
    }
    
    // Start location listener.
    private void startLocationManager() {
    	LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	String locationProvider = LocationManager.NETWORK_PROVIDER;
    	LocationListener locationListener = new LocationListener() {
    		@Override
    		public void onLocationChanged(Location location) {}

			@Override
			public void onProviderDisabled(String arg0) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
    	};
    	//locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }
    
    private void initializeCurrentScheduleLayout() {
    	this.activeScheduleId = 1;
        
        // Create all fragments.
    	this.dayFragments = new EditDayFragment[7];
        for (int i = 0; i < 7; i++) {
        	this.dayFragments[i] = new EditDayFragment();
        	Bundle args = new Bundle();
        	args.putInt("day", i);
        	args.putLong("scheduleId", this.activeScheduleId);
        	this.dayFragments[i].setArguments(args);
        }
        
        // Get an instance of FragmentTransaction from your Activity.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Put Monday in view.
        fragmentTransaction.add(R.id.active_schedule_container, this.dayFragments[0]);
        this.lastViewedDay = 0;
        fragmentTransaction.commit();
        
        // Create scroller to switch between days of the week.
        this.dayButtonScroller = (LinearLayout)findViewById(R.id.active_schedule_day_button_scroller);
        OnClickListener dayButtonClickListener = new OnClickListener() {
        	@Override
			public void onClick(View view) {
        		Toast.makeText(MainMenuActivity.this,
        				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();
        		
        		FragmentManager fragmentManager = getFragmentManager();
        		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        		fragmentTransaction.replace(R.id.active_schedule_container, MainMenuActivity.this.dayFragments[view.getId()]);
        		MainMenuActivity.this.lastViewedDay = view.getId();
        		fragmentTransaction.commit();
			}
        };
        
        // Create all 7 buttons.
        createDayButton(dayButtonClickListener, 0, "Mon", 150, 75);
        createDayButton(dayButtonClickListener, 1, "Tue", 150, 75);
        createDayButton(dayButtonClickListener, 2, "Wed", 150, 75);
        createDayButton(dayButtonClickListener, 3, "Thu", 150, 75);
        createDayButton(dayButtonClickListener, 4, "Fri", 150, 75);
        createDayButton(dayButtonClickListener, 5, "Sat", 150, 75);
        createDayButton(dayButtonClickListener, 6, "Sun", 150, 75);
    }
    
    private void createDayButton(OnClickListener listener, int id, String text, int width, int height) {    	
    	Button dayButton = new Button(this);
        dayButton.setId(id);
        dayButton.setOnClickListener(listener);
        dayButton.setText(text);
        dayButton.setLayoutParams(new LayoutParams(width, height));
        
        this.dayButtonScroller.addView(dayButton);
    }
}
