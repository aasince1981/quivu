package com.it.reloved;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.it.reloved.adapter.CategoryGridAdapter;
import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

public class Category extends RelovedPreference{

	private ImageView ivSearchHeader,ivRefreshHeader,ivAddUserHeader;
	public static CategoryDTO categoryDTO = null;
	private GridView categoryGridView;
	private CategoryGridAdapter categoryGridAdapter;
	private Intent intent=null;
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		/* method for initialising init components */
		ivSearchHeader=(ImageView)findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		ivRefreshHeader=(ImageView)findViewById(R.id.iv_refresh_header);		
		ivRefreshHeader.setOnClickListener(this);
		ivAddUserHeader=(ImageView)findViewById(R.id.iv_adduser_header);		
		ivAddUserHeader.setOnClickListener(this);
	
		categoryGridView=(GridView)findViewById(R.id.gridview_category);
		if (isNetworkAvailable()) {
			//--------------------------------------------------------------------------------------		
			if ( new AppSession(Category.this).getJson(AppSession.CATEGORY_JSON).equals("") ) {
				// Get data from server
				new TaskForCategory().execute();
			}
			else if(!(RelovedPreference.getTimeDiferenceMinute(new AppSession(Category.this).getLastSeenTime(AppSession.CATEGORY_SEEN_TIME)) 
					< RelovedPreference.UPDATE_INTERVAL) ) { // if time limit is over
				// Note:- Don't put this condition with above condition with OR operator 
				// this same body repetition is required to get last seen time first time. 
				new TaskForCategory().execute();
			}
			else {
				// Just show the saved activity data
				categoryDTO = UtilityDAO.parseCategory( new AppSession(Category.this).getJson(AppSession.CATEGORY_JSON)) ;
				showCategoryList(categoryDTO);
			}
			//--------------------------------------------------------------------------------------
		} else {
			Toast.makeText(Category.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
		}		
		
		/*perform click on grid items*/
		categoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent=new Intent(Category.this, SubCategory.class);
				intent.putExtra("categoryId", categoryDTO.getCategoryItemDTOs()
						.get(arg2).getCategoryId());
				intent.putExtra("CategoryName", categoryDTO.getCategoryItemDTOs()
						.get(arg2).getCategoryName());
				startActivity(intent);				
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
		case R.id.iv_search_header:
			intent=new Intent(Category.this, SubCategory.class);
			intent.putExtra("categoryId", "0");
			intent.putExtra("CategoryName", "");
			startActivity(intent);		
			break;
		case R.id.iv_adduser_header:
			intent=new Intent(Category.this, FindInviteFriends.class);
			startActivity(intent);	
			break;
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskForCategory().execute();
			} else {
				Toast.makeText(Category.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
	
	/*Task for category*/
	private class TaskForCategory extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(Category.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(Category.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(Category.this);
				categoryDTO=new UtilityDAO(Category.this).getCategories(Category.this, appSession.getBaseUrl(),getResources().getString(R.string.method_getCategories));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);			
			try {
				pd.dismiss();
				if (categoryDTO != null) {
					if (categoryDTO.getSuccess().equals("1")) {
						showCategoryList(categoryDTO);
					} else {
						Toast.makeText(Category.this,categoryDTO.getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(Category.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	/*method for setting adapter*/
	private void showCategoryList(CategoryDTO dto) {
		categoryGridAdapter=new CategoryGridAdapter(Category.this, R.layout.category_item,
				dto.getCategoryItemDTOs());
		categoryGridView.setAdapter(categoryGridAdapter);
	}
}
