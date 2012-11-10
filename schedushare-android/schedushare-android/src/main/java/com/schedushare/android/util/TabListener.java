package com.schedushare.android.util;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    public Fragment fragment;
    private final Activity activity;
    private final String tag;
    private final Class<T> fragClass;

    /** Constructor used each time a new tab is created.
      * @param activity  The host Activity, used to instantiate the fragment
      * @param tag  The identifier tag for the fragment
      * @param clz  The fragment's Class, used to instantiate the fragment
      */
    public TabListener(Activity activity, String tag, Class<T> clz) {
        this.activity = activity;
        this.tag = tag;
        this.fragClass = clz;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // Check if the fragment is already initialized
        if (this.fragment == null) {
            // If not, instantiate and add it to the activity
            this.fragment = Fragment.instantiate(this.activity, this.fragClass.getName());
            ft.add(android.R.id.content, this.fragment, this.tag);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(this.fragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (this.fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(this.fragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}