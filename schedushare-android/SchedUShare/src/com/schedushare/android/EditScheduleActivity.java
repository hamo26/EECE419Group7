package com.schedushare.android;

import java.util.concurrent.ExecutionException;

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

import com.facebook.Session;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment;
import com.schedushare.android.fragments.DeleteScheduleDialogFragment.DeleteScheduleDialogListener;
import com.schedushare.android.fragments.EditDayFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment;
import com.schedushare.android.fragments.RenameScheduleDialogFragment.RenameScheduleDialogListener;
import com.schedushare.android.fragments.SetActiveScheduleDialogFragment;
import com.schedushare.android.fragments.SetActiveScheduleDialogFragment.SetActiveScheduleDialogListener;
import com.schedushare.android.schedule.task.DeleteScheduleTask;
import com.schedushare.android.schedule.task.UpdateScheduleTask;
import com.schedushare.common.domain.dto.ScheduleEntity;
import com.schedushare.common.domain.dto.SchedushareExceptionEntity;
import com.schedushare.common.domain.rest.RestResult;

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

	@Inject
	Provider<DeleteScheduleTask> deleteScheduleTaskProvider;
	
	@Inject
	Provider<UpdateScheduleTask> updateScheduleTaskProvider;
	
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
		Session session = Session.getActiveSession();
		
		switch (item.getItemId()) {
			case R.id.delete_schedule_option:
				if (session != null && session.isOpened()) {
					// Open dialog box to delete schedule.
					DialogFragment newFragment = new DeleteScheduleDialogFragment();
				    newFragment.show(getSupportFragmentManager(), "delete_schedule");
				} else {
					Toast.makeText(this, "You must log into Facebook!", Toast.LENGTH_LONG).show();
				}
			    break;
			case R.id.rename_schedule_option:
				if (session != null && session.isOpened()) {
					// Open dialog box to delete schedule.
					DialogFragment newFragment2 = new RenameScheduleDialogFragment();
				    newFragment2.show(getSupportFragmentManager(), "rename_schedule");
				} else {
					Toast.makeText(this, "You must log into Facebook!", Toast.LENGTH_LONG).show();
				}
			    break;
			case R.id.set_active_schedule_option:
				if (session != null && session.isOpened()) {
					// Open dialog box to set active schedule.
					DialogFragment newFragment3 = new SetActiveScheduleDialogFragment();
				    newFragment3.show(getSupportFragmentManager(), "set_active_schedule");
				} else {
					Toast.makeText(this, "You must log into Facebook!", Toast.LENGTH_LONG).show();
				}
			    break;
			default:
				break;
		}
	
		return true;
	}

	@Override
	public void onDeleteScheduleDialogPositiveClick(DialogFragment dialog) {
		if (this.schedule.active) {
			Toast.makeText(this, "You must set another active schedule first!", Toast.LENGTH_LONG).show();
		} else {
			// Delete current schedule from back end using schedule.sid (server id).
			try {
				RestResult<ScheduleEntity> restResult = deleteScheduleTaskProvider.get()
						.execute((int) this.schedule.sid).get();
				if (restResult.isFailure()) {
					// Delete from server failed.
					Toast.makeText(this, "Delete Failed!", Toast.LENGTH_LONG).show();
				} else {
					// Delete from server success.
					ScheduleEntity deletedScheduleEntity = restResult.getRestResult();
					
					// Delete current schedule from local db on server delete success.
					SchedulesDataSource dataSource = new SchedulesDataSource(this);
					dataSource.open();
					dataSource.deleteSchedule(this.schedule.id);
					dataSource.close();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	    finish();
	}

	@Override
	public void onDeleteScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}

	@Override
	public void onRenameScheduleDialogPositiveClick(DialogFragment dialog, String newName) {
		// Rename schedule in back end using schedule.sid (server id) and newName.
		ScheduleEntity scheduleEntity = new ScheduleEntity((int)this.schedule.sid, 
				newName, this.schedule.active, Long.toString(this.schedule.ownerId), null); 
		try {
			// TODO: Rename back end does not work.
			RestResult<ScheduleEntity> restResult = updateScheduleTaskProvider.get()
					.execute(scheduleEntity).get();
			if (restResult.isFailure()) {
				// Rename in server failed.
				SchedushareExceptionEntity error = restResult.getError();
				
				Toast.makeText(this, "Rename failed!", Toast.LENGTH_LONG).show();
			} else {
				// Rename in server success.
				ScheduleEntity updatedScheduleEntity = restResult.getRestResult();
				
				// Rename current schedule in local db and change title.
				SchedulesDataSource dataSource = new SchedulesDataSource(this);
		    	dataSource.open();
		    	this.schedule.name = newName;
		    	dataSource.updateSchedule(this.schedule);
		    	dataSource.close();
		    	
		    	this.setTitle(this.schedule.name);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRenameScheduleDialogNegativeClick(DialogFragment dialog) {
		
	}

	@Override
	public void onSetActiveScheduleDialogPositiveClick(DialogFragment dialog) {
		if (!this.schedule.active) {
			SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
			
			// Update local database.
			SchedulesDataSource dataSource = new SchedulesDataSource(this);
			dataSource.open();
			ScheduleData lastActiveSchedule = dataSource.getActiveScheduleFromOwnerId(
					p.getLong(getString(R.string.settings_owner_id), -1));
			lastActiveSchedule.active = false;
			this.schedule.active = true;
			
			// Update back end with both this.schedule and lastActiveSchedule.
			ScheduleEntity lastActiveScheduleEntity = new ScheduleEntity((int)lastActiveSchedule.sid,
					lastActiveSchedule.name, lastActiveSchedule.active, Long.toString(lastActiveSchedule.ownerId), null);
			
			ScheduleEntity scheduleEntity = new ScheduleEntity((int) this.schedule.sid,
					this.schedule.name, this.schedule.active, Long.toString(this.schedule.ownerId), null);
			
			try {
				RestResult<ScheduleEntity> restResult = updateScheduleTaskProvider.get()
						.execute(lastActiveScheduleEntity).get();
				if (restResult.isFailure()) {
					// Update in server failed.
					SchedushareExceptionEntity error = restResult.getError();
					
					Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
				} else {
					restResult = updateScheduleTaskProvider.get().execute(scheduleEntity).get();
					if (restResult.isFailure()) {
						// Update in server failed.
						SchedushareExceptionEntity error = restResult.getError();
						
						Toast.makeText(this, "Update failed.", Toast.LENGTH_LONG).show();
					} else {
						// Update in server success. Update local db and preferences as well.
						dataSource.updateSchedule(lastActiveSchedule);
						dataSource.updateSchedule(this.schedule);
						
						SharedPreferences.Editor editor = p.edit();
						editor.putLong(getString(R.string.settings_owner_active_schedule_id), this.schedule.id);
						editor.commit();
						
						// Success message.
						Toast.makeText(this, "Set active", Toast.LENGTH_LONG).show();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			dataSource.close();
		}
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
