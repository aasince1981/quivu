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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.FollowersListAdapter;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

public class Likes extends RelovedPreference {

	ImageView ivBackHeader, ivRefresh;
	TextView tvHeader;
	ListView followersListView;
	FollowersListAdapter followersListAdapter=null;
	List<CategoryDTO> followersList=new ArrayList<CategoryDTO>();
	Intent intent=null;
	String ProductId="",UserId="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followers);
		
		intent=getIntent();
		if (intent!=null) {
			ProductId=intent.getStringExtra("ProductId");
			UserId=intent.getStringExtra("UserId");
		}
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		tvHeader=(TextView)findViewById(R.id.tv_header);
		tvHeader.setText("Likes");	
		
		followersListView=(ListView)findViewById(R.id.list_followers);
		
		if (isNetworkAvailable()) {
			new TaskFollowers().execute();
		} else {
			Toast.makeText(Likes.this,getString(R.string.NETWORK_ERROR),
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
				new TaskFollowers().execute();
			} else {
				Toast.makeText(Likes.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}	
			break;
		default:
			break;
		}
	}
	
	/*Task for getiing Likes*/
	public class TaskFollowers extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				followersList=new ArrayList<CategoryDTO>();
				AppSession appSession=new AppSession(Likes.this);
				followersList=new OfferDAO(Likes.this).getLikes(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getLikesUsers), ProductId, UserId);								
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(Likes.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			try {				
				if (followersList!=null) {
					if (followersList.get(0).getSuccess().equals("1")) {
						followersListAdapter=new FollowersListAdapter(Likes.this, R.layout.listitem_follower,
								followersList.get(0).getCategoryItemDTOs(),"");
						followersListView.setAdapter(followersListAdapter);							
					} else {
						Toast.makeText(Likes.this,followersList.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(Likes.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
