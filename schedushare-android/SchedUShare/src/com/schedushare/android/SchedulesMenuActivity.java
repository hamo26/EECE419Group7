package com.schedushare.android;

import com.schedushare.android.fragments.NewScheduleDialogFragment;
import com.schedushare.android.fragments.ScheduleListFragment;
import com.schedushare.android.util.TabListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.ContentView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@ContentView(R.layout.activity_schedules_menu)
public class SchedulesMenuActivity extends RoboFragmentActivity {
	private TabListener<ScheduleListFragment> userSchedulesTabListener;
	private TabListener<ScheduleListFragment> friendSchedulesTabListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (findViewById(R.id.schedule_list_container) != null) {
        	this.userSchedulesTabListener = new TabListener<ScheduleListFragment>(
    	            this, "user_schedules", ScheduleListFragment.class);
    		this.friendSchedulesTabListener = new TabListener<ScheduleListFragment>(
    	            this, "friend_schedules", ScheduleListFragment.class);
    		
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
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.schedules_menu, menu);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.create_schedule:
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
}
