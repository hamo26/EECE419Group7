package com.schedushare.android;

import java.util.List;

import com.facebook.GraphUser;

import android.app.Application;

public class SchedUShareApplication extends Application {
	private List<GraphUser> selectedUsers;
	
	public List<GraphUser> getSelectedUsers() {
	    return this.selectedUsers;
	}

	public void setSelectedUsers(List<GraphUser> users) {
	    this.selectedUsers = users;
	}
}