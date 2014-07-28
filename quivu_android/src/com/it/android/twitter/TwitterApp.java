/**
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * http://www.londatiga.net
 */

package com.it.android.twitter;

import java.io.File;

import twitter4j.auth.AccessToken;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

public class TwitterApp {
		
	private Configuration configuration ;
	private static Twitter twitter;
	public static RequestToken requestToken;
	private static String login_url;
	
	public static Twitter mTwitter;
	private static TwitterSession mSession;
	public static AccessToken mAccessToken;
	
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private Context context;
	private boolean mInit = true;
	
//	private String mstr;
	public static final String CALLBACK_URL = "twitterapp://connect";
	private static final String TAG = "TwitterApp";
	public static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	
	public TwitterApp(Context context, String consumerKey, String secretKey) {
		this.context	= context;
	
		System.out.println("------------------inside varify01");
		mTwitter 		= new TwitterFactory().getInstance();
		mSession		= new TwitterSession(context);
		mProgressDlg	= new ProgressDialog(context);
		
		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//mstr=str;
		mConsumerKey 	= consumerKey;
		mSecretKey	 	= secretKey;
	
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(mConsumerKey);
		builder.setOAuthConsumerSecret(mSecretKey);
		 configuration = builder.build();
		
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		new FindTwitterLoginUrl().execute();
				
		mAccessToken	= mSession.getAccessToken();
		configureToken();		
	}
	
	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}
	
	
	private void configureToken() {
		if (mAccessToken != null) {
			if (mInit) {
				mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
				mInit = false;
			}		
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}
	
	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}
	
	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();	
			mAccessToken = null;
			Log.i(TAG, "Succesfully logout");
		}
	}
	
	public String getUsername() {	
		
		return mSession.getUsername();	
		
	}
	
	public void updateStatus(String status) throws Exception {
		try {
			//mTwitter.updateStatus(status);
			long tweet_id=mTwitter.updateStatus(status).getId();
			Log.v("Tweet Id:=","with out Image :::"+tweet_id);	
		} catch (TwitterException e) {
			throw e;
		}
	}
	
	public boolean uploadPic(File file, String message){
		 
	    try{
	    StatusUpdate status = new StatusUpdate(message);
	    status.setMedia(file);
	   
	    long tweet_id= mTwitter.updateStatus(status).getId();
	    Log.v("Tweet Id:=","with  Image :::"+tweet_id);}
	    catch(TwitterException e){	    	
	        Log.d("TAG", "Pic Upload error" + e.getMessage());
	        return false;	       
	    }
		return true;
	}

	
	public void authorize() {
		mProgressDlg.setMessage("Initializing ...");
		mProgressDlg.show();
		
		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;				
				try {				
				  authUrl=login_url;	
					what = 0;					
					Log.d(TAG, "Request token url " + authUrl);
				} catch (Exception e) {
					Log.d(TAG, "Failed to get request token");					
					e.printStackTrace();
				}				
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}
	
	public void processToken(String callbackUrl)  {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();
		
		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;
				
				try {				
					mAccessToken= twitter.getOAuthAccessToken(requestToken, verifier);
					configureToken();
					User user = mTwitter.verifyCredentials();
			        mSession.storeAccessToken(mAccessToken, user.getName());
			        Log.i(TAG, ""+mTwitter.getId());
			        Log.i(TAG, mSession.getUsername());
			        what = 0;
				} catch (Exception e){
					Log.d(TAG, "Error getting access token");
					
					e.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}
	
	private String getVerifier(String callbackUrl) {
		String verifier	 = "";
		System.out.println("------------------inside varify00");
		
			callbackUrl = callbackUrl.replace("twitterapp", "http");
			Uri uri = null;
			verifier = uri.parse(callbackUrl).getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);			
			System.out.println("verifier:="+verifier);
		return verifier;
	}
	
	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {
			@Override
			public void onComplete(String value) {
				processToken(value);
			}
			
			@Override
			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};
		
		new TwitterDialog(context, url, listener).show();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();
			
			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};
	
	public interface TwDialogListener {
		public void onComplete(String value);		
		
		public void onError(String value);
	}
	
	class FindTwitterLoginUrl extends AsyncTask<String, String, String> {

		
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			
			

		  // twitter = factory.getInstance();
			try {
				requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
				
				login_url=requestToken.getAuthenticationURL();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
			return login_url;
			
		}

		
		protected void onPostExecute(String login_url) {
			// dismiss the dialog after getting all products		
			System.out.println("AuthorizationURLURL := "+login_url);
			System.out.println("AuthenticationURL := "+login_url);
		
		}

	}

}