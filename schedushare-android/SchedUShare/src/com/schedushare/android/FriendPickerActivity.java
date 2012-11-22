package com.schedushare.android;

import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

@ContentView(R.layout.activity_friend_picker)
public class FriendPickerActivity extends RoboFragmentActivity {
	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	public static final int REQUEST_CODE = 419;
	
	private FriendPickerFragment friendPickerFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    Bundle args = getIntent().getExtras();
	    FragmentManager manager = getSupportFragmentManager();
	    Fragment fragmentToShow = null;
	    Uri intentUri = getIntent().getData();

	    if (FriendPickerActivity.FRIEND_PICKER.equals(intentUri)) {
	        if (savedInstanceState == null) {
	            this.friendPickerFragment = new FriendPickerFragment(args);
	        } else {
	            this.friendPickerFragment = 
	                (FriendPickerFragment)manager.findFragmentById(R.id.friend_picker_container);
	        }
	        // Set the listener to handle errors
	        this.friendPickerFragment.setOnErrorListener(new FriendPickerFragment.OnErrorListener() {
				@Override
				public void onError(PickerFragment<?> fragment,
						FacebookException error) {
					FriendPickerActivity.this.onError(error);
				}
	        });
	        // Set the listener to handle button clicks
	        this.friendPickerFragment.setOnDoneButtonClickedListener(
	                new FriendPickerFragment.OnDoneButtonClickedListener() {
				@Override
				public void onDoneButtonClicked(PickerFragment<?> fragment) {
					finishActivity();
				}
	        });
	        fragmentToShow = this.friendPickerFragment;

	    } else {
	        // Nothing to do, finish
	        setResult(Activity.RESULT_CANCELED);
	        finish();
	        return;
	    }

	    manager.beginTransaction()
	           .replace(R.id.friend_picker_container, fragmentToShow)
	           .commit();
	}

	private void onError(Exception error) {
	    onError(error.getLocalizedMessage(), false);
	}

	private void onError(String error, final boolean finishActivity) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.error_dialog_title).
	            setMessage(error).
	            setPositiveButton(R.string.error_dialog_button_text, 
	               new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialogInterface, int i) {
	                    if (finishActivity) {
	                        finishActivity();
	                    }
	                }
	            });
	    builder.show();
	}

	private void finishActivity() {
		SchedUShareApplication app = (SchedUShareApplication)getApplication();
		if (FriendPickerActivity.FRIEND_PICKER.equals(getIntent().getData())) {
		    if (this.friendPickerFragment != null) {
		        app.setSelectedUsers(this.friendPickerFragment.getSelection());
		    }   
		}  
		
	    setResult(Activity.RESULT_OK, null);
	    finish();
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    if (FriendPickerActivity.FRIEND_PICKER.equals(getIntent().getData())) {
	        try {
	            this.friendPickerFragment.loadData(false);
	        } catch (Exception ex) {
	            onError(ex);
	        }
	    }
	}	
}
