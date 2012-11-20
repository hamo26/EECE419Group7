package com.schedushare.android;

import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment.DeleteScheduleDialogListener;
import com.schedushare.android.fragments.EditDayFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment.RenameScheduleDialogListener;
import com.schedushare.android.fragments.SetActiveScheduleDialogFragment;
import com.schedushare.android.fragments.SetActiveScheduleDialogFragment.SetActiveScheduleDialogListener;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@ContentView(R.layout.activity_edit_schedule)
public class EditScheduleActivity extends RoboFragmentActivity 
		implements DeleteScheduleDialogListener, RenameScheduleDialogListener, SetActiveScheduleDialogListener {
	@InjectView(R.id.day_button_scroller) private LinearLayout dayButtonScroller;
	
	// Used for callbacks (e.g. passing information to EditTimeBlockActivity).
	public static final String[] TIME_DATA = {
			"0:00:00 AM", "0:30:00 AM", "1:00:00 AM", "1:30:00 AM",
			"2:00:00 AM", "2:30:00 AM", "3:00:00 AM", "3:30:00 AM", 
			"4:00:00 AM", "4:30:00 AM", "5:00:00 AM", "5:30:00 AM",
			"6:00:00 AM", "6:30:00 AM", "7:00:00 AM", "7:30:00 AM",
			"8:00:00 AM", "8:30:00 AM", "9:00:00 AM", "9:30:00 AM",
			"10:00:00 AM", "10:30:00 AM", "11:00:00 AM", "11:30:00 AM", 
			"12:00:00 PM", "12:30:00 PM", "1:00:00 PM", "1:30:00 PM",
			"2:00:00 PM", "2:30:00 PM", "3:00:00 PM", "3:30:00 PM", 
			"4:00:00 PM", "4:30:00 PM", "5:00:00 PM", "5:30:00 PM",
			"6:00:00 PM", "6:30:00 PM", "7:00:00 PM", "7:30:00 PM", 
			"8:00:00 PM", "8:30:00 PM", "9:00:00 PM", "9:30:00 PM",
			"10:00:00 PM", "10:30:00 PM", "11:00:00 PM", "11:30:00 PM"};
	
	public ScheduleData schedule;
	private EditDayFragment[] dayFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get bundle passed from last activity.
        Bundle extras = getIntent().getExtras();        
        if (extras != null) {
        	SchedulesDataSource dataSource = new SchedulesDataSource(this);
        	dataSource.open();
        	this.schedule = dataSource.getScheduleFromId(extras.getLong("scheduleId"));
        	dataSource.close();
        }
        	
        // Create all fragments.
        this.dayFragments = new EditDayFragment[7];
        for (int i = 0; i < 7; i++) {
        	this.dayFragments[i] = new EditDayFragment();
        	Bundle args = new Bundle();
        	args.putInt("day", i);
        	args.putLong("scheduleId", this.schedule.id);
        	this.dayFragments[i].setArguments(args);
        }
        
        // Get an instance of FragmentTransaction from your Activity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Put Monday in view.
        fragmentTransaction.add(R.id.day_schedule_container, this.dayFragments[0]);
        fragmentTransaction.commit();
        
        // Create scroller to switch between days of the week.
        this.dayButtonScroller = (LinearLayout)findViewById(R.id.day_button_scroller);
        OnClickListener dayButtonClickListener = new OnClickListener() {
        	@Override
			public void onClick(View view) {
        		Toast.makeText(EditScheduleActivity.this,
        				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();
        		
        		FragmentManager fragmentManager = getSupportFragmentManager();
        		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        		fragmentTransaction.replace(R.id.day_schedule_container, EditScheduleActivity.this.dayFragments[view.getId()]);
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
        
        this.setTitle(this.schedule.name);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create menu with new schedule button.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_schedule, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.delete_schedule_option:
				// Open dialog box to delete schedule.
				DialogFragment newFragment = new DeleteScheduleDialogFragment();
			    newFragment.show(getSupportFragmentManager(), "delete_schedule");
			    break;
			case R.id.rename_schedule_option:
				// Open dialog box to delete schedule.
				DialogFragment newFragment2 = new RenameScheduleDialogFragment();
			    newFragment2.show(getSupportFragmentManager(), "rename_schedule");
			case R.id.set_active_schedule_option:
				// Open dialog box to set active schedule.
				DialogFragment newFragment3 = new SetActiveScheduleDialogFragment();
			    newFragment3.show(getSupportFragmentManager(), "set_active_schedule");
			default:
				break;
		}
	
		return true;
	}

	@Override
	public void onDeleteScheduleDialogPositiveClick(DialogFragment dialog) {
		// For Hamid:
		// Delete current schedule from back end using schedule.sid (server id).
		
		
		// Delete current schedule and inform schedule menu to update list.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
		dataSource.open();
		dataSource.deleteSchedule(this.schedule.id);
		dataSource.close();
		
	    finish();
	}

	@Override
	public void onDeleteScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}

	@Override
	public void onRenameScheduleDialogPositiveClick(DialogFragment dialog, String newName) {
		// For Hamid:
		// Rename schedule in back end using schedule.sid (server id) and newName.
		
		
		// Rename current schedule in local db and change title.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
    	dataSource.open();
    	this.schedule.name = newName;
    	dataSource.updateSchedule(this.schedule);
    	dataSource.close();
    	
    	this.setTitle(this.schedule.name);
	}

	@Override
	public void onRenameScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}

	@Override
	public void onSetActiveScheduleDialogPositiveClick(DialogFragment dialog) {
		Toast.makeText(this, "Set active", Toast.LENGTH_LONG).show();
		
		SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		
		// Update local database.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
		dataSource.open();
		ScheduleData lastActiveSchedule = dataSource.getActiveScheduleFromOwnerId(
				p.getLong(getString(R.string.settings_owner_id), 1));
		this.schedule.active = true;
		lastActiveSchedule.active = false;
		
		// For Hamid:
		// Update back end with both this.schedule and lastActiveSchedule.
		
		dataSource.updateSchedule(lastActiveSchedule);
		dataSource.updateSchedule(this.schedule);
		dataSource.close();
		
		// Update preferences.
		SharedPreferences.Editor editor = p.edit();
		editor.putLong(getString(R.string.settings_owner_active_schedule_id), this.schedule.id);
		editor.commit();
	}

	@Override
	public void onSetActiveScheduleDialogNegativeClick(DialogFragment dialog) {
		
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
