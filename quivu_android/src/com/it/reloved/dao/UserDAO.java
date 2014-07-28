package com.it.reloved.dao;

import java.io.File;
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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;


public class UserDAO {

	private String json = null;
	private String serviceUrl = null;	
	String TAG=getClass().getSimpleName();	
	Context mContext;
		
	public UserDAO(Context context) {
		super();
		// TODO Auto-generated constructor stub
		mContext=context;
	}

	/*method for calling webservice from server*/
	public String makeConnection(String serviceUrl, MultipartEntity entity) {
		String response="",urlStr="";
		String completeURL ="";
		try {			
			urlStr = serviceUrl;
			DefaultHttpClient httpClient;
			httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(urlStr);
			httpPost.setEntity(entity);			
			Log.i(TAG, "makeConnection completeURL: " + serviceUrl + "&"+ completeURL);
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
	
	/*method for Login user*/
	public List<UserDTO> loginUser(String BASE_URL,String methodName,String UserName, String UserPassword,
			String UserNotificationId,String UserDeviceId,String UserAppVersion) {	
	// http://192.168.1.111/carousell-app/carousellServices/login.php?method=checkLogin
	// &UserName=sachin123&UserPassword=123456&UserNotificationId=12345678912&UserDeviceId=abced3444
	//	&UserAppVersion=1.23			
		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "UserName=" + UserName);		
		Log.i(TAG, "UserPassword=" + UserPassword);		
		Log.i(TAG, "UserNotificationId" + UserNotificationId);		
		Log.i(TAG, "UserDeviceId==" + UserDeviceId);		
		Log.i(TAG, "UserAppVersion=" + UserAppVersion);		
			
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("UserName",new StringBody( UserName));
			reqEntity.addPart("UserPassword", new StringBody(UserPassword));
			reqEntity.addPart("UserNotificationId",new StringBody( UserNotificationId));
			reqEntity.addPart("UserDeviceId",new StringBody(UserDeviceId));
			reqEntity.addPart("UserAppVersion",new StringBody(UserAppVersion));		
			reqEntity.addPart("UserDeviceType",new StringBody("1"));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseUserInfo(json);
	}	
	
	/*method for normal user registration*/
	public List<UserDTO> normalUserRegistration(String BASE_URL,String methodName,String UserName, String UserPassword,String UserEmailAddress,
			String UserCity,String UserLatitude,String UserLongitude,
			String UserNotificationId,String UserAppVersion,String UserDeviceId,String UserDeviceType,
			String Type,String UserDefaultCity/*,String UserImage*/) {		
	/*	http://192.168.1.111/carousell-app/carousellServices/userRegistration.php?method=userRegistrations
		&UserName=om1236&UserPassword=123456&UserEmailAddress=om236@gmail.com&UserCity=2&UserLatitude=2.73000
		&UserLongitude=4.56666&DeviceType=1&UserNotificationId=21222&UserAppVersion=1.23
		&UserDeviceId=1234567890&UserDeviceType=1&Type=1&UserDefaultCity=Ratlam&UserImage=%27%27*/
		
		serviceUrl = BASE_URL+methodName;	
		Log.i(TAG, "normalUserRegistration serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("UserName", new StringBody(UserName));
			reqEntity.addPart("UserPassword", new StringBody(UserPassword));
			reqEntity.addPart("UserEmailAddress", new StringBody(UserEmailAddress));			
			reqEntity.addPart("UserCity",new StringBody(UserCity));			
			reqEntity.addPart("UserLatitude",new StringBody(UserLatitude));	
			reqEntity.addPart("UserLongitude",new StringBody(UserLongitude));				
			reqEntity.addPart("UserNotificationId",new StringBody(UserNotificationId));		
			reqEntity.addPart("UserAppVersion",new StringBody(UserAppVersion));	
			reqEntity.addPart("UserDeviceId",new StringBody(UserDeviceId));	
			reqEntity.addPart("UserDeviceType",new StringBody(UserDeviceType));	
			reqEntity.addPart("Type",new StringBody(Type));	
			reqEntity.addPart("UserDefaultCity",new StringBody(UserDefaultCity));	
			
			/*if(!UserImage.equals(""))
			reqEntity.addPart("UserImage", new FileBody(new File(UserImage)));*/			
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}						
		return parseUserInfo(json);
	}	
	
	/*method for social user registration*/
	public List<UserDTO> socialUserRegistration(String BASE_URL,String methodName,String UserSocialId,String UserName,
			String UserEmailAddress,String UserLatitude,
			String UserLongitude,String UserNotificationId,String UserAppVersion,String UserDeviceId,
			String UserDeviceType,String Type,String UserDefaultCity,String UserImage,String UserGender,
			String UserFirstName,String UserLastName) {	
		/*http://mobilitytesting.com/quivu-app/webServices/userRegistration.php?method=userRegistrations
		&UserSocialId=mani236&UserName=maniratan&UserEmailAddress=mani@gmail.com&UserCity=2
		&UserLatitude=2.73000&UserLongitude=4.56666&DeviceType=1&UserNotificationId=21222&UserAppVersion=1.23
		&UserDeviceId=1234567890&UserDeviceType=1&Type=2&UserDefaultCity=Ratlam&UserGender=1&UserImage=''*/
		
		Log.i("socialUserRegistration", "UserSocialId="+UserSocialId+"---UserName="+UserName+"--UserEmailAddress"+UserEmailAddress);
		Log.i("socialUserRegistration", "UserDefaultCity="+UserDefaultCity+"---UserImage="+UserImage+"--Type"+Type);
		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "socialUserRegistration serviceUrl-->" + serviceUrl+"&UserSocialId="+UserSocialId+"&UserName="+UserName+"&UserEmailAddress="+UserEmailAddress+"&UserCity="
				+"&UserLatitude="+UserLatitude+"&UserLongitude="+UserLongitude+"&DeviceType="+1+"&UserNotificationId="+UserNotificationId+"&UserAppVersion="+UserAppVersion
				+"&UserDeviceId="+UserDeviceId+"&UserDeviceType="+UserDeviceType+"&Type="+Type+"&UserDefaultCity="+UserDefaultCity+"&UserGender="+UserGender+"&UserImage=");
		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("UserSocialId", new StringBody(UserSocialId));
			reqEntity.addPart("UserName", new StringBody(UserName));
			reqEntity.addPart("UserEmailAddress", new StringBody(UserEmailAddress));
			reqEntity.addPart("UserCity", new StringBody(""));
			reqEntity.addPart("UserLatitude",new StringBody(UserLatitude));
			reqEntity.addPart("UserLongitude",new StringBody(UserLongitude));			
			reqEntity.addPart("UserNotificationId",new StringBody(UserNotificationId));	
			reqEntity.addPart("UserAppVersion",new StringBody(UserAppVersion));				
			reqEntity.addPart("UserDeviceId",new StringBody(UserDeviceId));		
			reqEntity.addPart("UserDeviceType",new StringBody(UserDeviceType));	
			reqEntity.addPart("Type",new StringBody(Type));	
			reqEntity.addPart("UserDefaultCity",new StringBody(UserDefaultCity));	
			reqEntity.addPart("UserFirstName",new StringBody(UserFirstName));	
			reqEntity.addPart("UserLastName",new StringBody(UserLastName));	
				
			
			String genderStr="";
			if(UserGender.equals("Male")||UserGender.equals("male"))
				genderStr="0";
			else if(UserGender.equals("Female")||UserGender.equals("female"))
				genderStr="1";			
			reqEntity.addPart("UserGender",new StringBody(genderStr));
			
			if(!UserImage.equals(""))
			reqEntity.addPart("UserImage", new FileBody(new File(UserImage)));
			
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseUserInfo(json);
	}	
	
	/*method for edit user profile*/
	public List<UserDTO> editUserProfile(String BASE_URL,String methodName,String UserName, String UserFirstName,
			String UserLastName,String UserDefaultCity,String UserWebsiteUrl,String UserBio,
			String UserEmailAddress,String UserMobileNumber,String UserDateofBirth,String UserId,
			String UserImage,String UserGender) {	
		
		/*http://192.168.1.111/carousell-app/carousellServices/editProfile.php?method=editProfile
		&UserName=om123&UserFirstName=om&UserLastName=rathod&UserDefaultCity=indore&UserWebsiteUrl=om.com
		&UserBio=hello&UserEmailAddress=om@gmail.com&UserMobileNumber=750914562&UserDateofBirth=1989-07-05
		&UserId=11&UserGender=0*/
		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "editUserProfile serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("UserName", new StringBody(UserName));
			reqEntity.addPart("UserFirstName", new StringBody(UserFirstName));
			reqEntity.addPart("UserLastName", new StringBody(UserLastName));			
			reqEntity.addPart("UserDefaultCity",new StringBody(UserDefaultCity));	
			//reqEntity.addPart("UserWebsiteUrl",new StringBody(UserWebsiteUrl));	
			reqEntity.addPart("UserBio",new StringBody(UserBio));				
			reqEntity.addPart("UserEmailAddress",new StringBody(UserEmailAddress));		
			reqEntity.addPart("UserMobileNumber",new StringBody(UserMobileNumber));	
			reqEntity.addPart("UserDateofBirth",new StringBody(UserDateofBirth));	
			reqEntity.addPart("UserId",new StringBody(UserId));	
			
			String genderStr="";
			if(UserGender.equals("Male")||UserGender.equals("male"))
				genderStr="0";
			else if(UserGender.equals("Female")||UserGender.equals("female"))
				genderStr="1";			
			reqEntity.addPart("UserGender",new StringBody(genderStr));			
			
			if(!UserImage.equals(""))
			reqEntity.addPart("UserImage", new FileBody(new File(UserImage)));
			
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}					
		return parseUserInfo(json);
	}	
		
	/*method for parsing user response*/
	private List<UserDTO> parseUserInfo(String jsonResponse) {	
		String success = "",msg = "";				
		String UserId="",UserName="",UserEmailAddress="",UserDateofBirth="",UserFirstName="",UserLastName="",
			UserMobileNumber="",UserImage="",UserLatitude="",UserLongitude="",UserEItemListedStatus="",
			UserENewOfferStatus="",UserENewChatStatus="",UserECommentStatus="",UserPNOfferStatus="",
			UserPNChatStatus="",UserPNFeedbackStatus="",UserPNCommentStatus="",UserPNSubscribeStatus="",
			UserPOfferStatus="",UserAddress="",UserCity="",UserCityName="",UserDefaultCity="",
			UserWebsiteUrl="",UserGender="",UserBio="",UserSoundStatus="",UserVibrateStatus="",
			UserLightStatus="";
		
		List<UserDTO> userDTO = new ArrayList<UserDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");				
				if (jsonObject.has("msg")) 
					msg = jsonObject.getString("msg");				
				if (success.equals("1") && jsonObject.has("UserInformation")) {				
					JSONObject jsonObject1 = jsonObject.getJSONObject("UserInformation");					
					if (jsonObject1.has("UserId"))
						UserId = jsonObject1.getString("UserId");					
					if (jsonObject1.has("UserName"))
						UserName = jsonObject1.getString("UserName");					
					if (jsonObject1.has("UserEmailAddress")) 
						UserEmailAddress = jsonObject1.getString("UserEmailAddress");					
					if (jsonObject1.has("UserDateofBirth")) 
						UserDateofBirth = jsonObject1.getString("UserDateofBirth");
					if (jsonObject1.has("UserFirstName"))
						UserFirstName = jsonObject1.getString("UserFirstName");					
					if (jsonObject1.has("UserLastName"))
						UserLastName = jsonObject1.getString("UserLastName");					
					if (jsonObject1.has("UserMobileNumber"))
						UserMobileNumber = jsonObject1.getString("UserMobileNumber");					
					if (jsonObject1.has("UserImage")){
						//AppSession appSession=new AppSession(mContext);
						UserImage = jsonObject1.getString("UserImage");					
					}if (jsonObject1.has("UserLatitude")) 
						UserLatitude = jsonObject1.getString("UserLatitude");					
					if (jsonObject1.has("UserLongitude")) 
						UserLongitude = jsonObject1.getString("UserLongitude");
					if (jsonObject1.has("UserEItemListedStatus"))
						UserEItemListedStatus = jsonObject1.getString("UserEItemListedStatus");					
					if (jsonObject1.has("UserENewOfferStatus"))
						UserENewOfferStatus = jsonObject1.getString("UserENewOfferStatus");					
					if (jsonObject1.has("UserENewChatStatus"))
						UserENewChatStatus = jsonObject1.getString("UserENewChatStatus");
					if (jsonObject1.has("UserECommentStatus"))
						UserECommentStatus = jsonObject1.getString("UserECommentStatus");
					if (jsonObject1.has("UserPNOfferStatus"))
						UserPNOfferStatus = jsonObject1.getString("UserPNOfferStatus");
					if (jsonObject1.has("UserPNChatStatus"))
						UserPNChatStatus = jsonObject1.getString("UserPNChatStatus");
					if (jsonObject1.has("UserPNFeedbackStatus"))
						UserPNFeedbackStatus = jsonObject1.getString("UserPNFeedbackStatus");
					if (jsonObject1.has("UserPNCommentStatus"))
						UserPNCommentStatus = jsonObject1.getString("UserPNCommentStatus");					
					if (jsonObject1.has("UserPNSubscribeStatus"))
						UserPNSubscribeStatus = jsonObject1.getString("UserPNSubscribeStatus");
					if (jsonObject1.has("UserPOfferStatus"))
						UserPOfferStatus = jsonObject1.getString("UserPOfferStatus");					
					if (jsonObject1.has("UserAddress"))
						UserAddress = jsonObject1.getString("UserAddress");
					if (jsonObject1.has("UserCity"))
						UserCity = jsonObject1.getString("UserCity");
					if (jsonObject1.has("UserCityName"))
						UserCityName = jsonObject1.getString("UserCityName");
					if (jsonObject1.has("UserDefaultCity"))
						UserDefaultCity = jsonObject1.getString("UserDefaultCity");
					if (jsonObject1.has("UserWebsiteUrl"))
						UserWebsiteUrl = jsonObject1.getString("UserWebsiteUrl");
					if (jsonObject1.has("UserGender"))
						UserGender = jsonObject1.getString("UserGender");
					if (jsonObject1.has("UserBio"))
						UserBio = jsonObject1.getString("UserBio");
					
					if (jsonObject1.has("UserSoundStatus"))
						UserSoundStatus = jsonObject1.getString("UserSoundStatus");
					if (jsonObject1.has("UserVibrateStatus"))
						UserVibrateStatus = jsonObject1.getString("UserVibrateStatus");
					if (jsonObject1.has("UserLightStatus"))
						UserLightStatus = jsonObject1.getString("UserLightStatus");
					
				}
				
				userDTO.add(new UserDTO(success, msg, UserId, UserName, UserEmailAddress, UserDateofBirth,
						UserFirstName, UserLastName, UserMobileNumber, UserImage, UserLatitude, UserLongitude,
						UserEItemListedStatus, UserENewOfferStatus, UserENewChatStatus, UserECommentStatus,
						UserPNOfferStatus, UserPNChatStatus, UserPNFeedbackStatus, UserPNCommentStatus,
						UserPNSubscribeStatus, UserPOfferStatus, UserAddress, UserCity,UserCityName,
						UserDefaultCity,UserWebsiteUrl,UserGender,UserBio,UserSoundStatus,UserVibrateStatus,UserLightStatus));				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");		
				return null;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDTO;
	}
	
	
	/*method for getting content*/
	public String[] getContent(String BASE_URL,String methodName,String title) {	
	// http://192.168.1.111/carousell-app/carousellServices/getContentPage.php?method=getContentPage
	//	&title=Termsandconditions		
		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "getContent serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("title",new StringBody( title));			
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseContent(json);
	}	
	
	/*method for changing password*/
	public String[] changePassword(String BASE_URL,String methodName,String PreviousPassword,String UserNewPassword,
			String UserId) {	
	//http://192.168.1.111/carousell-app/carousellServices/changePassword.php?method=ChangePasssword
	//&PreviousPassword=123456&UserNewPassword=12345&UserId=11
		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "changePassword serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("PreviousPassword",new StringBody( PreviousPassword));		
			reqEntity.addPart("UserNewPassword",new StringBody( UserNewPassword));		
			reqEntity.addPart("UserId",new StringBody( UserId));		
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseContent(json);
	}	
	
	/*method for changing password*/
	public String[] forgotPassword(String BASE_URL, String methodName, String EmailAddress) {	
	//http://mobilitytesting.com/reloved-app/relovedServices/forgetPassword.php?method=forgetPassword
	//&EmailAddress=sachin@idealtechnologys.com		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "forgotPassword serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("EmailAddress",new StringBody( EmailAddress));				
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseContent(json);
	}	
	
	/*method for resend email*/
	public String[] resendEmail(String BASE_URL, String methodName,
			  String UserId, String UserName, String UserEmail) {	
		//http://mobilitytesting.com/quivu-app/webServices/editProfile.php?method=resendMail
		//	&UserId=7&UserName=sachin&UserEmail=solanki.sachin1989@gmail.com				
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "resendEmail serviceUrl-->" + serviceUrl+"&UserId="+UserId+"&UserName="+UserName+"&UserEmail="+UserEmail);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {						
			reqEntity.addPart("UserId",new StringBody(UserId));
			reqEntity.addPart("UserName",new StringBody(UserName));	
			reqEntity.addPart("UserEmail",new StringBody(UserEmail));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
		return parseContent(json);
	}	

	/*method for parsing*/
	private String[] parseContent(String jsonResponse) {
		String success = "", msg = "",Content="";
		String[] responseArr = new String[3];
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("Content")){
					String contentStr = jsonObject.getString("Content");
					byte[] data = Base64.decode(contentStr, Base64.DEFAULT);
					Content = new String(data, "UTF-8");
				}
				responseArr[0]=success;				
				responseArr[1]=msg;				
				responseArr[2]=Content;				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseArr;
	}

}
