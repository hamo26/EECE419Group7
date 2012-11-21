package com.schedushare.android;

import com.schedushare.android.fragments.NewScheduleDialogFragment;
import com.schedushare.android.fragments.NewScheduleDialogFragment.CreateScheduleDialogListener;
import com.schedushare.android.fragments.ScheduleListFragment;

import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.ContentView;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@ContentView(R.layout.activity_schedules_menu)
public class SchedulesMenuActivity extends RoboFragmentActivity implements CreateScheduleDialogListener {
	// List views connected to the database for both user and friend schedules.
	private ScheduleListFragment scheduleListFragment;
	
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
		switch (item.getItemId()) {
			case R.id.create_schedule_option:
				// Open dialog box to create new schedule.
				RoboDialogFragment newFragment = new NewScheduleDialogFragment();
			    newFragment.show(getSupportFragmentManager(), "new_schedule");
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
	public void onNewScheduleDialogPositiveClick(RoboDialogFragment dialog) {
		
    }
	
	// Called when user clicks cancel in new schedule dialog box. 
	public void onNewScheduleDialogNegativeClick(RoboDialogFragment dialog) {
		
	}
}
