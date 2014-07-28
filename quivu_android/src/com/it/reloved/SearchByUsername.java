package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import com.it.reloved.adapter.FollowersListAdapter;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchByUsername extends RelovedPreference{

	ImageView ivBackHeader,ivFilter,ivSearchHeader; 
	EditText filterEditText;	
	String filterString="";
	List<CategoryDTO> usersList=new ArrayList<CategoryDTO>();
	FollowersListAdapter followersListAdapter=null;
	ListView userListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchbyuser);
		
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivFilter=(ImageView)findViewById(R.id.iv_filter_header);
		ivFilter.setOnClickListener(this);
		ivFilter.setVisibility(View.GONE);
		ivSearchHeader=(ImageView)findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		filterEditText=(EditText)findViewById(R.id.et_search_header);
		filterEditText.setHint("Search by username");
		
		userListView=(ListView)findViewById(R.id.list_searchbyuser);
		
		//filter edittext on serch action
		filterEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {		    
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					 filterString=filterEditText.getText().toString();
					 if (!filterString.equals("")) {
						if (isNetworkAvailable()) {
							new TaskSearchUser().execute();
						} else {
							Toast.makeText(SearchByUsername.this,getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG)
									.show();
						}
					 }
			            return true;
			        }
				return false;
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
	
	//perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;		
		case R.id.iv_search_header:
			filterString=filterEditText.getText().toString();
			if (!filterString.equals("")) {
				if (isNetworkAvailable()) {
					new TaskSearchUser().execute();
				} else {
					Toast.makeText(SearchByUsername.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}
			break;	
		}
	}
	
	//Task for search user
	public class TaskSearchUser extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				usersList=new ArrayList<CategoryDTO>();
				AppSession appSession=new AppSession(SearchByUsername.this);
				usersList=new OfferDAO(SearchByUsername.this).searchByUsername(appSession.getBaseUrl(),
							getResources().getString(R.string.method_searchUsers),filterString,
							appSession.getUserId());						
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(SearchByUsername.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			try {				
				if (usersList!=null) {
					if (usersList.get(0).getSuccess().equals("1")) {
						followersListAdapter=new FollowersListAdapter(SearchByUsername.this, R.layout.listitem_follower,
								usersList.get(0).getCategoryItemDTOs(),"");
						userListView.setAdapter(followersListAdapter);							
					} else {
						Toast.makeText(SearchByUsername.this,usersList.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(SearchByUsername.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
