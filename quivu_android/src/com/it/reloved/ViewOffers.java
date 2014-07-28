package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.OffersListAdapter;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.OfferDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;

public class ViewOffers extends RelovedPreference{

	ImageView ivBackHeader,ivRefresh;
	ImageLoader imageLoader=null;
	Intent intent=null;
	ImageView productImageView;
	TextView tvProductName,tvProductPrice;
	ListView offerListView;
	OffersListAdapter offersListAdapter=null;
	
	List<OfferDTO> offerDTOs=new ArrayList<OfferDTO>();
	String OfferProductId="",OfferProductName="",OfferProductPrice="",OfferProductImage="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_offers);
		/* method for initialising init components */
		imageLoader=new ImageLoader(ViewOffers.this);
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh=(ImageView)findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		
		intent=getIntent();
		if (intent!=null) {			
			OfferProductId=intent.getStringExtra("OfferProductId");
			OfferProductName=intent.getStringExtra("OfferProductName");
			OfferProductPrice=intent.getStringExtra("OfferProductPrice");
			OfferProductImage=intent.getStringExtra("OfferProductImage");
		}
		
		productImageView=(ImageView)findViewById(R.id.iv_item_viewoffers);
		tvProductName=(TextView)findViewById(R.id.tv_item_name_viewoffers);
		tvProductPrice=(TextView)findViewById(R.id.tv_item_price_viewoffers);		
		tvProductName.setText(OfferProductName);
		tvProductPrice.setText("$"+OfferProductPrice);
		
		offerListView=(ListView)findViewById(R.id.list_viewoffers);
		
		if (!OfferProductImage.equals("")) {
			imageLoader.DisplayImage(new AppSession(ViewOffers.this).getProductBaseUrl()+"thumbtwo_"+OfferProductImage,
					(Activity) ViewOffers.this,productImageView,0, 0, 0, R.drawable.no_image);
		}else{
			productImageView.setImageResource(R.drawable.no_image);
		}
	
		if (isNetworkAvailable()) {
			new TaskForOffers().execute();
		} else {
			Toast.makeText(ViewOffers.this,getString(R.string.NETWORK_ERROR),
				Toast.LENGTH_LONG).show();
		}
		//click on listitem
		offerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//Log.i("ViewOffered", "getOfferAmount : "+offerDTOs.get(0).getOfferItemDTOs().get(arg2).getOfferAmount());
				
				// TODO Auto-generated method stub
				intent=new Intent(ViewOffers.this, ViewChat.class);
				intent.putExtra("productImage",OfferProductImage);
				intent.putExtra("productName",OfferProductName);
				intent.putExtra("productPrice",OfferProductPrice);
				intent.putExtra("priceOffered",offerDTOs.get(0).getOfferItemDTOs().get(arg2).getOfferAmount());				
				intent.putExtra("dealLocation","");
				intent.putExtra("productUsername",offerDTOs.get(0).getOfferItemDTOs().get(arg2).getOfferUserName());
				intent.putExtra("productUserId",offerDTOs.get(0).getOfferItemDTOs().get(arg2).getOfferUserId());
				intent.putExtra("productAddTime", offerDTOs.get(0).getOfferItemDTOs().get(arg2).getOfferAddTime());
				intent.putExtra("productId",OfferProductId);
				intent.putExtra("fromClass","ViewOffers");
				intent.putExtra("productSoldStatus", "");
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
				new TaskForOffers().execute();
			} else {
				Toast.makeText(ViewOffers.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}	
	}
	
	//Task for offers
	public class TaskForOffers extends AsyncTask<Void, Void, Void> {				
		ProgressDialog pd=null;		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				offerDTOs=new ArrayList<OfferDTO>();
				AppSession appSession=new AppSession(ViewOffers.this);
				offerDTOs=new OfferDAO(ViewOffers.this).getOffers(appSession.getBaseUrl(),
						getResources().getString(R.string.method_getOffers), OfferProductId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(ViewOffers.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		@Override
		protected void onPostExecute(Void result) {
			pd.dismiss();
			try {				
				if (offerDTOs!=null) {
					if (offerDTOs.get(0).getSuccess().equals("1")) {
						offersListAdapter=new OffersListAdapter(ViewOffers.this, R.layout.offer_list_item,
								offerDTOs.get(0).getOfferItemDTOs());
						offerListView.setAdapter(offersListAdapter);
					} else {
						Toast.makeText(ViewOffers.this,offerDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}					
				} else {
					Toast.makeText(ViewOffers.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
