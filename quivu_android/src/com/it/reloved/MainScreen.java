package com.it.reloved;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.it.reloved.dao.UserDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AddressDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.FBConnector;
import com.it.reloved.utils.GPSTracker;
import com.it.reloved.utils.ReverseGeocode;
import com.sromku.simple.fb.SimpleFacebook;

public class MainScreen extends RelovedPreference implements ConnectionCallbacks, OnConnectionFailedListener{

	Context context=this;
	private ImageView ivSignup, ivFacebook,ivGooglePlus;
	TextView tvLogin;
	
	protected static boolean flagGPLUS;
	private final static String TAG=Login.class.getSimpleName();
	private int mSignInProgress;
	private PendingIntent mSignInIntent;
	private int mSignInError;
	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;
	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;
//	private ListView mCirclesListView;
	private ArrayAdapter<String> mCirclesAdapter;
	private ArrayList<String> mCirclesList;
	private static final int RC_SIGN_IN = 0;
	private static final int DIALOG_PLAY_SERVICES_ERROR = 0;
	private static final String SAVED_PROGRESS = "sign_in_progress";
	private GoogleApiClient mGoogleApiClient;

	public static String strDefaultCity="";
	public static double latitiude=0.0,longitude=0.0;
	
	public static String UserGPlusId="",gUserName="",gEmail="",gPlusImagePath="",
			gcmId = "", deviceId = "",appVersion = "";
	TextView tvWelcome,tvDesc,tvDesc2,tvOr,tvAlready;
	
	@Override
	public void onResume() {
	    super.onResume();
	    FBConnector.mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		FBConnector.configureMyFB(MainScreen.this); 

		/* method for initialising init components */
		tvLogin = (TextView) findViewById(R.id.tv_login_main);
		tvLogin.setOnClickListener(this);
		tvLogin.setTypeface(typeface);
		ivSignup = (ImageView) findViewById(R.id.iv_signup_main);
		ivSignup.setOnClickListener(this);
		ivFacebook = (ImageView) findViewById(R.id.iv_fb_main);
		ivFacebook.setOnClickListener(this);
		ivGooglePlus = (ImageView) findViewById(R.id.iv_googleplus_main);
		ivGooglePlus.setOnClickListener(this);		
				
		tvWelcome = (TextView) findViewById(R.id.tv_welcome_main);
		tvWelcome.setTypeface(typeface);
		tvDesc = (TextView) findViewById(R.id.tv_desc_main);
		tvDesc.setTypeface(typeface);
		tvDesc2 = (TextView) findViewById(R.id.tv_desc2_main);
		tvDesc2.setTypeface(typeface);
		tvOr = (TextView) findViewById(R.id.tv_or_main);
		tvOr.setTypeface(typeface);
		tvAlready = (TextView) findViewById(R.id.tv_already_main);
		tvAlready.setTypeface(typeface);		
		
		/* initialise facebook object */
	//	this.fConnector = new FacebookConnector(getString(R.string.FACEBOOK_APPID), this,
	//			getApplicationContext(), PERMS);	

		if (isNetworkAvailable()) {
			gcmId = getGCMId();
			deviceId = getIMEIorDeviceId();
			appVersion = getAppVersion();
		}else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}
		
		//for getting latitude and longitude
		GPSTracker gpsTracker = new GPSTracker(MainScreen.this);
		if (gpsTracker.canGetLocation()) {
			latitiude = gpsTracker.getLatitude();
			longitude = gpsTracker.getLongitude();
			if (latitiude != 0.0 && longitude != 0.0) {
				if (isNetworkAvailable()) {
					new TaskCity().execute();
				} else {
					Toast.makeText(context, getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			gpsTracker.showSettingsAlert();
			Log.v(TAG, " Can not get Location = ");
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

	//perform click
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_login_main:
	        startActivity(new Intent(MainScreen.this,Login.class));
			break;
		case R.id.iv_signup_main: 
			startActivity(new Intent(MainScreen.this,  Registration.class));
			break;		
		case R.id.iv_fb_main:
			if (isNetworkAvailable()) {
			//	fConnector.login();
				FBConnector.mSimpleFacebook.login(FBConnector.onLoginListener);
				// FBConnector.postToFacebook(this);
			} else {
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			}			
			break;		
		case R.id.iv_googleplus_main:
			if (isNetworkAvailable()) {
			mGoogleApiClient = buildGoogleApiClient();
			resolveSignInError();
			} else {
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;				
      }
	
	}
	
	/*Task for getting city*/
	private class TaskCity extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			strDefaultCity=getGeoAddress(latitiude, longitude);
			return null;
		}	
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.i("TaskCity", "strDefaultCity="+strDefaultCity);
		}
	} 
		
	/** Google Plus Area_________________________________________________________________*/
	private GoogleApiClient buildGoogleApiClient() {
		// When we build the GoogleApiClient we specify where connected and
		// connection failed callbacks should be returned, which Google APIs our
		// app uses and which OAuth 2.0 scopes our app requests.
		return new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}


	/**
	 * Fetching user's information name, email, profile pic
	 * */
	@SuppressWarnings("unused")
	private void getProfileInformation() {
	
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				String Birthday = currentPerson.getBirthday();
				String id = currentPerson.getId();
				String Nickname = currentPerson.getNickname() ;
				String FamilyName = currentPerson.getName().getFamilyName();
				String MiddleName = currentPerson.getName().getMiddleName();
				String GivenName = currentPerson.getName().getGivenName();
				String Language = currentPerson.getLanguage();
				String CurrentLocation = currentPerson.getCurrentLocation();
				String Tagline = currentPerson.getTagline();
				int agw = currentPerson.getRelationshipStatus();
				String AboutMe = currentPerson.getAboutMe();
				personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.length() - 2)+ PROFILE_PIC_SIZE;

				UserGPlusId=id;
				gUserName=personName;
				gEmail=email;
				/*getGplusData(id, email, GivenName, FamilyName, personName, personPhotoUrl);
				Log.i(TAG, "Name: " + personName + ", plusProfile: "
						+ personGooglePlusProfile + ", email: " + email
						+ ", Image: " + personPhotoUrl);
				Log.i(TAG, "personName="+personName);
				Log.i(TAG, "email="+email);		*/		
				

  				GPSTracker gpsTracker = new GPSTracker(MainScreen.this);
  				if (gpsTracker.canGetLocation()) {
  				latitiude = gpsTracker.getLatitude();
  				longitude = gpsTracker.getLongitude();  				
  				if (latitiude!=0.0&&longitude!=0.0) {
					if (isNetworkAvailable()) {
						new TaskCity().execute();
					} else {
						Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}  	  					
  				} else {
  					gpsTracker.showSettingsAlert();
  					Log.v(TAG, " Can not get Location = ");
  				}
  			

				if (isNetworkAvailable()) {
					new TaskImage().execute(personPhotoUrl);
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
				
				

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Task for social login*/
	private class TaskForSocialLogin extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		List<UserDTO> userList = new ArrayList<UserDTO>();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(context, "",getResources().getString(R.string.logging_in));
				pd = ProgressDialog.show(MainScreen.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {				
				AppSession appSession=new AppSession(MainScreen.this);
				userList = new UserDAO(MainScreen.this).socialUserRegistration(appSession.getBaseUrl(),
						getResources().getString(R.string.method_registration),
						UserGPlusId, gUserName, gEmail, ""+latitiude, ""+longitude, 
						gcmId, appVersion, deviceId, "1", "3", 
						strDefaultCity, gPlusImagePath, "","","");						
						
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
						AppSession appSession=new AppSession(MainScreen.this);
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
	
	/*
	 * onConnected is called when our Activity successfully connects to Google
	 * Play services. onConnected indicates that an account was selected on the
	 * device, that the selected account has granted any requested permissions
	 * to our app and that we were able to establish a service connection to
	 * Google Play services.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		// Reaching onConnected means we consider the user signed in.
		Log.i("", "onConnected");
		flagGPLUS = true;
		// Retrieve some profile information to personalize our app for the
		// user.
		Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		getProfileInformation();

		// Indicate that the sign in process is complete.
		mSignInProgress = STATE_DEFAULT;
	}

	/*
	 * onConnectionFailed is called when our Activity could not connect to
	 * Google Play services. onConnectionFailed indicates that the user needs to
	 * select an account, grant permissions or resolve an error in order to sign
	 * in.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes
		// might
		// be returned in onConnectionFailed.
		Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
				+ result.getErrorCode());

		if (mSignInProgress != STATE_IN_PROGRESS) {			
			mSignInIntent = result.getResolution();
			mSignInError = result.getErrorCode();

			if (mSignInProgress == STATE_SIGN_IN) {				
				resolveSignInError();
			}
		}

	}


	private void resolveSignInError() {
		flagGPLUS =false;
		mGoogleApiClient.connect();
		final ProgressDialog progressDialog = ProgressDialog.show(MainScreen.this, null, null);
		progressDialog.setContentView(R.layout.progressloader);
		/*final ProgressDialog progressDialog =ProgressDialog.show(MainScreen.this, "",
				getString(R.string.signing_in));*/
		Handler handler =new Handler() ;
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				if (mSignInIntent != null) {
					// We have an intent which will allow our user to sign in or
					// resolve an error. For example if the user needs to
					// select an account to sign in with, or if they need to consent
					// to the permissions your app is requesting.

					try {
						// Send the pending intent that we stored on the most recent
						// OnConnectionFailed callback. This will allow the user to
						// resolve the error currently preventing our connection to
						// Google Play services.
						mSignInProgress = STATE_IN_PROGRESS;
						startIntentSenderForResult(mSignInIntent.getIntentSender(),
								RC_SIGN_IN, null, 0, 0, 0);
					} catch (SendIntentException e) {
						Log.i(TAG,
								"Sign in intent could not be sent: "
										+ e.getLocalizedMessage());
						// The intent was canceled before it was sent. Attempt to
						// connect to
						// get an updated ConnectionResult.
						mSignInProgress = STATE_SIGN_IN;
						mGoogleApiClient.connect();
					}
				} else {
					// Google Play services wasn't able to provide an intent for some
					// error types, so we show the default Google Play services error
					// dialog which may still start an intent on our behalf if the
					// user can resolve the issue.
					if(!flagGPLUS){
					showDialog(DIALOG_PLAY_SERVICES_ERROR);}
				}
			}
		}, 3000);
		
	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		switch (requestCode) {
		case RC_SIGN_IN:
			if (resultCode == RESULT_OK) {
				// If the error resolution was successful we should continue
				// processing errors.
				mSignInProgress = STATE_SIGN_IN;
			} else {
				// If the error resolution was not successful or the user
				// canceled,
				// we should stop processing errors.
				mSignInProgress = STATE_DEFAULT;
			}

			if (!mGoogleApiClient.isConnecting()) {
				// If Google Play services resolved the issue with a dialog then
				// onStart is not called so we need to re-attempt connection
				// here.
				mGoogleApiClient.connect();
			}
			break;
			default:
				// this is required for login(-) method that is define in FBConnector.
				FBConnector.mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data); 
			    super.onActivityResult(requestCode, resultCode, data);						
		}
	}
	


	/*method for signout*/
	private void onSignedOut() {
		mCirclesList.clear();
		mCirclesAdapter.notifyDataSetChanged();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PLAY_SERVICES_ERROR:
			if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
				return GooglePlayServicesUtil.getErrorDialog(mSignInError,
						this, RC_SIGN_IN,
						new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								Log.e(TAG,
										"Google Play services resolution cancelled");
								mSignInProgress = STATE_DEFAULT;
//								mStatus.setText(R.string.status_signed_out);
							}
						});
			} else {
				return new AlertDialog.Builder(this)
						.setMessage(R.string.play_services_error)
						.setPositiveButton(R.string.close,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Log.e(TAG,
												"Google Play services error could not be "
														+ "resolved: "
														+ mSignInError);
										mSignInProgress = STATE_DEFAULT;
//										mStatus.setText(R.string.status_signed_out);
									}
								}).create();
			}
		default:
			return super.onCreateDialog(id);
		}
	}

	/*String UserDeviceType="1";
	String UserGPlusId="", UserType="2", UserEmailAddress="";
	String UserAppVersion="", UserFirstName="", UserLastName="", UserName="", UserLatitude="";
	String UserLongitude="", UserProfileImage="",image_path="";*/
	
	
	
/*	private void getGplusData(final String id, final String email ,final String fname ,
			final String lname, final String  uname,String imageUrl)
	{
		try {
		  String address = "";		
		  Bitmap fbPic=null;
		 
		if(!id.equals("")){
  			try {
				//new TaskImage().execute(imageUrl);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
  			
  		}
  		
  		runOnUiThread(new Runnable() {
  			public void run() { 			
  				//send to server.......  			
  				try{  				  				
  				
  				GPSTracker gpsTracker = new GPSTracker(Login.this);
  				if (gpsTracker.canGetLocation()) {
  				latitiude = gpsTracker.getLatitude();
  				longitude = gpsTracker.getLongitude();
  				
  				
  				if (latitiude!=0.0&&longitude!=0.0) {
					if (isNetworkAvailable()) {
						new TaskCity().execute();
					} else {
						Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}  					
  					
  				} else {
  					gpsTracker.showSettingsAlert();
  					Log.v(TAG, " Can not get Location = ");
  				}
  			
  				Log.v(TAG, " Notification_Id= "+RelovedPreference.Notification_Id);  				
  				UserDeviceType = "1";
  				UserGPlusId = id;
  				UserType= "2";
  				UserEmailAddress = email;
  				UserAppVersion = getAppVersion();
  				UserFirstName = fname;
  				UserLastName = lname;
  				UserName = uname;
  				UserLatitude =  ""+latitiude;
  				UserLongitude = ""+longitude;
  				UserProfileImage = image_path;
  				Log.i(TAG, " UserProfileImage= "+UserProfileImage);
  				Handler handler=new Handler();
  				handler.post(new Runnable() {
					
					@Override
					public void run() {						
								
					}
				});
  				} catch (Exception e) {
  					e.printStackTrace();
  				}
  			}
  		});
 
  	} catch (Exception e) {
  		Log.w("EX", "JSON Error in response");
  	} catch (Error e) {
  		Log.w("EX", "Facebook Error: " + e.getMessage());
  	}
 
  }*/
  
	/*Task for image from Gplus*/
	public class TaskImage extends AsyncTask<String, Void, Void>{
		Bitmap bmp=null;
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String imageURL = params[0];
			Drawable myDrawable = RelovedPreference.downloadImage(imageURL);
			bmp= ((BitmapDrawable) myDrawable).getBitmap();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(bmp!=null){
				gPlusImagePath=getFilePath(bmp,"gPlusProfileImage");
				Log.i("999999999999999999999999", "gPlusImagePath="+gPlusImagePath);				
				if (isNetworkAvailable()) {
					new TaskForSocialLogin().execute();
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
			}
			
			
		}
		
	}
	  
	  public  Bitmap getGooglePlusPicture(String url) {
			String urldisplay = url;
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

	  /*get city from lat long*/
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
						Toast.makeText(context, getString(R.string.NETWORK_ERROR), Toast.LENGTH_SHORT).show();					
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
