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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.FollowersListAdapter;
import com.it.reloved.dao.FollowersDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

public class Followings extends RelovedPreference {

	private ImageView ivRefresh;
	private TextView tvHeader, tvServerMsg;
	private ListView followersListView;
	private FollowersListAdapter followersListAdapter=null;
	private List<CategoryDTO> followersList=new ArrayList<CategoryDTO>();
	private Intent intent=null;
	private boolean flagHistory = true;
	private boolean checkHistory = true;
	private int pageCountHistory = 1;
	
	private LinearLayout noDataLinLayout;
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_followings);			
		
		/* method for initialising init components */
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);		
		tvHeader=(TextView)findViewById(R.id.tv_header);
		tvHeader.setText("Following");
		noDataLinLayout=(LinearLayout)findViewById(R.id.lin_layout_nodata);
		tvServerMsg=(TextView)findViewById(R.id.tv_server_msg);
		
		followersListView=(ListView)findViewById(R.id.list_followers);
		if (isNetworkAvailable()) {
			new TaskFollowers().execute();
		} else {
			//Toast.makeText(Followings.this,getString(R.string.NETWORK_ERROR),
			//	Toast.LENGTH_LONG).show();
			showMessageView( getResources().getString(R.string.NETWORK_ERROR) );
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
				Toast.makeText(Followings.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}	
			break;
		default:
			break;
		}
	}
	
	/*Task for getting followings*/
	public class TaskFollowers extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				followersList=new ArrayList<CategoryDTO>();
				AppSession appSession=new AppSession(Followings.this);
				followersList=new FollowersDAO(Followings.this).getFollowing(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollowing), appSession.getUserId(),
							""+pageCountHistory,appSession.getUserId());
								
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(Followings.this, null, null);
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
						if (followersList.get(0).getCategoryItemDTOs().size() != 0) {
						//---------------------------SHOW CONTENTS LIST-----------------------
							followersListView.setVisibility(View.VISIBLE);
							noDataLinLayout.setVisibility(View.GONE);
						followersListAdapter=new FollowersListAdapter(Followings.this, R.layout.listitem_follower,
								followersList.get(0).getCategoryItemDTOs(),"following");
						followersListView.setAdapter(followersListAdapter);	
						
						if (followersList.get(0).getCategoryItemDTOs().size()>=10) {						
						followersListView.setOnScrollListener(new OnScrollListener() {							
							@Override
							public void onScrollStateChanged(AbsListView view, int scrollState) {							
							}							
							@Override
							public void onScroll(AbsListView view, int firstVisibleItem,
									int visibleItemCount, int totalItemCount) {
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
					//--------------------------------------------------------------------
					}//if (followersList.get(0).getCategoryItemDTOs().size() != 0)
					else {
						showMessageView(followersList.get(0).getMsg());
					} 
					}else {
						// Toast.makeText(Followings.this,followersList.get(0).getMsg(),Toast.LENGTH_LONG).show();
						showMessageView(followersList.get(0).getMsg());
					}					
				} else {
					//Toast.makeText(Followings.this,getString(R.string.NETWORK_ERROR),
					//		Toast.LENGTH_LONG).show();
					showMessageView( getResources().getString(R.string.NETWORK_ERROR) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	private void showMessageView( String msg ) {
		followersListView.setVisibility(View.GONE);
		noDataLinLayout.setVisibility(View.VISIBLE);
		tvServerMsg.setText(msg);
	}
	
	/*Task for getting following on scroll*/
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
						Toast.makeText(Followings.this,myRidesDTOs.get(0).getMsg(),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(Followings.this,getString(R.string.NETWORK_ERROR),
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
				pd = ProgressDialog.show(Followings.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(Followings.this);
				followersList = new FollowersDAO(Followings.this).getFollowing(appSession.getBaseUrl(),
							getResources().getString(R.string.method_getFollowing),appSession.getUserId(),
							""+pageCountHistory,appSession.getUserId());				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
