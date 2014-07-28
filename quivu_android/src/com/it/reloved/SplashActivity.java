package com.it.reloved;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.utils.AppSession;

public class SplashActivity extends RelovedPreference {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		//Task for base url
		if (isNetworkAvailable()) {
			new TaskForUrls().execute();
		} else {
			Toast.makeText(SplashActivity.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
			finish();
		}

		/*new Thread() {
			public void run() {
				try {
					for (int i = 50; i <= 3000; i += 50) {
						sleep(50);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					
				}
			}
		}.start();*/
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}
	
	
	// Task for base url
	public class TaskForUrls extends AsyncTask<Void, Void, Void> {
		String responce = "";
		//ProgressDialog mProgressDialoge = null;

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				//mProgressDialoge.dismiss();
				if (responce.equals("1")) {
					Thread.sleep(2000);
					AppSession appSession=new AppSession(SplashActivity.this);
					if(appSession.getUserId().equals(null)||appSession.getUserId().equals("")){
						startActivity(new Intent(SplashActivity.this,MainScreen.class));
						finish();
					}else{
						startActivity(new Intent(SplashActivity.this,TabSample.class)
						.putExtra("fromClass", ""));					
						finish();
					}
				} else {
					finish();
					Toast.makeText(SplashActivity.this, getResources().getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//mProgressDialoge = ProgressDialog.show(SplashActivity.this, "",
			//		getResources().getString(R.string.please_wait));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				//AppSession appSession=new AppSession(SplashActivity.this);
				responce = new UtilityDAO(SplashActivity.this).getBaseUrl(SplashActivity.this, getResources().getString(R.string.method_baseurl));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
