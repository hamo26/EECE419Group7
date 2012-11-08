package com.schedushare.android;

import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.CheckableScheduleListFragment;
import com.schedushare.android.fragments.NewScheduleDialogFragment;
import com.schedushare.android.fragments.NewScheduleDialogFragment.NoticeDialogListener;
import com.schedushare.android.fragments.ScheduleListFragment;
import com.schedushare.android.util.TabListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.ContentView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

@ContentView(R.layout.activity_schedules_menu)
public class SchedulesMenuActivity extends RoboFragmentActivity implements NoticeDialogListener {
	// List views connected to the database for both user and friend schedules.
	private TabListener<ScheduleListFragment> userSchedulesTabListener;
	private TabListener<CheckableScheduleListFragment> friendSchedulesTabListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    // Only create action bar if the fragment container exists in view.
	    if (findViewById(R.id.schedule_list_container) != null) {
	    	// Create the tab listeners which will facilitate user clicking on the tabs.
			this.userSchedulesTabListener = new TabListener<ScheduleListFragment>(
					this, "user_schedules", ScheduleListFragment.class);
			this.friendSchedulesTabListener = new TabListener<CheckableScheduleListFragment>(
					this, "friend_schedules", CheckableScheduleListFragment.class);
			
			// Set action bar to navigation mode and add the user and friend tabs.
			ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			
			Tab tab = actionBar.newTab()
					.setText(R.string.tab_user_schedules)
					.setTabListener(this.userSchedulesTabListener);
			actionBar.addTab(tab);
			
			tab = actionBar.newTab()
					.setText(R.string.tab_friend_schedules)
					.setTabListener(this.friendSchedulesTabListener);
			actionBar.addTab(tab);
	    }
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
		// Get new cursor from database.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
		dataSource.open();
		Cursor cursor = dataSource.getAllSchedulesCursor();
		dataSource.close();
		
		// Swap existing cursor with new one.
		((SimpleCursorAdapter)((ScheduleListFragment)this.userSchedulesTabListener.fragment)
				.listView.getAdapter()).changeCursor(cursor);
    }
	
	// Called when user clicks cancel in new schedule dialog box. 
	public void onNewScheduleDialogNegativeClick(RoboDialogFragment dialog) {
		
	}
}
