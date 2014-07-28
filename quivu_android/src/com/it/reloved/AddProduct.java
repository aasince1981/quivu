package com.it.reloved;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.it.android.twitter.TwitterApp;
import com.it.android.twitter.TwitterConnector;
import com.it.reloved.adapter.CustomSpinnerAdapter;
import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.Place;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.CurrencyTextWatcher;
import com.it.reloved.utils.FBConnector;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;

public class AddProduct extends RelovedPreference implements
OnCheckedChangeListener {
	
	private static final String TAG = "AddProduct";
	
	private static final int PICK_IMAGE=1;
	private static final int CAPTURE_IMAGE = 2;
	private static final int CAPTURE_IMAGE_CUSTOM = 3;
	private static final int PIC_CROP = 4;
	private static final int AVIARY_EDITO = 5;

	private ImageView ivBackHeader, ivRight;
	private ImageView iv1Product, iv2Product, iv3Product, iv4Product;
	private Spinner categorySpinner;
	private TextView tvItemName;
	private EditText etPrice;
	private ToggleButton twitterToggleButton, fbWallToggleButton;
//	TextView configureTextView;
	private Intent intent = null;
	private String imagePath = "", imageCount = "";
	private String imagePath1 = "", imagePath2 = "", imagePath3 = "", imagePath4 = "";
	
	private TwitterApp twitterApp;
	private TwitterConnector tConnector;

	public static String itemNameString = "", itemDescString = "",
			itemPriceStr = "";
	public static Place place;
	
	private Uri camaraUri = null;
	private Bitmap bmpLogo = null;
	private String categoryId = "", categoryName = "", itemAddress = "", itemLat = "",
			itemLon = "";
	private String imageUrl4Share = "";
	private boolean isImgAvailable1 = false, isImgAvailable2 = false, isImgAvailable3 = false, isImgAvailable4 = false;

	@Override
	public void onBackPressed() {
		if( dialog != null ) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		tvItemName.setText(itemNameString);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);
		FBConnector.configureMyFB(AddProduct.this); 

		intent = getIntent();
		if (intent != null) {
			imageCount = intent.getStringExtra("imageCount");
			imagePath = intent.getStringExtra("imagePath");
			Log.i("AddProduct", "imagePath==" + imagePath + "--imageCount="
					+ imageCount);

			if (imageCount.equals("1")) {
				imagePath1 = imagePath;
			} else if (imageCount.equals("2")) {
				imagePath2 = imagePath;
			} else if (imageCount.equals("3")) {
				imagePath3 = imagePath;
			} else if (imageCount.equals("4")) {
				imagePath4 = imagePath;
			}

		}

		itemNameString = "";
		itemDescString = "";
		itemPriceStr = "";

		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRight = (ImageView) findViewById(R.id.iv_right_header);
		ivRight.setOnClickListener(this);

		iv1Product = (ImageView) findViewById(R.id.iv_1_additem);
		iv2Product = (ImageView) findViewById(R.id.iv_2_additem);
		iv3Product = (ImageView) findViewById(R.id.iv_3_additem);
		iv4Product = (ImageView) findViewById(R.id.iv_4_additem);

		if (!imagePath1.equals("")) {
			File file1 = new File(imagePath1);
			Bitmap bitmap1 = decodeFile(file1, 640, 960);
			iv1Product.setImageBitmap(bitmap1);
			iv1Product.setScaleType(ScaleType.CENTER_CROP);
			isImgAvailable1 = true;
		}

		if (!imagePath2.equals("")) {
			File file2 = new File(imagePath2);
			Bitmap bitmap2 = decodeFile(file2, 640, 960);
			iv2Product.setImageBitmap(bitmap2);
			iv2Product.setScaleType(ScaleType.CENTER_CROP);
			isImgAvailable2 = true;
		}

		if (!imagePath3.equals("")) {
			File file3 = new File(imagePath3);
			Bitmap bitmap3 = decodeFile(file3, 640, 960);
			iv3Product.setImageBitmap(bitmap3);
			iv3Product.setScaleType(ScaleType.CENTER_CROP);
			isImgAvailable3 = true;
		}

		if (!imagePath4.equals("")) {
			File file4 = new File(imagePath4);
			Bitmap bitmap4 = decodeFile(file4, 640, 960);
			iv4Product.setImageBitmap(bitmap4);
			iv4Product.setScaleType(ScaleType.CENTER_CROP);
			isImgAvailable4 = true;
		}

		iv1Product.setOnClickListener(this);
		iv2Product.setOnClickListener(this);
		iv3Product.setOnClickListener(this);
		iv4Product.setOnClickListener(this);

		categorySpinner = (Spinner) findViewById(R.id.category_spinner_additem);
		tvItemName = (TextView) findViewById(R.id.tv_itemname_additem);
		etPrice = (EditText) findViewById(R.id.et_price_additem);
		tvItemName.setOnClickListener(this);
		etPrice.addTextChangedListener(new CurrencyTextWatcher());

		twitterToggleButton = (ToggleButton) findViewById(R.id.tb_twitter_additem);
		twitterToggleButton.setOnCheckedChangeListener(this);
		fbWallToggleButton = (ToggleButton) findViewById(R.id.tb_fbwall_additem);
		fbWallToggleButton.setOnCheckedChangeListener(this);
	//	configureTextView = (TextView) findViewById(R.id.tv_configure_additem);

		/* for setting category adapter */
		final List<CategoryItemDTO> categoryList = new ArrayList<CategoryItemDTO>();
		categoryList.add(new CategoryItemDTO("0", "Select Category", null));
		if (Category.categoryDTO.getCategoryItemDTOs() != null
				&& Category.categoryDTO.getCategoryItemDTOs().size() > 0) {
			for (int i = 0; i < Category.categoryDTO.getCategoryItemDTOs()
					.size(); i++) {
				categoryList.add(new CategoryItemDTO(Category.categoryDTO
						.getCategoryItemDTOs().get(i).getCategoryId(),
						Category.categoryDTO.getCategoryItemDTOs().get(i)
						.getCategoryName(), null));
			}

		}
		CustomSpinnerAdapter categoryAdapter = new CustomSpinnerAdapter(
				AddProduct.this, R.layout.spinner, categoryList);
		categorySpinner.setAdapter(categoryAdapter);

		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (categoryList != null) {
					categoryId = categoryList.get(arg2).getCategoryId();
					categoryName = categoryList.get(arg2).getCategoryName();
					Log.i("setOnItemSelectedListener", "categoryId="
							+ categoryId + "--categoryName=" + categoryName);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
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

	boolean isFBEnable = false, isTWEnable = false;
	private EditText message;  
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// Toast.makeText(this, isChecked+"", 1).show();
		switch (buttonView.getId()) {
		case R.id.tb_fbwall_additem:
			if (isChecked) {
				// The toggle is enabled
				isFBEnable = true;
			} else {
				// The toggle is disabled
				isFBEnable = false;
			}
			break;

		case R.id.tb_twitter_additem:
			if (isChecked) {
				// The toggle is enabled
				/*isTWEnable = true; 
				twitterApp = new TwitterApp(AddProduct.this, getResources().getString(R.string.TWITTER_CONSUMER_KEY), 
						getResources().getString(R.string.TWITTER_CONSUMER_SECRET));
				tConnector = new TwitterConnector(getResources().getString(R.string.TWITTER_CONSUMER_KEY),
						getResources().getString(R.string.TWITTER_CONSUMER_SECRET), AddProduct.this, imageUrl4Share); 
				Log.i(TAG, "mAccessToken===="+twitterApp.hasAccessToken());
				if (!twitterApp.hasAccessToken()) {
					tConnector.loginToTwitter();
				} else {
					boolean tweetId = shareOnTwitter(loadBitmap(imageUrl4Share), "Tweet from Boudoir!");
				}		*/					
			} else {
				// The toggle is disabled
				isTWEnable = false; 
			}
			break;
		}
	}
	
	// method for Share Image With Tweet on Twitter.
		public boolean shareOnTwitter(Bitmap bmp, String tweet) {
			boolean tweetId=false;
			try{		
								 
				File imgFile = storeToSDCard(bmp);			
				tweetId = twitterApp.uploadPic(imgFile, tweet);					    
			} catch (Exception e1) {
					e1.printStackTrace();
					Log.e("shareOnTwitter", "Exception in shareOnTwitter");
			} catch (Error e1) {
					e1.printStackTrace();
					Log.e("shareOnTwitter", "Error in shareOnTwitter");
			}
			return tweetId;
		}
		
		private File storeToSDCard(Bitmap bitmap) {
			try{
				 ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				 FileOutputStream fo;						
				 try {
						fo = new FileOutputStream("/mnt/sdcard/wallpaper.jpg");
						fo.write(bytes.toByteArray());
						// remember close de FileOutput
						fo.close();
	             } catch (Exception e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
				 }
				 // String path=Environment.getExternalStorageDirectory().getPath();
				 File file = new File("/mnt/sdcard/wallpaper.jpg");
				 return file;
			} catch (Exception e1) {
				 e1.printStackTrace();
				 Log.e("storeToSDCard", "Exception in storeToSDCard");
				 return null;
			} catch (Error e1) {
				 e1.printStackTrace();
				 Log.e("storeToSDCard", "Error in storeToSDCard");
				 return null;
			}
		}
	
	@Override
	public void onResume() {
		super.onResume();
		FBConnector.mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	/* perform click */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.iv_right_header:
			itemPriceStr = etPrice.getText().toString();
			if (place != null) {
				itemAddress = "" + place.getName();
				itemLat = "" + place.getLatitude();
				itemLon = "" + place.getLongitude();
			}

			if (categoryId.equals("") || categoryId.equals("0")) {
				Toast.makeText(AddProduct.this, "Please select category.",
						Toast.LENGTH_LONG).show();
			} else if (itemNameString.equals("")) {
				Toast.makeText(AddProduct.this, "Please enter product name.",
						Toast.LENGTH_LONG).show();
			} else if (itemPriceStr.equals("")) {
				Toast.makeText(AddProduct.this, "Please enter product price.",
						Toast.LENGTH_LONG).show();
			} else {
				if (isNetworkAvailable()) {
					new TaskAddProduct().execute();
				} else {
					Toast.makeText(AddProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.iv_1_additem:
			imageCount = "1";
			dailogBoxCamera(AddProduct.this);
			break;
		case R.id.iv_2_additem:
			imageCount = "2";
			dailogBoxCamera(AddProduct.this);
			break;
		case R.id.iv_3_additem:
			imageCount = "3";
			dailogBoxCamera(AddProduct.this);
			break;
		case R.id.iv_4_additem:
			imageCount = "4";
			dailogBoxCamera(AddProduct.this);
			break;
		case R.id.tv_itemname_additem:
			intent = new Intent(AddProduct.this, AddProductName.class);
			intent.putExtra("fromClass", "");
			startActivity(intent);
			break;
		}
	}

	/* Task for add product */
	private class TaskAddProduct extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr = new String[3];

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(AddProduct.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(AddProduct.this);
				List<UserDTO> userDTOs = appSession.getConnections();
				arr = new UtilityDAO(AddProduct.this).addProducts(
						appSession.getBaseUrl(),
						getResources().getString(R.string.method_addProduct),
						categoryId,
						itemPriceStr,
						itemNameString,
						itemDescString,
						itemLat,
						itemLon,
						appSession.getUserId(),
						userDTOs.get(0).getUserName(),
						userDTOs.get(0).getUserImage()
						.replace(appSession.getUserImageBaseUrl(), ""),
						itemAddress, imagePath1, imagePath2, imagePath3,
						imagePath4, categoryName);
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
				if (arr != null && arr.length != 0) {
					if (arr[0].equals("1")) {
						Toast.makeText(AddProduct.this, arr[1],
								Toast.LENGTH_LONG).show();

							if(isFBEnable) {//--------------------------------Post to FB---
						
					/*	
						Feed feed = new Feed.Builder()
					    .setMessage("Clone it out...")
					    .setName("Simple Facebook SDK for Android")
					    .setCaption("Code less, do the same.")
					    .setDescription("Login, publish feeds and stories, invite friends and more...")
					    .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
					    .setLink("https://github.com/sromku/android-simple-facebook")
					    .addAction("Clone", "https://github.com/sromku/android-simple-facebook")
					    .addProperty("Full documentation", "http://sromku.github.io/android-simple-facebook", "http://sromku.github.io/android-simple-facebook")
					    .addProperty("Stars", "14")
					    .build();
						*/							
						imageUrl4Share = new AppSession(
								AddProduct.this).getProductBaseUrl() + arr[2];		
						Feed feed = new Feed.Builder()
						.setMessage("")
						.setName(itemNameString + "-"+ itemPriceStr/*"Simple Facebook for Android"*/)
								.setCaption("")
								.setDescription(itemDescString+" "+" ")
								.setPicture(imageUrl4Share)
										.setLink("http://mobilitytesting.com/quivu-app")
										.build();
						FBConnector.mSimpleFacebook.publish(feed, FBConnector.onPublishListener);
						
				//		facebookPostDialog();
						
							}//-------------------------------------------------------------
						intent = new Intent(AddProduct.this, TabSample.class);
						intent.putExtra("fromClass", "");
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(AddProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(AddProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	/*public void facebookPostDialog() {
		final Dialog dialog = new Dialog(AddProduct.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.color.white);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.facebook_post);
		message = (EditText) dialog.findViewById(R.id.message);
		ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
		String Message = message.getText().toString().trim();
		TextView description = (TextView) dialog.findViewById(R.id.description);
		TextView link = (TextView) dialog.findViewById(R.id.link);
		

		final Feed feed = new Feed.Builder()
	    .setMessage(Message)
	    .setName("Quivu")
	    .setCaption("Code less, do the same.")
	    .setDescription("Login, publish feeds and stories, invite friends and more...")
	    .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
	    .setLink("https://github.com/sromku/android-simple-facebook")
	    .addAction("Clone", "https://github.com/sromku/android-simple-facebook")
	    .addProperty("Full documentation", "http://sromku.github.io/android-simple-facebook", "http://sromku.github.io/android-simple-facebook")
	    .addProperty("Stars", "14")
	    .build();
		
		
		description.setText("Login, publish feeds and stories, invite friends and more...");
		link.setText("https://github.com/sromku/android-simple-facebook");
		
		Button Share = (Button) dialog.findViewById(R.id.Share);
		Button cancel = (Button) dialog.findViewById(R.id.cancel);
		Share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				

				FBConnector.mSimpleFacebook.publish(feed, FBConnector.onPublishListener);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}*/
	
	
	private Dialog dialog = null;
	/* dialog for camera and gallery image */
	private void dailogBoxCamera(final Context context) {
		dialog = new Dialog(context);
		Window window = dialog.getWindow();
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.popup_camera);
		window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));

		TextView camera = (TextView) window.findViewById(R.id.camera_popup);
		TextView galary = (TextView) window.findViewById(R.id.gallary_popup);
		TextView delete = (TextView) window.findViewById(R.id.delete_popup);
		View bottoum = (View) window.findViewById(R.id.bottom_divider);
		showHideRemoveOption(delete, bottoum);

		camera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				/*Intent intent1 = new Intent();
				intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				camaraUri = RelovedPreference.setImageUri();
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, camaraUri);
				startActivityForResult(intent1, CAPTURE_IMAGE);*/
							
				AppSession appSession=new AppSession(AddProduct.this);
				if (appSession.getBuiltinCamera().equals("1")) {
					// ------------------------------Go for default camera
					Intent intent1 = new Intent();
					intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					camaraUri = RelovedPreference.setImageUri();
					intent1.putExtra(MediaStore.EXTRA_OUTPUT, camaraUri);
					startActivityForResult(intent1, CAPTURE_IMAGE);	
				} 
				// -------------------------------------Go for custom Camera
				else {
					intent=new Intent(AddProduct.this, CustomCamera.class);	
					startActivityForResult(intent, CAPTURE_IMAGE_CUSTOM);
				}
				
				dialog.dismiss();
				dialog = null;
			}
		});
		galary.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// --------------------------------------Go for Gallery
				Intent intent1 = new Intent();
				intent1.setType("image/*");
				intent1.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent1, ""),
						PICK_IMAGE);
				dialog.dismiss();
				dialog = null;
			}
		});

		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeImageFromUI(Integer.parseInt(imageCount));
				dialog.dismiss();
				dialog = null;
			}
		});

		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if (resultCode != Activity.RESULT_CANCELED) {

			// ------------------------------Come from Gallery
			if (requestCode == PICK_IMAGE) {
				Log.d(TAG, "onActivityResult::PICK_IMAGE");
				go4AviaryEditor(data.getData());
			} 
			// ------------------------------Come from Default camera
			else if (requestCode == CAPTURE_IMAGE) {
				Log.d(TAG, "onActivityResult::CAPTURE_IMAGE");
				if (camaraUri != null) {
					performCrop(camaraUri);
				}
			} 
			// ------------------------------Come from Crop
			else if (requestCode == PIC_CROP) {
				Log.d(TAG, "onActivityResult::PIC_CROP");
				go4AviaryEditor(camaraUri);
			} 
			// ------------------------------Come from Custom camera
			else if (requestCode == CAPTURE_IMAGE_CUSTOM) {
				Log.d(TAG, "onActivityResult::CAPTURE_IMAGE_CUSTOM");
				go4AviaryEditor(data.getData());
			}
			// ------------------------------Come from Editor
			else if (requestCode == AVIARY_EDITO) {
				Log.d(TAG, "onActivityResult::AVIARY_EDITO");
				// output image path
				Uri mImageUri = data.getData();
				Bundle extra = data.getExtras();
				boolean changed = false;
				if (null != extra) {
					// image has been changed by the user?
					changed = extra
							.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
				}
				
				imagePath = getRealPathFromURI(mImageUri);
				if (imageCount.equals("1")) {
					imagePath1 = imagePath;
				} else if (imageCount.equals("2")) {
					imagePath2 = imagePath;
				} else if (imageCount.equals("3")) {
					imagePath3 = imagePath;
				} else if (imageCount.equals("4")) {
					imagePath4 = imagePath;
				}
				
				if (!imagePath1.equals("")) {
					File file1 = new File(imagePath1);
					Bitmap bitmap1 = decodeFile(file1, 640, 960);
					iv1Product.setImageBitmap(bitmap1);
					iv1Product.setScaleType(ScaleType.CENTER_CROP);
					isImgAvailable1 = true;
				}

				if (!imagePath2.equals("")) {
					File file2 = new File(imagePath2);
					Bitmap bitmap2 = decodeFile(file2, 640, 960);
					iv2Product.setImageBitmap(bitmap2);
					iv2Product.setScaleType(ScaleType.CENTER_CROP);
					isImgAvailable2 = true;
				}

				if (!imagePath3.equals("")) {
					File file3 = new File(imagePath3);
					Bitmap bitmap3 = decodeFile(file3, 640, 960);
					iv3Product.setImageBitmap(bitmap3);
					iv3Product.setScaleType(ScaleType.CENTER_CROP);
					isImgAvailable3 = true;
				}

				if (!imagePath4.equals("")) {
					File file4 = new File(imagePath4);
					Bitmap bitmap4 = decodeFile(file4, 640, 960);
					iv4Product.setImageBitmap(bitmap4);
					iv4Product.setScaleType(ScaleType.CENTER_CROP);
					isImgAvailable4 = true;
				}						
			} 
		}			
	}

	private void go4AviaryEditor(Uri imgURI) {
		Log.i(TAG, "imgURI:  "+imgURI);
		Intent newIntent = new Intent( this, FeatherActivity.class );
		newIntent.setData( imgURI );
		newIntent.putExtra( Constants.EXTRA_IN_API_KEY_SECRET, getResources().getString(R.string.AVIARY_API_SECRET) );	
		startActivityForResult( newIntent, AVIARY_EDITO ); 
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
	
	@SuppressLint("NewApi")
	private void removeImageFromUI(int imageCount) {				
		switch (imageCount) {
		case 1:			
			//this is main image it can't delete
			break;
		case 2:
			iv2Product.setImageBitmap(null);
			imagePath2 = "";
			isImgAvailable2 = false;
			break;
		case 3:
			iv3Product.setImageBitmap(null);
			imagePath3 = "";
			isImgAvailable3 = false;
			break;
		case 4:
			iv4Product.setImageBitmap(null);
			imagePath4 = "";
			isImgAvailable4 = false;
			break;
		}
	}

	private void showHideRemoveOption(TextView tv, View v) {
		switch (Integer.parseInt(imageCount)) {
		case 1:
			//this is main image it can't delete
				tv.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
			
			break;
		case 2:
			if (!isImgAvailable2) {
				tv.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
			} else {
				tv.setVisibility(View.VISIBLE);
				v.setVisibility(View.VISIBLE);
			}
			break;
		case 3:
			if (!isImgAvailable3) {
				tv.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
			} else {
				tv.setVisibility(View.VISIBLE);
				v.setVisibility(View.VISIBLE);
			}
			break;
		case 4:
			if (!isImgAvailable4) {
				tv.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
			} else {
				tv.setVisibility(View.VISIBLE);
				v.setVisibility(View.VISIBLE);
			}
			break;

		default:
			tv.setVisibility(View.VISIBLE);
			v.setVisibility(View.VISIBLE);
			break;
		}
	}

}
