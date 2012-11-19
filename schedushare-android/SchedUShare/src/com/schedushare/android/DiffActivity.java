package com.schedushare.android;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.GraphObjectWrapper;
import com.facebook.GraphUser;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import android.os.Bundle;

@ContentView(R.layout.activity_diff)
public class DiffActivity extends RoboFragmentActivity {
	List<GraphUser> selectedUsers;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get bundle passed from last activity.
        Bundle extras = getIntent().getExtras();        
        if (extras != null) {
        	this.selectedUsers = restoreByteArray(extras.getByteArray("selectedUsers"));
        	
        	System.out.println("DiffActivity: selected users");
        	for (GraphUser user : this.selectedUsers) {
        		System.out.println("user fb id: " + user.getId() + " user fb name: " + user.getName());
        	}
        }
    }
    
    private List<GraphUser> restoreByteArray(byte[] bytes) {
        try {
            List<String> usersAsString =
                    (List<String>)(new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
            if (usersAsString != null) {
                List<GraphUser> users = new ArrayList<GraphUser>(usersAsString.size());
                for (String user : usersAsString) {
                    GraphUser graphUser = GraphObjectWrapper.createGraphObject(new JSONObject(user), GraphUser.class);
                    users.add(graphUser);
                }   
                return users;
            }   
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); 
        } catch (IOException e) {
        	e.printStackTrace(); 
        } catch (JSONException e) {
        	e.printStackTrace();  
        }   
        
        return null;
    }
}
