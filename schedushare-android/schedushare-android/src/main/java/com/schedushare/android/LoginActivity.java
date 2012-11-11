package com.schedushare.android;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.user.task.LoginTask;
import com.schedushare.common.domain.dto.UserEntity;
import com.schedushare.common.domain.rest.RestResult;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {
	public final static String EXTRA_MESSAGE = "com.schedushare.MESSAGE";
	
	@InjectView(R.id.username_input) private EditText userIdInput;
	@InjectView(R.id.password_input) private EditText userPasswordInput;

	@Inject private Provider<LoginTask> getLoginTaskProvider;
	
	private Facebook facebook;
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        facebook = new Facebook(getString(R.string.app_id));
        
        //get access token if any
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token",null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null){
        	facebook.setAccessToken(access_token);
        }
        if(expires != 0){
        	facebook.setAccessExpires(expires);
        }

        //only call authorize if access_token has expired
        if(!facebook.isSessionValid()){        
	        facebook.authorize(this, new String[] {"email", "rsvp_event","read_friendlists","create_event"},
	        	new DialogListener() {
	            	public void onComplete(Bundle values) {
	            		SharedPreferences.Editor editor = mPrefs.edit();
	                    editor.putString("access_token", facebook.getAccessToken());
	                    editor.putLong("access_expires", facebook.getAccessExpires());
	                    editor.commit();
	            	}
	
	            	public void onFacebookError(FacebookError error) {}
	
	            	public void onError(DialogError e) {}
	
	            	public void onCancel() {}
	        });
        }
        
		try {
			
	        String userEmail = "test@email.com";	
	        //GetEmailTask task = new GetEmailTask();
	        //String userEmail = task.execute("email").get();
	        
	        //put user's email in shared preferences
	        SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString("email", userEmail);
            editor.commit();
            
            RestResult<UserEntity> loginResult = getLoginTaskProvider.get().execute(userEmail).get();
            if (loginResult.isFailure()) {
            	Toast.makeText(this, loginResult.getError().getException(), Toast.LENGTH_LONG).show();
            } else {
            	Toast.makeText(this, "Welcome user: " + loginResult.getRestResult().getName(), Toast.LENGTH_LONG).show();
            	
            	SchedulesDataSource dataSource = new SchedulesDataSource(this);
    	        dataSource.open();
    	        dataSource.dropAllTables();
    	        dataSource.createUser(loginResult.getRestResult().getUserId(), loginResult.getRestResult().getName());
    	        dataSource.close();

            }
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    private class GetEmailTask extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String email=null;
	    	try {
	    		String response = facebook.request("me");
		        JSONObject obj = Util.parseJson(response);
		        email= obj.getString(params[0]);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return email;
		}
    	
    }
}
