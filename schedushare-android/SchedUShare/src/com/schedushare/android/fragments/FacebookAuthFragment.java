package com.schedushare.android.fragments;

import com.facebook.widget.LoginButton;
import com.schedushare.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FacebookAuthFragment extends Fragment {
	private LoginButton facebookAuthButton;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_facebook_auth, container, false);

	    this.facebookAuthButton = (LoginButton)view.findViewById(R.id.facebook_auth_button);
	    this.facebookAuthButton.setApplicationId(getString(R.string.app_id));
		
		return view;
    }
}