package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.it.reloved.adapter.FeedbackListAdapter;
import com.it.reloved.dao.FeedbackDAO;
import com.it.reloved.dto.FeedbackDTO;
import com.it.reloved.dto.FeedbackItemDTO;
import com.it.reloved.utils.AppSession;

public class Feedback extends RelovedPreference{

	ImageView ivBackHeader, ivRefresh,ivPlus;
	Button allButton,asSellerButton,asBuyerButton;
	ListView listView;	
	List<FeedbackDTO> feedbackDTOs=new ArrayList<FeedbackDTO>();
	String UserId="",UserName="",UserImage="";
	Intent intent=null;
	FeedbackListAdapter feedbackListAdapter=null;
	List<FeedbackItemDTO> feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
	public static int feedbackTemp=0;
	int poss=0;
	boolean flag=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		feedbackTemp=0;
		intent=getIntent();
		if (intent!=null) {
			UserId=intent.getStringExtra("UserId");
			UserName=intent.getStringExtra("UserName");
			UserImage=intent.getStringExtra("UserImage");
		}
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivPlus = (ImageView) findViewById(R.id.iv_plus_header);
		ivPlus.setOnClickListener(this);
		
		AppSession appSession=new AppSession(Feedback.this);
		if(UserId.equals(appSession.getUserId())){
			ivPlus.setVisibility(View.GONE);			
		}else{
			ivPlus.setVisibility(View.VISIBLE);
		}
	
		allButton=(Button)findViewById(R.id.btn_all_feedback);
		asSellerButton=(Button)findViewById(R.id.btn_as_seller_feedback);
		asBuyerButton=(Button)findViewById(R.id.btn_as_buyer_feedback);
		
		allButton.setOnClickListener(this);
		asSellerButton.setOnClickListener(this);
		asBuyerButton.setOnClickListener(this);
		
		allButton.setEnabled(false);
		asSellerButton.setEnabled(true);
		asBuyerButton.setEnabled(true);
		allButton.setTextColor(getResources().getColor(R.color.black));
		asSellerButton.setTextColor(getResources().getColor(R.color.app_green));
		asBuyerButton.setTextColor(getResources().getColor(R.color.app_green));
		
		listView=(ListView)findViewById(R.id.list_feedback);
		
		if (isNetworkAvailable()) {
			new TaskFeedback().execute(UserId);
		} else {
			Toast.makeText(Feedback.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
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
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(feedbackTemp==1){
			feedbackTemp=0;
		if (isNetworkAvailable()) {
			new TaskFeedback().execute(UserId);
		} else {
			Toast.makeText(Feedback.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
		}	
		}
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
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskFeedback().execute(UserId);
			} else {
				Toast.makeText(Feedback.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.iv_plus_header:
			if(feedbackItemDTOs!=null){
				for (int i = 0; i < feedbackItemDTOs.size(); i++) {
					String uId=feedbackItemDTOs.get(i).getFeedbackByUserId();
					if (uId.equals(new AppSession(Feedback.this).getUserId())) {
						poss=i;
						flag=true;
						break;
					}
				}
			}
			if(!flag){
				intent=new Intent(Feedback.this, AddFeedback.class);
				intent.putExtra("UserId", UserId);
				intent.putExtra("UserName",UserName);
				intent.putExtra("UserImage",UserImage);
				intent.putExtra("feedbackType","");
				intent.putExtra("feedbackExp","1");
				intent.putExtra("feedback","");
				intent.putExtra("FeedbackId", "");
			startActivity(intent);
			}else{
				Intent intent=new Intent(Feedback.this, AddFeedback.class);
				intent.putExtra("UserId", feedbackItemDTOs.get(poss).getFeedbackToUserId());
				intent.putExtra("UserName",feedbackItemDTOs.get(poss).getFeedbackToUserName());
				intent.putExtra("UserImage",feedbackItemDTOs.get(poss).getFeedbackToUserImage());
				intent.putExtra("FeedbackId", feedbackItemDTOs.get(poss).getFeedbackId());
				intent.putExtra("feedbackType",feedbackItemDTOs.get(poss).getFeedbackType());
				intent.putExtra("feedbackExp",feedbackItemDTOs.get(poss).getFeedbackExperience());
				intent.putExtra("feedback",feedbackItemDTOs.get(poss).getFeedbackDescription());
				startActivity(intent);
			}
			break;
		case R.id.btn_all_feedback:
			allButton.setEnabled(false);
			asSellerButton.setEnabled(true);
			asBuyerButton.setEnabled(true);
			allButton.setTextColor(getResources().getColor(R.color.black));
			asSellerButton.setTextColor(getResources().getColor(R.color.app_green));
			asBuyerButton.setTextColor(getResources().getColor(R.color.app_green));
						
			setList("3");
			feedbackListAdapter=new FeedbackListAdapter(Feedback.this, R.layout.listitem_feedback,
					feedbackItemDTOs,1);
			listView.setAdapter(feedbackListAdapter);
			feedbackListAdapter.notifyDataSetChanged();
			
			break;
		case R.id.btn_as_seller_feedback:
			allButton.setEnabled(true);
			asSellerButton.setEnabled(false);
			asBuyerButton.setEnabled(true);
			allButton.setTextColor(getResources().getColor(R.color.app_green));
			asSellerButton.setTextColor(getResources().getColor(R.color.black));
			asBuyerButton.setTextColor(getResources().getColor(R.color.app_green));
			
			setList("1");
			feedbackListAdapter=new FeedbackListAdapter(Feedback.this, R.layout.listitem_feedback,
					feedbackItemDTOs,1);
			listView.setAdapter(feedbackListAdapter);
			feedbackListAdapter.notifyDataSetChanged();
			
			break;
		case R.id.btn_as_buyer_feedback:
			allButton.setEnabled(true);
			asSellerButton.setEnabled(true);
			asBuyerButton.setEnabled(false);
			allButton.setTextColor(getResources().getColor(R.color.app_green));
			asSellerButton.setTextColor(getResources().getColor(R.color.app_green));
			asBuyerButton.setTextColor(getResources().getColor(R.color.black));
			
			setList("2");
			feedbackListAdapter=new FeedbackListAdapter(Feedback.this, R.layout.listitem_feedback,
					feedbackItemDTOs,1);
			listView.setAdapter(feedbackListAdapter);
			feedbackListAdapter.notifyDataSetChanged();
			
			break;
		default:
			break;
		}
	}
	
	/*Task for getting feedback*/
	public class TaskFeedback extends AsyncTask<String, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(Feedback.this);
				feedbackDTOs=new FeedbackDAO(Feedback.this).getFeedback(appSession.getBaseUrl(),
						getResources().getString(R.string.method_getFeedback), params[0]);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(Feedback.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			try {				
				if (feedbackDTOs!=null) {
					if (feedbackDTOs.get(0).getSuccess().equals("1")) {
						feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
						feedbackItemDTOs.addAll(feedbackDTOs.get(0).getFeedbackItemDTOs());
						feedbackListAdapter=new FeedbackListAdapter(Feedback.this, R.layout.listitem_feedback,
								feedbackItemDTOs,1);
						listView.setAdapter(feedbackListAdapter);
						
					} else {
						Toast.makeText(Feedback.this,feedbackDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
						feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
						feedbackItemDTOs.addAll(feedbackDTOs.get(0).getFeedbackItemDTOs());
						feedbackListAdapter=new FeedbackListAdapter(Feedback.this, R.layout.listitem_feedback,
								feedbackItemDTOs,1);
						listView.setAdapter(feedbackListAdapter);
					}					
				} else {
					Toast.makeText(Feedback.this,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*set items according to type*/
	public void setList(String type){
		if (feedbackDTOs!=null) {	
			feedbackItemDTOs=new ArrayList<FeedbackItemDTO>();
			if (type.equals("3")) {
				feedbackItemDTOs.addAll(feedbackDTOs.get(0).getFeedbackItemDTOs());
			} else {
				for (int i = 0; i < feedbackDTOs.get(0).getFeedbackItemDTOs().size(); i++) {
					if (feedbackDTOs.get(0).getFeedbackItemDTOs().get(i).getFeedbackType().equals(type)) {					
						feedbackItemDTOs.add(feedbackDTOs.get(0).getFeedbackItemDTOs().get(i));
					}
				}
			}
			
		}
	}
}
