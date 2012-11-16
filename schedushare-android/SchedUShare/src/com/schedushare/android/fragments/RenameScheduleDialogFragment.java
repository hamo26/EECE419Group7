package com.schedushare.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.schedushare.android.R;

public class RenameScheduleDialogFragment extends DialogFragment {
	// The activity that creates an instance of this dialog fragment must
    // implement this interface in order to receive event callbacks.
    // Each method passes the DialogFragment in case the host needs to query it.
    public interface RenameScheduleDialogListener {
        public void onRenameScheduleDialogPositiveClick(DialogFragment dialog, String newName);
        public void onRenameScheduleDialogNegativeClick(DialogFragment dialog);
    }

    // Container for dialog's view.
    private View dialogView;
    
    // Use this instance of the interface to deliver action events
    private RenameScheduleDialogListener listener;
	
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			this.listener = (RenameScheduleDialogListener)activity;
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
        this.dialogView = inflater.inflate(R.layout.dialog_rename_schedule, null);
        
        // Build the dialog with view.
        builder.setView(dialogView)
               .setPositiveButton(R.string.confirm_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   EditText userInput = (EditText)RenameScheduleDialogFragment.this.dialogView
                			   .findViewById(R.id.rename_schedule_dialog_name_input);
                	   
                       // Notify activity of confirm.
                       RenameScheduleDialogFragment.this.listener.onRenameScheduleDialogPositiveClick(RenameScheduleDialogFragment.this, userInput.getText().toString());
                   }
               })
               .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       RenameScheduleDialogFragment.this.getDialog().cancel();
                       
                       // Notify activity of cancel.
                       RenameScheduleDialogFragment.this.listener.onRenameScheduleDialogNegativeClick(RenameScheduleDialogFragment.this);
                   }
               });
        
        return builder.create();
    }
}