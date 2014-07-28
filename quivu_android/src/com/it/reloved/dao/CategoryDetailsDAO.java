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
import com.it.reloved.dto.CountryDTO;
import com.it.reloved.utils.AppSession;

public class CategoryDetailsDAO {

	private String json = null;
	private String serviceUrl = null;	
	String TAG=getClass().getSimpleName();	
	Context mContext;	
		
	public CategoryDetailsDAO(Context context) {
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
	
	/*method for getting subcategories*/
	public List<CategoryDTO> getCategoryItems(String BASE_URL,String methodName,String CategoryId,
			String UserLatitude,String UserLongitude) {	
	// http://mobilitytesting.com/reloved-app/relovedServices/getProduct.php?method=getProducts
	// &CategoryId=10&UserLatitude=74.41000&UserLongitude=21.0000		
		serviceUrl =BASE_URL+methodName;
		Log.i(TAG, "getCategoryItems serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {					
			reqEntity.addPart("CategoryId",new StringBody( CategoryId));
			reqEntity.addPart("UserLatitude",new StringBody( UserLatitude));
			reqEntity.addPart("UserLongitude",new StringBody( UserLongitude));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseCategoryItem(json);
	}	
	
	/*method for getting offers made by me*/
	public List<CategoryDTO> getofferMadeByme(String BASE_URL, String methodName,String UserId) {
		//http://mobilitytesting.com/reloved-app/relovedServices/offer.php?method=offerMadeByme&UserId=35
		Log.i("getofferMadeByme", "UserId="+UserId);
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowing serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseOfferMade(json);
	}
	
	/*method for getting likes*/
	public List<CategoryDTO> getstuffLikes(String BASE_URL, String methodName,String UserId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/likes.php?method=stuffLikes&UserId=24
		Log.i("getstuffLikes", "UserId="+UserId);
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFollowing serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseCategoryItem(json);
	}
	
	/*method for searching products*/
	public List<CategoryDTO> searchProducts(String BASE_URL,String methodName,String searchName,
			String UserId,String shortBy,String categoryId,String searchRange,String minPrice,
			String maxPrice,String UserLatitude,String UserLongitude) {	
	// http://mobilitytesting.com/reloved-app/relovedServices/searchProduct.php?method=productFilter
	// &searchName=&UserId=15&shortBy=3&categoryId=0&searchRange=&minPrice=&maxPrice=&UserLatitude=22.1458
	//	&UserLongitude=75.124587	
		
		Log.i(TAG, "searchName="+searchName+"--UserId="+UserId+"--shortBy="+shortBy+"--categoryId="
		+categoryId+"---searchRange="+searchRange+"--minPrice="+minPrice+"--maxPrice="+maxPrice);
		serviceUrl =BASE_URL+methodName;
		Log.i(TAG, "getCategoryItems serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {					
			reqEntity.addPart("searchName",new StringBody(searchName));
			reqEntity.addPart("UserId",new StringBody(UserId));
			reqEntity.addPart("shortBy",new StringBody(shortBy));
			reqEntity.addPart("categoryId",new StringBody(categoryId));
			reqEntity.addPart("searchRange",new StringBody(searchRange));
			reqEntity.addPart("minPrice",new StringBody(minPrice));
			reqEntity.addPart("maxPrice",new StringBody(maxPrice));
			reqEntity.addPart("UserLatitude",new StringBody(UserLatitude));
			reqEntity.addPart("UserLongitude",new StringBody(UserLongitude));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseCategoryItem(json);
	}	
	
	/*method for parsing response of category*/
	private List<CategoryDTO> parseCategoryItem(String jsonResponse) {
		String success = "", msg = "";
		List<CategoryDTO> categoryDTOs=new ArrayList<CategoryDTO>();
		String ProductId="",ProductName="",ProductPrice="",ProductLikeCount="",ProductUserId="",
				ProductUserName="",ProductUserImage="",ProductAddDate="",ProductImageInfo="",
						ProductWebsiteUrl="",LikeStatus="",ProductSoldStatus="";
		
		List<CategoryItemDTO> categoryItemDTOs=new ArrayList<CategoryItemDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("Products")){
					categoryItemDTOs=new ArrayList<CategoryItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("Products");
					
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
						if (jsonObject2.has("ProductUserId"))
							ProductUserId = jsonObject2.getString("ProductUserId");						
						if (jsonObject2.has("ProductUserName"))
							ProductUserName = jsonObject2.getString("ProductUserName");
						//AppSession appSession1=new AppSession(mContext);
						if (jsonObject2.has("ProductUserImage"))
							ProductUserImage =jsonObject2.getString("ProductUserImage");
						if (jsonObject2.has("ProductAddDate"))
							ProductAddDate = jsonObject2.getString("ProductAddDate");
						if (jsonObject2.has("ProductWebsiteUrl"))
							ProductWebsiteUrl = jsonObject2.getString("ProductWebsiteUrl");
						if (jsonObject2.has("ProductSoldStatus"))
							ProductSoldStatus = jsonObject2.getString("ProductSoldStatus");
						
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
						
						if (jsonObject2.has("ProductLikeInfo")){
							LikeStatus="";
							JSONArray jsonArray2=jsonObject2.getJSONArray("ProductLikeInfo");							
							for (int j = 0; j < jsonArray2.length(); j++) {
								JSONObject jsonObject3=jsonArray2.getJSONObject(j);								
								if(jsonObject3.has("UserId")){
									String UserId=jsonObject3.getString("UserId");
									AppSession appSession1=new AppSession(mContext);
									if (UserId.equals(appSession1.getUserId())) {
										LikeStatus="1";
										break;
									}else{
										LikeStatus="0";
									}
								}
							}
							
							Log.i("CategoryDetailsDAO", "LikeStatus="+LikeStatus);
						}		
						categoryItemDTOs.add(new CategoryItemDTO(ProductId, ProductName, ProductImageInfo,
							ProductPrice, ProductLikeCount, ProductUserId, ProductUserName, ProductUserImage,
							ProductAddDate,ProductWebsiteUrl,LikeStatus,ProductSoldStatus));						
					}
				}				
				categoryDTOs.add(new CategoryDTO(success, msg, categoryItemDTOs));					
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryDTOs;
	}
	
	/*method for parsing response of offer made*/
	private List<CategoryDTO> parseOfferMade(String jsonResponse) {
		String success = "", msg = "";
		List<CategoryDTO> categoryDTOs=new ArrayList<CategoryDTO>();
		String ProductId="",ProductName="",ProductPrice="",ProductLikeCount="",ProductUserId="",
				ProductUserName="",ProductUserImage="",ProductAddDate="",ProductImageInfo="",
						ProductWebsiteUrl="",LikeStatus="",ProductSoldStatus="";
		
		List<CategoryItemDTO> categoryItemDTOs=new ArrayList<CategoryItemDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("offers")){
					categoryItemDTOs=new ArrayList<CategoryItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("offers");
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("ProductId"))
							ProductId = jsonObject2.getString("ProductId");
						if (jsonObject2.has("OfferProductName"))
							ProductName = jsonObject2.getString("OfferProductName");
						if (jsonObject2.has("OfferAmount"))
							ProductPrice = jsonObject2.getString("OfferAmount");
						if (jsonObject2.has("ProductLikeCount"))
							ProductLikeCount = jsonObject2.getString("ProductLikeCount");
						if (jsonObject2.has("ProductUserId"))
							ProductUserId = jsonObject2.getString("ProductUserId");						
						if (jsonObject2.has("ProductUserName"))
							ProductUserName = jsonObject2.getString("ProductUserName");
					//	AppSession appSession1=new AppSession(mContext);
						if (jsonObject2.has("ProductUserImage"))
							ProductUserImage =jsonObject2.getString("ProductUserImage");
						if (jsonObject2.has("OfferAddTime"))
							ProductAddDate = jsonObject2.getString("OfferAddTime");
						if (jsonObject2.has("ProductWebsiteUrl"))
							ProductWebsiteUrl = jsonObject2.getString("ProductWebsiteUrl");
						if (jsonObject2.has("ProductSoldStatus"))
							ProductSoldStatus = jsonObject2.getString("ProductSoldStatus");
						
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
						if (jsonObject2.has("ProductLikeInfo")){
							LikeStatus="";
							JSONArray jsonArray2=jsonObject2.getJSONArray("ProductLikeInfo");							
							for (int j = 0; j < jsonArray2.length(); j++) {
								JSONObject jsonObject3=jsonArray2.getJSONObject(j);								
								if(jsonObject3.has("UserId")){
									String UserId=jsonObject3.getString("UserId");
									AppSession appSession1=new AppSession(mContext);
									if (UserId.equals(appSession1.getUserId())) {
										LikeStatus="1";
										break;
									}else{
										LikeStatus="0";
									}
								}
							}
						}		
						categoryItemDTOs.add(new CategoryItemDTO(ProductId, ProductName, ProductImageInfo,
							ProductPrice, ProductLikeCount, ProductUserId, ProductUserName, ProductUserImage,
							ProductAddDate,ProductWebsiteUrl,LikeStatus,ProductSoldStatus));						
					}
				}				
				categoryDTOs.add(new CategoryDTO(success, msg, categoryItemDTOs));					
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryDTOs;
	}
}
