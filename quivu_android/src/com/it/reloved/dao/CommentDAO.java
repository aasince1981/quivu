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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.it.reloved.dto.CommentDTO;
import com.it.reloved.dto.CommentItemDTO;
import com.it.reloved.dto.MessageDTO;
import com.it.reloved.dto.MessageItemDTO;
import com.it.reloved.utils.AppSession;

import android.content.Context;
import android.util.Log;

public class CommentDAO {

	
	private String json = null;
	private String serviceUrl = null;
	String TAG = getClass().getSimpleName();
	Context mContext;

	public CommentDAO(Context context) {
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
	
	/*method for add comment*/
	public List<CommentDTO> addComment(String BASE_URL, String methodName,String CommentProductId,
			String CommentUserId,String CommentUserName,String CommentUserImage,
			String CommentMessage,String CommentProductImage,String CommentProductName,
			String CommentToUserId,String CommentToUserName,String OwnerStatus,
			String ReplyStaus,String ReplyToUserId,String ReplyToUserName) {
		// http://mobilitytesting.com/reloved-app/relovedServices/addComment.php?method=addcomment
		// &CommentProductId=1&CommentUserId=24&CommentUserName=userone&CommentUserImage=a.jpeg
		// &CommentMessage=hello one&CommentProductImage=j1.jpg&CommentProductName=guitar&CommentToUserId=25
		// &CommentToUserName=usertwo&OwnerStatus=0&ReplyStaus=0&ReplyToUserId=&ReplyToUserName=
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "addComment serviceUrl-->" + serviceUrl+"--ReplyStaus="+ReplyStaus);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("CommentProductId", new StringBody(CommentProductId));	
			reqEntity.addPart("CommentUserId", new StringBody(CommentUserId));
			reqEntity.addPart("CommentUserName", new StringBody(CommentUserName));
			reqEntity.addPart("CommentUserImage", new StringBody(CommentUserImage));
			reqEntity.addPart("CommentMessage", new StringBody(CommentMessage));
			reqEntity.addPart("CommentProductImage", new StringBody(CommentProductImage));
			reqEntity.addPart("CommentProductName", new StringBody(CommentProductName));
			reqEntity.addPart("CommentToUserId", new StringBody(CommentToUserId));
			reqEntity.addPart("CommentToUserName", new StringBody(CommentToUserName));
			reqEntity.addPart("OwnerStatus", new StringBody(OwnerStatus));
			reqEntity.addPart("ReplyStaus", new StringBody(ReplyStaus));
			reqEntity.addPart("ReplyToUserId", new StringBody(ReplyToUserId));
			reqEntity.addPart("ReplyToUserName", new StringBody(ReplyToUserName));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseComment(json);
	}
	
	/*method for parsing response of add comment*/
	private List<CommentDTO> parseComment(String jsonResponse) {
		String success = "", msg = "";
		String CommentId="",CommentUserId="",CommentUserName="",CommentUserImage="",CommentMessage="",
				CommentToReplyUserId="",ReplyToUserName="",CommentTime="",CommentOwnerStatus="",
				CommentReplyStaus="";		
		List<CommentItemDTO> commentItemDTOs=new ArrayList<CommentItemDTO>();
		List<CommentDTO> commentDTOs=new ArrayList<CommentDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");	
				
				if (jsonObject.has("Comment")) {
					JSONObject jsonObject3 = jsonObject.getJSONObject("Comment");
					//AppSession appSession = new AppSession(mContext);
					if (jsonObject3.has("CommentId"))
						CommentId = jsonObject3.getString("CommentId");
					if (jsonObject3.has("CommentUserId"))
						CommentUserId = jsonObject3.getString("CommentUserId");
					if (jsonObject3.has("CommentUserName"))
						CommentUserName = jsonObject3.getString("CommentUserName");
					if (jsonObject3.has("CommentUserImage"))
						CommentUserImage =jsonObject3.getString("CommentUserImage");
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

					commentItemDTOs.add(new CommentItemDTO(CommentUserId,CommentUserName, CommentUserImage,
							CommentMessage,CommentToReplyUserId, ReplyToUserName, CommentTime,
							CommentOwnerStatus, CommentReplyStaus,CommentId));
				}
				commentDTOs.add(new CommentDTO(success, msg, commentItemDTOs));					
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentDTOs;
	}
	
	/*method for delete comment*/
	public String[] deleteComment(String BASE_URL, String methodName,String CommentId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/comments.php?method=deleteComment
		// &CommentId=3
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "deleteComment serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("CommentId", new StringBody(CommentId));	
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseContent(json);
	}	
	
	/*method for parsing response of delete comment*/
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
	
	/*method for get message*/
	public List<MessageDTO> getMessages(String BASE_URL, String methodName,String UserId,
			String ToUserId,String MessageProductId) {
		// http://mobilitytesting.com/reloved-app/relovedServices/messages.php?method=getMessages&UserId=24
		//&MessageProductId=1
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getMessages serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("UserId", new StringBody(UserId));
			reqEntity.addPart("ToUserId", new StringBody(ToUserId));
			reqEntity.addPart("MessageProductId", new StringBody(MessageProductId));
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseMessages(json);
	}	
	
	/*method for parsing response of message*/
	private List<MessageDTO> parseMessages(String jsonResponse) {
		String success = "", msg = "";		
		List<MessageDTO> messageDTOs=new ArrayList<MessageDTO>();
		List<MessageItemDTO> messageItemDTOs=new ArrayList<MessageItemDTO>();		
		String MessageId="",MessageFromUserId="",MessageFromUserName="",MessageFromUserImage="",
				MessageToUserId="",Message="",MessageAddTime="",MessageType="";
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");		
				
				if (jsonObject.has("Messages")){
					messageItemDTOs=new ArrayList<MessageItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("Messages");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("MessageId"))
							MessageId = jsonObject2.getString("MessageId");	
						if (jsonObject2.has("MessageFromUserId"))
							MessageFromUserId = jsonObject2.getString("MessageFromUserId");							
						if (jsonObject2.has("MessageFromUserName"))
							MessageFromUserName = jsonObject2.getString("MessageFromUserName");	
						//AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("MessageFromUserImage"))
							MessageFromUserImage =jsonObject2.getString("MessageFromUserImage");	
						if (jsonObject2.has("MessageToUserId"))
							MessageToUserId = jsonObject2.getString("MessageToUserId");	
						if (jsonObject2.has("Message"))
							Message = jsonObject2.getString("Message");	
						if (jsonObject2.has("MessageAddTime"))
							MessageAddTime = jsonObject2.getString("MessageAddTime");	
						if (jsonObject2.has("MessageType"))
							MessageType = jsonObject2.getString("MessageType");
						
						messageItemDTOs.add(new MessageItemDTO(MessageId, MessageFromUserId, 
								MessageFromUserName, MessageFromUserImage, MessageToUserId, Message, 
								MessageAddTime, MessageType));
					}
				}
				
				messageDTOs.add(new MessageDTO(success, msg, messageItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageDTOs;
	}
	
	/*method for add message*/
	public List<MessageDTO> addMessages(String BASE_URL, String methodName,String MessageFromUserId,
			String MessageFromUserName,String MessageFromUserImage,String MessageToUserId,
			String Message,String MessageType,String MessageProductId,String MessageProductImage) {
		// http://mobilitytesting.com/reloved-app/relovedServices/messages.php?method=addMessages
		// &MessageFromUserId=25&MessageFromUserName=sachin&MessageFromUserImage=a.jpeg&MessageToUserId=24
		// &Message=hello%20one&MessageType=0
		serviceUrl = BASE_URL + methodName;
		Log.i(TAG, "getMessages serviceUrl-->" + serviceUrl);
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			reqEntity.addPart("MessageFromUserId", new StringBody(MessageFromUserId));	
			reqEntity.addPart("MessageFromUserName", new StringBody(MessageFromUserName));	
			reqEntity.addPart("MessageFromUserImage", new StringBody(MessageFromUserImage));	
			reqEntity.addPart("MessageToUserId", new StringBody(MessageToUserId));				
			reqEntity.addPart("MessageType", new StringBody(MessageType));	
			reqEntity.addPart("MessageProductId", new StringBody(MessageProductId));
			reqEntity.addPart("MessageProductImage", new StringBody(MessageProductImage));
			
			if(MessageType.equals("1"))
				reqEntity.addPart("Message", new FileBody(new File(Message)));	
			else
				reqEntity.addPart("Message", new StringBody(Message));	
			
			json = makeConnection(serviceUrl, reqEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parseAddMessages(json);
	}	
	
	/*method for parsing response of add message*/
	private List<MessageDTO> parseAddMessages(String jsonResponse) {
		String success = "", msg = "";		
		List<MessageDTO> messageDTOs=new ArrayList<MessageDTO>();
		List<MessageItemDTO> messageItemDTOs=new ArrayList<MessageItemDTO>();		
		String MessageId="",MessageFromUserId="",MessageFromUserName="",MessageFromUserImage="",
				MessageToUserId="",Message="",MessageAddTime="",MessageType="";
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");		
				
				if (jsonObject.has("message")){					
						JSONObject jsonObject2=jsonObject.getJSONObject("message");
						if (jsonObject2.has("MessageId"))
							MessageId = jsonObject2.getString("MessageId");	
						if (jsonObject2.has("MessageFromUserId"))
							MessageFromUserId = jsonObject2.getString("MessageFromUserId");							
						if (jsonObject2.has("MessageFromUserName"))
							MessageFromUserName = jsonObject2.getString("MessageFromUserName");	
					//	AppSession appSession=new AppSession(mContext);
						if (jsonObject2.has("MessageFromUserImage"))
							MessageFromUserImage = jsonObject2.getString("MessageFromUserImage");	
						if (jsonObject2.has("MessageToUserId"))
							MessageToUserId = jsonObject2.getString("MessageToUserId");	
						if (jsonObject2.has("Message"))
							Message = jsonObject2.getString("Message");	
						if (jsonObject2.has("MessageAddTime"))
							MessageAddTime = jsonObject2.getString("MessageAddTime");	
						if (jsonObject2.has("MessageType"))
							MessageType = jsonObject2.getString("MessageType");						
						messageItemDTOs.add(new MessageItemDTO(MessageId, MessageFromUserId, 
								MessageFromUserName, MessageFromUserImage, MessageToUserId, Message, 
								MessageAddTime, MessageType));					
				}				
				messageDTOs.add(new MessageDTO(success, msg, messageItemDTOs));
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return messageDTOs;
	}
}
