package com.schedushare.android;

import com.schedushare.android.fragments.DiffFragment;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

@ContentView(R.layout.activity_diff)
public class DiffActivity extends RoboFragmentActivity {	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get bundle passed from last activity.
        Bundle extras = getIntent().getExtras();        
        if (extras != null) {
        	// Put Diff in view.
            DiffFragment diffFragment = new DiffFragment();
            Bundle args = new Bundle();
        	args.putByteArray("selectedUsers", extras.getByteArray("selectedUsers"));
        	diffFragment.setArguments(args);
            
        	FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();     
            fragmentTransaction.add(R.id.diff_schedule_container, diffFragment);
            fragmentTransaction.commit();
        }
    }
    
    
}
