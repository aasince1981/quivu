package com.it.reloved;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.it.reloved.dao.UserDAO;
import com.it.reloved.utils.AppSession;

public class ForgotPassword extends RelovedPreference {

	Intent intent;
	String emailStr = "";
	EditText emailEditText;
	Button submitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgot_password);
		/* method for initialising init components */
		emailEditText=(EditText)findViewById(R.id.et_email_forgot);
		submitButton=(Button)findViewById(R.id.btn_submit_forgot);
		submitButton.setOnClickListener(this);
		submitButton.setTypeface(typeface);
		
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
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_submit_forgot:
			emailStr=emailEditText.getText().toString();
			if (emailStr == null || emailStr.equals("")) {
				Toast.makeText(ForgotPassword.this,"Please enter Email.", Toast.LENGTH_LONG).show();				
			} else if (!checkEmail(emailStr)) {
				Toast.makeText(ForgotPassword.this, "Invalid Email address!",Toast.LENGTH_SHORT).show();				
			} else {
				if (isNetworkAvailable()) {
					new TaskForgotPassword().execute();
				} else {
					Toast.makeText(ForgotPassword.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
			}
			break;

		default:
			break;
		}
	}
	
	/*Task for forgot password*/
	private class TaskForgotPassword extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(ForgotPassword.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(ForgotPassword.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ForgotPassword.this);
				arr=new UserDAO(ForgotPassword.this).forgotPassword(appSession.getBaseUrl(),
						getResources().getString(R.string.method_forgotpassword), emailStr);
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
						Toast.makeText(ForgotPassword.this,arr[1],Toast.LENGTH_LONG).show();
						emailEditText.setText("");
					} else {
						Toast.makeText(ForgotPassword.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ForgotPassword.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
