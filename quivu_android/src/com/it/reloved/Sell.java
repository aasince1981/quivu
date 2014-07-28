package com.it.reloved;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.it.reloved.utils.AppSession;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Sell extends RelovedPreference {

	private static final String TAG = "Sell";

	private static final int PICK_IMAGE = 1;
	private static final int CAPTURE_IMAGE = 2;
	private static final int CAPTURE_IMAGE_CUSTOM = 3;
	private static final int PIC_CROP = 4;
	private static final int AVIARY_EDITO = 5;

	ImageView ivSearchHeader, ivRefreshHeader, ivAddUserHeader;
	TextView tvTop;
	Intent intent = null;
	LinearLayout cameraLayout, galleryLayout;
	private Uri camaraUri = null;

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);

		/* method for initialising init components */
		ivSearchHeader = (ImageView) findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		ivRefreshHeader = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefreshHeader.setOnClickListener(this);
		ivAddUserHeader = (ImageView) findViewById(R.id.iv_adduser_header);
		ivAddUserHeader.setOnClickListener(this);
		ivAddUserHeader.setVisibility(View.GONE);
		ivRefreshHeader.setVisibility(View.GONE);

		cameraLayout = (LinearLayout) findViewById(R.id.takePhoto_layout);
		galleryLayout = (LinearLayout) findViewById(R.id.gallery_layout);
		cameraLayout.setOnClickListener(this);
		galleryLayout.setOnClickListener(this);

		tvTop = (TextView) findViewById(R.id.tv_text_sell);
		tvTop.setTypeface(typefaceBold);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);

	}

	// perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_search_header:
			intent = new Intent(Sell.this, SubCategory.class);
			intent.putExtra("categoryId", "0");
			intent.putExtra("CategoryName", "");
			startActivity(intent);
			break;
		case R.id.takePhoto_layout:
			AppSession appSession = new AppSession(Sell.this);
			if (appSession.getBuiltinCamera().equals("1")) {
				Intent intent1 = new Intent();
				intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				camaraUri = RelovedPreference.setImageUri();
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, camaraUri);
				startActivityForResult(intent1, CAPTURE_IMAGE);
			} else {
				intent = new Intent(Sell.this, CustomCamera.class);
				intent.putExtra("imageCount", "1");
				// startActivity(intent);
				startActivityForResult(intent, CAPTURE_IMAGE_CUSTOM);
			}

			break;
		case R.id.gallery_layout:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
			break;
		default:
			break;
		}
	}

	private void go4AviaryEditor(Uri imgURI) {
		Log.i(TAG, "imgURI:  " + imgURI);
		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(imgURI);
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET, getResources()
				.getString(R.string.AVIARY_API_SECRET));
		startActivityForResult(newIntent, AVIARY_EDITO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_CANCELED) {

			if (requestCode == PICK_IMAGE) {
				go4AviaryEditor(data.getData());
			} 
			
			else if (requestCode == CAPTURE_IMAGE) {
				if (camaraUri != null) {
					performCrop(camaraUri);
				}
			} 
			
			else if (requestCode == PIC_CROP) {
				go4AviaryEditor(camaraUri);
			} 
			
			else if (requestCode == AVIARY_EDITO) {
				// output image path
				Uri mImageUri = data.getData();
				Bundle extra = data.getExtras();
				boolean changed = false;
				if (null != extra) {
					// image has been changed by the user?
					changed = extra
							.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
				}
				Log.i(TAG, "isChanged:  " + changed);
				intent = new Intent(Sell.this, AddProduct.class);
				intent.putExtra("imagePath", getRealPathFromURI(data.getData()));
				intent.putExtra("imageCount", "1");
				startActivity(intent);
			} 
			
			else if (requestCode == CAPTURE_IMAGE_CUSTOM) {
				// Toast.makeText(this, data.getData().toString() , 1).show();
				go4AviaryEditor(data.getData());
			}

		}

	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			return null;
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/* method for cropping */
	private void performCrop(Uri picUri) {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// cropIntent.setClassName("com.android.gallery",
			// "com.android.camera.CropImage");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			cropIntent.putExtra("scale", true);
			// intent.putExtra("noFaceDetection", true);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);

			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

}
