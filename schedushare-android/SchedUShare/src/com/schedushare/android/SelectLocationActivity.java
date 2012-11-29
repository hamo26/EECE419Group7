package com.schedushare.android;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import roboguice.activity.RoboMapActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

@ContentView(R.layout.activity_select_location)
public class SelectLocationActivity extends RoboMapActivity {
	public static final int REQUEST_CODE = 421;
	
	@InjectView(R.id.select_location_map_view) private MapView mapView;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.mapView = (MapView)findViewById(R.id.select_location_map_view);
        this.mapView.setBuiltInZoomControls(true);
        
        // Create overlay with marker.
        Drawable markerDefault = this.getResources().getDrawable(R.drawable.map_pin);
        MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(markerDefault, this);
        Bundle extras = getIntent().getExtras();
        
        if (extras == null) {
	        // Set the default geolocation to the last known location.
	    	LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	    	try {
	    		String locationProvider = LocationManager.NETWORK_PROVIDER;
	    		locationManager.getBestProvider(new Criteria(), false);
	    		Location location = locationManager.getLastKnownLocation(locationProvider);
	    		if (location != null) {
	    			GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1e6), (int)(location.getLongitude() * 1e6));
	    			this.mapView.getController().setCenter(point);
	    			
	    			OverlayItem overlayItem = new OverlayItem(point, "Last Known Location", null);
	    	        itemizedOverlay.addOverlayItem(overlayItem);
	    		} else {
	    			System.out.println("MapView: No known last location.");
	    			
	    			GeoPoint point = new GeoPoint((int)(49.2505 * 1e6), (int)(-123.1119 * 1e6));
	    			this.mapView.getController().setCenter(point);
	    			
	    			OverlayItem overlayItem = new OverlayItem(point, "No Known Last Location", null);
	    	        itemizedOverlay.addOverlayItem(overlayItem);
	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		
	    		System.out.println("MapView: Location manager failed.");
	    		
	    		GeoPoint point = new GeoPoint((int)(49.2505 * 1e6), (int)(-123.1119 * 1e6));
    			this.mapView.getController().setCenter(point);
    			
    			OverlayItem overlayItem = new OverlayItem(point, "No Known Last Location", null);
    	        itemizedOverlay.addOverlayItem(overlayItem);
	    	}
        } else {
        	GeoPoint point = new GeoPoint((int)(extras.getDouble("latitude") * 1e6), (int)(extras.getDouble("longitude") * 1e6));
        	this.mapView.getController().setCenter(point);
			
			OverlayItem overlayItem = new OverlayItem(point, "Last Time Block Location", null);
	        itemizedOverlay.addOverlayItem(overlayItem);
        }
    	
    	this.mapView.getOverlays().add(itemizedOverlay);
        this.mapView.getController().setZoom(14);
//    	this.mapView.getController().zoomToSpan(itemizedOverlay.getLatSpanE6(), itemizedOverlay.getLonSpanE6());
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
		private Context context;
		
		public MyItemizedOverlay(Drawable defaultMarker, Context context) {
            super(boundCenterBottom(defaultMarker));
            
            this.context = context;
        }
        
        @Override
        protected OverlayItem createItem(int i) {
            return this.overlays.get(i);
        }

        @Override
        public int size() {
            return this.overlays.size();
        }
        
        @Override
        protected boolean onTap(int index) {
        	Toast.makeText(SelectLocationActivity.this, getItem(index).getTitle(), Toast.LENGTH_LONG).show();
        	return true;
    	} 
        
        @Override
		public boolean onTap(GeoPoint p, MapView mapView) {
		    mapView.getController().animateTo(p);
		    final GeoPoint selectedPoint = p;

		    AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
		    dialog.setTitle("Select Location");
		    dialog.setPositiveButton("Confirm", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MapActivity ma = (MapActivity)context;
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putInt("latitude", selectedPoint.getLatitudeE6());
					b.putInt("longitude", selectedPoint.getLongitudeE6());
					i.putExtras(b);
					ma.setResult(Activity.RESULT_OK, i);
					ma.finish();
				}
		    });
		    dialog.setNegativeButton("Cancel", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
		    });
		    dialog.show();
		    
		    return true;        
		}  
        
        public void addOverlayItem(OverlayItem overlayItem) {
            overlays.add(overlayItem);
            populate();
        }
	}
}
