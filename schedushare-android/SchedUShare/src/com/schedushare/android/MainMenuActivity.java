package com.schedushare.android;

import com.schedushare.android.db.SchedulesDataSource;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

@ContentView(R.layout.activity_main_menu)
public class MainMenuActivity extends RoboActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState == null) {
	        SchedulesDataSource dataSource = new SchedulesDataSource(this);
	        dataSource.open();
	        dataSource.dropAllTables();
	        dataSource.createUser(1, "oscarlee");
	        for (int i = 0; i < 100; i++) {
	        	dataSource.createSchedule(i, " schedule" + i + " ", true, 1, "whatever");
	        }
	        dataSource.close();
        }
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
}
