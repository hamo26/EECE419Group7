package com.schedushare.android;

import com.schedushare.android.db.SchedulesDataSource;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

@ContentView(R.layout.activity_main_menu)
public class MainMenuActivity extends RoboActivity {
	@InjectView(R.id.output_text) private TextView outputTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the message from the intent.
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        // Create the text view
        this.outputTextView.setTextSize(30);
        this.outputTextView.setText(message);
        
        SchedulesDataSource dataSource = new SchedulesDataSource(this);
        dataSource.open();
        dataSource.dropAllTables();
        for (int i = 0; i < 100; i++) {
        	dataSource.createSchedule(i, " Schedule Name ");
        }
        dataSource.close();
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
