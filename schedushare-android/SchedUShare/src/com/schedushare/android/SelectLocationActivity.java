package com.schedushare.android;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

@ContentView(R.layout.activity_select_location)
public class SelectLocationActivity extends RoboMapActivity {
	@InjectView(R.id.select_location_map_view) private MapView mapView;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.mapView = (MapView)findViewById(R.id.select_location_map_view);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getController().setZoom(12);
        this.mapView.getOverlays().add(new SelectMapOverlay(this));
        
        // Set the default geolocation to the last known location.
    	LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    	try {
    		String locationProvider = LocationManager.NETWORK_PROVIDER;
    		locationManager.getBestProvider(new Criteria(), false);
    		Location location = locationManager.getLastKnownLocation(locationProvider);
    		if (location != null) {
    			this.mapView.getController().setCenter(new GeoPoint((int)(location.getLatitude() * 1e6),
    					(int)(location.getLongitude() * 1e6)));
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private static class SelectMapOverlay extends Overlay {
		public GeoPoint lastTap;
		private Context context;
		
		public SelectMapOverlay(Context context) {
			this.context = context;
		}
		
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
		    this.lastTap = p;
		    mapView.getController().animateTo(p);

		    MapActivity ma = (MapActivity)this.context;
		    Intent i = new Intent();
		    Bundle b = new Bundle();
		    b.putInt("latitude", this.lastTap.getLatitudeE6());
		    b.putInt("longitude", this.lastTap.getLongitudeE6());
		    i.putExtras(b);
		    ma.setResult(Activity.RESULT_OK, i);
		    ma.finish();
		    
		    return true;        
		}  
	}
}
