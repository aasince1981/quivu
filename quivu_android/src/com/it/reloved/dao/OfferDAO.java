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
import android.util.Base64;
import android.util.Log;

import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.OfferDTO;
import com.it.reloved.dto.OfferItemDTO;

public class OfferDAO {

	private String json = null;
	private String serviceUrl = null;	
	String TAG=getClass().getSimpleName();	
	
	Context mContext;
	
	public OfferDAO(Context context) {
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

	/*method for get offers*/
	public List<OfferDTO> getOffers(String BASE_URL,String methodName,String OfferProductId) {	
	// http://mobilitytesting.com/reloved-app/relovedServices/getOffer.php?method=getOffers&OfferProductId=4		
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "getProfileDetails serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("OfferProductId",new StringBody( OfferProductId));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseOffers(json);
	}	
	
	/*method for parsing offers*/
	private List<OfferDTO> parseOffers(String jsonResponse) {
		
		String success = "", msg = "";				
		String OfferUserId="",OfferUserName="",OfferUserImage="",OfferProductId="",
				OfferAmount="",OfferAddTime="",OfferMessage="";
		
		List<OfferDTO> offerDTOs=new ArrayList<OfferDTO>();
		List<OfferItemDTO> offerItemDTOs=new ArrayList<OfferItemDTO>();
	
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("offers")){
					offerItemDTOs=new ArrayList<OfferItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("offers");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);						
						if (jsonObject2.has("OfferUserId"))
							OfferUserId = jsonObject2.getString("OfferUserId");
						if (jsonObject2.has("OfferUserName"))
							OfferUserName = jsonObject2.getString("OfferUserName");
						//AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("OfferUserImage"))
							OfferUserImage = jsonObject2.getString("OfferUserImage");
						if (jsonObject2.has("OfferProductId"))
							OfferProductId = jsonObject2.getString("OfferProductId");
						if (jsonObject2.has("OfferAmount"))
							OfferAmount = jsonObject2.getString("OfferAmount");
						if (jsonObject2.has("OfferAddTime"))
							OfferAddTime = jsonObject2.getString("OfferAddTime");
						if (jsonObject2.has("OfferMessage"))
							OfferMessage = jsonObject2.getString("OfferMessage");
						
						offerItemDTOs.add(new OfferItemDTO(OfferUserId, OfferUserName, OfferUserImage,
							OfferProductId, OfferAmount, OfferAddTime,OfferMessage));
					}
				}
				offerDTOs.add(new OfferDTO(success, msg, offerItemDTOs));
				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offerDTOs;
	}
	
	/*method for add offers*/
	public String[] addOffers(String BASE_URL,String methodName,String OfferFromUserId,String OfferFromUserName,
			String OfferFromUserImage,String OfferToUserId,String OfferProductId,String OfferProductName,
			String OfferProductImage,String OfferAmount) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/addoffer.php?method=addOffer
		// &OfferFromUserId=3&OfferFromUserName=lomesh123@&OfferFromUserImage=a.jpeg&OfferToUserId=15
		// &OfferProductId=1&OfferProductName=Latest&OfferProductImage=chilis.jpg&OfferAmount=50
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "addOffers serviceUrl-->" + serviceUrl);		
			Log.i(TAG, "OfferFromUserId="+OfferFromUserId+"--OfferFromUserName="+OfferFromUserName
					+"--OfferFromUserImage="+OfferFromUserImage+"--OfferToUserId="+OfferToUserId
					+"---OfferProductId="+OfferProductId+"--OfferProductName="+OfferProductName+"--OfferAmount="+OfferAmount);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("OfferFromUserId",new StringBody( OfferFromUserId));
				reqEntity.addPart("OfferFromUserName",new StringBody( OfferFromUserName));
				reqEntity.addPart("OfferFromUserImage",new StringBody(OfferFromUserImage));
				reqEntity.addPart("OfferToUserId",new StringBody( OfferToUserId));
				reqEntity.addPart("OfferProductId",new StringBody( OfferProductId));
				reqEntity.addPart("OfferProductName",new StringBody( OfferProductName));
				reqEntity.addPart("OfferProductImage",new StringBody(OfferProductImage));
				reqEntity.addPart("OfferAmount",new StringBody( OfferAmount.replace("$", "")));				
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseContent(json);
		}	
	
	/*method for add likes*/
	public String[] addLikes(String BASE_URL,String methodName,String FromUserId,String FromUserName,
			String ToUserId,String ToUserName,String LikeProductId,String LikeProductName,String LikeStatus,
			String FromUserImage,String ProductImage) {	
		// http://mobilitytesting.com/reloved-app/likes.php?method=addLike&FromUserId=15
		// &FromUserName=sanjay&ToUserId=1&ToUserName=sachin&LikeProductId=1&LikeProductName=apple
		// &LikeStatus=1&FromUserImage=a.jpeg&ProductImage=b.jpeg
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "addOffers serviceUrl-->" + serviceUrl);		
			Log.i(TAG, "FromUserId="+FromUserId+"--FromUserName="+FromUserName+"---ToUserId="+ToUserId+
					"---ToUserName="+ToUserName+"--LikeProductId="+LikeProductId+"---LikeProductName="+LikeProductName
					+"--LikeStatus="+LikeStatus+"--FromUserImage="+FromUserImage+"---ProductImage="+ProductImage);
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("FromUserId",new StringBody( FromUserId));
				reqEntity.addPart("FromUserName",new StringBody( FromUserName));
				reqEntity.addPart("ToUserId",new StringBody(ToUserId));
				reqEntity.addPart("ToUserName",new StringBody( ToUserName));
				reqEntity.addPart("LikeProductId",new StringBody( LikeProductId));
				reqEntity.addPart("LikeProductName",new StringBody( LikeProductName));
				reqEntity.addPart("LikeStatus",new StringBody(LikeStatus));
				reqEntity.addPart("FromUserImage",new StringBody( FromUserImage));	
				reqEntity.addPart("ProductImage",new StringBody( ProductImage));	
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
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
	
	/*method for cancel offers*/
	public String[] cancelOffer(String BASE_URL,String methodName,String CancelFromUserId,String CancelFromUserName,
			String CancelFromUserImage,String CancelToUserId,String CancelProductId,String CancelProductName,
			String CancelProductImage,String OfferCancelBy) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/offer.php?method=cancelOffer
		// &CancelFromUserId=3&CancelFromUserName=testone&CancelFromUserImage=a.jpeg&CancelToUserId=1
		// &CancelStatus=1&CancelProductId=1&CancelProductName=camera&CancelProductImage=camera.jpeg&OfferCancelBy=
		
		Log.i(TAG, "CancelFromUserId="+CancelFromUserId+"--CancelFromUserName="+CancelFromUserName
				+"---CancelFromUserImage="+CancelFromUserImage+"--CancelToUserId="+CancelToUserId
				+"--CancelProductId="+CancelProductId+"---CancelProductName="+CancelProductName
				+"--CancelProductImage="+CancelProductImage+"--OfferCancelBy="+OfferCancelBy);
		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "cancelOffer serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("CancelFromUserId",new StringBody( CancelFromUserId));
				reqEntity.addPart("CancelFromUserName",new StringBody( CancelFromUserName));
				reqEntity.addPart("CancelFromUserImage",new StringBody(CancelFromUserImage));
				reqEntity.addPart("CancelToUserId",new StringBody( CancelToUserId));
				reqEntity.addPart("CancelStatus",new StringBody( "1"));
				reqEntity.addPart("CancelProductId",new StringBody( CancelProductId));
				reqEntity.addPart("CancelProductName",new StringBody(CancelProductName));
				reqEntity.addPart("CancelProductImage",new StringBody( CancelProductImage));		
				reqEntity.addPart("OfferCancelBy",new StringBody( OfferCancelBy));		
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseContent(json);
		}	
	
	/*method for mark as sold*/
	public String[] markAsSoldOffer(String BASE_URL,String methodName,String AcceptFromUserId,
			String AcceptFromUserName,
			String AcceptFromUserImage,String AcceptToUserId,String AcceptProductId,String AcceptProducName,
			String AcceptProducImage) {	
		//http://mobilitytesting.com/reloved-app/relovedServices/offer.php?method=acceptOffer
		//&AcceptFromUserId=15&AcceptFromUserName=sachin&AcceptFromUserImage=a.jpeg&AcceptToUserId=3
		//&AcceptStatus=1&AcceptProductId=8&AcceptProducName=guitar&AcceptProducImage=guitar.jpeg
		
		Log.i(TAG, "AcceptFromUserId="+AcceptFromUserId+"--AcceptFromUserName="+AcceptFromUserName
				+"---AcceptFromUserImage="+AcceptFromUserImage+"--AcceptToUserId="+AcceptToUserId
				+"--AcceptProductId="+AcceptProductId+"---AcceptProducName="+AcceptProducName
				+"--AcceptProducImage="+AcceptProducImage);
		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "cancelOffer serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("AcceptFromUserId",new StringBody( AcceptFromUserId));
				reqEntity.addPart("AcceptFromUserName",new StringBody( AcceptFromUserName));
				reqEntity.addPart("AcceptFromUserImage",new StringBody(AcceptFromUserImage));
				reqEntity.addPart("AcceptToUserId",new StringBody( AcceptToUserId));
				reqEntity.addPart("AcceptStatus",new StringBody( "1"));
				reqEntity.addPart("AcceptProductId",new StringBody( AcceptProductId));
				reqEntity.addPart("AcceptProducName",new StringBody(AcceptProducName));
				reqEntity.addPart("AcceptProducImage",new StringBody( AcceptProducImage));					
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseContent(json);
		}
	
	/*method for get Likes*/
	public List<CategoryDTO> getLikes(String BASE_URL,String methodName,String ProductId,String UserId) {	
	// http://mobilitytesting.com/reloved-app/relovedServices/likes.php?method=getLikesUsers&ProductId=1&UserId=1	
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "getProfileDetails serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {			
			reqEntity.addPart("ProductId",new StringBody( ProductId));
			reqEntity.addPart("UserId",new StringBody( UserId));
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseLikes(json);
	}	
	
	/*method for search by username*/
	public List<CategoryDTO> searchByUsername(String BASE_URL,String methodName,String UserName,String UserId) {	
		//http://mobilitytesting.com/reloved-app/relovedServices/searchUser.php?method=searchUsers
		//&UserName=userone&UserId=15
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "getProfileDetails serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {			
				reqEntity.addPart("UserName",new StringBody( UserName));
				reqEntity.addPart("UserId",new StringBody( UserId));
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseLikes(json);
		}	
	
	/*method for parsing Likes*/
	private List<CategoryDTO> parseLikes(String jsonResponse) {
		String success = "", msg = "";
		String UserId="",UserName="",UserImage="",FollowStatus="";
		List<CategoryDTO> likeDTOs=new ArrayList<CategoryDTO>();
		 List<CategoryItemDTO> likeItemDTOs=new ArrayList<CategoryItemDTO>();
		 
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("Users")){
					JSONArray jsonArray=jsonObject.getJSONArray("Users");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("UserId"))
							UserId = jsonObject2.getString("UserId");
						if (jsonObject2.has("UserName"))
							UserName = jsonObject2.getString("UserName");
					//	AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("UserImage"))
							UserImage =jsonObject2.getString("UserImage");
						if (jsonObject2.has("FollowStatus"))
							FollowStatus = jsonObject2.getString("FollowStatus");
						
						likeItemDTOs.add(new CategoryItemDTO(UserId, UserName, UserImage, FollowStatus));
					}
				}
				likeDTOs.add(new CategoryDTO(success, msg, likeItemDTOs));		
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return likeDTOs;
	}
}
