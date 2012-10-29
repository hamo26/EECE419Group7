package com.schedushare.android.fragments;

import com.schedushare.android.R;
import com.schedushare.android.db.SchedulesDataSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import roboguice.fragment.RoboDialogFragment;

public class NewScheduleDialogFragment extends RoboDialogFragment {
	// The activity that creates an instance of this dialog fragment must
    // implement this interface in order to receive event callbacks.
    // Each method passes the DialogFragment in case the host needs to query it.
    public interface NoticeDialogListener {
        public void onNewScheduleDialogPositiveClick(RoboDialogFragment dialog);
        public void onNewScheduleDialogNegativeClick(RoboDialogFragment dialog);
    }
    
    // Container for dialog's view.
    private View dialogView;
    
    // Use this instance of the interface to deliver action events
    private NoticeDialogListener listener;
	
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			this.listener = (NoticeDialogListener)activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
    	// Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the content view.
        this.dialogView = inflater.inflate(R.layout.dialog_new_schedule, null);
        
        // Build the dialog with view.
        builder.setView(dialogView)
               .setPositiveButton(R.string.create_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       createNewSchedule();
                       
                       // Notify activity of confirm.
                       NewScheduleDialogFragment.this.listener.onNewScheduleDialogPositiveClick(NewScheduleDialogFragment.this);
                   }
               })
               .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       NewScheduleDialogFragment.this.getDialog().cancel();
                       
                       // Notify activity of cancel.
                       NewScheduleDialogFragment.this.listener.onNewScheduleDialogNegativeClick(NewScheduleDialogFragment.this);
                   }
               });
        
        return builder.create();
    }
    
    private void createNewSchedule() {
    	// Get user input.
    	EditText userInput = (EditText)this.dialogView.findViewById(R.id.new_schedule_dialog_name_input);
    	
    	// Open connection to db and create new schedule.
    	SchedulesDataSource dataSource = new SchedulesDataSource(NewScheduleDialogFragment.this.getActivity());
    	dataSource.open();
    	dataSource.createSchedule(1, userInput.getText().toString(), false, 1, "whatever");
    	dataSource.close();
    }
}