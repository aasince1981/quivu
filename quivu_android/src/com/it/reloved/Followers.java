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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.FollowersListAdapter;
import com.it.reloved.dao.FollowersDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

public class Followers extends RelovedPreference {

	ImageView ivBackHeader, ivRefresh;
	TextView tvHeader;
	ListView followersListView;
	FollowersListAdapter followersListAdapter=null;
	List<CategoryDTO> followersList=new ArrayList<CategoryDTO>();
	Intent intent=null;
	String type="",UserId="";
	boolean flagHistory = true;
	boolean checkHistory = true;
	int pageCountHistory = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followers);
		
		intent=getIntent();
		if (intent!=null) {
			type=intent.getStringExtra("type");
			UserId=intent.getStringExtra("UserId");
		}
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		tvHeader=(TextView)findViewById(R.id.tv_header);
		if (type.equals("followers")) {
			tvHeader.setText("Followers");
		} else {
			tvHeader.setText("Following");
		}
		
		
		followersListView=(ListView)findViewById(R.id.list_followers);
		if (isNetworkAvailable()) {
			new TaskFollowers().execute();
		} else {
			Toast.makeText(Followers.this,getString(R.string.NETWORK_ERROR),
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
				Toast.makeText(Followers.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}	
			break;
		default:
			break;
		}
	}
	
	/*Task for getting followers*/
	public class TaskFollowers extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				followersList=new ArrayList<CategoryDTO>();
				AppSession appSession=new AppSession(Followers.this);
				if (type.equals("followers")) {
					followersList=new FollowersDAO(Followers.this).getFollowers(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollower), appSession.getUserId(),
							""+pageCountHistory,UserId);
				} else {
					followersList=new FollowersDAO(Followers.this).getFollowing(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollowing), appSession.getUserId(),
							""+pageCountHistory,UserId);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(Followers.this, null, null);
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
						followersListAdapter=new FollowersListAdapter(Followers.this, R.layout.listitem_follower,
								followersList.get(0).getCategoryItemDTOs(),type);
						followersListView.setAdapter(followersListAdapter);
						
						if (followersList.get(0).getCategoryItemDTOs().size()>=10) {						
						followersListView.setOnScrollListener(new OnScrollListener() {
							
							@Override
							public void onScrollStateChanged(AbsListView view, int scrollState) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onScroll(AbsListView view, int firstVisibleItem,
									int visibleItemCount, int totalItemCount) {
								// TODO Auto-generated method stub
								if (checkHistory) {									
									int lastItem = view.getLastVisiblePosition();
									int lastInScreen = firstVisibleItem+ visibleItemCount;
									if ((lastInScreen == totalItemCount && lastItem == totalItemCount - 1)) {
										followersListView.setFastScrollEnabled(false);
										checkHistory = false;										
										pageCountHistory = pageCountHistory + 1;										
										if (isNetworkAvailable()) {
											new TaskScrolHistoryList().execute();
										}
									}
								}
							}
						});
						}
						
					} else {
						Toast.makeText(Followers.this,followersList.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(Followers.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*Task for getting followers on scrooll list*/
	private class TaskScrolHistoryList extends AsyncTask<Void, Void, Void> {
		List<CategoryDTO> local;
		ProgressDialog pd=null;
		List<CategoryDTO> myRidesDTOs;

		@Override
		protected void onPostExecute(Void result1) {
			try {
				pd.dismiss();
				if (local != null && local.size() > 0) {
					if (local.get(0).getSuccess().equals("1")) {	
						followersList.addAll(local);
						followersListView.setFastScrollEnabled(true);
						checkHistory = true;
						if (local != null) {
							if (local.size() == 0) {
								checkHistory = false;
							} else if (local.size() < 10) {
								checkHistory = false;
							}
						} else {
							checkHistory = false;							
						}
						followersListAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(Followers.this,myRidesDTOs.get(0).getMsg(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(Followers.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(Followers.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(Followers.this);
				if (type.equals("followers")) {
					followersList = new FollowersDAO(Followers.this).getFollowers(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollower),appSession.getUserId(),
							""+pageCountHistory,UserId);
				} else {
					followersList = new FollowersDAO(Followers.this).getFollowing(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollowing),appSession.getUserId(),
							""+pageCountHistory,UserId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
