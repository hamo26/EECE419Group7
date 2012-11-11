package com.schedushare.android;

import com.schedushare.android.fragments.EditDayFragment;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@ContentView(R.layout.activity_edit_schedule)
public class EditScheduleActivity extends RoboActivity {
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
        this.mondayFragment.setArguments(args);
        Bundle args2 = new Bundle();
        args2.putInt("day", 2);
        this.tuesdayFragment.setArguments(args2);
        Bundle args3 = new Bundle();
        args3.putInt("day", 3);
        this.wednesdayFragment.setArguments(args3);
        Bundle args4 = new Bundle();
        args4.putInt("day", 4);
        this.thursdayFragment.setArguments(args4);
        Bundle args5 = new Bundle();
        args5.putInt("day", 5);
        this.fridayFragment.setArguments(args5);
        Bundle args6 = new Bundle();
        args6.putInt("day", 6);
        this.saturdayFragment.setArguments(args6);
        Bundle args7 = new Bundle();
        args7.putInt("day", 7);
        this.sundayFragment.setArguments(args7);
        
        // Get an instance of FragmentTransaction from your Activity.
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Put Monday in view.
        fragmentTransaction.add(R.id.day_schedule_container, this.mondayFragment);
        fragmentTransaction.commit();
        
        LinearLayout dayButtonScroller = (LinearLayout)findViewById(R.id.day_button_scroller);
        OnClickListener dayButtonClickListener = new OnClickListener() {
        	@Override
			public void onClick(View view) {
        		Toast.makeText(EditScheduleActivity.this,
        				"Button " + view.getId() + " is clicked!", Toast.LENGTH_SHORT).show();
        		
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
        
        Button monButton = new Button(this);
        monButton.setId(1);
        monButton.setOnClickListener(dayButtonClickListener);
        monButton.setText("Mon");
        monButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button tueButton = new Button(this);
        tueButton.setId(2);
        tueButton.setOnClickListener(dayButtonClickListener);
        tueButton.setText("Tue");
        tueButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button wedButton = new Button(this);
        wedButton.setId(3);
        wedButton.setOnClickListener(dayButtonClickListener);
        wedButton.setText("Wed");
        wedButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button thuButton = new Button(this);
        thuButton.setId(4);
        thuButton.setOnClickListener(dayButtonClickListener);
        thuButton.setText("Thu");
        thuButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button friButton = new Button(this);
        friButton.setId(5);
        friButton.setOnClickListener(dayButtonClickListener);
        friButton.setText("Fri");
        friButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button satButton = new Button(this);
        satButton.setId(6);
        satButton.setOnClickListener(dayButtonClickListener);
        satButton.setText("Sat");
        satButton.setLayoutParams(new LayoutParams(100, 75));
        
        Button sunButton = new Button(this);
        sunButton.setId(7);
        sunButton.setOnClickListener(dayButtonClickListener);
        sunButton.setText("Sun");
        sunButton.setLayoutParams(new LayoutParams(100, 75));
        
        dayButtonScroller.addView(monButton);
        dayButtonScroller.addView(tueButton);
        dayButtonScroller.addView(wedButton);
        dayButtonScroller.addView(thuButton);
        dayButtonScroller.addView(friButton);
        dayButtonScroller.addView(satButton);
        dayButtonScroller.addView(sunButton);
    }
}
