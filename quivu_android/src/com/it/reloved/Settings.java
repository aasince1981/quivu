package com.it.reloved;

import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.FBConnector;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends RelovedPreference {

	Intent intent=null;
	ImageView ivBackHeader; 
	TextView tvShareSetting,tvNotification,tvChangePassword,tvLogout,tvEmail,
			tvCommunityRules,tvAbout,tvBuiltinCamera;
	CheckBox cbBuiltinCamera;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		
		tvBuiltinCamera=(TextView)findViewById(R.id.tv_builtin_camera_setting);
		tvShareSetting=(TextView)findViewById(R.id.tv_share_settings_setting);
		tvShareSetting.setOnClickListener(this);
		tvNotification=(TextView)findViewById(R.id.tv_notifications_setting);
		tvNotification.setOnClickListener(this);
		tvChangePassword=(TextView)findViewById(R.id.tv_change_password_setting);
		tvChangePassword.setOnClickListener(this);
		tvLogout=(TextView)findViewById(R.id.tv_logout_setting);
		tvLogout.setOnClickListener(this);
		
		tvEmail=(TextView)findViewById(R.id.tv_email_setting);
		tvEmail.setOnClickListener(this);
		tvCommunityRules=(TextView)findViewById(R.id.tv_community_rules_setting);
		tvCommunityRules.setOnClickListener(this);
		tvAbout=(TextView)findViewById(R.id.tv_about_reloved_setting);
		tvAbout.setOnClickListener(this);
		
		tvChangePassword.setTypeface(typeface);
		tvLogout.setTypeface(typeface);		
		tvShareSetting.setTypeface(typeface);
		tvNotification.setTypeface(typeface);
		tvEmail.setTypeface(typeface);
		tvCommunityRules.setTypeface(typeface);
		tvAbout.setTypeface(typeface);
		tvBuiltinCamera.setTypeface(typeface);
		
		cbBuiltinCamera=(CheckBox)findViewById(R.id.cb_camera_setting);
		cbBuiltinCamera.setOnClickListener(this);
		
		//for setting cb cheked
		AppSession appSession=new AppSession(Settings.this);
		if (appSession.getBuiltinCamera().equals("1")) {
			cbBuiltinCamera.setChecked(true);
		} else {
			cbBuiltinCamera.setChecked(false);
		}
		
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
		
	}
	
	//perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.cb_camera_setting:
			AppSession appSession1=new AppSession(Settings.this);
			if (cbBuiltinCamera.isChecked()) {							
				appSession1.storeBuiltinCamera("1");			
			} else {
				appSession1.storeBuiltinCamera("");
			}
			break;
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.tv_share_settings_setting:
			
			break;
		case R.id.tv_notifications_setting:
			intent=new Intent(Settings.this, NotificationSetting.class);
			startActivity(intent);
			break;
		case R.id.tv_change_password_setting:
			intent=new Intent(Settings.this, ChangePassword.class);
			startActivity(intent);
			break;
		case R.id.tv_logout_setting:
			if(FBConnector.mSimpleFacebook!=null)
			FBConnector.mSimpleFacebook.logout(FBConnector.onLogoutListener);
			
			AppSession appSession=new AppSession(Settings.this);
			appSession.resetUserId();				
			appSession.resetConnections();
			appSession.setJson(AppSession.PROFILE_ME_JSON, "");
			appSession.setJson(AppSession.ACTIVITY_JSON, "");
			appSession.setJson(AppSession.CATEGORY_JSON, "");
			
			appSession.resetBuiltinCamera();
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction("CLOSE_ALL");
			this.sendBroadcast(broadcastIntent);
			((Activity)this).finish();			
			
			intent=new Intent(Settings.this, MainScreen.class);
			startActivity(intent);
			break;
		case R.id.tv_email_setting:
			Intent i1 = new Intent(Intent.ACTION_SEND);
			i1.setType("message/rfc822");
			i1.putExtra(Intent.EXTRA_EMAIL,new String[] { "support@reloved.com" });
			i1.putExtra(Intent.EXTRA_SUBJECT, "Support/Feedback");
			i1.putExtra(Intent.EXTRA_TEXT, "");
			try {
				startActivity(Intent.createChooser(i1, "Email:"));
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(Settings.this,"There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_community_rules_setting:
			intent=new Intent(Settings.this, WebViewClass.class);
			intent.putExtra("content", "CommunityRule");
			startActivity(intent);
			break;
		case R.id.tv_about_reloved_setting:
			intent=new Intent(Settings.this, WebViewClass.class);
			intent.putExtra("content", "AboutApp");
			startActivity(intent);
			break;
		}
	}
}
