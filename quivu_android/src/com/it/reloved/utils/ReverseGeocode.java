/*
Apparently geocoder is not working with the Emulated android.  This is a workaround.

Here is how I implemented it:
 
try {
	possibleAddresses = g.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
} catch (IOException e) {
	if("sdk".equals( Build.PRODUCT )) {
		Log.d(TAG, "Geocoder doesn't work under emulation.");
		possibleAddresses = ReverseGeocode.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
	} else
		e.printStackTrace();
}

*/
package com.it.reloved.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class ReverseGeocode {

	public static final String ZERO_RESULT="Address Not Found.";

    	 public AddressDTO getFromLocation(double lat, double lon, int maxResults) {
    //	String urlStr = "http://maps.google.com/maps/geo?q=" + lat + "," + lon + "&output=json&sensor=false&key=AIzaSyC15AB-NrrvT1iqkAWJu6QWfo8wHTws33M";
    //	String urlStr = "http://maps.google.com/maps/api/geocode/json?latlng=23.0043673,72.5411868999996&sensor=false";
    	String urlStr = "http://maps.google.com/maps/api/geocode/json?latlng=" + lat + "," 
    			+ lon + "&output=json&sensor=false";
    	String response = "";	
		HttpClient client = new DefaultHttpClient();
		
		Log.d("ReverseGeocode", urlStr);
		try {
			HttpResponse hr = client.execute(new HttpGet(urlStr));
			HttpEntity entity = hr.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));

			String buff = null;
			while ((buff = br.readLine()) != null)
				response += buff;
			System.out.println("response:="+response);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return parseGoogleMapData(response);
		return parseGoogleMapAPIData(response);
		}

		
		public String parseGoogleMapData(String xmlLine) {
		
		//	List<Address> list = new ArrayList<Address>();
			String address =null;
			try {
				JSONObject jsonObject = new JSONObject(xmlLine);
				Log.i("Number of entries ", "Length="+jsonObject.length() + " "+jsonObject);
		
				for (int i = 0; i < jsonObject.length(); i++) {

					
					JSONArray data=jsonObject.names();;
					Log.i("data",""+data);	
					for (int j = 0; j < data.length(); j++) {
					String datainarray=data.getString(j);
					Log.i("datainarray",""+datainarray);	
					}
					
					String name = jsonObject.getString("name");
					Log.i("name",name);	
				
					String Status = jsonObject.getString("Status");
					Log.i("Status",Status);
					JSONObject jsonObjectstatus = new JSONObject(Status);
					String code = jsonObjectstatus.getString("code");
					Log.i("code",code);	
					
					String request = jsonObjectstatus.getString("request");
					Log.i("aaaaaa request",request);
				
					JSONArray mPlacemark=jsonObject.getJSONArray("Placemark");
				//	for (int i1 = 0; i1 <mPlacemark.length(); i1++) {
					JSONObject jObjPlacemark =mPlacemark.getJSONObject(0);
					
					
						String id = jObjPlacemark.getString("id");
						Log.i("id",id);	
						address= jObjPlacemark.getString("address");
						Log.i("address",address);	
					
					
						JSONObject JObj_AD =jObjPlacemark.getJSONObject("AddressDetails");
						String Accuracy = JObj_AD.getString("Accuracy");
						Log.i("Accuracy",Accuracy);
						try{
						JSONObject JObj_Country =JObj_AD.getJSONObject("Country");
						try{
						JSONObject JObj_AdmArea =JObj_Country.getJSONObject("AdministrativeArea");
						String AdministrativeAreaName = JObj_AdmArea.getString("AdministrativeAreaName");
						Log.i("AdministrativeAreaName",AdministrativeAreaName);
					
						JSONObject JObj_SubAdmArea =JObj_AdmArea.getJSONObject("SubAdministrativeArea");
						
						JSONObject JObj_Locality =JObj_SubAdmArea.getJSONObject("Locality");
						
						JSONObject JObj_DependentLocality =JObj_Locality.getJSONObject("DependentLocality");
						String DependentLocalityName = JObj_DependentLocality.getString("DependentLocalityName");
						Log.i("DependentLocalityName",DependentLocalityName);
						try{
						JSONObject JObj_PostalCode =JObj_DependentLocality.getJSONObject("PostalCode");
						String PostalCodeNumber = JObj_PostalCode.getString("PostalCodeNumber");
						Log.i("PostalCodeNumber",PostalCodeNumber);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						String LocalityName = JObj_Locality.getString("LocalityName");
						Log.i("LocalityName",LocalityName);
						String SubAdministrativeAreaName = JObj_SubAdmArea.getString("SubAdministrativeAreaName");
						Log.i("SubAdministrativeAreaName",SubAdministrativeAreaName);
						try{
						JSONObject JObj_Thoroughfare =JObj_DependentLocality.getJSONObject("Thoroughfare");
						String ThoroughfareName = JObj_Thoroughfare.getString("ThoroughfareName");
						Log.i("ThoroughfareName",ThoroughfareName);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						String CountryName = JObj_Country.getString("CountryName");
						Log.i("CountryName",CountryName);
						String CountryNameCode = JObj_Country.getString("CountryNameCode");
						Log.i("CountryNameCode",CountryNameCode);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						JSONObject JObj_ExtendedData =jObjPlacemark.getJSONObject("ExtendedData");
						JSONObject JObj_LatLonBox =JObj_ExtendedData.getJSONObject("LatLonBox");
						String north = JObj_LatLonBox.getString("north");
						Log.i("north",north);
						String south = JObj_LatLonBox.getString("south");
						Log.i("south",south);
						String east = JObj_LatLonBox.getString("east");
						Log.i("east",east);
						String west = JObj_LatLonBox.getString("west");
						Log.i("west",west);
						
						JSONObject JObj_Point =jObjPlacemark.getJSONObject("Point");
						JSONArray JObj_coordinates =JObj_Point.getJSONArray("coordinates");
						
						String coordinates_x = JObj_coordinates.getString(0);
						String coordinates_y = JObj_coordinates.getString(1);
						String coordinates_z = JObj_coordinates.getString(2);
						System.out.println("coordinates_x"+coordinates_x+","+"coordinates_y"+coordinates_y+","+"coordinates_z"+coordinates_z);
						
					}
				//}

			} catch (Exception e) {

				e.printStackTrace();
			}
		return address;

		}
		
		class TaskGetAddressFromLocation extends AsyncTask<Void, Void, Void>
		{

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return null;
			}}
	
		/*{

    "results": [
        {
            "address_components": [
                {
                    "long_name": "520",
                    "short_name": "520",
                    "types": [
                        "street_number"
                    ]
                },
                {
                    "long_name": "Carondelett Cove Southwest",
                    "short_name": "Carondelett Cove SW",
                    "types": [
                        "route"
                    ]
                },
                {
                    "long_name": "Atlanta",
                    "short_name": "Atlanta",
                    "types": [
                        "locality",
                        "political"
                    ]
                },
                {
                    "long_name": "Fulton",
                    "short_name": "Fulton",
                    "types": [
                        "administrative_area_level_2",
                        "political"
                    ]
                },
                {
                    "long_name": "Georgia",
                    "short_name": "GA",
                    "types": [
                        "administrative_area_level_1",
                        "political"
                    ]
                },
                {
                    "long_name": "United States",
                    "short_name": "US",
                    "types": [
                        "country",
                        "political"
                    ]
                },
                {
                    "long_name": "30331",
                    "short_name": "30331",
                    "types": [
                        "postal_code"
                    ]
                }
            ],
            "formatted_address": "520 Carondelett Cove Southwest, Atlanta, GA 30331, USA",*/
	// {   "results" : [],   "status" : "ZERO_RESULTS"}

//		public String[] parseGoogleMapAPIData(String xmlLine) {
			public AddressDTO parseGoogleMapAPIData(String xmlLine) {
			AddressDTO addressDTO =null;
			 String street_number="";
			 String route="";
			 List<String> sublocality= new  ArrayList<String>();
			 String locality="";
			 String administrative_area_level_2="";
			 String administrative_area_level_1="";
			 String postal_code="";
			 String country="";
			 String formatted_address="";
	//		List<Address> list = new ArrayList<Address>();
			 String[] arrayOfString=new String[]{"",""};
//			String address =null;
			try {
				Object object = new JSONTokener(xmlLine).nextValue();
				if (object instanceof JSONObject) {
				JSONObject jsonObject = new JSONObject(xmlLine);
				Log.i("Number of entries ", "Length="+jsonObject.length() + " "+jsonObject);
				
				if(jsonObject.has("results"))
				{
					JSONArray jArray=jsonObject.getJSONArray("results");
				
					if(jArray.length()>0)
					{
				
					JSONObject jsonObject1=jArray.getJSONObject(0);
					
					if(jsonObject1.has("formatted_address"))
					{
						formatted_address=		 arrayOfString[0] = jsonObject1.getString("formatted_address");
			             Log.v(getClass().getSimpleName(), "address=" + arrayOfString[0]);
					}
					/*{

					    "long_name": "Indore",
					    "short_name": "Indore",
					    "types": [
					        "locality",
					        "political"
					    ]

					},*/
					if (jsonObject1.has("address_components"))
		            {
						Log.v(getClass().getSimpleName(), "address_components");
		              JSONArray localJSONArray2 = jsonObject1.getJSONArray("address_components");
		              if (localJSONArray2.length() > 0)
		              { 
		            	  Log.v(getClass().getSimpleName(), "address_components length="+localJSONArray2.length());
		            	  for (int j = 0; j < localJSONArray2.length(); j++) {
		            			Log.v(getClass().getSimpleName(), "address_components="+localJSONArray2.getJSONObject(j));
		            	  JSONObject localJSONObject3 = localJSONArray2.getJSONObject(j);
		                  if (localJSONObject3.has("types"))
		                  {
		                	
		                  JSONArray localJSONArray3 = localJSONObject3.getJSONArray("types");
		                  Log.v(getClass().getSimpleName(), "types ="+localJSONArray3.get(0));
		               
		                  if ((localJSONArray3.length() == 1) && (localJSONArray3.get(0).equals("street_number")) && (localJSONObject3.has("long_name")))
		                  {
		                	  street_number = localJSONObject3.getString("long_name");
		                  Log.v(getClass().getSimpleName(), "street_number=" + street_number);
		                  }
//		              {"types":["route"],"short_name":"MP SH 27","long_name":"Indore-Icchapur Road"}

		                   if ((localJSONArray3.length() == 1) && (localJSONArray3.get(0).equals("route")) && (localJSONObject3.has("long_name")))
		                  {
		                	  route = localJSONObject3.getString("long_name");
		                      Log.v(getClass().getSimpleName(), "route=" +route);
		                  }
		                   if ((localJSONArray3.length() == 2) && (localJSONArray3.get(0).equals("sublocality")) && (localJSONObject3.has("long_name")))
			                  {
		                	   if(sublocality.size()==0)
		                	   {
		                		   sublocality.add(localJSONObject3.getString("long_name"));
		                		   Log.v(getClass().getSimpleName(), "sublocality=" +localJSONObject3.getString("long_name"));
		                	   }
		                	   else if(sublocality.size()>0)
		                	   {
		                		  if(!sublocality.contains(localJSONObject3.getString("long_name")))
		                		  {
		                			  sublocality.add(localJSONObject3.getString("long_name"));
		                			  Log.v(getClass().getSimpleName(), "sublocality=" +localJSONObject3.getString("long_name"));
		                		  }
		                	   }
			                	
			                	  
			                  }
		                   if ((localJSONArray3.length() == 2) && (localJSONArray3.get(0).equals("locality")) && (localJSONObject3.has("long_name")))
		                  {
		                	   locality = localJSONObject3.getString("long_name");
		                      Log.v(getClass().getSimpleName(), "locality=" + locality);
		                      arrayOfString[1] = locality;
		                  }
		                   if ((localJSONArray3.length() == 2) && (localJSONArray3.get(0).equals("administrative_area_level_2")) && (localJSONObject3.has("long_name")))
			                  {
		                	   administrative_area_level_2 = localJSONObject3.getString("long_name");
			                  Log.v(getClass().getSimpleName(), "administrative_area_level_2=" + administrative_area_level_2);
			                  }
		                   if ((localJSONArray3.length() == 2) && (localJSONArray3.get(0).equals("administrative_area_level_1")) && (localJSONObject3.has("long_name")))
			                  {
		                	   administrative_area_level_1 = localJSONObject3.getString("short_name");
			                  Log.v(getClass().getSimpleName(), "administrative_area_level_1=" + administrative_area_level_1);
			                  }
//		              {"types":["postal_code"],"short_name":"452001","long_name":"452001"}

		                   if ((localJSONArray3.length() == 1) && (localJSONArray3.get(0).equals("postal_code")) && (localJSONObject3.has("long_name")))
			                  {
		                	   postal_code = localJSONObject3.getString("long_name");
			                  Log.v(getClass().getSimpleName(), "postal_code=" + postal_code);
			                  }
		                   if ((localJSONArray3.length() == 2) && (localJSONArray3.get(0).equals("country")) && (localJSONObject3.has("long_name")))
			                  {
		                	   country = localJSONObject3.getString("long_name");
			                  Log.v(getClass().getSimpleName(), "country=" + country);
			                  }
		                  }
		                }
		            	  
		            	  addressDTO =new AddressDTO(street_number, route, sublocality, locality, administrative_area_level_2, administrative_area_level_1, postal_code, country, formatted_address);
		            	  Log.v(getClass().getSimpleName(), "getStreet_number=" + addressDTO.getStreet_number());
		            	  Log.v(getClass().getSimpleName(), "getRoute=" + addressDTO.getRoute());
		            	  Log.v(getClass().getSimpleName(), "getSublocality() size=" + addressDTO.getSublocality().size());
		            	  for (int i = 0; i < addressDTO.getSublocality().size(); i++) {
		            		  Log.v(getClass().getSimpleName(), "getSublocality ="+"["+i+"]" +addressDTO.getSublocality().get(i));
						}
		            	  Log.v(getClass().getSimpleName(), "getLocality=" + addressDTO.getLocality());
		            	  Log.v(getClass().getSimpleName(), "getAdministrative_area_level_2" + addressDTO.getAdministrative_area_level_2());
		            	  Log.v(getClass().getSimpleName(), "getAdministrative_area_level_1=" + addressDTO.getAdministrative_area_level_1());
		            	  Log.v(getClass().getSimpleName(), "getPostal_code=" + addressDTO.getPostal_code());
		            	  Log.v(getClass().getSimpleName(), "getCountry=" + addressDTO.getCountry());
		            	 
		              
		              
		              }
					}
					}
					else
					{
						if(jsonObject.has("results"))
						{
								String str = jsonObject.getString("status");
					            Log.v(getClass().getSimpleName(), "address=" + str);
					            arrayOfString[0] = ZERO_RESULT;
					            Log.v(getClass().getSimpleName(), "address=" + arrayOfString[0]);
						}
					}
						
				}
				}
				else {
					  Log.i("object not a json object", "INVALID JSON FORMAT ");
				      arrayOfString[0] = ZERO_RESULT;
				      Log.v(getClass().getSimpleName(), "address=" + arrayOfString[0]);
				      
				  }
			}catch (Exception e) {
				e.printStackTrace();
			}
			return addressDTO;
		}
		

}