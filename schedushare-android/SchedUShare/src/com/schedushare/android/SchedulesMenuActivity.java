package com.schedushare.android;

import com.schedushare.android.fragments.ScheduleListFragment;
import com.schedushare.android.util.TabListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

@ContentView(R.layout.activity_schedules_menu)
public class SchedulesMenuActivity extends RoboFragmentActivity {	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (findViewById(R.id.schedule_list_container) != null) {
        	ActionBar actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            
            Tab tab = actionBar.newTab()
                    .setText(R.string.tab_user_schedules)
                    .setTabListener(new TabListener<ScheduleListFragment>(
                            this, "user_schedules", ScheduleListFragment.class));
            actionBar.addTab(tab);

            tab = actionBar.newTab()
                .setText(R.string.tab_friend_schedules)
                .setTabListener(new TabListener<ScheduleListFragment>(
                        this, "friend_schedules", ScheduleListFragment.class));
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
    			Toast.makeText(this, "Create new schedule.", Toast.LENGTH_SHORT).show();
    		default:
    			break;
      }

      return true;
    }
}
