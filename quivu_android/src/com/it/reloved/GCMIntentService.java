package com.it.reloved;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;


public class GCMIntentService extends GCMBaseIntentService{
	private final static String TAG=GCMIntentService.class.getSimpleName();
	final static String MY_ACTION = "MY_ACTION";
	Intent notificationIntent;
	Intent dintent;	
	String title ="",msg="",type="",data="",equipmentid="", userid="", startdate="",enddate="",
			requestedid="";		
	public GCMIntentService() {
		super(RelovedPreference.GCMSenderId);
	}
	@Override
	protected void onError(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e(TAG, "error registration id : " + regId);
	}
	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"messages in onMessage()"+ "msg      " );		
		reactToNotification(context, intent);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		handleRegistration(context, regId);
		Log.i(TAG,"msg 4"+ "Registration ID = " + regId);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub
	}
	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	private void reactToNotification(Context context, Intent intent) {
		//AnimationSound aSound=new AnimationSound(context, R.raw.phonering);
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
		        | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
		wakeLock.acquire();

		KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
		kl.disableKeyguard();
		
		title	= intent.getStringExtra("title");
		msg     = intent.getStringExtra("msg");
	    type    = intent.getStringExtra("type");
	    userid =  intent.getStringExtra("userid");
	    
		Log.i(TAG, " type: "+type);
		Log.i(TAG, " title: "+title);
		Log.i(TAG, " msg: "+msg);
		Log.i(TAG, " userid: "+userid);
//		05-30 14:59:37.644: I/GCMIntentService(20488):  type: CHAT_TYPE
//		05-30 14:59:37.644: I/GCMIntentService(20488):  title: Add Message
//		05-30 14:59:37.644: I/GCMIntentService(20488):  msg: sachin sent you the following private chat message: hello one

		
	
		int icon = R.drawable.icon_reloved;
		CharSequence tickerText = title;
		long when = System.currentTimeMillis();
		CharSequence contentTitle = title; 
		CharSequence contentText = msg;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, tickerText, when);
		notificationIntent=null;
		notificationIntent = new Intent(context, TabSample.class);
		notificationIntent.putExtra("fromClass", "Notification");
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,pendingIntent);
		notification.flags |= notification.FLAG_AUTO_CANCEL;
		
		AppSession appSession=new AppSession(context);
		List<UserDTO> userDTOs=appSession.getConnections();
		
		if(userDTOs.get(0).getUserSoundStatus().equals("1"))
		notification.defaults |= Notification.DEFAULT_SOUND;//| Notification.DEFAULT_LIGHTS;
		if(userDTOs.get(0).getUserLightStatus().equals("1"))
		notification.defaults|= Notification.DEFAULT_LIGHTS;
		if(userDTOs.get(0).getUserVibrateStatus().equals("1"))
		notification.vibrate = new long[] { 100L, 100L, 200L, 500L };
		
		notificationManager.notify(1, notification);
		
		PowerManager pm1 = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		WakeLock wl = pm1.newWakeLock(PowerManager.FULL_WAKE_LOCK| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
		wl.acquire();
		
	
		

	}


	private void handleRegistration(Context context, String regId) {
		// TODO Auto-generated method stub
	    RelovedPreference.Notification_Id = regId;
		Log.i(TAG,"msg 5"+ "Registration ID = " + regId);
	}
	
	
}
