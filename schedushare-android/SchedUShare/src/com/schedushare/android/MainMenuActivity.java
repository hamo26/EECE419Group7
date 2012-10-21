package com.schedushare.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

@ContentView(R.layout.activity_main_menu)
public class MainMenuActivity extends RoboActivity {
	@InjectView(R.id.output_text) private TextView outputTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        // Create the text view
        this.outputTextView.setTextSize(40);
        this.outputTextView.setText(message);
    }
}
