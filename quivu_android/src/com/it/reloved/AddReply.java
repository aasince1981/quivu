package com.it.reloved;

import java.util.List;

import com.it.reloved.dao.FeedbackDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddReply extends RelovedPreference{

	ImageView ivBackHeader, ivRight;
	TextView tvCharCount;
	EditText replyEditText;
	Intent intent=null;
	String replyString="";
	String UserId="",UserName="",UserImage="",FeedbackId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_reply);
		
		intent=getIntent();
		if (intent!=null) {
			UserId=intent.getStringExtra("UserId");
			UserName=intent.getStringExtra("UserName");
			UserImage=intent.getStringExtra("UserImage");			
			replyString=intent.getStringExtra("replyMsg");
			FeedbackId=intent.getStringExtra("FeedbackId");
		}
		
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRight = (ImageView) findViewById(R.id.iv_right_header);
		ivRight.setOnClickListener(this);
	
		tvCharCount=(TextView)findViewById(R.id.tv_char_count_addreply);
		replyEditText=(EditText)findViewById(R.id.et_reply_addreply);
		replyEditText.setText(replyString);
		replyEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(500)});
		replyEditText.addTextChangedListener(new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if(replyEditText.getText().toString().length()<=500)
					tvCharCount.setText(""+(500-replyEditText.getText().toString().length()));
			}			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
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
			replyString=replyEditText.getText().toString();
			if (replyString.equals("")) {
				Toast.makeText(AddReply.this, "Please enter reply.", Toast.LENGTH_LONG).show();
			}else{
				if (FeedbackId.equals(null)||FeedbackId.equals("")) {
					if (isNetworkAvailable()) {
						new TaskAddReply().execute();
					} else {
						Toast.makeText(AddReply.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				} else {
					if (isNetworkAvailable()) {
						new TaskEditReply().execute();
					} else {
						Toast.makeText(AddReply.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}			
			}	
			break;
		}
	}
	
	/*Task for add reply*/
	private class TaskAddReply extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(AddReply.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(AddReply.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FeedbackDAO(AddReply.this).addReply(appSession.getBaseUrl(),
					getResources().getString(R.string.method_addFeedback),appSession.getUserId(),
					userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
					userDTOs.get(0).getUserName(),UserId, replyString);			
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
						Toast.makeText(AddReply.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(AddReply.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AddReply.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	/*Task for edit reply*/
	private class TaskEditReply extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(AddReply.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(AddReply.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FeedbackDAO(AddReply.this).editFeedback(appSession.getBaseUrl(),
						getResources().getString(R.string.method_editFeedback), FeedbackId,
						"1", replyString,"","");	
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
						Toast.makeText(AddReply.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(AddReply.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AddReply.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
