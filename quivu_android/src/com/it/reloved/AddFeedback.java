package com.it.reloved;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.FeedbackDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;

public class AddFeedback extends RelovedPreference{

	ImageView ivBackHeader, ivRight;
	ImageView ivUser;
	TextView tvUsername,tvCharCount;
	Button btnSeller,btnBuyer,btnPositive,btnNegative,btnNeutral;
	EditText experienceEditText;
	String expString="",FeedbackType="",FeedbackExperience="";
	ImageLoader imageLoader;
	String UserId="",UserName="",UserImage="",FeedbackId="";
	Intent intent=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_feedback);
		
		intent=getIntent();
		if (intent!=null) {
			UserId=intent.getStringExtra("UserId");
			UserName=intent.getStringExtra("UserName");
			UserImage=intent.getStringExtra("UserImage");			
			FeedbackType=intent.getStringExtra("feedbackType");
			FeedbackExperience=intent.getStringExtra("feedbackExp");
			expString=intent.getStringExtra("feedback");
			FeedbackId=intent.getStringExtra("FeedbackId");
		}
		
		/* method for initialising init components */
		imageLoader=new ImageLoader(AddFeedback.this);
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRight = (ImageView) findViewById(R.id.iv_right_header);
		ivRight.setOnClickListener(this);
		
		ivUser = (ImageView) findViewById(R.id.iv_user_addfeedback);
		tvUsername=(TextView)findViewById(R.id.tv_username_addfeedback);
		
		btnSeller=(Button)findViewById(R.id.btn_seller_addfeedback);
		btnBuyer=(Button)findViewById(R.id.btn_buyer_addfeedback);
		btnPositive=(Button)findViewById(R.id.btn_positive_addfeedback);
		btnNeutral=(Button)findViewById(R.id.btn_neutral_addfeedback);
		btnNegative=(Button)findViewById(R.id.btn_negetive_addfeedback);
		
		btnSeller.setOnClickListener(this);
		btnBuyer.setOnClickListener(this);
		btnPositive.setOnClickListener(this);
		btnNeutral.setOnClickListener(this);
		btnNegative.setOnClickListener(this);
		
		tvCharCount=(TextView)findViewById(R.id.tv_char_count_addfeedback);
		experienceEditText=(EditText)findViewById(R.id.et_exp_addfeedback);
		experienceEditText.setText(expString);
		
		experienceEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});
		experienceEditText.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if(experienceEditText.getText().toString().length()<=500)
					tvCharCount.setText(""+(500-experienceEditText.getText().toString().length()));
			}			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		/*condition for setting feedback according to value*/ 
		tvUsername.setText(UserName+" was a");
		if (!UserImage.equals(null)&&!UserImage.equals("")) {
			imageLoader.DisplayImage(new AppSession(AddFeedback.this).getUserImageBaseUrl()+UserImage,
					(Activity) AddFeedback.this, ivUser,0, 0, 0, R.drawable.default_user_image);
		}else{
			ivUser.setImageResource(R.drawable.default_user_image);
		}
		
		if(FeedbackType.equals("1")){
			btnSeller.setEnabled(false);
			btnBuyer.setEnabled(true);
			btnSeller.setTextColor(getResources().getColor(R.color.black));
			btnBuyer.setTextColor(getResources().getColor(R.color.gray));
		}else if(FeedbackType.equals("2")){
			btnSeller.setEnabled(true);
			btnBuyer.setEnabled(false);
			btnSeller.setTextColor(getResources().getColor(R.color.gray));
			btnBuyer.setTextColor(getResources().getColor(R.color.black));
		}
		
		
		if (FeedbackExperience.equals("1")) {
			btnPositive.setEnabled(false);
			btnNeutral.setEnabled(true);
			btnNegative.setEnabled(true);
			btnPositive.setTextColor(getResources().getColor(R.color.app_green));
			btnNeutral.setTextColor(getResources().getColor(R.color.black));
			btnNegative.setTextColor(getResources().getColor(R.color.black));	
		} else if (FeedbackExperience.equals("2")){
			btnPositive.setEnabled(true);
			btnNeutral.setEnabled(false);
			btnNegative.setEnabled(true);
			btnPositive.setTextColor(getResources().getColor(R.color.black));
			btnNeutral.setTextColor(getResources().getColor(R.color.app_green));
			btnNegative.setTextColor(getResources().getColor(R.color.black));
		}else if (FeedbackExperience.equals("3")){
			btnPositive.setEnabled(true);
			btnNeutral.setEnabled(true);
			btnNegative.setEnabled(false);
			btnPositive.setTextColor(getResources().getColor(R.color.black));
			btnNeutral.setTextColor(getResources().getColor(R.color.black));
			btnNegative.setTextColor(getResources().getColor(R.color.app_green));	
		}
		
		/*btnPositive.setEnabled(false);
		btnNeutral.setEnabled(true);
		btnNegative.setEnabled(true);
		btnPositive.setTextColor(getResources().getColor(R.color.app_green));
		btnNeutral.setTextColor(getResources().getColor(R.color.black));
		btnNegative.setTextColor(getResources().getColor(R.color.black));	*/		
		
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
			expString=experienceEditText.getText().toString();
			if (FeedbackType.equals("")) {
				Toast.makeText(AddFeedback.this, "Please select for Seller or Buyer.", Toast.LENGTH_LONG).show();
			}else if (expString.equals("")) {
				Toast.makeText(AddFeedback.this, "Please enter description of experience.", Toast.LENGTH_LONG).show();
			}else{
				if (FeedbackId.equals(null)||FeedbackId.equals("")) {
					if (isNetworkAvailable()) {
						new TaskAddFeedback().execute();
					} else {
						Toast.makeText(AddFeedback.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				} else {
					if (isNetworkAvailable()) {
						new TaskEditFeedback().execute();
					} else {
						Toast.makeText(AddFeedback.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}
				
			}
		
			break;
		case R.id.btn_seller_addfeedback:
			FeedbackType="1";
			btnSeller.setEnabled(false);
			btnBuyer.setEnabled(true);
			btnSeller.setTextColor(getResources().getColor(R.color.black));
			btnBuyer.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.btn_buyer_addfeedback:	
			FeedbackType="2";
			btnSeller.setEnabled(true);
			btnBuyer.setEnabled(false);
			btnSeller.setTextColor(getResources().getColor(R.color.gray));
			btnBuyer.setTextColor(getResources().getColor(R.color.black));
			
			break;
		case R.id.btn_positive_addfeedback:	
			FeedbackExperience="1";
			btnPositive.setEnabled(false);
			btnNeutral.setEnabled(true);
			btnNegative.setEnabled(true);
			btnPositive.setTextColor(getResources().getColor(R.color.app_green));
			btnNeutral.setTextColor(getResources().getColor(R.color.black));
			btnNegative.setTextColor(getResources().getColor(R.color.black));			
			
			break;
		case R.id.btn_neutral_addfeedback:	
			FeedbackExperience="2";
			btnPositive.setEnabled(true);
			btnNeutral.setEnabled(false);
			btnNegative.setEnabled(true);
			btnPositive.setTextColor(getResources().getColor(R.color.black));
			btnNeutral.setTextColor(getResources().getColor(R.color.app_green));
			btnNegative.setTextColor(getResources().getColor(R.color.black));
			
			break;
		case R.id.btn_negetive_addfeedback:		
			FeedbackExperience="3";
			btnPositive.setEnabled(true);
			btnNeutral.setEnabled(true);
			btnNegative.setEnabled(false);
			btnPositive.setTextColor(getResources().getColor(R.color.black));
			btnNeutral.setTextColor(getResources().getColor(R.color.black));
			btnNegative.setTextColor(getResources().getColor(R.color.app_green));			
			break;
		default:
			break;
		}
	}
	
	/*Task for adding feedback*/
	private class TaskAddFeedback extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(ChangePassword.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(AddFeedback.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(AddFeedback.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FeedbackDAO(AddFeedback.this).addFeedback(appSession.getBaseUrl(),
						getResources().getString(R.string.method_addFeedback), appSession.getUserId(),
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						userDTOs.get(0).getUserName(), UserId, expString,
						FeedbackType,FeedbackExperience,UserImage.replace(appSession.getUserImageBaseUrl(),""),
						UserName);						
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
						Feedback.feedbackTemp=1;
						Toast.makeText(AddFeedback.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(AddFeedback.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AddFeedback.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	/*Task for edit feedback*/
	private class TaskEditFeedback extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(AddFeedback.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(AddFeedback.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FeedbackDAO(AddFeedback.this).editFeedback(appSession.getBaseUrl(),
						getResources().getString(R.string.method_editFeedback), FeedbackId,
						"0", expString,FeedbackType,FeedbackExperience);	
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
						Feedback.feedbackTemp=1;
						Toast.makeText(AddFeedback.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(AddFeedback.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AddFeedback.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
