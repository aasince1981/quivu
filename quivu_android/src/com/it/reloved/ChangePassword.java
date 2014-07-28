package com.it.reloved;

import com.it.reloved.dao.UserDAO;
import com.it.reloved.utils.AppSession;

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
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePassword extends RelovedPreference{

	ImageView ivBackHeader; 
	EditText oldPasswordEditText,newPasswordEditText,confirmPasswordEditText;
	Button submitButton;
	String oldStr="",newStr="",confirmStr="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
	
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		oldPasswordEditText=(EditText)findViewById(R.id.et_old_password_change);
		newPasswordEditText=(EditText)findViewById(R.id.et_new_password_change);
		confirmPasswordEditText=(EditText)findViewById(R.id.et_confirm_password_change);
		submitButton=(Button)findViewById(R.id.btn_submit_change);
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
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.btn_submit_change:
			oldStr=oldPasswordEditText.getText().toString();
			newStr= newPasswordEditText.getText().toString();
			confirmStr=confirmPasswordEditText.getText().toString();
			if (isValidate(oldStr, newStr, confirmStr)) {
				if (isNetworkAvailable()) {
					new TaskChangePassword().execute();
				} else {
					Toast.makeText(ChangePassword.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
			}
			break;

		default:
			break;
		}
	}
	
	/*method for validations*/
	boolean isValidate(String oldPass,String newPass,String confPass) {
		if (oldPass == null || oldPass.equals("")) {
			Toast.makeText(ChangePassword.this, "Please enter old password.", Toast.LENGTH_LONG).show();
			return false;
		} else if (newPass == null || newPass.equals("")) {
			Toast.makeText(ChangePassword.this, "Please enter new password.", Toast.LENGTH_LONG).show();
			return false;
		}else if (confPass == null || confPass.equals("")) {
			Toast.makeText(ChangePassword.this, "Please enter confirm password.", Toast.LENGTH_LONG).show();
			return false;
		}else if (!newPass.equals(confPass)) {
			Toast.makeText(ChangePassword.this, "New password and Confirm password not match.", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}
	
	/*Task for change password*/
	private class TaskChangePassword extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(ChangePassword.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(ChangePassword.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ChangePassword.this);
				arr=new UserDAO(ChangePassword.this).changePassword(appSession.getBaseUrl(),
						getResources().getString(R.string.method_changePassword), oldStr,
						newStr, appSession.getUserId());						
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
						Toast.makeText(ChangePassword.this,arr[1],Toast.LENGTH_LONG).show();
						oldPasswordEditText.setText("");
						newPasswordEditText .setText("");
						confirmPasswordEditText.setText("");
					} else {
						Toast.makeText(ChangePassword.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ChangePassword.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
}
