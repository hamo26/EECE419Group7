package com.schedushare.android;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.schedushare.android.db.ScheduleData;
import com.schedushare.android.db.SchedulesDataSource;
import com.schedushare.android.db.TimeBlockData;
import com.schedushare.android.db.UserData;
import com.schedushare.android.fragments.BeamDiffFragment;
import com.schedushare.android.util.ScheduleTimeBlockWrapper;

@ContentView(R.layout.activity_beam)
public class BeamActivity extends RoboFragmentActivity implements CreateNdefMessageCallback, OnNdefPushCompleteCallback  {
	
	NfcAdapter mNfcAdapter;
	
	boolean sent,showDiff;
	long currentScheduleId;
	int currentDay;
	List<String> users;
	static ScheduleTimeBlockWrapper receivedSchedule=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        sent = false;
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
        	Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }
        //register callback to set NFC message
        mNfcAdapter.setNdefPushMessageCallback(this, this);
        //register callback to listen for message sent success
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_beam, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
    	dataSource.open();
    	UserData user = dataSource.getUserFromSid(p.getLong(getString(R.string.settings_owner_facebook_id), 1));
		ScheduleData mySchedule = dataSource.getScheduleFromId(p.getLong(getString(R.string.settings_owner_active_schedule_id), 1));
		ArrayList<TimeBlockData> myTimeBlocks = dataSource.getSchdeduleTimeBlocks(mySchedule.id);
		
		ScheduleTimeBlockWrapper beamMessage = new ScheduleTimeBlockWrapper();
		beamMessage.user = user;
		beamMessage.schedule = mySchedule;
		beamMessage.timeBlocks = myTimeBlocks;
		byte[] payload=null;
		try {
			payload = beamMessage.serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        NdefMessage msg = new NdefMessage(
        		new NdefRecord[] { new NdefRecord(
        		        	    NdefRecord.TNF_MIME_MEDIA ,
        		        	    "application/com.schedushare.android".getBytes(Charset.forName("US-ASCII")),
        		        	    new byte[0], payload)}
        		);
  
        dataSource.close();
        return msg;
	}
	
	@Override
	public void onNdefPushComplete(NfcEvent arg0) {
		sent = true;
		if (sent && receivedSchedule != null){
			//call diff
			SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
			SchedulesDataSource dataSource = new SchedulesDataSource(this);
	    	dataSource.open();
			dataSource.createUser(receivedSchedule.user.sid, receivedSchedule.user.name);
			dataSource.createSchedule(receivedSchedule.schedule.sid, receivedSchedule.schedule.name, true, receivedSchedule.schedule.ownerId, receivedSchedule.schedule.lastModified);
			for(TimeBlockData t : receivedSchedule.timeBlocks){
				dataSource.createTimeBlock(t.sid, t.name, t.startTime, t.endTime, t.day, t.blockTypeId, t.scheduleId, t.longitude, t.latitude);
			}

			Intent intent2 = new Intent(this, BeamDiffActivity.class);
			Bundle b = new Bundle();
		    try {
				b.putByteArray("receivedSchedule", receivedSchedule.serialize());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    intent2.putExtras(b);
	        startActivity(intent2);
	        dataSource.close();
	        finish();
	        return;
		}
	}	
	
	@Override
	public void onResume(){
		super.onResume();
		//Check if activity started due to an Android Beam
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()))
			processIntent(getIntent());
	}
	
	@Override
	public void onNewIntent(Intent intent){
		//onResume gets called after this to handle the intent
		setIntent(intent);
	}

	//parses Ndef intent and toast text (temporary)
	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		//only one message set during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		// record 0 contains the MIME type, record 1 is the AAR, if present
		//set receivedSchedule and call diff if schedule has been sent and received
		try {
			receivedSchedule = ScheduleTimeBlockWrapper.deserialize(msg.getRecords()[0].getPayload());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sent && receivedSchedule != null){
			//call diff
			SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
			SchedulesDataSource dataSource = new SchedulesDataSource(this);
	    	dataSource.open();
			dataSource.createUser(receivedSchedule.user.sid, receivedSchedule.user.name);
			dataSource.createSchedule(receivedSchedule.schedule.sid, receivedSchedule.schedule.name, true, receivedSchedule.schedule.ownerId, receivedSchedule.schedule.lastModified);
			for(TimeBlockData t : receivedSchedule.timeBlocks){
				dataSource.createTimeBlock(t.sid, t.name, t.startTime, t.endTime, t.day, t.blockTypeId, t.scheduleId, t.longitude, t.latitude);
			}

			Intent intent2 = new Intent(this, BeamDiffActivity.class);
			Bundle b = new Bundle();
		    try {
				b.putByteArray("receivedSchedule", receivedSchedule.serialize());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    intent2.putExtras(b);
	        startActivity(intent2);
	        dataSource.close();
	        finish();
	        return;
		}
	}

	public static ScheduleData getSelectedUserSchedule() {
		if(receivedSchedule != null) return receivedSchedule.schedule;
		
		return null;
	}
	
    public void testBeam(View view) {
    	SharedPreferences p = getSharedPreferences(MainMenuActivity.PREFS_NAME, 0);
		SchedulesDataSource dataSource = new SchedulesDataSource(this);
    	dataSource.open();
    	UserData user = dataSource.getUserFromSid(p.getLong(getString(R.string.settings_owner_facebook_id), 1));
		ScheduleData mySchedule = dataSource.getScheduleFromId(p.getLong(getString(R.string.settings_owner_active_schedule_id), 1));
		ArrayList<TimeBlockData> myTimeBlocks = dataSource.getSchdeduleTimeBlocks(mySchedule.id);
		
		ScheduleTimeBlockWrapper beamMessage = new ScheduleTimeBlockWrapper();
		beamMessage.user = user;
		beamMessage.schedule = mySchedule;
		beamMessage.timeBlocks = myTimeBlocks;
		receivedSchedule = beamMessage;
		
		dataSource.createUser(beamMessage.user.sid, beamMessage.user.name);
		dataSource.createSchedule(beamMessage.schedule.sid, beamMessage.schedule.name, true, beamMessage.schedule.ownerId, beamMessage.schedule.lastModified);
		for(TimeBlockData t : beamMessage.timeBlocks){
			dataSource.createTimeBlock(t.sid, t.name, t.startTime, t.endTime, t.day, t.blockTypeId, t.scheduleId, t.longitude, t.latitude);
		}

		Intent intent = new Intent(this, BeamDiffActivity.class);
		Bundle b = new Bundle();
	    try {
			b.putByteArray("receivedSchedule", receivedSchedule.serialize());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    intent.putExtras(b);
        startActivity(intent);
        dataSource.close();
        finish();
        return;
    }
}
