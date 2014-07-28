package com.it.reloved;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.CurrencyTextWatcher;

public class ChatToBuy extends RelovedPreference{

	TextView tvCancel,tvDone,tvPriceDesc;
	EditText priceEditText;
	String priceStr="",userName="",OfferProductId="", OfferProductName="",
			OfferProductImage="",OfferToUserId="";
	Intent intent=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chattobuy);
		Window window=getWindow();
		window.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//overridePendingTransition(R.anim.slide_top_down,R.anim.fadein);
		
		
		intent=getIntent();
		if (intent!=null) {
			userName=intent.getStringExtra("userName");
			priceStr=intent.getStringExtra("price");
			OfferProductId=intent.getStringExtra("OfferProductId");
			OfferProductName=intent.getStringExtra("OfferProductName");
			OfferProductImage=intent.getStringExtra("OfferProductImage");
			OfferToUserId=intent.getStringExtra("OfferToUserId");
		}
		/* method for initialising init components */
		priceEditText=(EditText)findViewById(R.id.et_chattobuy);		
		priceEditText.addTextChangedListener(new CurrencyTextWatcher());
		priceEditText.setText("$"+priceStr);
		
		tvCancel=(TextView)findViewById(R.id.tv_cancel_chattobuy);
		tvDone=(TextView)findViewById(R.id.tv_done_chattobuy);
		tvPriceDesc=(TextView)findViewById(R.id.tv_price_desc_chattobuy);
		tvPriceDesc.setText(userName+" is sellingit for $"+priceStr);
		
		tvCancel.setOnClickListener(this);
		tvDone.setOnClickListener(this);
		
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
		case R.id.tv_cancel_chattobuy:
			SubCategoryDetails.tmp=0;
			finish();
			break;
		case R.id.tv_done_chattobuy:
			SubCategoryDetails.tmp=1;
			priceStr=priceEditText.getText().toString();
			if (priceStr.equals(null)||priceStr.equals("")) {
				priceStr="0";
			} 		
			if (isNetworkAvailable()) {
				new TaskAddOffer().execute();
			} else {
				Toast.makeText(ChatToBuy.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			
			break;
		default:
			break;
		}
	}
	
	/*Task for add offer*/
	private class TaskAddOffer extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(ForgotPassword.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(ChatToBuy.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ChatToBuy.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new OfferDAO(ChatToBuy.this).addOffers(appSession.getBaseUrl(),
						getResources().getString(R.string.method_addOffer), appSession.getUserId(),
						userDTOs.get(0).getUserName(), userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						OfferToUserId, OfferProductId, OfferProductName, OfferProductImage, priceStr);
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
						Toast.makeText(ChatToBuy.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(ChatToBuy.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ChatToBuy.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
