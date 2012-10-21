package com.schedushare.android;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {
	public final static String EXTRA_MESSAGE = "com.schedushare.MESSAGE";
	
	@InjectView(R.id.username_input) private EditText userIdInput;
	@InjectView(R.id.password_input) private EditText userPasswordInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    // Called when the user clicks the Send button
    public void loginUser(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        String message = this.userIdInput.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
