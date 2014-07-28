package com.it.reloved;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.it.reloved.dao.UserDAO;
import com.it.reloved.utils.AppSession;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewClass extends RelovedPreference {

	ImageView ivBackHeader; 
	Intent intent = null;
	String content = "";
	WebView webView;
	private ProgressDialog mSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		intent = getIntent();
		if (intent != null) {			
			content = intent.getStringExtra("content");
		}		
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);

		/*mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage("Loading...");*/
		
		mSpinner = ProgressDialog.show(WebViewClass.this, null, null);
		mSpinner.setContentView(R.layout.progressloader);

		webView = (WebView) findViewById(R.id.webView1);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().getBuiltInZoomControls();
		webView.setWebViewClient(new OAuthWebViewClient());
		
		if (isNetworkAvailable()) {
			new TaskForContent().execute(content);
		} else {
			Toast.makeText(WebViewClass.this,getResources().getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG).show();
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
		case R.id.iv_back_header:
			finish();
			break;

		default:
			break;
		}
	}
	
	//Task for content
	private class TaskForContent extends AsyncTask<String, Void, Void> {
		ProgressDialog pd = null;	
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(WebViewClass.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(WebViewClass.this);
				arr = new UserDAO(WebViewClass.this).getContent(appSession.getBaseUrl(), 
						getResources().getString(R.string.method_getContentPage),content);
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
						webView.loadData("<font color='#000'>"+arr[2]+"</font>", "text/html", "utf-8");
					} else {
						Toast.makeText(WebViewClass.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(WebViewClass.this, getResources().getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private class OAuthWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return true;
		}
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mSpinner.dismiss();
		}
	}
}
