package com.it.reloved.utils;

import java.util.ArrayList;
import java.util.List;

import android.Manifest.permission;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.it.reloved.AddProduct;
import com.it.reloved.FindCommanFacebookFriends;
import com.it.reloved.MainScreen;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.TabSample;
import com.it.reloved.dao.UserDAO;
import com.it.reloved.dto.UserDTO;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnFriendsListener;
import com.sromku.simple.fb.listeners.OnInviteListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class FBConnector {

	// ---------------------------------------------------------------
	protected static final String TAG = null;
	public final static Permission[] PERMISSION = new Permission[] {/*Permission.PUBLIC_PROFILE,*/
			Permission.USER_PHOTOS, Permission.EMAIL, Permission.PUBLISH_ACTION,Permission.READ_FRIENDLISTS,Permission.USER_FRIENDS };
	private final static String NAME_SPACE = /* "quivutestingapp" */"quivuideal";
//	public static OnPublishListener onPublishListener = null;

	public static SimpleFacebook mSimpleFacebook = null;
	private static Context context = null;
	private static ProgressDialog pd = null;
	// ---------------------------------------------------------------

	private static String id = "";
	private static String email = "";
	private static String fname = "";
	private static String lname = "";
	private static String name = "";
	private static String gender = "";
	private static String fbImagePath = "";

	public static void configureMyFB(Context _context) {
		context = _context;
		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
				.setAppId(
						context.getResources().getString(
								R.string.FACEBOOK_APPID))
				.setNamespace(NAME_SPACE).setPermissions(PERMISSION).build();
		SimpleFacebook.setConfiguration(configuration);
	}

	public static OnLoginListener onLoginListener = new OnLoginListener() {
			
		
		@Override
		public void onLogin() {
			try {
				pd = ProgressDialog.show(context, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// change the state of the button or do whatever you want
			Log.i(TAG, "Logged in");
			//Toast.makeText(context, "Logged in", Toast.LENGTH_SHORT).show();
			mSimpleFacebook.getProfile(onProfileListener);
		}
		@Override
		public void onNotAcceptingPermissions(Permission.Type type) {
			// user didn't accept READ or WRITE permission
			Log.w(TAG,
					String.format("You didn't accept %s permissions",
							type.name()));
		}
		@Override
		public void onThinking() {
			Log.i(TAG, "onThinking");
		}

		@Override
		public void onException(Throwable arg0) {
			Log.i(TAG, "onException");
		}

		@Override
		public void onFail(String arg0) {
			Log.i(TAG, "onFail");
		}

		/*
		 * You can override other methods here: onThinking(), onFail(String
		 * reason), onException(Throwable throwable)
		 */
	};

	private static OnProfileListener onProfileListener = new OnProfileListener() {
		@Override
		public void onComplete(com.sromku.simple.fb.entities.Profile response) {

		//	Toast.makeText(context, "" + response.getEmail(), 1).show();
			Log.d(TAG, "Response: " + response.toString());
			id = response.getId();
			name = response.getName();
			email = response.getEmail();
			gender = response.getGender();
			fname = response.getFirstName();
			lname = response.getLastName();
			((Activity) context).runOnUiThread(new Runnable() {
				public void run() {
					try {
						if (RelovedPreference.isNetworkAvailable()) {
							new TaskImage(context)
									.execute("http://graph.facebook.com/" + id
											+ "/picture?type=large");
						} else {
							Toast.makeText(
									context,
									context.getResources().getString(
											R.string.NETWORK_ERROR),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		};

	};

	// logout listener
	public static OnLogoutListener onLogoutListener = new OnLogoutListener() {

		@Override
		public void onLogout() {
			Log.i(TAG, "You are logged out");
		}

		@Override
		public void onThinking() {
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onFail(String reason) {
		}

		/*
		 * You can override other methods here: onThinking(), onFail(String
		 * reason), onException(Throwable throwable)
		 */
	};

	private static class TaskImage extends AsyncTask<String, Void, Void> {
		Bitmap bmp = null;
		Context context;
		TaskImage(Context _context) {
			context = _context;
		}

		@Override
		protected Void doInBackground(String... params) {
			String imageURL = params[0];
			Drawable myDrawable = RelovedPreference.downloadImage(imageURL);
			bmp = ((BitmapDrawable) myDrawable).getBitmap();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (bmp != null) {
				fbImagePath = RelovedPreference.getFilePath(bmp,
						"FBProfileImage");
				Log.i("999999999999999999999999", "fbImagePath=" + fbImagePath);
				if (RelovedPreference.isNetworkAvailable()) {
					new TaskForSocialLogin(context).execute(id, name, email,fbImagePath, gender);
				
				} else {
					Toast.makeText(context,
							context.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}// TaskImage

	private static class TaskForSocialLogin extends AsyncTask<String, Void, Void> {
		// ProgressDialog pd = null;
		List<UserDTO> userList = new ArrayList<UserDTO>();
		Context context;

		TaskForSocialLogin(Context _context) {
			context = _context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				// pd = ProgressDialog.show(context,
				// "",context.getResources().getString(R.string.logging_in));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				AppSession appSession = new AppSession(context);
				userList = new UserDAO(context).socialUserRegistration(
						appSession.getBaseUrl(), context.getResources()
								.getString(R.string.method_registration),
						params[0], params[1], params[2], ""
								+ MainScreen.latitiude, ""
								+ MainScreen.longitude, MainScreen.gcmId,
						MainScreen.appVersion, MainScreen.deviceId, "1", "2",
						MainScreen.strDefaultCity, params[3], params[4], fname,
						lname);

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
						AppSession appSession = new AppSession(context);
						appSession.storeConnections(userList);
						appSession.storeUserId(userList.get(0).getUserId());
						Intent loginIntent = new Intent(context,
								TabSample.class);
						loginIntent.putExtra("fromClass", "");
						context.startActivity(loginIntent);
					} else {
						Toast.makeText(context, userList.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,
							context.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}// TaskForSocialLogin

	// publish feed
	public static OnPublishListener onPublishListener = new OnPublishListener() {
		private Intent intent;

		@Override
		public void onComplete(String postId) {
			Log.i(TAG, "Published successfully. The new post id = " + postId);
			intent = new Intent(context, TabSample.class);
			intent.putExtra("fromClass", "");
			context.startActivity(intent);
			((Activity) context).finish();
		}

		/*
		 * You can override other methods here: onThinking(), onFail(String
		 * reason), onException(Throwable throwable)
		 */
	};
		
	// Invite Friend
	
	public static OnInviteListener onInviteListener = new OnInviteListener() {
	    @Override
	    public void onComplete(List<String> invitedFriends, String requestId) {
	        Log.i(TAG, "Invitation was sent to " + invitedFriends.size() + " users with request id " + requestId); 
	    }

	    @Override
	    public void onCancel() {
	        Log.i(TAG, "Canceled the dialog");
	    }

		@Override
		public void onException(Throwable throwable) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFail(String reason) {
			// TODO Auto-generated method stub
			
		}

	    /* 
	     * You can override other methods here: 
	     * onFail(String reason), onException(Throwable throwable)
	     */
	};
	
	// Get My Friends
	
	public static OnFriendsListener onFriendsListener = new OnFriendsListener() {       
		String stringdIds="";
		StringBuilder sb=new StringBuilder();
		
	    @Override
	    public void onComplete(List<Profile> friends) {
	        Log.i(TAG, "Number of friends = " + friends.size());	        
	        for(int i=0; i<friends.size(); i++) {	           	
	        	if(i==0)				
					sb.append(friends.get(i).getId());				
				else				
					sb.append(",").append(friends.get(i).getId());	        	
	        }
	        stringdIds =sb.toString();	     
	        Intent intent = new Intent(context, FindCommanFacebookFriends.class);
	        intent.putExtra("from", "fbconnector");
	        intent.putExtra("friends_ids", stringdIds);
	        context.startActivity(intent);
	        } 
	       	};
	
}
