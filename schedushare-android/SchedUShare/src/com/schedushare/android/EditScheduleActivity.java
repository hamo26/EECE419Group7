package com.schedushare.android;

import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment.DeleteScheduleDialogListener;
import com.schedushare.android.fragments.EditDayFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment.RenameScheduleDialogListener;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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
public class EditScheduleActivity extends RoboActivity implements DeleteScheduleDialogListener, RenameScheduleDialogListener {
	// Used for callbacks (e.g. passing information to EditTimeBlockActivity).
	public static final String[] timeData = {
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
	
	public long scheduleId;
	private EditDayFragment mondayFragment;
	private EditDayFragment tuesdayFragment;
	private EditDayFragment wednesdayFragment;
	private EditDayFragment thursdayFragment;
	private EditDayFragment fridayFragment;
	private EditDayFragment saturdayFragment;
	private EditDayFragment sundayFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get bundle passed from last activity.
        Bundle extras = getIntent().getExtras();        
        if (extras != null)
        	this.scheduleId = extras.getLong("scheduleId");
        
        // Create all fragments.
        this.mondayFragment = new EditDayFragment();
        this.tuesdayFragment = new EditDayFragment();
        this.wednesdayFragment = new EditDayFragment();
        this.thursdayFragment = new EditDayFragment();
        this.fridayFragment = new EditDayFragment();
        this.saturdayFragment = new EditDayFragment();
        this.sundayFragment = new EditDayFragment();
        
        // Initialize days.
        Bundle args = new Bundle();
        args.putInt("day", 1);
        args.putLong("scheduleId", this.scheduleId);
        this.mondayFragment.setArguments(args);
        Bundle args2 = new Bundle();
        args2.putInt("day", 2);
        args2.putLong("scheduleId", this.scheduleId);
        this.tuesdayFragment.setArguments(args2);
        Bundle args3 = new Bundle();
        args3.putInt("day", 3);
        args3.putLong("scheduleId", this.scheduleId);
        this.wednesdayFragment.setArguments(args3);
        Bundle args4 = new Bundle();
        args4.putInt("day", 4);
        args4.putLong("scheduleId", this.scheduleId);
        this.thursdayFragment.setArguments(args4);
        Bundle args5 = new Bundle();
        args5.putInt("day", 5);
        args5.putLong("scheduleId", this.scheduleId);
        this.fridayFragment.setArguments(args5);
        Bundle args6 = new Bundle();
        args6.putInt("day", 6);
        args6.putLong("scheduleId", this.scheduleId);
        this.saturdayFragment.setArguments(args6);
        Bundle args7 = new Bundle();
        args7.putInt("day", 7);
        args7.putLong("scheduleId", this.scheduleId);
        this.sundayFragment.setArguments(args7);
        
        // Get an instance of FragmentTransaction from your Activity.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Put Monday in view.
        fragmentTransaction.add(R.id.day_schedule_container, this.mondayFragment);
        fragmentTransaction.commit();
        
        // Create scroller to switch between days of the week.
        LinearLayout dayButtonScroller = (LinearLayout)findViewById(R.id.day_button_scroller);
        OnClickListener dayButtonClickListener = new OnClickListener() {
        	@Override
			public void onClick(View view) {
        		Toast.makeText(EditScheduleActivity.this,
        				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();
        		
        		FragmentManager fragmentManager = getFragmentManager();
        		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        		EditDayFragment f;
        		switch (view.getId()) {
        			case 1:
        				f = EditScheduleActivity.this.mondayFragment;
        				break;
        			case 2:
        				f = EditScheduleActivity.this.tuesdayFragment;
        				break;
        			case 3:
        				f = EditScheduleActivity.this.wednesdayFragment;
        				break;
        			case 4:
        				f = EditScheduleActivity.this.thursdayFragment;
        				break;
        			case 5:
        				f = EditScheduleActivity.this.fridayFragment;
        				break;
        			case 6:
        				f = EditScheduleActivity.this.saturdayFragment;
        				break;
        			case 7:
        				f = EditScheduleActivity.this.sundayFragment;
        				break;
        			default:
        				f = EditScheduleActivity.this.mondayFragment;
        		}
        		fragmentTransaction.replace(R.id.day_schedule_container, f);
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
        
        SchedulesDataSource dataSource = new SchedulesDataSource(this);
        dataSource.open();
        ScheduleData schedule = dataSource.getScheduleFromId(this.scheduleId);
        this.setTitle(schedule.name);
        dataSource.close();
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
			    newFragment.show(getFragmentManager(), "delete_schedule");
			    break;
			case R.id.rename_schedule_option:
				// Open dialog box to delete schedule.
				DialogFragment newFragment2 = new RenameScheduleDialogFragment();
			    newFragment2.show(getFragmentManager(), "rename_schedule");
			default:
				break;
		}
	
		return true;
	}

	@Override
	public void onDeleteScheduleDialogPositiveClick(DialogFragment dialog) {
		// Delete current schedule and inform schedule menu to update list.
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
		dataSource.open();
		dataSource.deleteSchedule(this.scheduleId);
		dataSource.close();
		
	    finish();
	}

	@Override
	public void onDeleteScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}

	@Override
	public void onRenameScheduleDialogPositiveClick(DialogFragment dialog, String newName) {
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
    	dataSource.open();
    	ScheduleData schedule = dataSource.getScheduleFromId(this.scheduleId);
    	schedule.name = newName;
    	dataSource.updateSchedule(schedule);
    	dataSource.close();
    	
    	this.setTitle(schedule.name);
	}

	@Override
	public void onRenameScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}
}
