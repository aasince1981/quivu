package com.it.reloved;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.it.reloved.adapter.MessageListAdapter;
import com.it.reloved.dao.CommentDAO;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.MessageDTO;
import com.it.reloved.dto.MessageItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.NonUnderlinedClickbleSpan;
import com.it.reloved.utils.Utility;

public class ViewChat extends RelovedPreference{
	
	private ImageView ivBackHeader,ivRefresh;
	private ImageLoader imageLoader=null;
	private TextView tvProductName,tvProductPrice,tvPriceOffered,tvDealLocation,tvCancelOffer,tvMarkasSoldOffer,
			tvUserOfferedPrice,tvTime;
	private ImageView ivProduct,ivPlus,ivSend;
	private ListView listViewMsg;
	private EditText etMessage;
	private Intent intent=null;
	private String productImage="",productName="",productPrice="",priceOffered="",
			dealLocation="",productUsername="",productUserId="",productAddTime="",
			productId="",productSoldStatus="";
	private MessageListAdapter messageListAdapter=null;
	private String msgStr="";
	private List<MessageDTO> messageDTOs=new ArrayList<MessageDTO>();
	
	private int PICK_IMAGE = 1;
	private int CAPTURE_IMAGE = 2;
	private int PIC_CROP = 3;
	private Uri camaraUri = null;
	private Bitmap bmpLogo = null;
	private String imagepath = "";
	private String fromUserId="",toUserId="",fromClass="";
	
	@Override
	public void onBackPressed() {
		if( dialog != null ) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.view_chat);
		
		intent=getIntent();
		if (intent!=null) {
			productImage=intent.getStringExtra("productImage");
			productName=intent.getStringExtra("productName");
			productPrice=intent.getStringExtra("productPrice");
			priceOffered=intent.getStringExtra("priceOffered");
			dealLocation=intent.getStringExtra("dealLocation");
			productUsername=intent.getStringExtra("productUsername");
			productUserId=intent.getStringExtra("productUserId");
			Log.i("productUserId", "productUserId="+productUserId);
			productAddTime=intent.getStringExtra("productAddTime");
			productId=intent.getStringExtra("productId");
			fromClass=intent.getStringExtra("fromClass");
			productSoldStatus=intent.getStringExtra("productSoldStatus");
		}
		/* method for initialising init components */
		imageLoader=new ImageLoader(ViewChat.this);
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh=(ImageView)findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		
		tvProductName=(TextView)findViewById(R.id.tv_productname_viewchat);
		tvProductPrice=(TextView)findViewById(R.id.tv_productprice_viewchat);
		tvPriceOffered=(TextView)findViewById(R.id.tv_priceoffered_viewchat);
		tvDealLocation=(TextView)findViewById(R.id.tv_deal_location_viewchat);
		tvCancelOffer=(TextView)findViewById(R.id.tv_cancel_offer_viewchat);
		tvUserOfferedPrice=(TextView)findViewById(R.id.tv_userofferd_price_viewchat);
		tvTime=(TextView)findViewById(R.id.tv_time_viewchat);
		tvCancelOffer.setOnClickListener(this);
		
		tvMarkasSoldOffer=(TextView)findViewById(R.id.tv_markassold_offer_viewchat);
		tvMarkasSoldOffer.setOnClickListener(this);
		
		AppSession appSession=new AppSession(ViewChat.this);
		if (productSoldStatus.equals("1")) {
			tvMarkasSoldOffer.setVisibility(View.GONE);
			tvCancelOffer.setVisibility(View.VISIBLE);
			tvCancelOffer.setText("Sold");
			tvCancelOffer.setEnabled(false);
		} else {
			if (fromClass.equals("ViewOffers")) {
				tvMarkasSoldOffer.setVisibility(View.VISIBLE);
				tvMarkasSoldOffer.setText("Mark as sold to " + productUsername);
				tvCancelOffer.setVisibility(View.GONE);
			} else {
				tvMarkasSoldOffer.setVisibility(View.GONE);
				tvCancelOffer.setVisibility(View.VISIBLE);
			}
		}
		//setting values
		tvProductName.setText(""+productName);
		tvProductPrice.setText("$"+productPrice);
		tvPriceOffered.setText("$"+priceOffered);
		tvDealLocation.setText(""+dealLocation);		
		tvTime.setText(getUpdateTime(productAddTime));
		
		tvUserOfferedPrice.setText(productUsername+" offered $"+priceOffered,BufferType.SPANNABLE);
		tvUserOfferedPrice.setMovementMethod(LinkMovementMethod.getInstance());
		Spannable spannableName=(Spannable) tvUserOfferedPrice.getText();
		spannableName.setSpan(new NonUnderlinedClickbleSpan(){
            @Override
            public void onClick(View widget) {            	
            }
        }, 0,productUsername.length(), 0);
		spannableName.setSpan(new ForegroundColorSpan(Color.BLACK),0,productUsername.length(), 0);
		
		ivProduct=(ImageView)findViewById(R.id.iv_product_viewchat);
		ivPlus=(ImageView)findViewById(R.id.iv_plus_viewchat);
		ivSend=(ImageView)findViewById(R.id.iv_send_viewchat);
		ivPlus.setOnClickListener(this);
		ivSend.setOnClickListener(this);
		
		listViewMsg=(ListView)findViewById(R.id.list_viewchat);
		etMessage=(EditText)findViewById(R.id.et_chatmsg_viewchat);
		
		if (!productImage.equals("")) {
			imageLoader.DisplayImage(new AppSession(ViewChat.this).getProductBaseUrl()+productImage,
					(Activity) ViewChat.this, ivProduct,0, 0, 0, R.drawable.no_image);
		}else{
			ivProduct.setImageResource(R.drawable.no_image);
		}
		
		if (isNetworkAvailable()) {
			new TaskGetMessage().execute();
		} else {
			Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
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
	
	//perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.tv_markassold_offer_viewchat:
			if (isNetworkAvailable()) {
				new TaskMarkSoldOffer().execute();
			} else {
				Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskGetMessage().execute();
			} else {
				Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.tv_cancel_offer_viewchat:
			DailogCancelOffer(ViewChat.this, "Cancel Offer", "Are you sure you want to cancel your offer?");
			break;
		case R.id.iv_plus_viewchat:
			dailogBoxCamera(ViewChat.this);
			break;
		case R.id.iv_send_viewchat:
			msgStr=etMessage.getText().toString();
			if (!msgStr.equals("")) {
				if (isNetworkAvailable()) {
					new TaskAddMessage().execute("0");
					AppSession appSession=new AppSession(ViewChat.this);
					List<UserDTO> userDTOs=appSession.getConnections();	
					Calendar c = Calendar.getInstance();
			        System.out.println("Current time => "+c.getTime());
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        String formattedDate = df.format(c.getTime());
					etMessage.setText("");
					MessageItemDTO messageItemDTO=new MessageItemDTO("", appSession.getUserId(),
							userDTOs.get(0).getUserName(), userDTOs.get(0).getUserImage(),
							productUserId, msgStr, ""+formattedDate, "0");					
					messageDTOs.get(0).getMessageItemDTOs().add(messageItemDTO);
					messageListAdapter=new MessageListAdapter(ViewChat.this,
							R.layout.listitem_msg, messageDTOs.get(0).getMessageItemDTOs());
					listViewMsg.setAdapter(messageListAdapter);
					Utility.getListViewSize(listViewMsg);
					messageListAdapter.notifyDataSetChanged();
					listViewMsg.setSelection(messageDTOs.get(0).getMessageItemDTOs().size() - 1);
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
			}
			break;
		default:
			break;
		}
	}
	
	//Task for getting message
	private class TaskGetMessage extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(ViewChat.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ViewChat.this);				
				messageDTOs=new CommentDAO(ViewChat.this).getMessages(appSession.getBaseUrl(),
				getResources().getString(R.string.method_getMessages), appSession.getUserId(),
				productUserId,productId);				
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
				if (messageDTOs != null) {
					if (messageDTOs.get(0).getSuccess().equals("1")) {
						messageListAdapter=new MessageListAdapter(ViewChat.this,
								R.layout.listitem_msg, messageDTOs.get(0).getMessageItemDTOs());
						listViewMsg.setAdapter(messageListAdapter);
						Utility.getListViewSize(listViewMsg);
						listViewMsg.setSelection(messageDTOs.get(0).getMessageItemDTOs().size() - 1);
						
					} else {
						//Toast.makeText(ViewChat.this,messageDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//Task for add message
	private class TaskAddMessage extends AsyncTask<String, Void, Void> {
		//ProgressDialog pd = null;
		List<MessageDTO> messageDTOsNew=new ArrayList<MessageDTO>();		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				/*pd = ProgressDialog.show(ViewChat.this, null, null);
				pd.setContentView(R.layout.progressloader);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(ViewChat.this);	
				List<UserDTO> userDTOs=appSession.getConnections();				
				messageDTOsNew=new CommentDAO(ViewChat.this).addMessages(appSession.getBaseUrl(),
						getResources().getString(R.string.method_addMessages), appSession.getUserId(),
						userDTOs.get(0).getUserName(), 
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						productUserId, msgStr, params[0],productId,productImage);
							
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
				//pd.dismiss();
				if (messageDTOsNew != null) {
					if (messageDTOsNew.get(0).getSuccess().equals("1")) {
						etMessage.setText("");
						/*messageDTOs.get(0).getMessageItemDTOs().addAll(messageDTOsNew.get(0).getMessageItemDTOs());
						messageListAdapter=new MessageListAdapter(ViewChat.this,
								R.layout.listitem_msg, messageDTOs.get(0).getMessageItemDTOs());
						listViewMsg.setAdapter(messageListAdapter);
						Utility.getListViewSize(listViewMsg);
						messageListAdapter.notifyDataSetChanged();*/					
					} else {
						Toast.makeText(ViewChat.this,messageDTOsNew.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//Task for cancel offer
	private class TaskCancelOffer extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(ViewChat.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ViewChat.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new OfferDAO(ViewChat.this).cancelOffer(appSession.getBaseUrl(),
						getResources().getString(R.string.method_cancelOffer),fromUserId,
						userDTOs.get(0).getUserName(), 
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						toUserId, productId,
						productName, productImage.replace(appSession.getProductBaseUrl(),""),
						appSession.getUserId());					
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
						Toast.makeText(ViewChat.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(ViewChat.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//Task for mark as sold offer
	private class TaskMarkSoldOffer extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(ViewChat.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(ViewChat.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new OfferDAO(ViewChat.this).markAsSoldOffer(appSession.getBaseUrl(),
						getResources().getString(R.string.method_acceptOffer), appSession.getUserId(), 
						userDTOs.get(0).getUserName(), userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""), 
						productUserId, productId, productName, 
						productImage.replace(appSession.getProductBaseUrl(),""));								
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
						Toast.makeText(ViewChat.this,arr[1],Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(ViewChat.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//dialog for cancel offer
	private void DailogCancelOffer(Context mCtx,final String title,final String msg) {
		final Dialog dialog = new Dialog(mCtx);
		Window window = dialog.getWindow();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_yesno);
		window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		TextView tvTitle = (TextView) window.findViewById(R.id.title_yesno);
		TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
		TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
		TextView tvNo = (TextView) window.findViewById(R.id.no_yesno);
		tvTitle.setText(title);
		tvMsg.setText(msg);
		
		tvYes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {		
				
				if (fromClass.equals("ViewOffers")) {
					fromUserId=""+productUserId;
					toUserId=""+productUserId;
				} else {
					fromUserId=""+new AppSession(ViewChat.this).getUserId();
					toUserId=""+productUserId;
				}
				
				Log.i("tvYes", "fromUserId="+fromUserId+"---toUserId="+toUserId);
				if (isNetworkAvailable()) {
					new TaskCancelOffer().execute();
				} else {
					Toast.makeText(ViewChat.this,getString(R.string.NETWORK_ERROR),
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
	
	//dialog for camera and gallery
	private Dialog dialog = null;
	private void dailogBoxCamera(Context context) {
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
		delete.setVisibility(View.GONE);
		View bottoum = (View) window.findViewById(R.id.bottom_divider);
		bottoum.setVisibility(View.GONE);

		camera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				camaraUri = RelovedPreference.setImageUri();
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, camaraUri);
				startActivityForResult(intent1, CAPTURE_IMAGE);
				dialog.dismiss();
				dialog = null;
			}
		});

		galary.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent();
				intent1.setType("image/*");
				intent1.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent1, ""),PICK_IMAGE);
				dialog.dismiss();
				dialog = null;
			}
		});

		dialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Bitmap bmp;
		if (requestCode == PIC_CROP) {
			try {
				Log.v(getClass().getSimpleName(), "PICK_IMAGE crop 111 try");
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				bmpLogo = extras.getParcelable("data");

				if (bmpLogo != null) {
					//ivUser.setImageBitmap(bmpLogo);
					imagepath = RelovedPreference.getFilePath(bmpLogo,"image");
					Log.v("onActivityResult", "imagepath=" + imagepath);
					msgStr=imagepath;
					if (isNetworkAvailable()) {
						new TaskAddMessage().execute("1");
					} else {
						Toast.makeText(ViewChat.this,getResources().getString(R.string.NETWORK_ERROR)
								,Toast.LENGTH_LONG).show();	
					}
					
				} else {
					try {
						Log.v(getClass().getSimpleName(),
								"PICK_IMAGE crop 222 try");
						RelovedPreference.selectedImagePath = getAbsolutePath(data
								.getData());
						bmpLogo = RelovedPreference.decodeFile(new File(RelovedPreference.selectedImagePath),
								200,200);
						Log.i("bmpLogo", "" + bmpLogo);
						//ivUser.setImageBitmap(bmpLogo);
						imagepath = RelovedPreference.selectedImagePath;
						Log.v("onActivityResult", "imagepath=" + imagepath);
						msgStr=imagepath;
						if (isNetworkAvailable()) {
							new TaskAddMessage().execute("1");
						} else {
							Toast.makeText(ViewChat.this,getResources().getString(R.string.NETWORK_ERROR)
									,Toast.LENGTH_LONG).show();	
						}
					} catch (Exception e) {
						e.printStackTrace();
						try {
							Log.v(getClass().getSimpleName(),
									"PICK_IMAGE crop 333 try");
							RelovedPreference.selectedImagePath = RelovedPreference.getImagePath();
							bmpLogo = RelovedPreference.decodeFile(new File(RelovedPreference.selectedImagePath)
												,200,200);
							Log.i("bmpLogo", "" + bmpLogo);
							//ivUser.setImageBitmap(bmpLogo);
							imagepath = RelovedPreference.selectedImagePath;
							Log.v("onActivityResult", "imagepath=" + imagepath);
							msgStr=imagepath;
							if (isNetworkAvailable()) {
								new TaskAddMessage().execute("1");
							} else {
								Toast.makeText(ViewChat.this,getResources().getString(R.string.NETWORK_ERROR),
										Toast.LENGTH_LONG).show();	
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}

			} catch (Exception e) {
				Log.v(getClass().getSimpleName(), " Exception catch" + e);
				// TODO: handle exception
			}
		} else if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == PICK_IMAGE) {
				Log.i("PICK_IMAGE", "PICK_IMAGE");

				try {
					Uri uriImage = data.getData();
					performCrop(uriImage);
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (requestCode == CAPTURE_IMAGE) {
				Log.i("CAPTURE_IMAGE", "CAPTURE_IMAGE");
				try {
					if (camaraUri != null)
						performCrop(camaraUri);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

		}
	}
	
	//method for cropping image
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
