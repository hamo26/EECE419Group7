package com.schedushare.android;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.UserData;
import com.schedushare.android.fragments.DiffFragment;
import com.schedushare.android.fragments.ViewDayFragment;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_diff)
public class DiffActivity extends RoboFragmentActivity {
	@InjectView(R.id.diff_schedule_user_button_scroller) private LinearLayout userButtonScroller;
	@InjectView(R.id.diff_schedule_day_button_scroller) private LinearLayout dayButtonScroller;
	
	boolean showDiff;
	long currentScheduleId;
	int currentDay;
	List<GraphUser> selectedUsers;
	
	private DiffFragment diffFragment;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get bundle passed from last activity.
        Bundle extras = getIntent().getExtras();        
        if (extras != null) {
        	// Put Diff in view.
            this.diffFragment = new DiffFragment();
            this.selectedUsers = restoreByteArray(extras.getByteArray("selectedUsers"));
            
            Bundle args = new Bundle();
        	args.putByteArray("selectedUsers", extras.getByteArray("selectedUsers"));
        	this.diffFragment.setArguments(args);
        	
        	FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();     
            fragmentTransaction.add(R.id.diff_schedule_container, diffFragment);
            fragmentTransaction.commit();
            
        	// Determines whether day button scroller is visible initially not showing as diff is in view.
        	this.showDiff = true;
        	
        	// Initialize currentScheduleId to the user's active schedule and day to Monday.
        	SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
        	System.out.println("Diff: active schedule id: " + p.getLong(getString(R.string.settings_owner_active_schedule_id), -1));
        	this.currentScheduleId = p.getLong(getString(R.string.settings_owner_active_schedule_id), 1);
        	this.currentDay = 0;
            
            // Create scroller to switch between days of the week.
            this.dayButtonScroller = (LinearLayout)findViewById(R.id.diff_schedule_day_button_scroller);
            OnClickListener dayButtonClickListener = new OnClickListener() {
            	@Override
    			public void onClick(View view) {
            		Toast.makeText(DiffActivity.this,
            				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();
            		
            		currentDay = view.getId();
            		
            		if (!showDiff) {
            			ViewDayFragment fragment = new ViewDayFragment();
                    	Bundle args = new Bundle();
                    	args.putInt("day", currentDay);
                    	args.putLong("scheduleId", currentScheduleId);
                    	fragment.setArguments(args);
            			
            			FragmentManager fragmentManager = getSupportFragmentManager();
                		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                		fragmentTransaction.replace(R.id.diff_schedule_container, fragment);
                		fragmentTransaction.commit();
            		}
    			}
            };
            
            // Create all 7 day buttons.
            createDayButton(dayButtonClickListener, 0, "Mon", 150, 75);
            createDayButton(dayButtonClickListener, 1, "Tue", 150, 75);
            createDayButton(dayButtonClickListener, 2, "Wed", 150, 75);
            createDayButton(dayButtonClickListener, 3, "Thu", 150, 75);
            createDayButton(dayButtonClickListener, 4, "Fri", 150, 75);
            createDayButton(dayButtonClickListener, 5, "Sat", 150, 75);
            createDayButton(dayButtonClickListener, 6, "Sun", 150, 75);
            
            if (this.showDiff)
            	this.dayButtonScroller.setVisibility(View.GONE);
            
            // Create scroller to switch between days of the week.
            this.userButtonScroller = (LinearLayout)findViewById(R.id.diff_schedule_user_button_scroller);
            OnClickListener userButtonClickListener = new OnClickListener() {
            	@Override
    			public void onClick(View view) {
            		Toast.makeText(DiffActivity.this,
            				((Button)view).getText() + " is clicked!", Toast.LENGTH_SHORT).show();            		            		
            		
            		if (((TextView)view).getText().toString().equals("Diff"))
            			showDiff = true;
            		else
            			showDiff = false;
            		
            		// Determine fragment to replace.
            		FragmentManager fragmentManager = getSupportFragmentManager();
            		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            		if (showDiff) {
            			fragmentTransaction.replace(R.id.diff_schedule_container, diffFragment);
            			
            			dayButtonScroller.setVisibility(View.GONE);
            		} else {
            			SchedulesDataSource dataSource = new SchedulesDataSource(DiffActivity.this);
                		dataSource.open();
                		
                		System.out.println("Diff: button tag: " + (Long)view.getTag());
                		UserData u = dataSource.getUserFromSid((Long)view.getTag());
                		System.out.println("Diff: user object: " + u);
                		
                		ScheduleData s = dataSource.getActiveScheduleFromOwnerId(u.id);
                		dataSource.close();
                		
                		currentScheduleId = s.id;
            			
            			ViewDayFragment fragment = new ViewDayFragment();
                    	Bundle args = new Bundle();
                    	args.putInt("day", currentDay);
                    	args.putLong("scheduleId", currentScheduleId);
                    	fragment.setArguments(args);
            			
                		fragmentTransaction.replace(R.id.diff_schedule_container, fragment);
                		
                		dayButtonScroller.setVisibility(View.VISIBLE);
            		}
            		fragmentTransaction.commit();
    			}
            };
            
            createUserButton(userButtonClickListener, Long.valueOf(0), "Diff", 75);
            System.out.println("Diff: owner fb id: " + p.getLong(getString(R.string.settings_owner_facebook_id), -1));
            createUserButton(userButtonClickListener, p.getLong(getString(R.string.settings_owner_facebook_id), 1), "Me", 75);
            for (GraphUser u : this.selectedUsers) {
            	createUserButton(userButtonClickListener, Long.parseLong(u.getId()), u.getName(), 75);
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create menu with new schedule button.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.diff_schedule, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.create_fb_event_option:
				// Launches Facebook.
				Intent intent = new Intent("android.intent.category.LAUNCHER");
				intent.setClassName("com.facebook.katana", "com.facebook.katana.LoginActivity");
				startActivity(intent);
				break;
			default:
				break;
	  }
	
	  return true;
	}
    
    private void createDayButton(OnClickListener listener, int id, String text, int width, int height) {    	
    	Button dayButton = new Button(this);
        dayButton.setId(id);
        dayButton.setOnClickListener(listener);
        dayButton.setText(text);
        dayButton.setLayoutParams(new LayoutParams(width, height));
        
        this.dayButtonScroller.addView(dayButton);
    }
    
    private void createUserButton(OnClickListener listener, Long id, String text, int height) {    	
    	Button userButton = new Button(this);
        userButton.setTag(id);
        userButton.setOnClickListener(listener);
        userButton.setText(text);
        userButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, height));
        
        this.userButtonScroller.addView(userButton);
    }
    
    public static List<GraphUser> restoreByteArray(byte[] bytes) {
        try {
            List<String> usersAsString =
                    (List<String>) (new ObjectInputStream
                                    (new ByteArrayInputStream(bytes)))
                                    .readObject();
            if (usersAsString != null) {
                List<GraphUser> users = new ArrayList<GraphUser>
                (usersAsString.size());
                for (String user : usersAsString) {
                    GraphUser graphUser = GraphObject.Factory
                    .create(new JSONObject(user), 
                                    GraphUser.class);
                    users.add(graphUser);
                }   
                return users;
            }   
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace(); 
        }   
        return null;
    }
}
