package com.it.reloved.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.it.reloved.ProfileMe;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.ActivityDTO;
import com.it.reloved.dto.ActivityItemDTO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.CommentItemDTO;
import com.it.reloved.dto.CountryDTO;
import com.it.reloved.dto.ItemDTO;
import com.it.reloved.dto.ProfileDTO;
import com.it.reloved.dto.SubCategoryDetailsDTO;
import com.it.reloved.utils.AppSession;

public class ProfileDAO {

	private String json = null;
	private String serviceUrl = null;	
	private String TAG=getClass().getSimpleName();	
	
	private static Context mContext;
	
	public ProfileDAO(Context context) {
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

	/*method for getting profile details*/
	public List<ProfileDTO> getProfileDetails(Context context, String BASE_URL,String methodName,String UserId,String FollowId) {	
	// http://192.168.1.111/reloved-app/relovedServices/profileView.php?method=viewProfile&UserId=1&FollowId=			
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "getProfileDetails serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("UserId",new StringBody( UserId));
			reqEntity.addPart("FollowId",new StringBody( FollowId));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		List<ProfileDTO> profileDTOs = parseCategory(json);
		if(FollowId.equals("")){
		if (profileDTOs.get(0).getSuccess().equals("1")) {
			AppSession appSession = new AppSession(context);
			appSession.setJson(AppSession.PROFILE_ME_JSON,json);
			appSession.setLastSeenTime(AppSession.PROFILE_ME_SEEN_TIME,RelovedPreference.getCurrentDateTime());	
		}
		}
		return profileDTOs;
	}	
	
	/*method for parsing profile details*/
	public static List<ProfileDTO> parseCategory(String jsonResponse) {
		
		String success = "", msg = "";
		String ProductId="",ProductName="",ProductImageInfo="",ProductPrice="",ProductLikeCount="";
		String UserId="",UserBio="",UserEmailVerificationStatus="",UserPositiveFeedBCount="",
				UserNeutralFeedBCount="",UserNegativeFeedBCount="",UserFollowerCount="",
						UserFollowingCount="", OfferMadeByMe="", StuffLikes="", UserRegistationDate="",FollowStatus="";
		String UserName="",UserCity="",UserCityName="",UserDefaultCity="",UserImage="",UserWebsiteUrl="";
		String ProductUserId="",ProductUserName="",ProductUserImage="",ProductAddDate="",ProductWebsiteUrl="",
				ProductSoldStatus="",LikeStatus="";
		List<ProfileDTO> profileDTOs=new ArrayList<ProfileDTO>();
		List<CategoryItemDTO> productItemDTOs=new ArrayList<CategoryItemDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("UserInformation")){
					JSONObject jsonObject1=jsonObject.getJSONObject("UserInformation");
				
					if (jsonObject1.has("UserId"))
						UserId = jsonObject1.getString("UserId");
					if (jsonObject1.has("UserBio"))
						UserBio = jsonObject1.getString("UserBio");
					if (jsonObject1.has("UserEmailVerificationStatus"))
						UserEmailVerificationStatus = jsonObject1.getString("UserEmailVerificationStatus");
					if (jsonObject1.has("UserPositiveFeedBCount"))
						UserPositiveFeedBCount = jsonObject1.getString("UserPositiveFeedBCount");
					if (jsonObject1.has("UserNeutralFeedBCount"))
						UserNeutralFeedBCount = jsonObject1.getString("UserNeutralFeedBCount");
					if (jsonObject1.has("UserNegativeFeedBCount"))
						UserNegativeFeedBCount = jsonObject1.getString("UserNegativeFeedBCount");
					if (jsonObject1.has("UserFollowerCount"))   
						UserFollowerCount = jsonObject1.getString("UserFollowerCount");
					if (jsonObject1.has("UserFollowingCount"))
						UserFollowingCount = jsonObject1.getString("UserFollowingCount");
					
					if (jsonObject1.has("OfferMadeByMe"))
						OfferMadeByMe = jsonObject1.getString("OfferMadeByMe");
					if (jsonObject1.has("StuffLikes"))
						StuffLikes = jsonObject1.getString("StuffLikes");
					
					if (jsonObject1.has("UserRegistationDate"))
						UserRegistationDate = jsonObject1.getString("UserRegistationDate");
					if (jsonObject1.has("FollowStatus"))
						FollowStatus = jsonObject1.getString("FollowStatus");
					
					if (jsonObject1.has("UserName"))
						UserName = jsonObject1.getString("UserName");
					if (jsonObject1.has("UserCity"))
						UserCity = jsonObject1.getString("UserCity");
					if (jsonObject1.has("UserCityName"))
						UserCityName = jsonObject1.getString("UserCityName");
					if (jsonObject1.has("UserDefaultCity"))
						UserDefaultCity = jsonObject1.getString("UserDefaultCity");
					//AppSession appSession1=new AppSession(mContext);
					if (jsonObject1.has("UserImage"))
						UserImage =jsonObject1.getString("UserImage");
					if (jsonObject1.has("UserWebsiteUrl"))
						UserWebsiteUrl =jsonObject1.getString("UserWebsiteUrl");
					
					if (jsonObject1.has("Products")){
						productItemDTOs=new ArrayList<CategoryItemDTO>();
						JSONArray jsonArray=jsonObject1.getJSONArray("Products");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject2=jsonArray.getJSONObject(i);
							if (jsonObject2.has("ProductId"))
								ProductId = jsonObject2.getString("ProductId");
							if (jsonObject2.has("ProductName"))
								ProductName = jsonObject2.getString("ProductName");
							if (jsonObject2.has("ProductPrice"))
								ProductPrice = jsonObject2.getString("ProductPrice");
							if (jsonObject2.has("ProductLikeCount"))
								ProductLikeCount = jsonObject2.getString("ProductLikeCount");	
							
							if (jsonObject2.has("ProductImageInfo")){
								JSONArray jsonArray2=jsonObject2.getJSONArray("ProductImageInfo");
								for (int j = 0; j < jsonArray2.length(); j++) {
									JSONObject jsonObject3=jsonArray2.getJSONObject(j);
									if(jsonObject3.has("ProMainImageStatus")){
										String ProMainImageStatus=jsonObject3.getString("ProMainImageStatus");
										if (ProMainImageStatus.equals("1")) {
											String ProImageName=jsonObject3.getString("ProImageName");
											//AppSession appSession=new AppSession(mContext);
											ProductImageInfo=ProImageName;
											break;
										}
									}
								}
							}	
							
							if (jsonObject2.has("ProductUserId"))
								ProductUserId = jsonObject2.getString("ProductUserId");						
							if (jsonObject2.has("ProductUserName"))
								ProductUserName = jsonObject2.getString("ProductUserName");						
							if (jsonObject2.has("ProductUserImage"))
								ProductUserImage = jsonObject2.getString("ProductUserImage");
							if (jsonObject2.has("ProductAddDate"))
								ProductAddDate = jsonObject2.getString("ProductAddDate");
							if (jsonObject2.has("ProductWebsiteUrl"))
								ProductWebsiteUrl = jsonObject2.getString("ProductWebsiteUrl");
							if (jsonObject2.has("ProductSoldStatus"))
								ProductSoldStatus = jsonObject2.getString("ProductSoldStatus");
							
							if (jsonObject2.has("ProductLikeInfo")){
								LikeStatus="";
								JSONArray jsonArray2=jsonObject2.getJSONArray("ProductLikeInfo");							
								for (int j = 0; j < jsonArray2.length(); j++) {
									JSONObject jsonObject3=jsonArray2.getJSONObject(j);								
									if(jsonObject3.has("UserId")){
										String UserId1=jsonObject3.getString("UserId");
										AppSession appSession1=new AppSession(mContext);
										if (UserId1.equals(appSession1.getUserId())) {
											LikeStatus="1";
											break;
										}else{
											LikeStatus="0";
										}
									}
								}
							}		
							
							//productItemDTOs.add(new  CategoryItemDTO(ProductId, ProductName, ProductImageInfo,
							//		ProductPrice,ProductLikeCount));
							
							productItemDTOs.add(new CategoryItemDTO(ProductId, ProductName, ProductImageInfo,
									ProductPrice,ProductLikeCount, ProductUserId, ProductUserName, 
									ProductUserImage,ProductAddDate, ProductWebsiteUrl, LikeStatus,ProductSoldStatus));
						}
					}	
					
				}
				profileDTOs.add(new ProfileDTO(success, msg, UserId, UserBio, UserEmailVerificationStatus,
						UserPositiveFeedBCount, UserNeutralFeedBCount, UserNegativeFeedBCount,
						UserFollowerCount, UserFollowingCount, OfferMadeByMe, StuffLikes, UserRegistationDate, productItemDTOs,
						UserName,UserCity,UserCityName,UserDefaultCity,UserImage,UserWebsiteUrl,FollowStatus));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileDTOs;
	}
	
	/*method for getting subcategory details*/
	public List<SubCategoryDetailsDTO> getSubCategoryDetails(String BASE_URL,String methodName,String ProductId) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/productView.php?method=viewProduct
		// &ProductId=1		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "getSubCategoryDetails serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("ProductId",new StringBody(ProductId));
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseSubCategoryDetails(json);
	}	
	
	/*method for parsing subcategories details*/
	private List<SubCategoryDetailsDTO> parseSubCategoryDetails(String jsonResponse) {
		
		String success = "", msg = "";		
		String ProductUserId="",ProductUserName="",ProductUserImage="",ProductName="",ProductPrice="",
				ProductLikeCount="",ProductCommentCount="",ProductLatitude="",ProductLongitude="",
						ProductDescription="",ProductOfferCount="",ProductAddDate="",
						ProductAddress="",ProductSoldStatus="";
		String ProImageId="",ProImageName="",ProMainImageStatus="",ProductWebsiteUrl="",ProductCatId="";
		String CommentId="",CommentUserId="",CommentUserName="",CommentUserImage="",CommentMessage="",
				CommentToReplyUserId="",ReplyToUserName="",CommentTime="",CommentOwnerStatus="",
				CommentReplyStaus="";
		String LikeUserId="",OfferUserId="",OfferAmount="";
		
		List<SubCategoryDetailsDTO> subCategoryDetailsDTOs=new ArrayList<SubCategoryDetailsDTO>();
		List<CategoryItemDTO> productImageDtos=new ArrayList<CategoryItemDTO>();
		String LikeStatus="";
		List<CommentItemDTO> commentItemDTOs=new ArrayList<CommentItemDTO>();
		List<CategoryItemDTO> offerItemDTOs=new ArrayList<CategoryItemDTO>();
	
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("ProductInformation")){
					JSONObject jsonObject2=jsonObject.getJSONObject("ProductInformation");
					if (jsonObject2.has("ProductUserId"))
						ProductUserId = jsonObject2.getString("ProductUserId");
					if (jsonObject2.has("ProductCatId"))
						ProductCatId = jsonObject2.getString("ProductCatId");
					if (jsonObject2.has("ProductUserName"))
						ProductUserName = jsonObject2.getString("ProductUserName");
					//AppSession appSession=new AppSession(mContext);
					if (jsonObject2.has("ProductUserImage"))
						ProductUserImage =jsonObject2.getString("ProductUserImage");
					if (jsonObject2.has("ProductName"))
						ProductName = jsonObject2.getString("ProductName");
					if (jsonObject2.has("ProductPrice"))
						ProductPrice = jsonObject2.getString("ProductPrice");
					if (jsonObject2.has("ProductLikeCount"))
						ProductLikeCount = jsonObject2.getString("ProductLikeCount");					
					if (jsonObject2.has("ProductCommentCount"))
						ProductCommentCount = jsonObject2.getString("ProductCommentCount");					
					if (jsonObject2.has("ProductLatitude"))
						ProductLatitude = jsonObject2.getString("ProductLatitude");
					if (jsonObject2.has("ProductLongitude"))
						ProductLongitude = jsonObject2.getString("ProductLongitude");
					if (jsonObject2.has("ProductDescription"))
						ProductDescription = jsonObject2.getString("ProductDescription");
					if (jsonObject2.has("ProductOfferCount"))
						ProductOfferCount = jsonObject2.getString("ProductOfferCount");
					if (jsonObject2.has("ProductAddDate"))
						ProductAddDate = jsonObject2.getString("ProductAddDate");
					if (jsonObject2.has("ProductWebsiteUrl"))
						ProductWebsiteUrl = jsonObject2.getString("ProductWebsiteUrl");
					if (jsonObject2.has("ProductAddress"))
						ProductAddress = jsonObject2.getString("ProductAddress");
					if (jsonObject2.has("ProductSoldStatus"))
						ProductSoldStatus = jsonObject2.getString("ProductSoldStatus");
					
					if (jsonObject2.has("ProductImageInfo")){
						productImageDtos=new ArrayList<CategoryItemDTO>();
						JSONArray jsonArray=jsonObject2.getJSONArray("ProductImageInfo");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject3=jsonArray.getJSONObject(i);
							if (jsonObject3.has("ProImageId"))
								ProImageId = jsonObject3.getString("ProImageId");							
							if (jsonObject3.has("ProImageName"))
								ProImageName = jsonObject3.getString("ProImageName");
							if (jsonObject3.has("ProMainImageStatus"))
								ProMainImageStatus = jsonObject3.getString("ProMainImageStatus");
							
							productImageDtos.add(new CategoryItemDTO(ProImageId, ProMainImageStatus, 
									ProImageName));							
						}
					}
					
					if (jsonObject2.has("ProductLikeInfo")){							
						JSONArray jsonArray=jsonObject2.getJSONArray("ProductLikeInfo");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject3=jsonArray.getJSONObject(i);
							if(jsonObject3.has("UserId")){
								String UserId=jsonObject3.getString("UserId");
								AppSession appSession=new AppSession(mContext);
								if (UserId.equals(appSession.getUserId())) {
									LikeStatus="1";
									break;
								}else{
									LikeStatus="0";
								}
							}
						}
					}
					
					if (jsonObject2.has("ProductCommentInfo")){		
						commentItemDTOs=new ArrayList<CommentItemDTO>();
						JSONArray jsonArray=jsonObject2.getJSONArray("ProductCommentInfo");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject3=jsonArray.getJSONObject(i);
							if (jsonObject3.has("CommentId"))
								CommentId = jsonObject3.getString("CommentId");
							if (jsonObject3.has("CommentUserId"))
								CommentUserId = jsonObject3.getString("CommentUserId");
							if (jsonObject3.has("CommentUserName"))
								CommentUserName = jsonObject3.getString("CommentUserName");							
							if (jsonObject3.has("CommentUserImage"))
								CommentUserImage = jsonObject3.getString("CommentUserImage");
							if (jsonObject3.has("CommentMessage"))
								CommentMessage = jsonObject3.getString("CommentMessage");
							if (jsonObject3.has("CommentToReplyUserId"))
								CommentToReplyUserId = jsonObject3.getString("CommentToReplyUserId");
							if (jsonObject3.has("ReplyToUserName"))
								ReplyToUserName = jsonObject3.getString("ReplyToUserName");
							if (jsonObject3.has("CommentTime"))
								CommentTime = jsonObject3.getString("CommentTime");
							if (jsonObject3.has("CommentOwnerStatus"))
								CommentOwnerStatus = jsonObject3.getString("CommentOwnerStatus");
							if (jsonObject3.has("CommentReplyStaus"))
								CommentReplyStaus = jsonObject3.getString("CommentReplyStaus");
							
							commentItemDTOs.add(new CommentItemDTO(CommentUserId, CommentUserName, 
									CommentUserImage, CommentMessage, CommentToReplyUserId, ReplyToUserName, 
									CommentTime, CommentOwnerStatus, CommentReplyStaus,CommentId));
						}
					}
					
					if (jsonObject2.has("ProductOfferInfo")){	
						offerItemDTOs=new ArrayList<CategoryItemDTO>();
						JSONArray jsonArray=jsonObject2.getJSONArray("ProductOfferInfo");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject3=jsonArray.getJSONObject(i);
							if (jsonObject3.has("UserId"))
								OfferUserId = jsonObject3.getString("UserId");
							if (jsonObject3.has("Amount"))
								OfferAmount = jsonObject3.getString("Amount");
							offerItemDTOs.add(new CategoryItemDTO(OfferUserId, OfferAmount, ""));
						}
					}
					
				}
				subCategoryDetailsDTOs.add(new SubCategoryDetailsDTO(success, msg,ProductUserId,ProductUserName, 
						ProductUserImage, ProductName, ProductPrice, ProductLikeCount, ProductCommentCount, 
						ProductLatitude, ProductLongitude, ProductDescription, productImageDtos, LikeStatus, 
						commentItemDTOs,offerItemDTOs,ProductOfferCount,ProductAddDate,ProductWebsiteUrl,
						ProductCatId,ProductAddress,ProductSoldStatus));
				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subCategoryDetailsDTOs;
	}
	
	/*method for getting Activity*/
	public ActivityDTO getActivity(Context context, String BASE_URL,String methodName,String UserId,String page) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/getActivity.php?method=getActivities
		// &UserId=25&page=1	
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "getActivity	 serviceUrl-->" + serviceUrl+"---page="+page);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("UserId",new StringBody(UserId));
				reqEntity.addPart("page",new StringBody(page));
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			ActivityDTO activityDTO = parseActivities(json);
			if (activityDTO.getSuccess().equals("1")) {
				AppSession appSession = new AppSession(context);
				appSession.setJson(AppSession.ACTIVITY_JSON,json);
				appSession.setLastSeenTime(AppSession.ACTIVITY_SEEN_TIME,RelovedPreference.getCurrentDateTime());	
			}
			return activityDTO;
	}
	
	// Using Gson
		public static ActivityDTO parseActivities(String json) {
			ActivityDTO activityDTO = null;			
			try {
				JSONObject jsonObject = new JSONObject(json);
					Type type = new TypeToken<ActivityDTO>() {}.getType();			
					activityDTO = new Gson().fromJson(jsonObject.toString(),type);																
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
			return activityDTO;
		}
	
	// // Old traditional way to parse json
	/*private List<ActivityDTO> parseActivities(String jsonResponse) {
		
		String success = "", msg = "";				
		String ActivityTypeId="",ActivityFromUserId="",ActivityFromUserName="",ActivityFromUserImage="",
				ActivityProductId="",ActivityProductName="",ActivityProductImage="",ActivityProductMessage="",
						ActivityOfferPrice="",ActivityMessage="",ActivityTime="";
		
		List<ActivityDTO> activityDTOs=new ArrayList<ActivityDTO>();
		List<ActivityItemDTO> activityItemDTOs=new ArrayList<ActivityItemDTO>();
	
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("Activities")){
					activityItemDTOs=new ArrayList<ActivityItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("Activities");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("ActivityTypeId"))
							ActivityTypeId = jsonObject2.getString("ActivityTypeId");
						if (jsonObject2.has("ActivityFromUserId"))
							ActivityFromUserId = jsonObject2.getString("ActivityFromUserId");
						if (jsonObject2.has("ActivityFromUserName"))
							ActivityFromUserName = jsonObject2.getString("ActivityFromUserName");
					//	AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("ActivityFromUserImage"))
							ActivityFromUserImage =jsonObject2.getString("ActivityFromUserImage");
						if (jsonObject2.has("ActivityProductId"))
							ActivityProductId = jsonObject2.getString("ActivityProductId");
						if (jsonObject2.has("ActivityProductName"))
							ActivityProductName = jsonObject2.getString("ActivityProductName");
						if (jsonObject2.has("ActivityProductImage"))
							ActivityProductImage = jsonObject2.getString("ActivityProductImage");
						if (jsonObject2.has("ActivityProductMessage"))
							ActivityProductMessage = jsonObject2.getString("ActivityProductMessage");
						if (jsonObject2.has("ActivityOfferPrice"))
							ActivityOfferPrice = jsonObject2.getString("ActivityOfferPrice");						
						if (jsonObject2.has("ActivityMessage"))
							ActivityMessage = jsonObject2.getString("ActivityMessage");
						if (jsonObject2.has("ActivityTime"))
							ActivityTime = jsonObject2.getString("ActivityTime");
						
						activityItemDTOs.add(new ActivityItemDTO(ActivityTypeId, ActivityFromUserId, 
							ActivityFromUserName, ActivityFromUserImage, ActivityProductId, ActivityProductName,
							ActivityProductImage, ActivityProductMessage, ActivityOfferPrice, ActivityMessage,
							ActivityTime));
					}
				}
				activityDTOs.add(new ActivityDTO(success, msg, activityItemDTOs));
				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activityDTOs;
	}*/
	
	
}
