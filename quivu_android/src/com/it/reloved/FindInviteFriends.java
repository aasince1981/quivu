package com.it.reloved;

import java.util.List;

import com.it.reloved.utils.FBConnector;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile.Properties;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindInviteFriends extends RelovedPreference {
	public static final String[] PERMS = new String[] { "email","read_friendlists","manage_notifications" };
	private TextView tvSearch,tvRecommended,tvPromote,tvFindFacebookFriends,tvInviteFbFriends;
	private TextView tvContacts,tvShoutoutFacebook,tvTwitter;
	private TextView tvHeaderText;
	private ImageView ivBack;
	//private FacebookConnector fbConnector;
	Context mContext =this;
	String share_text="Hop onto Quivu, my fav shopping app! Snap to sell & Chat to buy. Free for iPhone & Android: http://qvivu.com";
	Intent intent=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_invite_friends);
		FBConnector.configureMyFB(FindInviteFriends.this); 
		initLayout();
		tvHeaderText.setText("Find & Invite Friends");
	}

	@Override
	public void onResume() {
		super.onResume();
		FBConnector.mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	/* method for initialising init components */
	
	private void initLayout()
	{
		tvHeaderText =(TextView)findViewById(R.id.tv_header);
		ivBack =(ImageView)findViewById(R.id.iv_back_header);
		tvSearch =(TextView)findViewById(R.id.tv_search_FIfriends);
		tvRecommended =(TextView)findViewById(R.id.tv_recommended_users_FIfriends);
		tvPromote =(TextView)findViewById(R.id.tv_promote_RELOVED_FIfriends);
		tvFindFacebookFriends =(TextView)findViewById(R.id.tv_find_facebook_FIfriends);
		tvInviteFbFriends =(TextView)findViewById(R.id.tv_invite_facebook_FIfriends);
		tvContacts =(TextView)findViewById(R.id.tv_invite_contects_FIfriends);
		tvShoutoutFacebook =(TextView)findViewById(R.id.tv_shoutout_facebook_FIfriends);
		tvTwitter =(TextView)findViewById(R.id.tv_shoutout_twitter_FIfriends);
		
		
		ivBack.setOnClickListener(this);
		tvSearch.setOnClickListener(this);
		tvRecommended.setOnClickListener(this);
		tvPromote.setOnClickListener(this);
		tvFindFacebookFriends.setOnClickListener(this);
		tvInviteFbFriends.setOnClickListener(this);
		tvContacts.setOnClickListener(this);
		tvShoutoutFacebook.setOnClickListener(this);
		tvTwitter.setOnClickListener(this);
		
		

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}

	/*perform click*/
	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.tv_search_FIfriends:
			intent =new Intent(FindInviteFriends.this, SearchByUsername.class);
			startActivity(intent);
			break;
		case R.id.tv_recommended_users_FIfriends:

			break;
		case R.id.tv_promote_RELOVED_FIfriends:
			intent =new Intent(FindInviteFriends.this, Promote.class);
			startActivity(intent);
			break;
		case R.id.tv_find_facebook_FIfriends:
			//fbConnector=new FacebookConnector(getString(R.string.FACEBOOK_APPID), ((Activity)mContext), mContext, PERMS);
			//fbConnector.login("findfriends");
			FBConnector.mSimpleFacebook.invite("I invite you to use this app", FBConnector.onInviteListener,"dfs");
			
			break;
		case R.id.tv_invite_facebook_FIfriends:
			//FBConnector.mSimpleFacebook.getFriends(FBConnector.onFriendsListener);
		//	FBConnector.mSimpleFacebook.isAllPermissionsGranted();
			FBConnector.mSimpleFacebook.invite("I invite you to use this app", FBConnector.onInviteListener,"dfs");
			//fbConnector=new FacebookConnector(getString(R.string.FACEBOOK_APPID), ((Activity)mContext), mContext, PERMS);
			//fbConnector.apprequestDailog("Join QUIVU");
			break;
		case R.id.tv_invite_contects_FIfriends:
			
			String text="Get quivu app now! Hop onto quivu, my fav shopping app! Snap to sell & Chat to buy. Free for iPhone & Android: http://quivu.co/app via @thequivu";
			Intent i1 = new Intent(Intent.ACTION_SEND);
			i1.setType("message/rfc822");
			i1.putExtra(Intent.EXTRA_EMAIL,new String[] { "" });
			i1.putExtra(Intent.EXTRA_SUBJECT, "Get Quivu app now!");
			i1.putExtra(Intent.EXTRA_TEXT, text);
			try {
				startActivity(Intent.createChooser(i1, "Email:"));
			} catch (ActivityNotFoundException ex) {
				Toast.makeText(FindInviteFriends.this,"There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_shoutout_twitter_FIfriends:
			shareonTwitter();
			break;
		case R.id.tv_shoutout_facebook_FIfriends:
			shareOnFacebook();
			break;
		}
	}
	
	/*method for share on twitter*/
	private void shareonTwitter()
	{
	 if (verificaTwitter()) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");			
			shareIntent.putExtra(Intent.EXTRA_TEXT, share_text);
			shareIntent.setPackage("com.twitter.android");
			startActivity(shareIntent);
		} else {
			createConfirmDialog("https://play.google.com/store/apps/details?id=com.twitter.android",FindInviteFriends.this);
					
		}}
	
	/*method for share on fb*/
	public void shareOnFacebook() {
		
		boolean hasThirdpartyApp = false;
		Intent shareIntent = new Intent();
		shareIntent.setClassName("com.facebook.katana","com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
		shareIntent.setAction("android.intent.action.SEND");
		shareIntent.setType("text/plain");
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		for (final ResolveInfo app : activityList) {
			if ((app.activityInfo.name).contains("facebook")) {
				hasThirdpartyApp = true;
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				shareIntent.setComponent(name);
				startActivity(shareIntent);
				break;
			}
		}
		if (!hasThirdpartyApp)
			createConfirmDialog("https://play.google.com/store/apps/details?id=com.facebook.katana",FindInviteFriends.this);
	}
	
	private boolean verificaTwitter() {
		boolean instalado = false;
		try {
			@SuppressWarnings("unused")
			ApplicationInfo info = getPackageManager().getApplicationInfo("com.twitter.android", 0);
			instalado = true;
		} catch (NameNotFoundException e) {
			instalado = false;
		}
		return instalado;
	}
}
