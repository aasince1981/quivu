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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.it.reloved.adapter.ProductGridAdapter;
import com.it.reloved.dao.CategoryDetailsDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.utils.AppSession;

public class StuffLiked extends RelovedPreference{

	List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
	GridView categoryGridView;
	ProductGridAdapter productGridAdapter;
	Intent intent=null;	
	String fromClass="",UserId="";
	ImageView ivBackHeader, ivRefresh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stuffliked);		
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		
		intent=getIntent();
		if (intent!=null) {
			fromClass=intent.getStringExtra("fromClass");		
			UserId=intent.getStringExtra("UserId");		
			Log.i("StuffLiked", "fromClass==" + fromClass);
		}
		
		categoryGridView=(GridView)findViewById(R.id.gridview_category);
		
		if (isNetworkAvailable()) {
			new TaskForSubCategory().execute();
		} else {
			Toast.makeText(StuffLiked.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
		}		
		
		//perform click on grid item
		categoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent=new Intent(StuffLiked.this, SubCategoryDetails.class);
				intent.putExtra("productId", categoryDTOs.get(0).getCategoryItemDTOs().get(arg2).getCategoryId());
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
	
	//perform click
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
				new TaskForSubCategory().execute();
			} else {
				Toast.makeText(StuffLiked.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}	
			break;
		default:
			break;
		}
	}
	
	//Task for get offers made by me
	private class TaskForSubCategory extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(Category.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(StuffLiked.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(StuffLiked.this);
				if (fromClass.equals("Offer")) {
					categoryDTOs=new CategoryDetailsDAO(StuffLiked.this).getofferMadeByme(appSession.getBaseUrl(),
							getResources().getString(R.string.method_offerMadeByme),UserId);
				} else {
					categoryDTOs=new CategoryDetailsDAO(StuffLiked.this).getstuffLikes(appSession.getBaseUrl(),
							getResources().getString(R.string.method_stuffLikes),UserId);
				}				
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
				if (categoryDTOs != null) {
					if (categoryDTOs.get(0).getSuccess().equals("1")) {
						productGridAdapter=new ProductGridAdapter(StuffLiked.this, R.layout.grid_item_product,
								categoryDTOs.get(0).getCategoryItemDTOs());
						categoryGridView.setAdapter(productGridAdapter);
					} else {
						Toast.makeText(StuffLiked.this,categoryDTOs.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(StuffLiked.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
