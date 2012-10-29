package com.schedushare.android.fragments;

import com.schedushare.android.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import roboguice.fragment.RoboDialogFragment;

public class NewScheduleDialogFragment extends RoboDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_new_schedule, null))
        // Add action buttons
               .setPositiveButton(R.string.create_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Create new schedule.
                	   
                   }
               })
               .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       NewScheduleDialogFragment.this.getDialog().cancel();
                   }
               });      
        return builder.create();
    }
}