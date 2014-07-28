package com.it.reloved;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import android.graphics.BitmapFactory;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.it.reloved.adapter.CustomSpinnerAdapter;
import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.Place;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;

public class EditProduct extends RelovedPreference {

	private final String TAG = getClass().getSimpleName();

	private static final int PICK_IMAGE = 1;
	private static final int CAPTURE_IMAGE = 2;
	private static final int CAPTURE_IMAGE_CUSTOM = 3;
	private static final int PIC_CROP = 4;
	private static final int AVIARY_EDITO = 5;

	private ImageView ivBackHeader, ivRight;
	private ImageView iv1Product, iv2Product, iv3Product, iv4Product;
	private Spinner categorySpinner;
	private TextView tvItemName, tvDeleteListing, tvMarkAsSold;
	private EditText etPrice;
	private ImageLoader imageLoader = null;
	private Intent intent = null;
	private String imagePath = "", imageCount = "";
	private String imagePath1 = "", imagePath2 = "", imagePath3 = "",
			imagePath4 = "";

	public static String itemNameString = "", itemDescString = "",
			itemPriceStr = "";
	public static Place place;
	private Uri camaraUri = null;
	private Bitmap bmpLogo = null;
	private String categoryId = "", categoryName = "", ProductId = "",
			itemAddress = "", itemLat = "", itemLon = "", ProductImage = "";

	private boolean isImgAvailable1 = false, isImgAvailable2 = false, isImgAvailable3 = false, isImgAvailable4 = false;
	//private String imgID1="", imgID2="", imgID3="", imgID4=""; 
	private Set<String> deleteImgIDs = new HashSet<String>();
	
	@Override
	public void onBackPressed() {
		if (dialog != null) {
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
		setContentView(R.layout.edit_item);
		imageLoader = new ImageLoader(EditProduct.this);

		intent = getIntent();
		if (intent != null) {
			ProductId = intent.getStringExtra("ProductId");
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

		iv1Product.setOnClickListener(this);
		iv2Product.setOnClickListener(this);
		iv3Product.setOnClickListener(this);
		iv4Product.setOnClickListener(this);

		categorySpinner = (Spinner) findViewById(R.id.category_spinner_additem);
		tvItemName = (TextView) findViewById(R.id.tv_itemname_additem);
		etPrice = (EditText) findViewById(R.id.et_price_additem);
		tvItemName.setOnClickListener(this);

		tvDeleteListing = (TextView) findViewById(R.id.tv_delete_listing_edititem);
		tvDeleteListing.setOnClickListener(this);
		tvMarkAsSold = (TextView) findViewById(R.id.tv_markas_sold_edititem);
		tvMarkAsSold.setOnClickListener(this);

		/* for setting category spinner adapter */
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
				EditProduct.this, R.layout.spinner, categoryList);
		categorySpinner.setAdapter(categoryAdapter);

		/* perform action on item selected */
		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (categoryList != null) {
					if (arg2 < categoryList.size()) {
						categoryId = categoryList.get(arg2).getCategoryId();
						categoryName = categoryList.get(arg2).getCategoryName();
						Log.i("setOnItemSelectedListener", "categoryId="
								+ categoryId + "--categoryName=" + categoryName);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/* set values */
		if (SubCategoryDetails.subCategoryDetailsDTOs != null) {

			itemNameString = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductName();
			itemDescString = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductDescription();
			itemPriceStr = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductPrice();

			itemAddress = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductAddress();
			itemLat = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductLatitude();
			itemLon = ""
					+ SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductLongitude();

			tvItemName.setText(itemNameString);
			etPrice.setText(itemPriceStr);

			int poss1111 = 0;
			for (int j = 0; j < categoryList.size(); j++) {
				if (categoryList
						.get(j)
						.getCategoryId()
						.equals(SubCategoryDetails.subCategoryDetailsDTOs
								.get(0).getProductCatId())) {
					poss1111 = j;
					break;
				}
			}
			Log.i("spinnerCountry position", "poss=" + poss1111);
			categorySpinner.setSelection(poss1111);

			if (SubCategoryDetails.subCategoryDetailsDTOs.get(0)
					.getProductImageDtos().size() == 1) {
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(0).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(0).getCategoryImage(),
							(Activity) EditProduct.this, iv1Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable1 = true;
				}
			} else if (SubCategoryDetails.subCategoryDetailsDTOs.get(0)
					.getProductImageDtos().size() == 2) {
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(0).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(0).getCategoryImage(),
							(Activity) EditProduct.this, iv1Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable1 = true;
				}

				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(1).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(1).getCategoryImage(),
							(Activity) EditProduct.this, iv2Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable2 = true;
				}
			} else if (SubCategoryDetails.subCategoryDetailsDTOs.get(0)
					.getProductImageDtos().size() == 3) {
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(0).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(0).getCategoryImage(),
							(Activity) EditProduct.this, iv1Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable1 = true;
				}

				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(1).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(1).getCategoryImage(),
							(Activity) EditProduct.this, iv2Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable2 = true;
				}
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(2).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(2).getCategoryImage(),
							(Activity) EditProduct.this, iv3Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable3 = true;
				}
			} else if (SubCategoryDetails.subCategoryDetailsDTOs.get(0)
					.getProductImageDtos().size() == 4) {
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(0).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(0).getCategoryImage(),
							(Activity) EditProduct.this, iv1Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable1 = true;
				}

				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(1).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(1).getCategoryImage(),
							(Activity) EditProduct.this, iv2Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable2 = true;
				}
				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(2).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(2).getCategoryImage(),
							(Activity) EditProduct.this, iv3Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable3 = true;
				}

				if (!SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getProductImageDtos().get(3).getCategoryImage()
						.equals("")) {
					imageLoader.DisplayImage(
							new AppSession(EditProduct.this)
									.getProductBaseUrl()
									+ "thumbtwo_"
									+ SubCategoryDetails.subCategoryDetailsDTOs
											.get(0).getProductImageDtos()
											.get(3).getCategoryImage(),
							(Activity) EditProduct.this, iv4Product, 0, 0, 0,
							R.drawable.add_photo_icon);
					isImgAvailable4 = true;
				}
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

	/* perform click */
	@Override
	public void onClick(View v) {
		super.onClick(v);			
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.tv_delete_listing_edititem:
			DailogDeleteProductOffer(EditProduct.this, "Confirm Deletion",
					"Are you sure you want to delete this listing?");
			break;
		case R.id.tv_markas_sold_edititem:
			if (SubCategoryDetails.subCategoryDetailsDTOs != null) {
				for (int i = 0; i < SubCategoryDetails.subCategoryDetailsDTOs
						.get(0).getProductImageDtos().size(); i++) {
					if (SubCategoryDetails.subCategoryDetailsDTOs.get(0)
							.getProductImageDtos().get(i).getCategoryName()
							.equals("1")) {
						ProductImage = SubCategoryDetails.subCategoryDetailsDTOs
								.get(0).getProductImageDtos().get(i)
								.getCategoryImage();
						break;
					}
				}
			}
			DailogMarkasSoldProduct(
					EditProduct.this,
					"Mark this item as sold to someone outside of Quivu?",
					"You'll no longer be able to access all offers or chat messages for this item.");
			break;
		case R.id.iv_right_header:				
			itemPriceStr = etPrice.getText().toString();
			if (place != null) {
				itemAddress = "" + place.getName();
				itemLat = "" + place.getLatitude();
				itemLon = "" + place.getLongitude();
			}
			if (categoryId.equals("") || categoryId.equals("0")) {
				Toast.makeText(EditProduct.this, "Please select category.",
						Toast.LENGTH_LONG).show();
			} else if (itemNameString.equals("")) {
				Toast.makeText(EditProduct.this, "Please enter product name.",
						Toast.LENGTH_LONG).show();
			} else if (itemPriceStr.equals("")) {
				Toast.makeText(EditProduct.this, "Please enter product price.",
						Toast.LENGTH_LONG).show();
			} else {
				if (isNetworkAvailable()) {	
					new TaskEditProduct().execute();
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.iv_1_additem:
			imageCount = "1";
			dailogBoxCamera(EditProduct.this);
			break;
		case R.id.iv_2_additem:
			imageCount = "2";
			dailogBoxCamera(EditProduct.this);
			break;
		case R.id.iv_3_additem:
			imageCount = "3";
			dailogBoxCamera(EditProduct.this);
			break;
		case R.id.iv_4_additem:
			imageCount = "4";
			dailogBoxCamera(EditProduct.this);
			break;
		case R.id.tv_itemname_additem:
			intent = new Intent(EditProduct.this, AddProductName.class);
			intent.putExtra("fromClass", "EditProduct");
			startActivity(intent);
			break;
		}
	}

	/* Task for Edit Product */
	private class TaskEditProduct extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr = new String[2];

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(EditProduct.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(EditProduct.this);

				arr = new UtilityDAO(EditProduct.this).editProducts(
						appSession.getBaseUrl(),
						getResources().getString(R.string.method_editProduct),
						categoryId, itemPriceStr, itemNameString,
						itemDescString, itemLat, itemLon,
						appSession.getUserId(), ProductId, itemAddress,
						imagePath1, imagePath2, imagePath3, imagePath4,
						categoryName, deleteImgIDs.toString().replace("[", "").replace("]", ""));
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
				if (arr != null) {
					if (arr[0].equals("1")) {
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
						setResult(RESULT_OK);
						finish();
					} else {
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Task for delete producy */
	private class TaskDeleteProduct extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr = new String[2];

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(EditProduct.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(EditProduct.this);
				List<UserDTO> userDTOs = appSession.getConnections();
				arr = new UtilityDAO(EditProduct.this)
						.deleteProducts(
								appSession.getBaseUrl(),
								getResources().getString(
										R.string.method_deleteProduct),
								ProductId,
								itemNameString,
								appSession.getUserId(),
								userDTOs.get(0).getUserName(),
								userDTOs.get(0)
										.getUserImage()
										.replace(
												appSession
														.getUserImageBaseUrl(),
												""));
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
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* Task for mark as sold */
	private class TaskMarkAsSoldProduct extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr = new String[2];

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(EditProduct.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				AppSession appSession = new AppSession(EditProduct.this);
				List<UserDTO> userDTOs = appSession.getConnections();
				arr = new UtilityDAO(EditProduct.this).markAsSoldProducts(
						appSession.getBaseUrl(),
						getResources().getString(R.string.method_markAsSold),
						ProductId,
						itemNameString,
						ProductImage,
						appSession.getUserId(),
						userDTOs.get(0).getUserName(),
						userDTOs.get(0).getUserImage()
								.replace(appSession.getUserImageBaseUrl(), ""));
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
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(EditProduct.this, arr[1],
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/* dialog for delete offer */
	private void DailogDeleteProductOffer(Context mCtx, final String title,
			final String msg) {
		final Dialog dialog = new Dialog(mCtx);
		Window window = dialog.getWindow();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_yesno);
		window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));

		TextView tvTitle = (TextView) window.findViewById(R.id.title_yesno);
		TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
		TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
		TextView tvNo = (TextView) window.findViewById(R.id.no_yesno);
		tvTitle.setText(title);
		tvMsg.setText(msg);

		tvYes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new TaskDeleteProduct().execute();
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
			}
		});

		tvNo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/* dialog for mark as sold */
	private void DailogMarkasSoldProduct(Context mCtx, final String title,
			final String msg) {
		final Dialog dialog = new Dialog(mCtx);
		Window window = dialog.getWindow();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_yesno);
		window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));

		TextView tvTitle = (TextView) window.findViewById(R.id.title_yesno);
		TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
		TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
		TextView tvNo = (TextView) window.findViewById(R.id.no_yesno);
		tvTitle.setText(title);
		tvMsg.setText(msg);

		tvYes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new TaskMarkAsSoldProduct().execute();
				} else {
					Toast.makeText(EditProduct.this,
							getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
			}
		});

		tvNo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/* dialog for camera */
	private Dialog dialog = null;
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
				AppSession appSession = new AppSession(EditProduct.this);
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
					intent = new Intent(EditProduct.this, CustomCamera.class);
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
	
	@SuppressLint("NewApi")
	private void removeImageFromUI(int imageCount) {
		if( imageCount !=1 ) {
			if( SubCategoryDetails.subCategoryDetailsDTOs
					.get(0).getProductImageDtos().size() >= imageCount ) {
				// This will give product image id.
				String	imageID = SubCategoryDetails.subCategoryDetailsDTOs
						.get(0).getProductImageDtos()
						.get(imageCount - 1).getCategoryId();
				
				//Toast.makeText(EditProduct.this, "imageID: " + imageID, 1).show();
				deleteImgIDs.add(imageID);
			}
		}
				
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

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

		}// if( resultCode == resultCode )
	}

	private void go4AviaryEditor(Uri imgURI) {
		Log.i(TAG, "imgURI:  " + imgURI);
		Intent newIntent = new Intent(this, FeatherActivity.class);
		newIntent.setData(imgURI);
		newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET, getResources()
				.getString(R.string.AVIARY_API_SECRET));
		startActivityForResult(newIntent, AVIARY_EDITO);
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
