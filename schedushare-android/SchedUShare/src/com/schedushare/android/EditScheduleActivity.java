package com.schedushare.android;

import com.schedushare.android.fragments.EditDayFragment;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

@ContentView(R.layout.activity_edit_schedule)
public class EditScheduleActivity extends RoboActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // get an instance of FragmentTransaction from your Activity
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        EditDayFragment fragment = new EditDayFragment();
        fragmentTransaction.add(R.id.day_schedule_container, fragment);
        fragmentTransaction.commit();
    }
}
