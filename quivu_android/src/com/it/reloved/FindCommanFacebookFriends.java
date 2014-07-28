package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.FollowersListAdapter;
import com.it.reloved.dao.FollowersDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.FBConnector;
import com.sromku.simple.fb.SimpleFacebook;

public class FindCommanFacebookFriends extends RelovedPreference{
	
	private ImageView ivBackHeader, ivRefresh;
	private TextView tvHeader,tvFindUsers;
	private Button btnFollowAll,btnInvite;
	private FollowersListAdapter followersListAdapter=null;
	private List<CategoryDTO> followersList=new ArrayList<CategoryDTO>();
	private ListView followersListView;
	private Context mContext =this;
	private String ToUserId="", ToUserName="", ToUserImage="", FollowStatus="1";
	String friendsIDs="";
	
	@Override
	protected void onCreate(Bundle activity_find_comman_fb_user) {
		// TODO Auto-generated method stub
		super.onCreate(activity_find_comman_fb_user);
		FBConnector.configureMyFB(FindCommanFacebookFriends.this); 
		setContentView(R.layout.activity_find_comman_fb_user);
		initLayouts();
		//-----------------------Facebook----------------------------------------
		if(getIntent().getExtras().getString("from").equals("fbconnector")) {		
			friendsIDs = getIntent().getExtras().getString("friends_ids");		
		}
		
		/*Task for getting common friend on fb*/
		if(!friendsIDs.equals(null)&&!friendsIDs.equals(""))
		if (isNetworkAvailable()) {
			new Task4GetFriendList().execute(friendsIDs);
		} else {
			Toast.makeText(FindCommanFacebookFriends.this,getString(R.string.NETWORK_ERROR),
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
	public void onResume() {
		super.onResume();
		FBConnector.mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	/* method for initialising init components */
	private void initLayouts()
	{
		tvFindUsers=(TextView)findViewById(R.id.tv_count);
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		tvHeader=(TextView)findViewById(R.id.tv_header);
		tvHeader.setText("Facebook");
		
		btnFollowAll=(Button)findViewById(R.id.btn_follow);
		btnInvite=(Button)findViewById(R.id.btn_invite);
		
		btnFollowAll.setOnClickListener(this);
		btnInvite.setOnClickListener(this);
		
		followersListView =(ListView)findViewById(R.id.list_command_fb_users);
		
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
		case R.id.btn_follow:
			methodFollowAll();
			break;
		case R.id.btn_invite:
			FBConnector.mSimpleFacebook.invite("I invite you to use this app", FBConnector.onInviteListener,"dfs");
		//	fbConnector=new FacebookConnector(getString(R.string.FACEBOOK_APPID), ((Activity)mContext), mContext, PERMS);
		//	fbConnector.apprequestDailog("Join QUIVU");
			break;
		case R.id.iv_refresh_header:
			if(!friendsIDs.equals(null)&&!friendsIDs.equals(""))
				if (isNetworkAvailable()) {
					new Task4GetFriendList().execute(friendsIDs);
				} else {
					Toast.makeText(FindCommanFacebookFriends.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
			break;
		default:
			break;
		}
	}
	/*Task for getting common friends*/
	public class TaskFollowers extends AsyncTask<String, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(String... params) {
			try {				
				
				followersList=new ArrayList<CategoryDTO>();
				AppSession appSession=new AppSession(FindCommanFacebookFriends.this);				
					followersList=new FollowersDAO(FindCommanFacebookFriends.this).getCommonFriendList(appSession.getBaseUrl(),
							getString(R.string.method_commonFriendList), appSession.getUserId(), params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(FindCommanFacebookFriends.this, null, null);
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
						if(followersList.get(0).getCategoryItemDTOs()!=null&&followersList.get(0).getCategoryItemDTOs().size()>0)
						{
							int s=followersList.get(0).getCategoryItemDTOs().size();
							
							if(s<1)
							{
								tvFindUsers.setText("No friends found");
							}
							else if(s>1)
							{
								tvFindUsers.setText(s+" friends found");
							}
							else
							{
								tvFindUsers.setText(s+" friend found");
							}
						}
					
						followersListAdapter=new FollowersListAdapter(FindCommanFacebookFriends.this, R.layout.listitem_follower,
								followersList.get(0).getCategoryItemDTOs(),"");
						followersListView.setAdapter(followersListAdapter);						
						
					} else {
						Toast.makeText(FindCommanFacebookFriends.this,followersList.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(FindCommanFacebookFriends.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*Task for getting fb friend list*/
	private class Task4GetFriendList extends AsyncTask<String, Void, String> {
		ProgressDialog mProgressDialog;
		@Override
		protected void onPostExecute(String result) {
			mProgressDialog.dismiss();	
			Log.v(getClass().getSimpleName(), "result="+result);
			if(!result.equals("")){
			if (isNetworkAvailable()) {
				new TaskFollowers().execute(result);
			} else {
				Toast.makeText(FindCommanFacebookFriends.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}	}
		}
		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(FindCommanFacebookFriends.this, "",
					"Please wait..");
		}

		@Override
		protected String doInBackground(String... params) {			
			return params[0];
		}
	}
	
	/*method for follow all*/
	private void methodFollowAll()
	{
		ToUserId="";ToUserName=""; ToUserImage="";
		StringBuilder sb1=new StringBuilder();
		StringBuilder sb2=new StringBuilder();
		StringBuilder sb3=new StringBuilder();
		if(followersList!=null)
		{
			List<CategoryItemDTO> dtoList=followersList.get(0).getCategoryItemDTOs();
			if(dtoList!=null&&dtoList.size()>0)
			{
				boolean flag=false;
				for (int i = 0; i < dtoList.size(); i++) {
					if(dtoList.get(i).getFollowStatus().equals("0")){
						flag=true;
					if (i==0) {
						sb1.append(dtoList.get(i).getProductUserId());
						sb2.append(dtoList.get(i).getProductUserName());
						sb3.append(dtoList.get(i).getProductUserImage());
					}
					else
					{
						sb1.append(",").append(dtoList.get(i).getProductUserId());
						sb2.append(",").append(dtoList.get(i).getProductUserName());
						sb3.append(",").append(dtoList.get(i).getProductUserImage());
					}
				}
				}
				ToUserId =sb1.toString();
				ToUserName =sb2.toString();
				ToUserImage =sb3.toString();
				FollowStatus="1";
				if(flag){
				if (isNetworkAvailable()) {
					new TaskAddFollow().execute(ToUserId, ToUserName, ToUserImage, FollowStatus);
				} else {
					Toast.makeText(mContext,mContext.getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}}
			}
		}
	}
	
	/*method for add follow*/
	private class TaskAddFollow extends AsyncTask<String, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(mContext, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {			
//				String ToUserId,String ToUserName,String ToUserImage,String FollowStatus
				AppSession appSession=new AppSession(mContext);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FollowersDAO(mContext).addFollowers(appSession.getBaseUrl(),
						mContext.getResources().getString(R.string.method_addFollow),
						appSession.getUserId(), userDTOs.get(0).getUserName(),
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						params[0], params[1], params[2], params[3]);
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
						Toast.makeText(mContext,arr[1],Toast.LENGTH_LONG).show();
						if(followersList!=null)
						{
							if(followersList.get(0).getCategoryItemDTOs()!=null)
							{
								if (followersList.get(0).getCategoryItemDTOs().size()>0) {
									for (int i = 0; i < followersList.get(0).getCategoryItemDTOs().size(); i++) {
										
										followersList.get(0).getCategoryItemDTOs().get(i).setFollowStatus("1");
									}
								}
							}
						}
						if(followersListAdapter!=null){
						followersListAdapter.notifyDataSetChanged();}					
					} else {
						Toast.makeText(mContext,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(mContext,mContext.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
}
