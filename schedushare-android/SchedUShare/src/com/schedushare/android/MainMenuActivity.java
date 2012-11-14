package com.schedushare.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.fragments.EditDayFragment;
import com.schedushare.android.fragments.ViewDayFragment;
import com.schedushare.android.schedule.task.GetSchedulesTask;
import com.schedushare.android.util.EditDayArrayAdapter;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.rest.RestResult;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
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

    @Inject
    Provider<GetSchedulesTask> getGetSchedulesTaskProvider;

    public long activeScheduleId;
	private ViewDayFragment mondayFragment;
	private ViewDayFragment tuesdayFragment;
	private ViewDayFragment wednesdayFragment;
	private ViewDayFragment thursdayFragment;
	private ViewDayFragment fridayFragment;
	private ViewDayFragment saturdayFragment;
	private ViewDayFragment sundayFragment;
    
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
        	dataSource.createSchedule(i, " schedule" + i + " ", true, 1, dateTime.getTime().toString());
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
    	locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
    }
    
    private void initializeCurrentScheduleLayout() {
    	this.activeScheduleId = 1;
        
        // Create all fragments.
        this.mondayFragment = new ViewDayFragment();
        this.tuesdayFragment = new ViewDayFragment();
        this.wednesdayFragment = new ViewDayFragment();
        this.thursdayFragment = new ViewDayFragment();
        this.fridayFragment = new ViewDayFragment();
        this.saturdayFragment = new ViewDayFragment();
        this.sundayFragment = new ViewDayFragment();
        
        // Initialize days.
        Bundle args = new Bundle();
        args.putInt("day", 1);
        this.mondayFragment.setArguments(args);
        Bundle args2 = new Bundle();
        args2.putInt("day", 2);
        this.tuesdayFragment.setArguments(args2);
        Bundle args3 = new Bundle();
        args3.putInt("day", 3);
        this.wednesdayFragment.setArguments(args3);
        Bundle args4 = new Bundle();
        args4.putInt("day", 4);
        this.thursdayFragment.setArguments(args4);
        Bundle args5 = new Bundle();
        args5.putInt("day", 5);
        this.fridayFragment.setArguments(args5);
        Bundle args6 = new Bundle();
        args6.putInt("day", 6);
        this.saturdayFragment.setArguments(args6);
        Bundle args7 = new Bundle();
        args7.putInt("day", 7);
        this.sundayFragment.setArguments(args7);
        
        // Get an instance of FragmentTransaction from your Activity.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Put Monday in view.
        fragmentTransaction.add(R.id.active_schedule_container, this.mondayFragment);
        fragmentTransaction.commit();
        
        // Create scroller to switch between days of the week.
        LinearLayout dayButtonScroller = (LinearLayout)findViewById(R.id.active_schedule_day_button_scroller);
        OnClickListener dayButtonClickListener = new OnClickListener() {
        	@Override
			public void onClick(View view) {
        		Toast.makeText(MainMenuActivity.this,
        				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();
        		
        		FragmentManager fragmentManager = getFragmentManager();
        		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        		ViewDayFragment f;
        		switch (view.getId()) {
        			case 1:
        				f = MainMenuActivity.this.mondayFragment;
        				break;
        			case 2:
        				f = MainMenuActivity.this.tuesdayFragment;
        				break;
        			case 3:
        				f = MainMenuActivity.this.wednesdayFragment;
        				break;
        			case 4:
        				f = MainMenuActivity.this.thursdayFragment;
        				break;
        			case 5:
        				f = MainMenuActivity.this.fridayFragment;
        				break;
        			case 6:
        				f = MainMenuActivity.this.saturdayFragment;
        				break;
        			case 7:
        				f = MainMenuActivity.this.sundayFragment;
        				break;
        			default:
        				f = MainMenuActivity.this.mondayFragment;
        		}
        		fragmentTransaction.replace(R.id.active_schedule_container, f);
        		fragmentTransaction.commit();
			}
        };
        
        // Create all 7 buttons.
        Button monButton = new Button(this);
        monButton.setId(1);
        monButton.setOnClickListener(dayButtonClickListener);
        monButton.setText("Mon");
        monButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button tueButton = new Button(this);
        tueButton.setId(2);
        tueButton.setOnClickListener(dayButtonClickListener);
        tueButton.setText("Tue");
        tueButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button wedButton = new Button(this);
        wedButton.setId(3);
        wedButton.setOnClickListener(dayButtonClickListener);
        wedButton.setText("Wed");
        wedButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button thuButton = new Button(this);
        thuButton.setId(4);
        thuButton.setOnClickListener(dayButtonClickListener);
        thuButton.setText("Thu");
        thuButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button friButton = new Button(this);
        friButton.setId(5);
        friButton.setOnClickListener(dayButtonClickListener);
        friButton.setText("Fri");
        friButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button satButton = new Button(this);
        satButton.setId(6);
        satButton.setOnClickListener(dayButtonClickListener);
        satButton.setText("Sat");
        satButton.setLayoutParams(new LayoutParams(150, 75));
        
        Button sunButton = new Button(this);
        sunButton.setId(7);
        sunButton.setOnClickListener(dayButtonClickListener);
        sunButton.setText("Sun");
        sunButton.setLayoutParams(new LayoutParams(150, 75));
        
        // Add all buttons to the scroller.
        dayButtonScroller.addView(monButton);
        dayButtonScroller.addView(tueButton);
        dayButtonScroller.addView(wedButton);
        dayButtonScroller.addView(thuButton);
        dayButtonScroller.addView(friButton);
        dayButtonScroller.addView(satButton);
        dayButtonScroller.addView(sunButton);
    }
}
