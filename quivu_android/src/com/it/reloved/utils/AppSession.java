package com.it.reloved.utils;

import java.lang.reflect.Type;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.it.reloved.dto.UserDTO;

@SuppressLint("CommitPrefEdits")
public class AppSession {

	private static SharedPreferences sharedPref;
	private static Editor editor;
	private static final String SHARED = "AShoes_Preferences";
	private static final String USER_ID = "user_id";
	private static final String BUILTIN_CAMERA = "builtin_camera";
	private static final String USERDTO = "USERDTO";
	//private static final String FB_ID = "fb_id";
	private static final String FB_ACCESSTOKEN = "fb_tokan";
	private static final String APP_ACCESSTOKEN = "app_tokan";
	
	public static final String ACTIVITY_JSON = "activity_json";
	public static final String CATEGORY_JSON = "category_json";	
	public static final String PROFILE_ME_JSON = "profile_me_json";
	public static final String ACTIVITY_SEEN_TIME = "activity_seen_time";
	public static final String CATEGORY_SEEN_TIME = "category_seen_time";	
	public static final String PROFILE_ME_SEEN_TIME = "profile_me_seen_time";

	public AppSession(Context context) {
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		editor = sharedPref.edit();
	}
	//////////////////
	public void storeBuiltinCamera(String camera) {
		editor.putString(BUILTIN_CAMERA, camera);
		editor.commit();
	}

	public void resetBuiltinCamera() {
		editor.putString(BUILTIN_CAMERA, null);
		editor.commit();
	}

	public String getBuiltinCamera() {
		String userid = sharedPref.getString(BUILTIN_CAMERA, "");
		return userid;
	}
	
	////////////////////
	

	public void setRememberMe(boolean isRememberMe) {
		editor = sharedPref.edit();
		editor.putBoolean("isRememberMe", isRememberMe);
		editor.commit();
	}

	public boolean isRememberMe() {
		return sharedPref.getBoolean("isRememberMe", false);

	}

	public void setLoginId(String loginId) {
		editor = sharedPref.edit();
		editor.putString("loginId", loginId);
		editor.commit();
	}

	public String getLoginId() {
		return sharedPref.getString("loginId", "");
	}

	// ----------------------------------------
	public void setBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("baseUrl", loginId);
		editor.commit();
	}

	public String getBaseUrl() {
		return sharedPref.getString("baseUrl", "");
	}

	public void setUserImageBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("userbaseUrl", loginId);
		editor.commit();
	}

	public String getUserImageBaseUrl() {
		return sharedPref.getString("userbaseUrl", "");
	}

	public void setCategoryBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("categorybaseUrl", loginId);
		editor.commit();
	}

	public String getCategoryBaseUrl() {
		return sharedPref.getString("categorybaseUrl", "");
	}

	public void setProductBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("productbaseUrl", loginId);
		editor.commit();
	}

	public String getProductBaseUrl() {
		return sharedPref.getString("productbaseUrl", "");
	}
	
	public void setFeedbackImageBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("FeedbackImageBaseUrl", loginId);
		editor.commit();
	}

	public String getFeedbackImageBaseUrl() {
		return sharedPref.getString("FeedbackImageBaseUrl", "");
	}
	
	public void setMessageImageBaseUrl(String loginId) {
		editor = sharedPref.edit();
		editor.putString("MessageImageBaseUrl", loginId);
		editor.commit();
	}

	public String getMessageImageBaseUrl() {
		return sharedPref.getString("MessageImageBaseUrl", "");
	}
	
	public void setTimeZone(String timeZone) {
		editor = sharedPref.edit();
		editor.putString("timeZone", timeZone);
		editor.commit();
	}

	public String getTimeZone() {
		return sharedPref.getString("timeZone", "");
	}

	// ----------------------------------------

	public void setLoginPassword(String loginPassword) {
		editor = sharedPref.edit();
		editor.putString("loginPassword", loginPassword);
		editor.commit();
	}

	public String getLoginPassword() {
		return sharedPref.getString("loginPassword", "");
	}

	public void storeUserId(String userid) {
		editor.putString(USER_ID, userid);
		editor.commit();
	}

	public void resetUserId() {
		editor.putString(USER_ID, null);
		editor.commit();
	}

	public String getUserId() {
		String userid = sharedPref.getString(USER_ID, "");
		return userid;
	}

	public void resetConnections() {
		editor.putString(USERDTO, null);
		editor.commit();
	}

	public List<UserDTO> getConnections() {
		String connectionsJSONString = sharedPref.getString(USERDTO, "");
		Type type = new TypeToken<List<UserDTO>>() {
		}.getType();
		List<UserDTO> connections = new Gson().fromJson(connectionsJSONString,
				type);
		return connections;
	}

	public void storeConnections(List<UserDTO> connections) {
		String connectionsJSONString = new Gson().toJson(connections);
		editor.putString(USERDTO, connectionsJSONString);
		editor.commit();
	}	
	
	//--------------------------------------------------
		public void storeAppTokan(String app_tokan) {
			editor.putString(APP_ACCESSTOKEN, app_tokan);
			editor.commit();
			//Log.v(TAG,"storeCityID="+ city_id);
		}
		
		public void resetAppTokan() {
			editor.putString(APP_ACCESSTOKEN, null);
			editor.commit();
			//Log.v(TAG,"resetCityId");
		}

		public String getAppTokan() {
			String app_tokan = sharedPref.getString(APP_ACCESSTOKEN, "");
			//Log.v(TAG,"getCityId="+ city_id);
			return app_tokan;
		}

		//--------------------------------------------------
			public void storeFBACESSTOKEN(String fbtoken) {
				editor.putString(FB_ACCESSTOKEN, fbtoken);
				editor.commit();
				//Log.v(TAG,"storeCityID="+ city_id);
			}
			
			public void resetFBACESSTOKEN() {
				editor.putString(FB_ACCESSTOKEN, null);
				editor.commit();
				//Log.v(TAG,"resetCityId");
			}

			public String getFBACESSTOKEN() {
				String fbtoken = sharedPref.getString(FB_ACCESSTOKEN, "");
				//Log.v(TAG,"getCityId="+ city_id);
				return fbtoken;
			}

		//--------------------------------------------------
		/*public void storeFBID(String fbid) {
			editor.putString(FB_ID, fbid);
			editor.commit();
			//Log.v(TAG,"storeCityID="+ city_id);
		}

		public void resetFBId() {
			editor.putString(FB_ID, null);
			editor.commit();
			//Log.v(TAG,"resetCityId");
		}

		public String getFBId() {
			String fb_id = sharedPref.getString(FB_ID, "");
			//Log.v(TAG,"getCityId="+ city_id);
			return fb_id;
		}
*/
		// Generic seter/geter to manage last seen time of app screens
		public void  setLastSeenTime(String key, String time) {
			editor = sharedPref.edit();
			editor.putString(key, time);
			editor.commit();
		}
		public String getLastSeenTime(String key) {
			return sharedPref.getString(key, "");
		}
		
		// Generic seter/geter to manage json 
		public void  setJson(String key, String JsonStr) {
			editor = sharedPref.edit();
			editor.putString(key, JsonStr);
			editor.commit();
		}
		public String getJson(String key) {
			return sharedPref.getString(key, "");
		}
		
}