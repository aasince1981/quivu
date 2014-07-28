package com.it.reloved;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.ProfileDAO;
import com.it.reloved.dao.UserDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;

public class EditProfile extends RelovedPreference{

	private ImageView ivBackHeader, ivRight,ivUser;
	private ImageLoader imageLoader;
	private EditText userNameEditText,firstNameEditText,lastNameEditText,cityEditText,
			websiteEditText,bioEditText,emailEditText;	
	private String userIDString="",userNameString="",firstNameString="",lastNameString="",cityString="",
				websiteString="",bioString="",emailString="";
	
	private LinearLayout resendEmailLayout;
	private TextView resendEmailText;
	public static final int PICK_IMAGE = 1;
	int PIC_CROP = 3;
	private Bitmap bmpLogo=null;
	private String imagepath = "";
	private List<UserDTO> userSessionDTOs = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editprofile);
		
		/* method for initialising init components */
		imageLoader=new ImageLoader(EditProfile.this);
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRight = (ImageView) findViewById(R.id.iv_right_header);
		ivRight.setOnClickListener(this);
		ivUser = (ImageView) findViewById(R.id.iv_user_editprofile);
		ivUser.setOnClickListener(this);
		
		userNameEditText=(EditText)findViewById(R.id.et_username_editprofile);
		firstNameEditText=(EditText)findViewById(R.id.et_firstname_editprofile);
		lastNameEditText=(EditText)findViewById(R.id.et_lastname_editprofile);
		cityEditText=(EditText)findViewById(R.id.et_mycity_editprofile);
		websiteEditText=(EditText)findViewById(R.id.et_website_editprofile);
		bioEditText=(EditText)findViewById(R.id.et_bio_editprofile);
		emailEditText=(EditText)findViewById(R.id.et_email_editprofile);
		
		resendEmailLayout=(LinearLayout)findViewById(R.id.lin_layout_resend_email);
		resendEmailText=(TextView)findViewById(R.id.tv_resend_email);
		resendEmailText.setOnClickListener(this);
		
		/*setting values in edittext*/
		AppSession appSession = new AppSession(EditProfile.this);
		userSessionDTOs = appSession.getConnections();
		if (userSessionDTOs != null) { 
			userIDString = userSessionDTOs.get(0).getUserId();
			userNameString = userSessionDTOs.get(0).getUserName();
			userNameEditText.setText("" + userSessionDTOs.get(0).getUserName());
			firstNameEditText.setText("" + userSessionDTOs.get(0).getUserFirstName());
			lastNameEditText.setText("" + userSessionDTOs.get(0).getUserLastName());
			cityEditText.setText("" + userSessionDTOs.get(0).getUserCityName());
			websiteEditText.setText("" + userSessionDTOs.get(0).getUserWebsiteUrl());
			bioEditText.setText("" + userSessionDTOs.get(0).getUserBio());
			emailEditText.setText("" + userSessionDTOs.get(0).getUserEmailAddress());			
			
			if (!userSessionDTOs.get(0).getUserImage().equals("")) {
				imageLoader.DisplayImage(appSession.getUserImageBaseUrl()+userSessionDTOs.get(0).getUserImage(), (Activity) EditProfile.this, ivUser,
						0, 0, 0, R.drawable.default_user_image);
			}else{
				ivUser.setImageResource(R.drawable.default_user_image);
			}		
			if ( getIntent() != null ) {
				if( getIntent().getStringExtra("email_verif_status").equals("1") ) {
					resendEmailLayout.setVisibility(View.GONE);
				}
				else resendEmailLayout.setVisibility(View.VISIBLE);
			}
		}
		Log.i(TAG, "onCreate: "+"userIDString: "+userIDString+" userNameString"+userNameString);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
		
	}
	
	/*perform click*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.iv_right_header:
			userNameString=userNameEditText.getText().toString();
			firstNameString=firstNameEditText.getText().toString();
			lastNameString=lastNameEditText.getText().toString();
			cityString=cityEditText.getText().toString();
			websiteString=websiteEditText.getText().toString();
			bioString=bioEditText.getText().toString();
			emailString=emailEditText.getText().toString();		
			
			if (isValidate(userNameString, emailString)) {
				if (isNetworkAvailable()) {
					new TaskEditProfile().execute();
				} else {
					Toast.makeText(EditProfile.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}			
			break;
		case R.id.iv_user_editprofile:
			Intent intent1 = new Intent();
			intent1.setType("image/*");
			intent1.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent1, ""),PICK_IMAGE);
			break;
			
		case R.id.tv_resend_email:
			//Toast.makeText(EditProfile.this, "ok", 1).show();
			emailString=emailEditText.getText().toString();
			if (isValidEmail(emailString)) {
				if (isNetworkAvailable()) {
					new TaskResendEmail().execute();
				} else {
					Toast.makeText(EditProfile.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}	
			break;
		}
	}	
	
	/*method for validation*/
	boolean isValidate(String strUsername,String strEmail) {
		if (strUsername == null || strUsername.equals("")) {
			Toast.makeText(EditProfile.this,"Please enter Username.",Toast.LENGTH_SHORT).show();
			return false;
		}else if (!checkName(strUsername)) {
			Toast.makeText(EditProfile.this,"Please enter valid Username.",Toast.LENGTH_SHORT).show();
			return false;
		}else if (strEmail == null || strEmail.equals("")) {
			Toast.makeText(EditProfile.this,"Please enter Email.",Toast.LENGTH_SHORT).show();
			return false;
		} else if (!checkEmail(strEmail)) {
			Toast.makeText(EditProfile.this, "Invalid Email address!",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true; 
	}
	
	/*method only for email validation*/
	boolean isValidEmail(String strEmail) {
		if (strEmail == null || strEmail.equals("")) {
			Toast.makeText(EditProfile.this,"Please enter Email.",Toast.LENGTH_SHORT).show();
			return false;
		} else if (!checkEmail(strEmail)) {
			Toast.makeText(EditProfile.this, "Invalid Email address!",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true; 
	}
	
	/*Task for edit Profile*/
	public class TaskEditProfile extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		List<UserDTO> userDTOs=new ArrayList<UserDTO>();
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(EditProfile.this);
				List<UserDTO> userList=appSession.getConnections();				
				userDTOs=new UserDAO(EditProfile.this).editUserProfile(appSession.getBaseUrl(),
						getResources().getString(R.string.method_editProfile),userNameString, firstNameString,
						lastNameString, userList.get(0).getUserDefaultCity(), userList.get(0).getUserWebsiteUrl(),
						bioString, emailString, userList.get(0).getUserMobileNumber(), 
						userList.get(0).getUserDateofBirth(), appSession.getUserId(), imagepath,
						userList.get(0).getUserGender());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(EditProfile.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			try {				
				if (userDTOs!=null) {
					if (userDTOs.get(0).getSuccess().equals("1")) {
						AppSession appSession=new AppSession(EditProfile.this);
						appSession.resetConnections();
						appSession.storeConnections(userDTOs);
						Toast.makeText(EditProfile.this,userDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(EditProfile.this,userDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(EditProfile.this,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// To Resend Verification Email
	private class TaskResendEmail extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr = new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(ForgotPassword.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(EditProfile.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(EditProfile.this);
				arr = new UserDAO(EditProfile.this).resendEmail(appSession.getBaseUrl(),
						getResources().getString(R.string.method_resendVerifEmail),
						userIDString, userNameString, emailString);						
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
				if (arr != null) {
					if (arr[0].equals("1")) {
						Toast.makeText(EditProfile.this,arr[1],Toast.LENGTH_LONG).show();
						// invisible view and update email in AppSession
						resendEmailLayout.setVisibility(View.GONE);
						if( userSessionDTOs != null ) {
						userSessionDTOs.get(0).setUserEmailAddress(emailString);
						AppSession appSession=new AppSession(EditProfile.this);
						appSession.resetConnections();
						appSession.storeConnections(userSessionDTOs);
						}						
					} else {
						Toast.makeText(EditProfile.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(EditProfile.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Bitmap bmp;
		if (requestCode == PIC_CROP) {
			try {
				Log.v(getClass().getSimpleName(), "PICK_IMAGE crop 111 try");
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				bmpLogo = extras.getParcelable("data");

				if (bmpLogo != null) {
					ivUser.setImageBitmap(bmpLogo);
					imagepath = RelovedPreference.getFilePath(bmpLogo,"Profile");
					Log.v("onActivityResult", "imagepath=" + imagepath);				
					
				} else {
					try {
						Log.v(getClass().getSimpleName(),
								"PICK_IMAGE crop 222 try");
						RelovedPreference.selectedImagePath = getAbsolutePath(data.getData());
						bmpLogo = RelovedPreference.decodeFile(new File(RelovedPreference.selectedImagePath),
								200,200);
						Log.i("bmpLogo", "" + bmpLogo);
						ivUser.setImageBitmap(bmpLogo);
						imagepath = RelovedPreference.selectedImagePath;
						Log.v("onActivityResult", "imagepath=" + imagepath);						
					} catch (Exception e) {
						e.printStackTrace();
						try {
							Log.v(getClass().getSimpleName(),
									"PICK_IMAGE crop 333 try");
							RelovedPreference.selectedImagePath = RelovedPreference.getImagePath();
							bmpLogo = RelovedPreference.decodeFile(new File(RelovedPreference.selectedImagePath),
									200,200);
							Log.i("bmpLogo", "" + bmpLogo);
							ivUser.setImageBitmap(bmpLogo);
							imagepath = RelovedPreference.selectedImagePath;
							Log.v("onActivityResult", "imagepath=" + imagepath);							
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}

			} catch (Exception e) {
				Log.v(getClass().getSimpleName(), " Exception catch" + e);
				// TODO: handle exception
			}
		} else if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == RelovedPreference.PICK_IMAGE) {
				Log.i("PICK_IMAGE", "PICK_IMAGE");
				try {
					Uri uriImage = data.getData();
					performCrop(uriImage);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} 

		}
	}
	
	/*method for cropping*/
	private void performCrop(Uri picUri) {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// cropIntent.setClassName("com.android.gallery",
			// "com.android.camera.CropImage");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("scale", true);
			// intent.putExtra("noFaceDetection", true);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);

			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	
}
