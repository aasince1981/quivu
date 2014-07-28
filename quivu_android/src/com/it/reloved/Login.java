package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.UserDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;

public class Login extends RelovedPreference  {

	private Context context = this;
	private Button btnLogin;
	private EditText etUsername, etPassword;
	private String strUsername = "", strPassword = "",gcmId = "", deviceId = "",appVersion = "";
	CheckBox checkBox;
	TextView forgotTextView,rememberTextView;	
	Intent intent=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		initLayout();
		if (isNetworkAvailable()) {
			gcmId = getGCMId();
			deviceId = getIMEIorDeviceId();
			appVersion = getAppVersion();
		}else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
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
	/* method for initialising init components */
	private void initLayout() {		
		btnLogin = (Button) findViewById(R.id.btn_login_login);
		btnLogin.setOnClickListener(this);
		btnLogin.setTypeface(typeface);
		
		etUsername = (EditText) findViewById(R.id.et_username_login);
		etPassword = (EditText) findViewById(R.id.et_password_login);	
		forgotTextView=(TextView)findViewById(R.id.tv_forgot_login);
		forgotTextView.setOnClickListener(this);
		forgotTextView.setTypeface(typeface);
		
		rememberTextView=(TextView)findViewById(R.id.tv_remember);
		rememberTextView.setTypeface(typeface);
		
		checkBox=(CheckBox)findViewById(R.id.cb_show__login);
		AppSession  appSession=new AppSession(Login.this);
		if (appSession.isRememberMe()) {
			checkBox.setChecked(true);
			etUsername.setText(appSession.getLoginId());
			etPassword.setText(appSession.getLoginPassword());
		} else {
			checkBox.setChecked(false);
			etUsername.setText(appSession.getLoginId());
			etPassword.setText(appSession.getLoginPassword());
		}		
	}
	
	/*perform click*/
	@Override
	public void onClick(View v) {	
		switch (v.getId()) {
			case R.id.btn_login_login:				
				if (isAndroidEmulator()) {
					gcmId = "APA91bFiUjakQmWgRTXVdvK2UDUEPJlm1XGv-RpWtlxmKlpt76-vHZj-T3-pYB3moKLZOD50A3o4GHGPJQJPFZCcmtDqNO7kB5Nxhzcl1DHJfVUmPIaqW9ziYVG3gI_hNenRezHrq3T2ESHtqA_qxn-ISB_Q6NeXTdNpcB1Z2Q3kF6JkpD_hBqM";
				} else {
					strUsername = etUsername.getText().toString();
					strPassword = etPassword.getText().toString();
					if (isValidate()) {
						AppSession appSession=new AppSession(Login.this);
						if (checkBox.isChecked()) {							
							appSession.setRememberMe(true);
							appSession.setLoginId(strUsername);
							appSession.setLoginPassword(strPassword);
						} else {
							appSession.setRememberMe(false);
							appSession.setLoginId("");
							appSession.setLoginPassword("");
						}
						if (isNetworkAvailable()) {
							new TaskForLogin().execute();
						} else {
							Toast.makeText(context,getString(R.string.NETWORK_ERROR),
								Toast.LENGTH_LONG).show();
						}
					}
				}
			break;		
			case R.id.tv_forgot_login:		
				intent=new Intent(Login.this, ForgotPassword.class);
				//intent.putExtra("url", "");
				startActivity(intent);
				break;
		}
	}
	
	/*method for validation*/
	boolean isValidate() {
		if (strUsername == null || strUsername.equals("")) {
			Toast.makeText(context,"Please enter Email or Username.",Toast.LENGTH_LONG).show();
			return false;
		} else if (strPassword == null || strPassword.equals("")) {
			Toast.makeText(context,"Please enter Password",Toast.LENGTH_LONG).show();
			return false;
		} else if (gcmId == null || gcmId.equals("")) {
			Toast.makeText(context,"We are not getting Device Notification Id. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				gcmId = getGCMId();	
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();			
			return false;
		} else if (deviceId == null || deviceId.equals("")) {
			Toast.makeText(context,"We are not getting Device Unique Id. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				deviceId = getIMEIorDeviceId();
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			return false;
		} else if (appVersion == null || appVersion.equals("")) {
			Toast.makeText(context,"We are not getting Current Application Version. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				appVersion = getAppVersion();
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	/*Task for login*/
	private class TaskForLogin extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		List<UserDTO> userList = new ArrayList<UserDTO>();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(context, "",getResources().getString(R.string.logging_in));
				pd = ProgressDialog.show(Login.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(Login.this);
				userList = new UserDAO(Login.this).loginUser(appSession.getBaseUrl(),getResources().getString(R.string.method_login),
						strUsername, strPassword, gcmId, deviceId, appVersion);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);			
			try {
				pd.dismiss();
				if (userList != null) {
					if (userList.get(0).getSuccess().equals("1")) {
						Toast.makeText(context,userList.get(0).getMsg(),Toast.LENGTH_LONG).show();
						AppSession appSession=new AppSession(Login.this);
						appSession.storeConnections(userList);
						appSession.storeUserId(userList.get(0).getUserId());						
						Intent loginIntent = new Intent(context, TabSample.class);
						loginIntent.putExtra("fromClass", "");
						startActivity(loginIntent);						
					} else {
						Toast.makeText(context,userList.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	

}
