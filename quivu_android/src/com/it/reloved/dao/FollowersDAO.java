package com.it.reloved.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.OfferDTO;
import com.it.reloved.dto.ProfileDTO;
import com.it.reloved.utils.AppSession;

public class FollowersDAO {

	private String json = null;
	private String serviceUrl = null;
	String TAG = getClass().getSimpleName();

	Context mContext;

	public FollowersDAO(Context context) {
		super();
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	/*method for calling webservice from server*/
	public String makeConnection(String serviceUrl, MultipartEntity entity) {
		String response = "", urlStr = "";
		String completeURL = "";
		try {
			urlStr = serviceUrl;
			DefaultHttpClient httpClient;
			httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlStr);
			httpPost.setEntity(entity);
			Log.i(TAG, "makeConnection completeURL: " + serviceUrl + "&"
					+ completeURL);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			entity = null;
		} catch (UnsupportedEncodingException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			response = "Can't connect to server.";
			e.printStackTrace();
			return null;
		}
		Log.i(TAG, "makeConnection response :: " + response);
		return response;
	}

	/*method for getting followers*/
	public List<CategoryDTO> getFollowers(String BASE_URL, String methodName,
			String UserId,String page,String ProfileId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/follow.php?method=getFollower&UserId=17&page=1
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowers serviceUrl-->" + serviceUrl+"--page="+page+"--UserId="+UserId+"--ProfileId="+ProfileId);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			reqEntity.addPart("page", new StringBody(page));
			reqEntity.addPart("ProfileId", new StringBody(ProfileId));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseFollowers(json);
	}

	/*method for getting followings*/
	public List<CategoryDTO> getFollowing(String BASE_URL, String methodName,
			String UserId,String page,String ProfileId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/follow.php?method=getFollowing&UserId=22&page=1
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowing serviceUrl-->" + serviceUrl+"--page="+page+"--UserId="+UserId);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			reqEntity.addPart("page", new StringBody(page));
			reqEntity.addPart("ProfileId", new StringBody(ProfileId));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseFollowing(json);
	}
	
	/*method for parsing response of followers*/
	private List<CategoryDTO> parseFollowers(String jsonResponse) {
		String success = "", msg = "";
		String UserId = "", UserName = "", UserImage = "",FollowStatus="";
		List<CategoryDTO> followersDtos = new ArrayList<CategoryDTO>();
		List<CategoryItemDTO> followersItemDTOs = new ArrayList<CategoryItemDTO>();

		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("Followers")) {
					JSONArray jsonArray = jsonObject.getJSONArray("Followers");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.has("UserId"))
							UserId = jsonObject2.getString("UserId");
						if (jsonObject2.has("UserName"))
							UserName = jsonObject2.getString("UserName");
						//AppSession appSession = new AppSession(mContext);
						if (jsonObject2.has("UserImage"))
							UserImage =jsonObject2.getString("UserImage");
						
						if (jsonObject2.has("FollowStatus"))
							FollowStatus = jsonObject2.getString("FollowStatus");
						
						followersItemDTOs.add(new CategoryItemDTO(UserId,UserName, UserImage,FollowStatus));
					}
				}
				followersDtos.add(new CategoryDTO(success, msg,followersItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return followersDtos;
	}
	
	/*method for parsing response of followings*/
	private List<CategoryDTO> parseFollowing(String jsonResponse) {
		String success = "", msg = "";
		String UserId = "", UserName = "", UserImage = "",FollowStatus="";
		List<CategoryDTO> followersDtos = new ArrayList<CategoryDTO>();
		List<CategoryItemDTO> followersItemDTOs = new ArrayList<CategoryItemDTO>();

		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("Following")) {
					JSONArray jsonArray = jsonObject.getJSONArray("Following");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.has("UserId"))
							UserId = jsonObject2.getString("UserId");
						if (jsonObject2.has("UserName"))
							UserName = jsonObject2.getString("UserName");
						//AppSession appSession = new AppSession(mContext);
						if (jsonObject2.has("UserImage"))
							UserImage = jsonObject2.getString("UserImage");
						
						if (jsonObject2.has("FollowingStatus"))
							FollowStatus = jsonObject2.getString("FollowingStatus");
						followersItemDTOs.add(new CategoryItemDTO(UserId,UserName, UserImage,FollowStatus));
					}
				}
				followersDtos.add(new CategoryDTO(success, msg,followersItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return followersDtos;
	}
	
	/*method for add followers*/
	public String[] addFollowers(String BASE_URL, String methodName,String FromUserId,String FromUserName,
			String FromUserImage,String ToUserId,String ToUserName,String ToUserImage,String FollowStatus) {
		// http://mobilitytesting.com/reloved-app/relovedServices/follow.php?method=addFollow
		// &FromUserId=24&FromUserName=one&FromUserImage=jpeg&ToUserId=25&ToUserName=testtwo&ToUserImage=b.jpeg
		// &FollowStatus=1
		
		Log.i(TAG, "FromUserId="+FromUserId+"--FromUserName="+FromUserName+"--FromUserImage="+FromUserImage
				+"--ToUserId="+ToUserId+"--ToUserName="+ToUserName+"--ToUserImage="+ToUserImage
				+"--FollowStatus="+FollowStatus);
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowers serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("FromUserId", new StringBody(FromUserId));
			reqEntity.addPart("FromUserName", new StringBody(FromUserName));
			reqEntity.addPart("FromUserImage", new StringBody(FromUserImage));
			reqEntity.addPart("ToUserId", new StringBody(ToUserId));
			reqEntity.addPart("ToUserName", new StringBody(ToUserName));
			reqEntity.addPart("ToUserImage", new StringBody(ToUserImage));
			reqEntity.addPart("FollowStatus", new StringBody(FollowStatus));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseAddFollow(json);
	}

	/*method for parsing response of add followers*/
	private String[] parseAddFollow(String jsonResponse) {		
		String success = "", msg = "";
		String[] arr=new String[2];
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");	
				arr[0]=success;
				arr[1]=msg;				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
	
	/*method for common friends list*/
	public List<CategoryDTO> getCommonFriendList(String BASE_URL, String methodName,
			String UserId,String FriendIds) {
		// http://mobilitytesting.com/reloved-app/relovedServices/facebookCommonFriend.php?method=commonFriendList
		//&UserId=24&FriendIds=103841937235084575034
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowing serviceUrl-->" + serviceUrl+"--UserId="+UserId+" --FriendIds="+FriendIds);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			reqEntity.addPart("FriendIds", new StringBody(FriendIds));
		
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseCommonFriendList(json);
	}
	
	/*method for parsing response of common friends*/
	private List<CategoryDTO> parseCommonFriendList(String jsonResponse) {
//		{"success":"1","msg":"Data Found",
/*		"UserList":
		  [{"UserId":"52",
			"UserName":"vineet.verma",
			"UserImage":"1400602005.jpg",
			"FollowerStatus":0}]}*/
		String success = "", msg = "";
		String UserId = "", UserName = "", UserImage = "",FollowerStatus="";
		List<CategoryDTO> followersDtos = new ArrayList<CategoryDTO>();
		List<CategoryItemDTO> followersItemDTOs = new ArrayList<CategoryItemDTO>();

		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("UserList")) {
					JSONArray jsonArray = jsonObject.getJSONArray("UserList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
						if (jsonObject2.has("UserId"))
							UserId = jsonObject2.getString("UserId");
						if (jsonObject2.has("UserName"))
							UserName = jsonObject2.getString("UserName");
						
						if (jsonObject2.has("UserImage"))
							UserImage = jsonObject2.getString("UserImage");
						
						if (jsonObject2.has("FollowerStatus"))
							FollowerStatus = jsonObject2.getString("FollowerStatus");
						
						followersItemDTOs.add(new CategoryItemDTO(UserId,UserName, UserImage,FollowerStatus));
					}
				}
				followersDtos.add(new CategoryDTO(success, msg,followersItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return followersDtos;
	}
}
