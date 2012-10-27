package com.schedushare.android;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

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

	Facebook facebook = new Facebook("100990793398405");
    AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        facebook.authorize(this, new String[] {"email", "create_event"},
        	new DialogListener() {
            	public void onComplete(Bundle values) {}

            	public void onFacebookError(FacebookError error) {}

            	public void onError(DialogError e) {}

            	public void onCancel() {}
        });
    }
    
    // Called when the user clicks the Send button
    public void loginUser(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        String message = this.userIdInput.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
}
