package com.schedushare.android;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class BeamActivity extends Activity implements CreateNdefMessageCallback  {
	NfcAdapter mNfcAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter == null){
        	Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
        	finish();
        	return;
        }
        //register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
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

    public void testBeam(View view){
    	
    }

	@TargetApi(16)
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		String text = ("Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
        		new NdefRecord[] { NdefRecord.createMime("application/vnd.com.example.android.beam", 
        				text.getBytes())}
        		);
        return msg;
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
		Toast.makeText(this, new String(msg.getRecords()[0].getPayload()), Toast.LENGTH_LONG).show();
	}	
}
