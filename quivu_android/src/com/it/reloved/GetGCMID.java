package com.it.reloved;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class GetGCMID extends FragmentActivity {
	private static ConnectivityManager cm;
	IntentFilter gcmFilter;

	protected static String GCMSenderId = "453106993389";
	private String broadcastMessage = "No broadcast message";
	private static String regId = "";
	private String registrationStatus = "Not yet registered";
	
	
	public BroadcastReceiver gcmReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			broadcastMessage = intent.getExtras().getString("gcm");
			broadcastMessage = broadcastMessage + "Zomo";

			if (broadcastMessage != null) {
				// display our received message
				// tvBroadcastMessage.setText(broadcastMessage);
			}
		}
	};
	public String getGCMId() {
		try{
	
		gcmFilter = new IntentFilter();
		gcmFilter.addAction("GCM_RECEIVED_ACTION");
		/*if (isNetworkAvailable()) {*/
			registerClient();
			RelovedPreference.Notification_Id=regId;
			Log.i("registrationId", "=="+RelovedPreference.Notification_Id);
			registerReceiver(gcmReceiver, gcmFilter);
			return regId;
		/*} else {
			Toast.makeText(getBaseContext().getApplicationContext(),"Server is not reachable. Please try again later! ", Toast.LENGTH_LONG).show();
			return null;
		}	*/
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}catch (Error r) {
			r.printStackTrace();
			return null;
		}		
	}
	
	
	public void registerClient() {

		try {
			// Check that the device supports GCM (should be in a try / catch)
			GCMRegistrar.checkDevice(getBaseContext().getApplicationContext());
			// Check the manifest to be sure this app has all the required
			// permissions.
			GCMRegistrar.checkManifest(getBaseContext().getApplicationContext());

			// Get the existing registration id, if it exists.
			regId = GCMRegistrar.getRegistrationId(getBaseContext().getApplicationContext());

			Log.i("msg 1", "Registration ID = " + regId);

			if (regId.equals("")) {

				registrationStatus = "Registering...";
				Log.i("msgssssssss 1", "registrationStatus = "
						+ registrationStatus);
				// tvRegStatusResult.setText(registrationStatus);

				// register this device for this project
				GCMRegistrar.register(this, GCMSenderId);
				regId = GCMRegistrar.getRegistrationId(this);
				Log.i("msg 2", "Registration ID = " + regId);
				registrationStatus = "Registration Acquired";
				// This is actually a dummy function. At this point, one
				// would send the registration id, and other identifying
				// information to your server, which should save the id
				// for use when broadcasting messages.
				sendRegistrationToServer();

			} else {
				registrationStatus = "Already registered";
				// MyPreferenceActivity.registrationId = regId;
				Log.v("MESSAGE", "Already registered");
				// Toast.makeText(getBaseContext(),
				// "Already registered",Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {

			e.printStackTrace();
			registrationStatus = e.getMessage();

		}

		Log.v("Registration status", registrationStatus);
		// tvRegStatusResult.setText(registrationStatus);
		// tv3.setText(registrationStatus);
		// This is part of our CHEAT. For this demo, you'll need to
		// capture this registration id so it can be used in our demo web
		// service.
		// Log.d(TAG, regId);

	}

	private void sendRegistrationToServer() {
		// This is an empty placeholder for an asynchronous task to post the
		// registration
		// id and any other identifying information to your server.

		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		// sets the app name in the intent
		registrationIntent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", GCMSenderId);
		Log.i("dssfgvfsd", "fdsdsfsdf");
		startService(registrationIntent);
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("BroadcastMessage", broadcastMessage);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		broadcastMessage = savedInstanceState.getString("BroadcastMessage");
		// tvBroadcastMessage.setText(broadcastMessage);

	}
	@Override
	protected void onDestroy() {
			
		try {
			super.onDestroy();
			unregisterReceiver(gcmReceiver);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
	}
}
