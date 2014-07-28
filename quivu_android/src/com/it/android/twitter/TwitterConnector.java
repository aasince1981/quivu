package com.it.android.twitter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.it.android.twitter.TwitterApp.TwDialogListener;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TwitterConnector {
	
//	String status = "@9039211975"+" "+ "NAME:" + "sdafasdfasdff";
//	String status = "@AndroidDev"+" "+ "NAME:" + "sdafasdfasdff";
//	String string="";
	protected static final String TAG = "TwitterConnector ";
	public static String imgUrl="";
    private Context context;
	private TwitterApp twitterApp;
	
	public TwitterConnector( String twitterConsumerKey,
			String twitterConsumeSecret, Context context, String imgUrl ) {
		this.context = context;
		this.imgUrl = imgUrl;
		
		twitterApp = new TwitterApp( context, twitterConsumerKey,
				 twitterConsumeSecret);	   	
	}
	
	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {
			Log.i("onComplete", "value::"+value);
		//	if (string.equals("ImageInfo")) {
				try {
					Bitmap bmp = RelovedPreference.loadBitmap(imgUrl); 
					twitterApp.uploadPic(storeToSDCard(bmp), "Tweet fom quivu!");
				} catch (Exception e) {
					e.printStackTrace();
				}
		/*	} else {
			String username = twitterApp.getUsername();
			username = (username.equals("")) ? "No Name" : username;
			Log.i(TAG, "username: "+username);
			//Toast.makeText(context,"onComplete", Toast.LENGTH_LONG).show();
			new TaskLoadBitmap().execute();
			} */
		}		
		@Override
		public void onError(String value) {
			Toast.makeText(context,"onError", Toast.LENGTH_LONG).show();
		}
	};
	
	class TaskLoadBitmap extends AsyncTask<Void, Void, Void>{
		Bitmap bmp=null;
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				bmp = RelovedPreference.loadBitmap(imgUrl);
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
			File imgFile=storeToSDCard(bmp);
			TwitterApp twitterApp=new TwitterApp(context, context.getString(R.string.TWITTER_CONSUMER_KEY),
					context.getString(R.string.TWITTER_CONSUMER_SECRET));
				new TaskUploadPic().execute(imgFile);
				
			//twitterApp.uploadPic(imgFile, "Tweet fom Boudoir!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	

		
	class TaskUploadPic extends AsyncTask<File, Void, Void>{
		boolean flag=false;
		ProgressDialog mProgressDialog;
		@Override
		protected Void doInBackground(File... params) {
			try{
				flag = twitterApp.uploadPic(params[0], "Tweet fom Boudoir!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(context, "",
					"Please wait..");
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			try {
				if (flag) {
					Toast.makeText(context, "Successfully tweeted from Boudoir!", Toast.LENGTH_LONG).show();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	private File storeToSDCard(Bitmap bitmap) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		FileOutputStream fo;
		try {
			fo = new FileOutputStream("/mnt/sdcard/quivu.jpg");
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File("/mnt/sdcard/quivu.jpg");
		return file;
	}
	
	public void loginToTwitter() {
		//context=mContext;
		twitterApp.setListener(mTwLoginDialogListener);
		  if (twitterApp.hasAccessToken()) {
				String username = twitterApp.getUsername();
				username		= (username.equals("")) ? "Unknown" : username;
				Log.i(TAG, "username: "+username);
				//new Task4GetUserAccess().execute();
				//Intent mIntent=new Intent(LuckyzActivity.this,LendActivity.class);
				//startActivity(mIntent);
				/*Bitmap bmp=MyPrefActivity.loadBitmap(string);
				File imgFile=storeToSDCard(bmp);
				TwitterApp twitterApp=new TwitterApp(context, context.getString(R.string.TWITTER_CONSUMER_KEY),
						context.getString(R.string.TWITTER_CONSUMER_SECRET),"");
				try {
					twitterApp.uploadPic(imgFile, "Tweet fom Boudoir!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		 else
			 twitterApp.authorize();
	}
	
	public void logoutToTwitter() {
		twitterApp.resetAccessToken();
	}
	
	public void tweetToTwitter( String msgText ) {
		if (twitterApp.hasAccessToken()) {
			try {
				postToTwitter(msgText);
			} catch (Exception e) {
				Log.i(TAG, e.getMessage());
				//Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
			}
			//postToTwitter(msgText);
		} else {
			twitterApp.authorize();
		}
	}
	
	private void postToTwitter(final String msgText) {
        new Thread() {
            @Override
            public void run() {
                int what = 0;
                try {
                	twitterApp.updateStatus(msgText);
                } catch (Exception e) {
                    what = 1;
                }
                mHandler.sendMessage(mHandler.obtainMessage(what));               
            }
        }.start();
	} 
        
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String text = (msg.what == 0) ? "Posted to Twitter successfully" : "Post to Twitter failed";
           Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    };


}
