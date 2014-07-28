package com.it.reloved;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;

public class NotificationSetting extends RelovedPreference{

	ImageView ivBackHeader;
	CheckBox cbVibrate,cbLight,cbSound,cbItemListed,cbNewOffer,cbNewChat,cbNewComment,
				cbNewOfferPush,cbNewChatPush;
	String vibrateStatus="",parameterName="",lightStatus="",soundStatus="",itemListedStatus="",
			newOfferStatus="",newChatStatus="",newCommentStatus="",newOfferPushStatus="",newChatPushStatus="";
	List<UserDTO> userDTOs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_settings);
	
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		
		cbVibrate=(CheckBox)findViewById(R.id.cb_vibrate_nsetting);
		cbLight=(CheckBox)findViewById(R.id.cb_light_nsetting);
		cbSound=(CheckBox)findViewById(R.id.cb_sound_nsetting);
		cbItemListed=(CheckBox)findViewById(R.id.cb_itemlisted_nsetting);
		cbNewOffer=(CheckBox)findViewById(R.id.cb_newoffer_nsetting);
		cbNewChat=(CheckBox)findViewById(R.id.cb_newchat_nsetting);
		cbNewComment=(CheckBox)findViewById(R.id.cb_new_comment_nsetting);
		cbNewOfferPush=(CheckBox)findViewById(R.id.cb_newoffer_push_nsetting);
		cbNewChatPush=(CheckBox)findViewById(R.id.cb_newchat_push_nsetting);
		
		
		cbVibrate.setOnClickListener(this);
		cbLight.setOnClickListener(this);
		cbSound.setOnClickListener(this);
		cbItemListed.setOnClickListener(this);
		cbNewOffer.setOnClickListener(this);
		cbNewChat.setOnClickListener(this);
		cbNewComment.setOnClickListener(this);
		cbNewOfferPush.setOnClickListener(this);
		cbNewChatPush.setOnClickListener(this);
		
		//setting cb according to value
		AppSession appSession=new AppSession(NotificationSetting.this);
		userDTOs=appSession.getConnections();
		if (userDTOs.get(0).getUserVibrateStatus().equals("1")) {
			cbVibrate.setChecked(true);
		} else {
			cbVibrate.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserLightStatus().equals("1")) {
			cbLight.setChecked(true);
		} else {
			cbLight.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserSoundStatus().equals("1")) {
			cbSound.setChecked(true);
		} else {
			cbSound.setChecked(false);
		}
		Log.i("NotificationSetting", "userDTOs.get(0).getUserEItemListedStatus()="+userDTOs.get(0).getUserEItemListedStatus());
		if (userDTOs.get(0).getUserEItemListedStatus().equals("1")) {
			cbItemListed.setChecked(true);
		} else {
			cbItemListed.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserENewOfferStatus().equals("1")) {
			cbNewOffer.setChecked(true);
		} else {
			cbNewOffer.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserENewChatStatus().equals("1")) {
			cbNewChat.setChecked(true);
		} else {
			cbNewChat.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserECommentStatus().equals("1")) {
			cbNewComment.setChecked(true);
		} else {
			cbNewComment.setChecked(false);
		}
		if (userDTOs.get(0).getUserPNOfferStatus().equals("1")) {
			cbNewOfferPush.setChecked(true);
		} else {
			cbNewOfferPush.setChecked(false);
		}
		
		if (userDTOs.get(0).getUserPNChatStatus().equals("1")) {
			cbNewChatPush.setChecked(true);
		} else {
			cbNewChatPush.setChecked(false);
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
	
	/*perform click*/
	@Override
	public void onClick(View v) {
		// UserVibrateStatus,UserSoundStatus,UserLightStatus,UserItemListStatus,UserENewOfferStatus,
		// UserENewChatStatus,UserPNChatStatus,UserPNOfferStatus,UserPNCommentStatus,UserPNSubscribeStatus,
		//UserPOfferStatus
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.cb_vibrate_nsetting:
			if (cbVibrate.isChecked()) {
				vibrateStatus="1";
			} else {
				vibrateStatus="0";
			}	
			parameterName="UserVibrateStatus";			
			Log.i("vibrateStatus", "vibrateStatus="+vibrateStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,vibrateStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_light_nsetting:
			if (cbLight.isChecked()) {
				lightStatus="1";
			} else {
				lightStatus="0";
			}	
			parameterName="UserLightStatus";			
			Log.i("lightStatus", "lightStatus="+lightStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,lightStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_sound_nsetting:
			if (cbSound.isChecked()) {
				soundStatus="1";
			} else {
				soundStatus="0";
			}	
			parameterName="UserSoundStatus";			
			Log.i("soundStatus", "soundStatus="+soundStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,soundStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_itemlisted_nsetting:
			if (cbItemListed.isChecked()) {
				itemListedStatus="1";
			} else {
				itemListedStatus="0";
			}	
			parameterName="UserItemListStatus";			
			Log.i("itemListedStatus", "itemListedStatus="+itemListedStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,itemListedStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_newoffer_nsetting:
			if (cbNewOffer.isChecked()) {
				newOfferStatus="1";
			} else {
				newOfferStatus="0";
			}	
			parameterName="UserENewOfferStatus";			
			Log.i("newOfferStatus", "newOfferStatus="+newOfferStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,newOfferStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_newchat_nsetting:
			if (cbNewChat.isChecked()) {
				newChatStatus="1";
			} else {
				newChatStatus="0";
			}	
			parameterName="UserENewChatStatus";			
			Log.i("newChatStatus", "newChatStatus="+newChatStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,newChatStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_new_comment_nsetting:
			if (cbNewComment.isChecked()) {
				newCommentStatus="1";
			} else {
				newCommentStatus="0";
			}	
			parameterName="UserECommentStatus";			
			Log.i("newCommentStatus", "newCommentStatus="+newCommentStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,newCommentStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_newoffer_push_nsetting:
			if (cbNewOfferPush.isChecked()) {
				newOfferPushStatus="1";
			} else {
				newOfferPushStatus="0";
			}	
			parameterName="UserPNOfferStatus";			
			Log.i("newOfferPushStatus", "newOfferPushStatus="+newOfferPushStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,newOfferPushStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.cb_newchat_push_nsetting:
			if (cbNewChatPush.isChecked()) {
				newChatPushStatus="1";
			} else {
				newChatPushStatus="0";
			}	
			parameterName="UserPNChatStatus";			
			Log.i("newChatPushStatus", "newChatPushStatus="+newChatPushStatus);
			if (isNetworkAvailable()) {
				new TaskNotificationSettings().execute(parameterName,newChatPushStatus);
			} else {
				Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}	
	}
	
	/*Task for notification settings*/
	private class TaskNotificationSettings extends AsyncTask<String, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(NotificationSetting.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(NotificationSetting.this);
				arr=new UtilityDAO(NotificationSetting.this).notificationSettings(appSession.getBaseUrl(),
						getResources().getString(R.string.method_setting), appSession.getUserId(),
						params[0], params[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);			
			try {
				pd.dismiss();
				if (arr != null) {
					if (arr[0].equals("1")) {						
						if (parameterName.equals("UserVibrateStatus")) {
							userDTOs.get(0).setUserVibrateStatus(vibrateStatus);
						} else if (parameterName.equals("UserLightStatus")) {
							userDTOs.get(0).setUserLightStatus(lightStatus);
						}else if (parameterName.equals("UserSoundStatus")) {
							userDTOs.get(0).setUserSoundStatus(soundStatus);
						}else if (parameterName.equals("UserItemListStatus")) {
							userDTOs.get(0).setUserEItemListedStatus(itemListedStatus);
						}else if (parameterName.equals("UserENewOfferStatus")) {
							userDTOs.get(0).setUserENewOfferStatus(newOfferStatus);
						}else if (parameterName.equals("UserENewChatStatus")) {
							userDTOs.get(0).setUserENewChatStatus(newChatStatus);
						}else if (parameterName.equals("UserECommentStatus")) {
							userDTOs.get(0).setUserECommentStatus(newCommentStatus);
						}else if (parameterName.equals("UserPNOfferStatus")) {
							userDTOs.get(0).setUserPNOfferStatus(newOfferPushStatus);
						}else if (parameterName.equals("UserPNChatStatus")) {
							userDTOs.get(0).setUserPNChatStatus(newChatPushStatus);
						}
					} else {
						Toast.makeText(NotificationSetting.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(NotificationSetting.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
