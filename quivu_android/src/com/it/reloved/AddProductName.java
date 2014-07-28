package com.it.reloved;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductName extends RelovedPreference implements SearchTypes{

	ImageView ivBackHeader, ivRight;
	EditText itemName,itemDesc;
	TextView itemLocation;
	ImageView ivCancel;
	Intent intent=null;	
	String fromClass="";
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item_name);
		
		intent=getIntent();
		if (intent!=null) {
			fromClass=intent.getStringExtra("fromClass");
		}
		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRight = (ImageView) findViewById(R.id.iv_right_header);
		ivRight.setOnClickListener(this);		
		itemName=(EditText)findViewById(R.id.et_itemname_item);
		itemDesc=(EditText)findViewById(R.id.et_desc_item);		
		itemLocation=(TextView)findViewById(R.id.tv_location_item);
		itemLocation.setOnClickListener(this);
		ivCancel=(ImageView)findViewById(R.id.iv_cancel_item);
		ivCancel.setOnClickListener(this);
		
		if(fromClass.equals("EditProduct")){
			itemName.setText(EditProduct.itemNameString);
			itemDesc.setText(EditProduct.itemDescString);		
			if (EditProduct.place!=null) {
				itemLocation.setText(EditProduct.place.getName());
			}
		}else{
			itemName.setText(AddProduct.itemNameString);
			itemDesc.setText(AddProduct.itemDescString);		
			if (AddProduct.place!=null) {
				itemLocation.setText(AddProduct.place.getName());
			}
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
		case R.id.iv_right_header:
			if (fromClass.equals("EditProduct")) {
				EditProduct.itemNameString=itemName.getText().toString();
				EditProduct.itemDescString=itemDesc.getText().toString();			
				if (EditProduct.itemNameString.equals("")) {
					Toast.makeText(AddProductName.this, "Please enter product name.", Toast.LENGTH_LONG).show();
				} else {
					finish();
				}
			} else {
				AddProduct.itemNameString=itemName.getText().toString();
				AddProduct.itemDescString=itemDesc.getText().toString();			
				if (AddProduct.itemNameString.equals("")) {
					Toast.makeText(AddProductName.this, "Please enter product name.", Toast.LENGTH_LONG).show();
				} else {
					finish();
				}
			}
			
			break;
		case R.id.tv_location_item:
			nearBy();
			break;
		case R.id.iv_cancel_item:
			itemLocation.setText("");
			break;
		}
	}
	
	/* method for getting nearby places*/
	private void nearBy() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < TYPE1.length; i++) {
			if (i != (TYPE1.length - 1)) {
				sb.append(TYPE1[i]).append("|");
			} else {
				sb.append(TYPE1[i]);
			}
		}
		String nearByString  = sb.toString();
		Intent intent = new Intent(AddProductName.this, NearByLocation.class);
		intent.putExtra("title", "Near to you");
		intent.putExtra("search", nearByString);
		intent.putExtra("fromClass", fromClass);
		startActivity(intent);

	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (fromClass.equals("EditProduct")) {
			if (EditProduct.place!=null) {
				itemLocation.setText(EditProduct.place.getName());
			}
		} else {
			if (AddProduct.place!=null) {
				itemLocation.setText(AddProduct.place.getName());
			}
		}
		
	}
}
