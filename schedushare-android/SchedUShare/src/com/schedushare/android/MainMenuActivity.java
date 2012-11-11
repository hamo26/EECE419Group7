package com.schedushare.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.schedule.task.GetSchedulesTask;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.ScheduleListEntity;
import com.schedushare.common.domain.rest.RestResult;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

@ContentView(R.layout.activity_main_menu)
public class MainMenuActivity extends RoboActivity {

    @Inject
    Provider<GetSchedulesTask> getGetSchedulesTaskProvider;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // For testing only.
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
			
        	
	        createTestData();
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
    
    // Creates test data.
    private void createTestData() {
    	SchedulesDataSource dataSource = new SchedulesDataSource(this);
        dataSource.open();
        dataSource.dropAllTables();
        dataSource.createUser(1, "oscarlee");
        for (int i = 0; i < 100; i++) {
        	dataSource.createSchedule(i, " schedule" + i + " ", true, 1, "whatever");
        }
        
        Calendar dateTime = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        dateTime.set(Calendar.HOUR, 10);
        dateTime.set(Calendar.MINUTE, 0);
        dateTime.set(Calendar.SECOND, 0);
        
        String startTime;
        String endTime;
		startTime = sdf.format(dateTime.getTime());
		dateTime.set(Calendar.HOUR, 15);
		endTime = sdf.format(dateTime.getTime());        
        
        dataSource.createBlockType(1, "School");
        dataSource.createTimeBlock(1, startTime.toString(), endTime.toString(), 1, 1, 1, 5.55, 5.55);
        dataSource.close();
    }
}
