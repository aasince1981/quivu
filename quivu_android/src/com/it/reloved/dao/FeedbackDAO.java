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

import com.it.reloved.dto.FeedbackDTO;
import com.it.reloved.dto.FeedbackItemDTO;
import com.it.reloved.utils.AppSession;

public class FeedbackDAO {

	private String json = null;
	private String serviceUrl = null;
	String TAG = getClass().getSimpleName();
	Context mContext;

	public FeedbackDAO(Context context) {
		super();
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

	/*method for getting feedback*/
	public List<FeedbackDTO> getFeedback(String BASE_URL, String methodName,String UserId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/feedback.php?method=getFeedback&UserId=3
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getFeedback serviceUrl-->" + serviceUrl+"--UserId="+UserId);
		MultipartEntity reqEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));			
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseFeedback(json);
	}
	
	/*method for parsing response feedback*/
	private List<FeedbackDTO> parseFeedback(String jsonResponse) {		
		String success = "", msg = "";
		List<FeedbackDTO> feedbackDTOs=new ArrayList<FeedbackDTO>();
		List<FeedbackItemDTO> feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
		String FeedbackByUserId="",FeedbackByUserName="",FeedbackByUserImage="",FeedbackToUserId="",
				FeedbackToUserName="",FeedbackType="",FeedbackExperience="",FeedbackDescription="",
				FeedbackTime="",FeedbackEditStatus="",FeedbackEditTime="",FeedbackReplyStatus="",
				FeedbackReplyMessage="",FeedbackReplyTime="",FeedbackEditReplyTime="",
				FeedbackToUserImage="",FeedbackId="";
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");	
				
				if (jsonObject.has("Feedback")) {
					feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("Feedback");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("FeedbackByUserId"))
							FeedbackByUserId = jsonObject2.getString("FeedbackByUserId");	
						if (jsonObject2.has("FeedbackByUserName"))
							FeedbackByUserName = jsonObject2.getString("FeedbackByUserName");	
						//AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("FeedbackByUserImage"))
							FeedbackByUserImage = jsonObject2.getString("FeedbackByUserImage");	
						if (jsonObject2.has("FeedbackToUserId"))
							FeedbackToUserId = jsonObject2.getString("FeedbackToUserId");	
						if (jsonObject2.has("FeedbackToUserName"))
							FeedbackToUserName = jsonObject2.getString("FeedbackToUserName");	
						if (jsonObject2.has("FeedbackToUserImage"))
							FeedbackToUserImage = jsonObject2.getString("FeedbackToUserImage");	
						if (jsonObject2.has("FeedbackType"))
							FeedbackType = jsonObject2.getString("FeedbackType");	
						if (jsonObject2.has("FeedbackExperience"))
							FeedbackExperience = jsonObject2.getString("FeedbackExperience");	
						if (jsonObject2.has("FeedbackDescription"))
							FeedbackDescription = jsonObject2.getString("FeedbackDescription");	
						if (jsonObject2.has("FeedbackTime"))
							FeedbackTime = jsonObject2.getString("FeedbackTime");	
						if (jsonObject2.has("FeedbackEditStatus"))
							FeedbackEditStatus = jsonObject2.getString("FeedbackEditStatus");	
						if (jsonObject2.has("FeedbackEditTime"))
							FeedbackEditTime = jsonObject2.getString("FeedbackEditTime");	
						if (jsonObject2.has("FeedbackReplyStatus"))
							FeedbackReplyStatus = jsonObject2.getString("FeedbackReplyStatus");	
						if (jsonObject2.has("FeedbackReplyMessage"))
							FeedbackReplyMessage = jsonObject2.getString("FeedbackReplyMessage");							
						if (jsonObject2.has("FeedbackReplyTime"))
							FeedbackReplyTime = jsonObject2.getString("FeedbackReplyTime");	
						if (jsonObject2.has("FeedbackEditReplyTime"))
							FeedbackEditReplyTime = jsonObject2.getString("FeedbackEditReplyTime");	
						if (jsonObject2.has("FeedbackId"))
							FeedbackId = jsonObject2.getString("FeedbackId");	
						
						feedbackItemDTOs.add(new FeedbackItemDTO(FeedbackByUserId, FeedbackByUserName, 
							FeedbackByUserImage, FeedbackToUserId, FeedbackToUserName, FeedbackType,
							FeedbackExperience, FeedbackDescription, FeedbackTime, FeedbackEditStatus,
							FeedbackEditTime, FeedbackReplyStatus, FeedbackReplyMessage, FeedbackReplyTime,
							FeedbackEditReplyTime,FeedbackToUserImage,FeedbackId));						
					}
				}							
				feedbackDTOs.add(new FeedbackDTO(success,msg,feedbackItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedbackDTOs;
	}
	
	/*method for add feedback*/
	public String[] addFeedback(String BASE_URL, String methodName,String FeedbackFromUserId,
			String FeedbackFromUserImage,String FeedbackFromUserName,String FeedbackToUserId,
			String FeedbackDescription,String FeedbackType,String FeedbackExperience,
			String FeedbackToUserImage,String FeedbackToUserName) {
		// http://mobilitytesting.com/reloved-app/relovedServices/feedback.php?method=addFeedback
		// &FeedbackFromUserId=1&FeedbackFromUserName=om123@&FeedbackFromUserImage=a.jpeg
		// &FeedbackToUserId=15&FeedbackReplyStatus=0&FeedbackDescription=hello%20one
		// &FeedbackType=1&FeedbackExperience=1
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "addFeedback serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("FeedbackFromUserId", new StringBody(FeedbackFromUserId));	
			reqEntity.addPart("FeedbackFromUserImage", new StringBody(FeedbackFromUserImage));
			reqEntity.addPart("FeedbackFromUserName", new StringBody(FeedbackFromUserName));
			reqEntity.addPart("FeedbackToUserId", new StringBody(FeedbackToUserId));
			reqEntity.addPart("FeedbackReplyStatus", new StringBody("0"));
			reqEntity.addPart("FeedbackDescription", new StringBody(FeedbackDescription));
			reqEntity.addPart("FeedbackType", new StringBody(FeedbackType));
			reqEntity.addPart("FeedbackExperience", new StringBody(FeedbackExperience));
			reqEntity.addPart("FeedbackToUserImage", new StringBody(FeedbackToUserImage));
			reqEntity.addPart("FeedbackToUserName", new StringBody(FeedbackToUserName));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseContent(json);
	}
	
	
	/*method for add reply*/
	public String[] addReply(String BASE_URL, String methodName,String FeedbackFromUserId,
			String FeedbackFromUserImage,String FeedbackFromUserName,String FeedbackToUserId,
			String FeedbackReplyMessage) {
		// http://mobilitytesting.com/reloved-app/relovedServices/feedback.php?method=addFeedback
		// &FeedbackFromUserId=15&FeedbackFromUserName=om123@&FeedbackFromUserImage=a.jpeg&FeedbackToUserId=1
		// &FeedbackReplyStatus=1&FeedbackReplyMessage=hello%20one
		
		Log.i(TAG, "FeedbackFromUserId="+FeedbackFromUserId+"--FeedbackFromUserImage="+FeedbackFromUserImage+
				"--FeedbackFromUserName="+FeedbackFromUserName+"--FeedbackToUserId="+FeedbackToUserId+
				"FeedbackReplyStatus="+"1"+"--FeedbackReplyMessage="+FeedbackReplyMessage);
		
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "addReply serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("FeedbackFromUserId", new StringBody(FeedbackFromUserId));	
			reqEntity.addPart("FeedbackFromUserImage", new StringBody(FeedbackFromUserImage));
			reqEntity.addPart("FeedbackFromUserName", new StringBody(FeedbackFromUserName));
			reqEntity.addPart("FeedbackToUserId", new StringBody(FeedbackToUserId));
			reqEntity.addPart("FeedbackReplyStatus", new StringBody("1"));
			reqEntity.addPart("FeedbackReplyMessage", new StringBody(FeedbackReplyMessage));			
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseContent(json);
	}
	
	/*method for edit feedback*/
	public String[] editFeedback(String BASE_URL, String methodName,String FeedbackId,
			String FeedbackReplyStatus,String FeedbackMessage,String FeedbackType,String FeedbackExperience) {
		// http://mobilitytesting.com/reloved-app/relovedServices/feedback.php?method=editFeedback
		// &FeedbackId=1&FeedbackReplyStatus=0&FeedbackMessage=hello one		
		Log.i("", "FeedbackId="+FeedbackId+"--FeedbackReplyStatus="+FeedbackReplyStatus
				+"--FeedbackMessage="+FeedbackMessage+"--FeedbackType="+FeedbackType+"--FeedbackExperience="+FeedbackExperience);
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "addFeedback serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("FeedbackId", new StringBody(FeedbackId));	
			reqEntity.addPart("FeedbackReplyStatus", new StringBody(FeedbackReplyStatus));
			reqEntity.addPart("FeedbackType", new StringBody(FeedbackType));
			reqEntity.addPart("FeedbackExperience", new StringBody(FeedbackExperience));			
			reqEntity.addPart("FeedbackMessage", new StringBody(FeedbackMessage));					
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseContent(json);
	}
	
	/*method for parsing feedback*/
	private String[] parseContent(String jsonResponse) {
		String success = "", msg = "";
		String[] responseArr = new String[2];
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");				
				responseArr[0]=success;				
				responseArr[1]=msg;							
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
