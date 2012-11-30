package com.schedushare.android;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.facebook.Session;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.NewScheduleDialogFragment;
import com.schedushare.android.fragments.NewScheduleDialogFragment.CreateScheduleDialogListener;
import com.schedushare.android.fragments.ScheduleListFragment;
import com.schedushare.android.schedule.task.CreateScheduleTask;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.rest.RestResult;

import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.ContentView;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

@ContentView(R.layout.activity_schedules_menu)
public class SchedulesMenuActivity extends RoboFragmentActivity implements CreateScheduleDialogListener {
	// List views connected to the database for both user and friend schedules.
	private ScheduleListFragment scheduleListFragment;
	
	@Inject Provider<CreateScheduleTask> getCreateScheduleTaskProvider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Only create action bar if the fragment container exists in view.
	    if (findViewById(R.id.schedule_list_container) != null) {
	    	// Create the tab listeners which will facilitate user clicking on the tabs.
			this.scheduleListFragment = new ScheduleListFragment();
			
			FragmentManager fragmentManager = getSupportFragmentManager();
	        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	        // Put Monday in view.
	        fragmentTransaction.add(R.id.schedule_list_container, this.scheduleListFragment);
	        fragmentTransaction.commit();
	    }
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		
		this.scheduleListFragment.swapCursor();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create menu with new schedule button.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.schedules_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Session session = Session.getActiveSession();
		
		switch (item.getItemId()) {
			case R.id.create_schedule_option:
				if (session != null && session.isOpened()) {
					// Open dialog box to create new schedule.
					RoboDialogFragment newFragment = new NewScheduleDialogFragment();
				    newFragment.show(getSupportFragmentManager(), "new_schedule");
				} else {
					Toast.makeText(this, "You must log into Facebook.", Toast.LENGTH_LONG).show();
				}
				
			    break;
			default:
				break;
	  }
	
	  return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
    
	// Called when user clicks create in new schedule dialog box.
	public void onNewScheduleDialogPositiveClick(RoboDialogFragment dialog, String newScheduleName) {
		// Check whether schedule name already exists.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
		dataSource.open();
		if (dataSource.getScheduleFromName(newScheduleName) != null) {
			Toast.makeText(this, "Schedule Name already exist!\nPlease enter a different name.", Toast.LENGTH_LONG).show();
			dataSource.close();
			return;
		}
		
		try {
    		SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
    		long facebookId = p.getLong(getString(R.string.settings_owner_facebook_id), -1);
    		ScheduleEntity scheduleEntity = new ScheduleEntity(0, newScheduleName, false, Long.toString(facebookId), null);
			RestResult<ScheduleEntity> createScheduleResult = getCreateScheduleTaskProvider.get().execute(scheduleEntity).get();
			
			if (createScheduleResult.isFailure()) {
				Toast.makeText(this, "Create failed.", Toast.LENGTH_LONG).show();
				
				System.out.println("NewScheduleDialog: " + createScheduleResult.getError().getException());
			} else {
				Toast.makeText(this, newScheduleName + " created.", Toast.LENGTH_LONG).show();
				
				// Get current time.
				Calendar currentTime = Calendar.getInstance();
				
				// Open connection to db and create new schedule.
		    	dataSource.open();
		    	dataSource.createSchedule(createScheduleResult.getRestResult().getScheduleId(),
		    			createScheduleResult.getRestResult().getScheduleName(), false,
		    			p.getLong(getString(R.string.settings_owner_id), -1),
		    			currentTime.getTime().toString());
		    	dataSource.close();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		this.scheduleListFragment.swapCursor();
    }
	
	// Called when user clicks cancel in new schedule dialog box. 
	public void onNewScheduleDialogNegativeClick(RoboDialogFragment dialog) {
		
	}
}
