package com.it.reloved.dao;

import java.io.File;
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
import org.apache.http.entity.mime.content.FileBody;
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
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.CountryDTO;
import com.it.reloved.dto.ItemDTO;
import com.it.reloved.utils.AppSession;
import com.sromku.simple.fb.entities.Language;

public class UtilityDAO {

	private String json = null;
	private String serviceUrl = null;	
	String TAG=getClass().getSimpleName();	
	Context mContext;	
		
	public UtilityDAO(Context context) {
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
	
	/*method for getCountries*/
	public List<CountryDTO> getCountries(String BASE_URL,String methodName) {	
	// http://192.168.1.111/carousell-app/carousellServices/getCountries.php?method=getCountries				
		serviceUrl =BASE_URL+methodName;
		Log.i(TAG, "getCountries serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {									
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseCountries(json);
	}	
	
	/*method for parsing countries response*/
	private List<CountryDTO> parseCountries(String jsonResponse) {
		String success = "", msg = "";
		String CountryId="",CountryName="",RegionId="",RegionName="",CityId="",CityName="";
		
		List<ItemDTO> cityItemDTOs=new ArrayList<ItemDTO>();
		List<ItemDTO> regionItemDTOs=new ArrayList<ItemDTO>();
		List<ItemDTO> countryItemDTOs=new ArrayList<ItemDTO>();
		List<CountryDTO> countryDTOs=new ArrayList<CountryDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("CountryList")){
					countryItemDTOs=new ArrayList<ItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("CountryList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("CountryId"))
							CountryId = jsonObject2.getString("CountryId");
						if (jsonObject2.has("CountryName"))
							CountryName = jsonObject2.getString("CountryName");
						if (jsonObject2.has("Region")){
							regionItemDTOs=new ArrayList<ItemDTO>();
							JSONArray jsonArray2=jsonObject2.getJSONArray("Region");
							for (int j = 0; j < jsonArray2.length(); j++) {
								JSONObject jsonObject3=jsonArray2.getJSONObject(j);
								if (jsonObject3.has("RegionId"))
									RegionId = jsonObject3.getString("RegionId");
								if (jsonObject3.has("RegionName"))
									RegionName = jsonObject3.getString("RegionName");
								if (jsonObject3.has("City")){
									cityItemDTOs=new ArrayList<ItemDTO>();
									JSONArray jsonArray3=jsonObject3.getJSONArray("City");
									for (int k = 0; k < jsonArray3.length(); k++) {
										JSONObject jsonObject4=jsonArray3.getJSONObject(k);
										if (jsonObject4.has("CityId"))
											CityId = jsonObject4.getString("CityId");
										if (jsonObject4.has("CityName"))
											CityName = jsonObject4.getString("CityName");
										cityItemDTOs.add(new ItemDTO(CityId, CityName));
									}
								}
								regionItemDTOs.add(new ItemDTO(RegionId, RegionName, cityItemDTOs));	
							}
						}
						countryItemDTOs.add(new ItemDTO(CountryId, CountryName, regionItemDTOs));	
					}
				}
				countryDTOs.add(new CountryDTO(success, msg, countryItemDTOs));	
				
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countryDTOs;
	}
	
	
	/*method for getCategories*/
	public CategoryDTO getCategories(Context context, String BASE_URL,String methodName) {	
	// http://192.168.1.111/carousell-app/carousellServices/getCategory.php?method=getCategories			
		serviceUrl = BASE_URL+methodName;
		Log.i(TAG, "getCategories serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {									
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		CategoryDTO categoryDTO = parseCategory(json);
		if (categoryDTO.getSuccess().equals("1")) {
			AppSession appSession = new AppSession(context);
			appSession.setJson(AppSession.CATEGORY_JSON,json);
			appSession.setLastSeenTime(AppSession.CATEGORY_SEEN_TIME,RelovedPreference.getCurrentDateTime());	
		}
		return categoryDTO;
	}	
	
	// Using Gson
			public static CategoryDTO parseCategory(String json) {
				CategoryDTO categoryDTO = null;			
				try {
					JSONObject jsonObject = new JSONObject(json);
						Type type = new TypeToken<CategoryDTO>() {
						}.getType();			
						categoryDTO = new Gson().fromJson(jsonObject.toString(),
								type);																
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				return categoryDTO;
			}
	//Old way
	/*private List<CategoryDTO> parseCategory(String jsonResponse) {
		String success = "", msg = "",CategoryId="",CategoryName="",CategoryImage="";
		List<CategoryDTO> categoryDTOs=new ArrayList<CategoryDTO>();
		List<CategoryItemDTO> categoryItemDTOs=new ArrayList<CategoryItemDTO>();
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				if (jsonObject.has("CategoryList")){
					categoryItemDTOs=new ArrayList<CategoryItemDTO>();
					JSONArray jsonArray=jsonObject.getJSONArray("CategoryList");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2=jsonArray.getJSONObject(i);
						if (jsonObject2.has("CategoryId"))
							CategoryId = jsonObject2.getString("CategoryId");
						if (jsonObject2.has("CategoryName"))
							CategoryName = jsonObject2.getString("CategoryName");
						if (jsonObject2.has("CategoryImage")){
							//AppSession appSession=new AppSession(mContext);
							CategoryImage =jsonObject2.getString("CategoryImage");
						}
						categoryItemDTOs.add(new  CategoryItemDTO(CategoryId, CategoryName, CategoryImage));
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
	}*/
	
	/*method for getBaseUrl*/
	public String getBaseUrl(Context context, String methodName) {	
	// http://mobilitytesting.com/reloved-app/relovedServices/baseUrl.php?method=baseURL			
	//	serviceUrl = "http://mobilitytesting.com/quiou-app/relovedServicesv2/"+methodName;		
		serviceUrl = context.getResources().getString(R.string.APP_BASE_URL)+methodName;
		Log.i(TAG, "getBaseUrl serviceUrl-->" + serviceUrl);		
		MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		try {									
			json=makeConnection(serviceUrl, reqEntity);			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return parseContent(json);
	}	
	
	/*method for parsing response of base url*/
	private String parseContent(String jsonResponse) {
		String success = "", msg = ""; 
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");
				
				if (jsonObject.has("web_service_links")){
					JSONObject jsonObject2=jsonObject.getJSONObject("web_service_links");
					AppSession appSession=new AppSession(mContext);
					if (jsonObject2.has("webServicesDirectory")){						
						String BASE_URL = jsonObject2.getString("webServicesDirectory");
						appSession.setBaseUrl(BASE_URL);
					}if (jsonObject2.has("userImagePath")){
						String USERIMAGEPATH_URL = jsonObject2.getString("userImagePath");
						appSession.setUserImageBaseUrl(USERIMAGEPATH_URL);
					}if (jsonObject2.has("categoryImagePath")){
						String CATEGORYIMAGE_URL= jsonObject2.getString("categoryImagePath");
						appSession.setCategoryBaseUrl(CATEGORYIMAGE_URL);
					}if (jsonObject2.has("productImagePath")){
						String PRODUCTIMAGE_URL= jsonObject2.getString("productImagePath");
						appSession.setProductBaseUrl(PRODUCTIMAGE_URL);
					}if (jsonObject2.has("feedbackImagePath")){
						String feedbackImagePath= jsonObject2.getString("feedbackImagePath");
						appSession.setFeedbackImageBaseUrl(feedbackImagePath);
					}if (jsonObject2.has("messageImage")){
						String messageImage= jsonObject2.getString("messageImage");
						appSession.setMessageImageBaseUrl(messageImage);
					}if (jsonObject2.has("timeZone")){
						String timeZone= jsonObject2.getString("timeZone");
						appSession.setTimeZone(timeZone);
					}
				}
					
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/*method for add products*/
	public String[] addProducts(String BASE_URL,String methodName,String ProductCatId,
			String ProductPrice,String ProductName,String ProductDescription,String ProductLatitude,
			String ProductLongitude,String ProductUserId,String ProductUserName,String ProductUserImage,
			String ProductAddress,String ProductImage1,String ProductImage2,String ProductImage3,
			String ProductImage4,String ProductCatName) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/products.php?method=addProduct
		// &ProductCatId=2&ProductPrice=50&ProductName=demo&ProductDescription=product%20description
		// &ProductLatitude=2.73000&ProductLongitude=4.56666&ProductUserId=1&ProductUserName=userone
		// &ProductUserImage=a.jpeg&ProductAddress=Bhavar%20Kua	
		
		Log.i(TAG, "addProducts ProductCatId="+ProductCatId+"--ProductPrice="+ProductPrice
			+"---ProductName="+ProductName+"--ProductDescription="+ProductDescription+"--ProductLatitude="
				+ProductLatitude+"--ProductLongitude="+ProductLongitude+"--ProductUserId="+ProductUserId+
				"--ProductUserName="+ProductUserName+"--ProductAddress="+ProductAddress
				+"--ProductUserImage="+ProductUserImage+"--ProductImage1="+ProductImage1+"--ProductImage2="
				+ProductImage2+"--ProductImage3="+ProductImage3+"--ProductImage4="+ProductImage4+"--ProductCatName="+ProductCatName);		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "addProducts serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("ProductCatId",new StringBody( ProductCatId));
				reqEntity.addPart("ProductPrice",new StringBody( ProductPrice));
				reqEntity.addPart("ProductName",new StringBody( ProductName));
				reqEntity.addPart("ProductDescription",new StringBody( ProductDescription));
				reqEntity.addPart("ProductLatitude",new StringBody( ProductLatitude));
				reqEntity.addPart("ProductLongitude",new StringBody( ProductLongitude));
				reqEntity.addPart("ProductUserId",new StringBody( ProductUserId));
				reqEntity.addPart("ProductUserName",new StringBody( ProductUserName));
				reqEntity.addPart("ProductUserImage",new StringBody( ProductUserImage));
				reqEntity.addPart("ProductAddress",new StringBody( ProductAddress));		
				reqEntity.addPart("ProductCatName",new StringBody( ProductCatName));		
				 
				if(!ProductImage1.equals(""))
					reqEntity.addPart("ProductImage1",new FileBody(new File(ProductImage1)));	
				else
					reqEntity.addPart("ProductImage1",new StringBody( ProductImage1));	
				
				if(!ProductImage2.equals(""))
					reqEntity.addPart("ProductImage2",new FileBody(new File(ProductImage2)));	
				else
					reqEntity.addPart("ProductImage2",new StringBody( ProductImage2));	
				
				if(!ProductImage3.equals(""))
					reqEntity.addPart("ProductImage3",new FileBody(new File(ProductImage3)));	
				else
					reqEntity.addPart("ProductImage3",new StringBody( ProductImage3));	
				
				if(!ProductImage4.equals(""))
					reqEntity.addPart("ProductImage4",new FileBody(new File(ProductImage4)));	
				else
					reqEntity.addPart("ProductImage4",new StringBody( ProductImage4));	
				
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseAddProduct(json);
		}
	
	/*method for edit products*/
	public String[] editProducts(String BASE_URL,String methodName,String ProductCatId,
			String ProductPrice,String ProductName,String ProductDescription,String ProductLatitude,
			String ProductLongitude,String ProductUserId,String ProductId,
			String ProductAddress,String ProductImage1,String ProductImage2,String ProductImage3,
			String ProductImage4,String ProductCatName, String ProductDeleteImage) {	
		//http://mobilitytesting.com/reloved-app/relovedServices/products.php?method=editProduct
		//&ProductCatId=1&ProductPrice=50&ProductName=camera&ProductDescription=camera one&ProductLatitude=24.123
		// &ProductLongitude=41.230&ProductId=1&ProductUserId=1
		
		Log.i(TAG, "addProducts ProductCatId="+ProductCatId+"--ProductPrice="+ProductPrice
			+"---ProductName="+ProductName+"--ProductDescription="+ProductDescription+"--ProductLatitude="
				+ProductLatitude+"--ProductLongitude="+ProductLongitude+"--ProductUserId="+ProductUserId+
				"--ProductAddress="+ProductAddress+"---ProductId="+ProductId
				+"--ProductImage1="+ProductImage1+"--ProductImage2="
				+ProductImage2+"--ProductImage3="+ProductImage3+"--ProductImage4="+ProductImage4+"--ProductCatName="+ProductCatName);		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "addProducts serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("ProductCatId",new StringBody( ProductCatId));
				reqEntity.addPart("ProductPrice",new StringBody( ProductPrice));
				reqEntity.addPart("ProductName",new StringBody( ProductName));
				reqEntity.addPart("ProductDescription",new StringBody( ProductDescription));
				reqEntity.addPart("ProductLatitude",new StringBody( ProductLatitude));
				reqEntity.addPart("ProductLongitude",new StringBody( ProductLongitude));
				reqEntity.addPart("ProductUserId",new StringBody( ProductUserId));
				reqEntity.addPart("ProductId",new StringBody( ProductId));				
				reqEntity.addPart("ProductAddress",new StringBody( ProductAddress));	
				reqEntity.addPart("ProductCatName",new StringBody( ProductCatName));
				reqEntity.addPart("ProductDeleteImage",new StringBody( ProductDeleteImage));
				
				if(!ProductImage1.equals(""))
					reqEntity.addPart("ProductImage1",new FileBody(new File(ProductImage1)));	
				else
					reqEntity.addPart("ProductImage1",new StringBody( ProductImage1));	
				
				if(!ProductImage2.equals(""))
					reqEntity.addPart("ProductImage2",new FileBody(new File(ProductImage2)));	
				else
					reqEntity.addPart("ProductImage2",new StringBody( ProductImage2));	
				
				if(!ProductImage3.equals(""))
					reqEntity.addPart("ProductImage3",new FileBody(new File(ProductImage3)));	
				else
					reqEntity.addPart("ProductImage3",new StringBody( ProductImage3));	
				
				if(!ProductImage4.equals(""))
					reqEntity.addPart("ProductImage4",new FileBody(new File(ProductImage4)));	
				else
					reqEntity.addPart("ProductImage4",new StringBody( ProductImage4));	
				
				json = makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseAddProduct(json);
		}
	
	/*method for delete products*/
	public String[] deleteProducts(String BASE_URL,String methodName,String ProductId,String ProductName,
			String FromUserId,String FromUserName,String FromUserImage) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/products.php?method=deleteProduct
		// &ProductId=1&ProductName=productone&FromUserId=1&FromUserName=sachin&FromUserImage=a.jpeg
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "deleteProducts serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("ProductId",new StringBody(ProductId));				
				reqEntity.addPart("ProductName",new StringBody(ProductName));	
				reqEntity.addPart("FromUserId",new StringBody(FromUserId));	
				reqEntity.addPart("FromUserName",new StringBody(FromUserName));	
				reqEntity.addPart("FromUserImage",new StringBody(FromUserImage));	
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseAddProduct(json);
		}
	
	/*method for mark as sold products*/
	public String[] markAsSoldProducts(String BASE_URL,String methodName,String ProductId,String ProductName,
			String ProductImage,String FromUserId,String FromUserName,String FromUserImage) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/products.php?method=markAsSold
		// &ProductId=17&ProductName=dgfs&ProductImage=a.jpeg&FromUserId=54&FromUserName=bella
		// &FromUserImage=a.jpeg
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "deleteProducts serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("ProductId",new StringBody(ProductId));				
				reqEntity.addPart("ProductName",new StringBody(ProductName));	
				reqEntity.addPart("ProductImage",new StringBody(ProductImage));
				reqEntity.addPart("FromUserId",new StringBody(FromUserId));	
				reqEntity.addPart("FromUserName",new StringBody(FromUserName));	
				reqEntity.addPart("FromUserImage",new StringBody(FromUserImage));	
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseAddProduct(json);
		}
	
	/*method for notification settings*/
	public String[] notificationSettings(String BASE_URL,String methodName,String UserId,String parameterName,
			String parmeterValue) {	
		// http://mobilitytesting.com/reloved-app/relovedServices/settings.php?method=setting
		// &UserId=54&UserVibrateStatus=0
		
		
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "notificationSettings serviceUrl-->" + serviceUrl+"--parameterName="+parameterName+
					"--parmeterValue="+parmeterValue);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("UserId",new StringBody(UserId));				
				reqEntity.addPart(parameterName,new StringBody(parmeterValue));	
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parseAddProduct(json);
		}
	
	/*method for parsing add products*/
	private String[] parseAddProduct(String jsonResponse) {
		String success = "", msg = "", productImage="";
		String[] responseArr = new String[3];
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");	
				if (jsonObject.has("ProductImage"))
					productImage = jsonObject.getString("ProductImage");
				responseArr[0]=success;				
				responseArr[1]=msg;	
				responseArr[2]=productImage;
			} else {
				Log.i("object not a json object", "INVALID JSON FORMAT ");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseArr;
	}
	
	/*method for parsing get products*/
	public String[] getPromote(String BASE_URL,String methodName,String UserId) {	
//		http://mobilitytesting.com/reloved-app/relovedServices/getPromotion.php?method=getPromotion&UserId=24
			serviceUrl = BASE_URL+methodName;
			Log.i(TAG, "getCategories serviceUrl-->" + serviceUrl);		
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {									
				reqEntity.addPart("UserId",new StringBody( UserId));
				json=makeConnection(serviceUrl, reqEntity);			
			} catch (Exception e) {
				e.printStackTrace();
			}		
			return parsePromote(json);
		}
	
	/*method for parsing promote*/
	private String[] parsePromote(String jsonResponse) {
//		{"success":"1","msg":"Data Found"
//		,"Images":[{"ProImageName":"productImage11400592257.jpeg"},
//		          {"ProImageName":"productImage11400595864.jpeg"},
//		          {"ProImageName":"productImage11400596209.jpg"}]}
		String success = "", msg = "";
		String[] responseArr = null ;
		try {
			Object object = new JSONTokener(jsonResponse).nextValue();
			if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(jsonResponse);
				if (jsonObject.has("success"))
					success = jsonObject.getString("success");
				if (jsonObject.has("msg"))
					msg = jsonObject.getString("msg");				
						
				if(success.equals("1"))
				{
					if (jsonObject.has("Images"))
					{
						JSONArray jsonArray =jsonObject.getJSONArray("Images");
						if(jsonArray.length()>0)
						{
							responseArr = new String[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject1=jsonArray.getJSONObject(i);
								if (jsonObject1.has("ProImageName")){
									String ProImageName = jsonObject1.getString("ProImageName");
									responseArr[i]=ProImageName;
								}
							}
						}
					}
						
				}
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
