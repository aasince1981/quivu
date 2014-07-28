package com.it.reloved;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.UserDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AddressDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.GPSTracker;
import com.it.reloved.utils.ReverseGeocode;

public class Registration extends RelovedPreference {

	private Context context = this;
	private EditText etUsername,etEmail,etPassword;
	private Button btnCreateAcc;
	private CheckBox checkBox;
	private String strUsername = "", strEmail="", strPassword = "",gcmId = "", deviceId = "",
			appVersion = "",strDefaultCity="";
	private GPSTracker gps;
	double latitude = 0.0, longitude = 0.0;
	private TextView tvShowPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration);
		
		initLayout();
		if (isNetworkAvailable()) {
			gcmId = getGCMId();
			deviceId = getIMEIorDeviceId();
			appVersion = getAppVersion();
		}else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}
		
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}
	
	/* method for initialising init components */
	private void initLayout() {		
		btnCreateAcc = (Button) findViewById(R.id.btn_create_acc_reg);
		btnCreateAcc.setOnClickListener(this);
		btnCreateAcc.setTypeface(typeface);
		etUsername = (EditText) findViewById(R.id.et_username_reg);
		etEmail= (EditText) findViewById(R.id.et_email_reg);
		etPassword = (EditText) findViewById(R.id.et_password_reg);	
		checkBox=(CheckBox)findViewById(R.id.cb_show_reg);
		checkBox.setChecked(false);
		tvShowPass=(TextView)findViewById(R.id.tv_show_pass_reg);
		tvShowPass.setTypeface(typeface);
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				//Log.i("onCheckedChanged", "isChecked="+isChecked);
				if (isChecked) {
					etPassword.setTransformationMethod(null);				
				} else {
					etPassword.setTransformationMethod(new PasswordTransformationMethod());
				}
			}
		});
		
		//get lat long
		gps = new GPSTracker(Registration.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			Log.i("Your Location is -:", "\nLat:" + latitude + "\nLong: "+ longitude);
		}
		
		if (latitude!=0.0&&longitude!=0.0) {
			if (isNetworkAvailable()) {
				new TaskCity().execute();
			} else {
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
		} 
	}
	
	//perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_create_acc_reg:
			gps = new GPSTracker(Registration.this);
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				Log.i("Your Location is -:", "\nLat:" + latitude + "\nLong: "+ longitude);
			}			
			if (isAndroidEmulator()) {
				gcmId = "APA91bFiUjakQmWgRTXVdvK2UDUEPJlm1XGv-RpWtlxmKlpt76-vHZj-T3-pYB3moKLZOD50A3o4GHGPJQJPFZCcmtDqNO7kB5Nxhzcl1DHJfVUmPIaqW9ziYVG3gI_hNenRezHrq3T2ESHtqA_qxn-ISB_Q6NeXTdNpcB1Z2Q3kF6JkpD_hBqM";
			} else {
				strUsername = etUsername.getText().toString();
				strEmail = etEmail.getText().toString();
				strPassword = etPassword.getText().toString();
				if (isValidate()) {
					if (isNetworkAvailable()) {
						new TaskForRegistration().execute();
					} else {
						Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}
			}
			break;

		default:
			break;
		}
	
	}
	
	//Task for getting city
	private class TaskCity extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			strDefaultCity=getGeoAddress(latitude, longitude);
			return null;
		}	
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("TaskCity", "strDefaultCity="+strDefaultCity);
		}
	} 
	
	//Task for user registration
	private class TaskForRegistration extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		List<UserDTO> userList = new ArrayList<UserDTO>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(context, "",getResources().getString(R.string.signing_up));
				pd = ProgressDialog.show(Registration.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {			
				AppSession appSession=new AppSession(Registration.this);
				userList = new UserDAO(Registration.this).normalUserRegistration(appSession.getBaseUrl(),
						getResources().getString(R.string.method_registration),
						strUsername, strPassword, strEmail, "", ""+latitude, ""+longitude, 
						gcmId, appVersion, deviceId, "1", "1", strDefaultCity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);			
			try {
				pd.dismiss();
				if (userList != null) {
					if (userList.get(0).getSuccess().equals("1")) {
						Toast.makeText(context,userList.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
						AppSession appSession=new AppSession(Registration.this);
						appSession.storeConnections(userList);
						appSession.storeUserId(userList.get(0).getUserId());						
						Intent loginIntent = new Intent(context, TabSample.class);
						loginIntent.putExtra("fromClass", "");
						startActivity(loginIntent);						
					} else {
						Toast.makeText(context,userList.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//method for validation
	boolean isValidate() {
		if (strEmail == null || strEmail.equals("")) {
			Toast.makeText(context,"Please enter Email.",Toast.LENGTH_SHORT).show();
			return false;
		} else if (!checkEmail(strEmail)) {
			Toast.makeText(context, "Invalid Email address!",
					Toast.LENGTH_SHORT).show();
			return false;
		}else if (strUsername == null || strUsername.equals("")) {
			Toast.makeText(context,"Please enter Username.",Toast.LENGTH_SHORT).show();
			return false;
		}else if (!checkName(strUsername)) {
			Toast.makeText(context,"Please enter valid Username.",Toast.LENGTH_SHORT).show();
			return false;
		} else if (strPassword == null || strPassword.equals("")) {
			Toast.makeText(context,"Please enter Password",Toast.LENGTH_SHORT).show();
			return false;
		} else if (gcmId == null || gcmId.equals("")) {
			Toast.makeText(context,"We are not getting Device Notification Id. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				gcmId = getGCMId();
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			return false;
		} else if (deviceId == null || deviceId.equals("")) {
			Toast.makeText(context,"We are not getting Device Unique Id. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				deviceId = getIMEIorDeviceId();
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			return false;
		} else if (appVersion == null || appVersion.equals("")) {
			Toast.makeText(context,"We are not getting Current Application Version. Please try again.",
					Toast.LENGTH_SHORT).show();
			if (isNetworkAvailable())
				appVersion = getAppVersion();
			else 
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			return false;
		}else if (latitude == 0.0 || longitude == 0.0) {
			Toast.makeText(context, "Cannot get current latitude and longitude. Please enable GPS.",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	//get address from lat long
	public String getGeoAddress(double Latitude, double Longitude) {
		/*----------to get City-Name from coordinates ------------- */
		Log.v("PicPointv2", "Latitude=" + Latitude);
		Log.v("PicPointv2", "Longitude=" + Longitude);
		String cityName = "";
		String addrs = "";
		String mZIPCode = "";
		String ImgURL = "";
		String address = "";
		String street = "";
		String statte = "";
		String country = "";
		
		Geocoder gcd = new Geocoder(context, Locale.getDefault());
		List<Address> addresses;
		AddressDTO response=null;
		try {			
			addresses = gcd.getFromLocation(Latitude, Longitude, 1);
			if (addresses.size() > 0) {				
				System.out.println(addresses.get(0).getLocality());
				cityName = addresses.get(0).getLocality();
				addrs = addresses.get(0).getAddressLine(0);
				mZIPCode = addresses.get(0).getPostalCode();
				ImgURL = addresses.get(0).getUrl();
				street = addresses.get(0).getSubLocality();
				statte = addresses.get(0).getAdminArea();
				country = addresses.get(0).getCountryName();
				System.out.println("cityName" + cityName);
				System.out.println("addrs" + addrs);
				System.out.println("mZIPCode" + mZIPCode);
				System.out.println("ImgURL" + ImgURL);
				StringBuilder sb = new StringBuilder();
				if (street != null && !street.equals("null")) {
					sb.append(street).append(",");
				}
				if (cityName != null && !cityName.equals("null")) {
					sb.append(cityName).append(",");
				}
				if (statte != null && !statte.equals("null")) {
					sb.append(statte).append(",");
				}
				if (country != null && !country.equals("null")) {
					sb.append(country).append(",");
				}
				address = sb.toString();
				Log.v(getClass().getSimpleName(), "address="+address);
			} else {
				System.out.println("Empty addresses");				
				if (isNetworkAvailable()) {
					if (Latitude != 0.0 && Longitude != 0.0){
						response= new ReverseGeocode().getFromLocation(Latitude, Longitude,1);
						cityName=response.getLocality();
					}
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Empty addresses");			
			if (isNetworkAvailable()) {
				try {
					if (Latitude != 0.0 && Longitude != 0.0){
						response= new ReverseGeocode().getFromLocation(Latitude, Longitude,1);
						cityName=response.getLocality();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} catch (Error e1) {
					e1.printStackTrace();
				}
			} else {
				Toast.makeText(context, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();				
			}
		} catch (Error e) {
			e.printStackTrace();
			System.out.println("Empty addresses");
			if (isNetworkAvailable()) {
				try {
					if (Latitude != 0.0 && Longitude != 0.0){
						response= new ReverseGeocode().getFromLocation(Latitude, Longitude,1);
						cityName=response.getLocality();
					}
				} catch (Error e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else {
				Toast.makeText(context, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();
			}
		}
		String s = Latitude + "\n" + Longitude + "\n\nMy Currrent City is: "
				+ cityName + " Address " + addrs + " Postal Code" + mZIPCode;
		Log.v(getClass().getSimpleName(), "address details="+s+"\n"+"Address="+address);
		return cityName;
	}
}
