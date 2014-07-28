package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.it.reloved.adapter.CommentListAdapter;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dao.ProfileDAO;
import com.it.reloved.dto.SubCategoryDetailsDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.Utility;


public class SubCategoryDetails extends RelovedPreference{
	
	private  final String TAG = getClass().getSimpleName();
	private static final int EDIT_PRODUCT = 1;
	private static final int COMMENTS = 2;

	protected View pview = null,pview1 = null;
	protected PopupWindow pw,pw1;
	private Context mContext=this;

	private ImageView ivBackHeader,ivRefresh;
	private TextView tvEditHeader;
	private TextView tvBidPrice,tvChatToBuy,tvUserName,tvTime,tvProductName,tvProductPrice,tvProductLikes,tvCommentsNumber;
	private ImageView ivUser,ivLikeCircle,ivShareCircle,ivChatCircle;
	private ViewFlipper viewFlipper;
	private ViewPager viewPager;
	private ListView commentsListView;
	private Intent intent=null;
	public static String productId="",productImage="";
	private ImageLoader imageLoader=null;
	public static List<SubCategoryDetailsDTO> subCategoryDetailsDTOs = new ArrayList<SubCategoryDetailsDTO>();
	public static int tempBid=0,commentListSize=0;
	public static String bidAmount="";
	private CommentListAdapter commentListAdapter;
	private LinearLayout moreCommentsLayout;	

	//private final GestureDetector detector = new GestureDetector(new MyGestureDetector());
	static int tmp=0;
	public static List<String> listImagesUrl = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcategory_details);

		intent=getIntent();
		if (intent!=null) {
			productId = intent.getStringExtra("productId");
		}
		/* method for initialising init components */
		imageLoader = new ImageLoader(SubCategoryDetails.this);
		//imageLoaderProgressBar=new ImageLoaderProgressBar(SubCategoryDetails.this);
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivRefresh=(ImageView)findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		ivRefresh.setVisibility(View.VISIBLE);
		tvEditHeader=(TextView)findViewById(R.id.tv_edit_header);
		tvEditHeader.setOnClickListener(this);

		tvBidPrice=(TextView)findViewById(R.id.tv_bid_price_subcategory_details);
		tvChatToBuy=(TextView)findViewById(R.id.tv_chat_subcategory_details);
		tvChatToBuy.setOnClickListener(this);
		tvUserName=(TextView)findViewById(R.id.tv_username_subcategory_details);
		tvTime=(TextView)findViewById(R.id.tv_time_subcategory_details);
		tvUserName.setOnClickListener(this);
		tvProductName=(TextView)findViewById(R.id.tv_productname_subcategory_details);
		tvProductPrice=(TextView)findViewById(R.id.tv_price_subcategory_details);
		tvProductLikes=(TextView)findViewById(R.id.tv_likes_subcategory_details);
		tvProductLikes.setOnClickListener(this);

		ivUser=(ImageView)findViewById(R.id.iv_user_subcategory_details);
		ivUser.setOnClickListener(this);
		//	viewFlipper=(ViewFlipper)findViewById(R.id.flipper_subcategory_details);
		viewPager = (ViewPager) findViewById(R.id.view_pager);


		ivLikeCircle=(ImageView)findViewById(R.id.iv_like_circle_subcategory_details);
		ivShareCircle=(ImageView)findViewById(R.id.iv_share_circle_subcategory_details);
		ivChatCircle=(ImageView)findViewById(R.id.iv_chat_circle_subcategory_details);
		ivLikeCircle.setOnClickListener(this);
		ivShareCircle.setOnClickListener(this);
		ivChatCircle.setOnClickListener(this);

		commentsListView=(ListView)findViewById(R.id.list_subcategory_details);

		moreCommentsLayout=(LinearLayout)findViewById(R.id.layout_more_comments);
		moreCommentsLayout.setOnClickListener(this);
		tvCommentsNumber=(TextView)findViewById(R.id.tv_more_comments_subcategory_details);

		if (isNetworkAvailable()) {
			new TaskForSubCategoryDetails().execute();
		} else {
			Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
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


	private class ImagePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return listImagesUrl.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Context context = SubCategoryDetails.this;
			ImageView  imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.white_image);
			imageView.setPadding(2, 2, 2, 2);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			/*imageView.getLayoutParams().height = 300;
			imageView.getLayoutParams().width = 300;*/
			for(int i=0; i<listImagesUrl.size(); i++ ) {	

				imageLoader.DisplayImage(listImagesUrl.get(position), (Activity)mContext, imageView, 0, 0, 
						0 , R.drawable.no_image);
				
				/*imageLoaderProgressBar.DisplayImage((Activity) context,listImagesUrl.get(position),
						imageView, holder.progressBar, 0, 0, 0, R.drawable.no_image,360);*/
			}
			viewPager.setPageMargin(10);
			viewPager.addView(imageView);

			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = null;
					intent = new Intent(SubCategoryDetails.this,ViewPagerActivity.class);
					intent.putExtra("PIC_ID",""+position);
					startActivity(intent);
				}
			});

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	//perform click
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.tv_likes_subcategory_details:
			intent=new Intent(SubCategoryDetails.this, Likes.class);					
			intent.putExtra("ProductId",productId);
			if(subCategoryDetailsDTOs!=null)
				intent.putExtra("UserId",subCategoryDetailsDTOs.get(0).getProductUserId());			
			startActivity(intent);	
			break;
		case R.id.tv_edit_header:
			intent=new Intent(SubCategoryDetails.this, EditProduct.class);	
			intent.putExtra("ProductId", productId);
		//	startActivity(intent);	
			startActivityForResult(intent, EDIT_PRODUCT);
			break;
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskForSubCategoryDetails().execute();
			} else {
				Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.layout_more_comments:
			intent=new Intent(SubCategoryDetails.this, Comments.class);			
			startActivity(intent);			
			break;
		case R.id.tv_chat_subcategory_details:
			String text=tvChatToBuy.getText().toString();
			if(subCategoryDetailsDTOs!=null){
				for (int i = 0; i < subCategoryDetailsDTOs.get(0).getProductImageDtos().size(); i++) {
					Log.i("111", "1111="+subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i)
							.getCategoryName());
					if (subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i)
							.getCategoryName().equals("1")) {
						productImage=subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i)
								.getCategoryImage();
						break;
					}
				}
			}

			if (text.equals("Chat to buy")) {
				AppSession appSession=new AppSession(SubCategoryDetails.this);
				intent=new Intent(SubCategoryDetails.this, ChatToBuy.class);
				intent.putExtra("userName", ""+subCategoryDetailsDTOs.get(0).getProductUserName());
				intent.putExtra("price", ""+subCategoryDetailsDTOs.get(0).getProductPrice());
				intent.putExtra("OfferProductId", ""+productId);
				intent.putExtra("OfferProductName", ""+subCategoryDetailsDTOs.get(0).getProductName());
				intent.putExtra("OfferProductImage", productImage.replace(appSession.getProductBaseUrl(),""));
				intent.putExtra("OfferToUserId", ""+subCategoryDetailsDTOs.get(0).getProductUserId());
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_bottom_up,R.anim.fadeout);
			}else if (text.equals("view offers")) {								
				intent=new Intent(SubCategoryDetails.this, ViewOffers.class);
				intent.putExtra("OfferProductId",productId);
				intent.putExtra("OfferProductName",subCategoryDetailsDTOs.get(0).getProductName());
				intent.putExtra("OfferProductPrice",subCategoryDetailsDTOs.get(0).getProductPrice());
				intent.putExtra("OfferProductImage",productImage);
				startActivity(intent);
			} else if(text.equals("view chat")){
				intent=new Intent(SubCategoryDetails.this, ViewChat.class);
				intent.putExtra("productImage",productImage);
				intent.putExtra("productName",subCategoryDetailsDTOs.get(0).getProductName());
				intent.putExtra("productPrice",subCategoryDetailsDTOs.get(0).getProductPrice());
				intent.putExtra("priceOffered",bidAmount);
				intent.putExtra("dealLocation",subCategoryDetailsDTOs.get(0).getProductAddress());
				intent.putExtra("productUsername",subCategoryDetailsDTOs.get(0).getProductUserName());
				intent.putExtra("productUserId",subCategoryDetailsDTOs.get(0).getProductUserId());
				intent.putExtra("productAddTime", subCategoryDetailsDTOs.get(0).getProductAddDate());
				intent.putExtra("productId",productId);
				intent.putExtra("fromClass","SubCategoryDetails");
				intent.putExtra("productSoldStatus", ""+subCategoryDetailsDTOs.get(0).getProductSoldStatus());
				startActivity(intent);
			}
			break;
		case R.id.tv_username_subcategory_details:
			AppSession appSession=new AppSession(SubCategoryDetails.this);
			if(!subCategoryDetailsDTOs.get(0).getProductUserId().equals(appSession.getUserId())){
				intent=new Intent(SubCategoryDetails.this, ProfileOther.class);
				if(subCategoryDetailsDTOs!=null)
					intent.putExtra("userId",subCategoryDetailsDTOs.get(0).getProductUserId());
				startActivity(intent);
			}
			break;
		case R.id.iv_user_subcategory_details:
			AppSession appSession1=new AppSession(SubCategoryDetails.this);
			if(!subCategoryDetailsDTOs.get(0).getProductUserId().equals(appSession1.getUserId())){
				intent=new Intent(SubCategoryDetails.this, ProfileOther.class);
				if(subCategoryDetailsDTOs!=null)
					intent.putExtra("userId",subCategoryDetailsDTOs.get(0).getProductUserId());
				startActivity(intent);
			}
			break;
		case R.id.iv_like_circle_subcategory_details:
			// ToUserId, ToUserName, LikeProductId, LikeProductName, LikeStatus,ProductImage
			String LikeStatus="";
			String ToUserId=subCategoryDetailsDTOs.get(0).getProductUserId();
			String ToUserName=subCategoryDetailsDTOs.get(0).getProductUserName();
			String LikeProductId=productId;
			String LikeProductName=subCategoryDetailsDTOs.get(0).getProductName();
			AppSession appSession11=new AppSession(SubCategoryDetails.this);
			String mainImg="";
			for (int i = 0; i < subCategoryDetailsDTOs.get(0).getProductImageDtos().size(); i++) {
				if (subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i).getCategoryName().equals("1")) {
					mainImg=subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i).getCategoryImage();
					break;
				} 
			}
			String ProductImage=mainImg.replace(appSession11.getProductBaseUrl(),"");	

			if (subCategoryDetailsDTOs.get(0).getLikeStatus().equals("1")) {
				LikeStatus="0";
			} else {
				LikeStatus="1";
			}

			if (isNetworkAvailable()) {
				new TaskAddLike().execute(ToUserId, ToUserName, LikeProductId, LikeProductName,
						LikeStatus,ProductImage);
			} else {
				Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.iv_share_circle_subcategory_details:
			share("Reloved", subCategoryDetailsDTOs.get(0).getProductWebsiteUrl());
			break;
		case R.id.iv_chat_circle_subcategory_details:
			intent = new Intent(SubCategoryDetails.this, Comments.class);			
			//startActivity(intent);	
			startActivityForResult(intent, COMMENTS);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case EDIT_PRODUCT:
				if (isNetworkAvailable()) {
					new TaskForSubCategoryDetails().execute();
				     } 
					else {
					Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				    }
				break;
			case COMMENTS:
				//Toast.makeText(this, subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size()+"", 1).show();
				// Update comments list view
				commentListAdapter=new CommentListAdapter(SubCategoryDetails.this, R.layout.listitem_comment,
						subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),
						subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size());
				commentsListView.setAdapter(commentListAdapter);
				Utility.getListViewSize(commentsListView);
				commentListAdapter.notifyDataSetChanged();
				break;
			}
			
		}		
	}
	
	//Task for sub category details
	private class TaskForSubCategoryDetails extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				// pd = ProgressDialog.show(Category.this, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(SubCategoryDetails.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {		
				AppSession appSession=new AppSession(SubCategoryDetails.this);
				subCategoryDetailsDTOs=new ProfileDAO(SubCategoryDetails.this).getSubCategoryDetails(appSession.getBaseUrl(),
						getResources().getString(R.string.method_viewProduct), productId);
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
				if (subCategoryDetailsDTOs != null) {
					if (subCategoryDetailsDTOs.get(0).getSuccess().equals("1")) {
						tmp=0;
						tvBidPrice.setText("");						
						tvUserName.setText(""+subCategoryDetailsDTOs.get(0).getProductUserName());
						tvTime.setText(getUpdateTime(subCategoryDetailsDTOs.get(0).getProductAddDate()));
						tvProductName.setText(""+subCategoryDetailsDTOs.get(0).getProductName());
						tvProductPrice.setText("$"+subCategoryDetailsDTOs.get(0).getProductPrice());
						tvProductLikes.setText(""+subCategoryDetailsDTOs.get(0).getProductLikeCount());

						if (subCategoryDetailsDTOs.get(0).getLikeStatus().equals("1")) {
							tvProductLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_bred,
									0, 0, 0);
							ivLikeCircle.setImageResource(R.drawable.like_circle_icon_hover);
						} else {
							tvProductLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_border,
									0, 0, 0);
							ivLikeCircle.setImageResource(R.drawable.like_circle_icon);
						}
						AppSession appSession=new AppSession(SubCategoryDetails.this);	
						// Conditions for main header contents	
						if(subCategoryDetailsDTOs.get(0).getProductSoldStatus().equals("0")){						
							if (appSession.getUserId().equals(subCategoryDetailsDTOs.get(0).getProductUserId())) {
								tvEditHeader.setVisibility(View.VISIBLE);
							} else {
								tvEditHeader.setVisibility(View.GONE);
							}
						}else{
							tvEditHeader.setVisibility(View.GONE);
						}

						// Conditions for sub header contents	
						if(subCategoryDetailsDTOs.get(0).getProductSoldStatus().equals("0")){
							tvChatToBuy.setVisibility(View.VISIBLE);					
							if (appSession.getUserId().equals(subCategoryDetailsDTOs.get(0).getProductUserId())) {
								if (subCategoryDetailsDTOs.get(0).getProductOfferCount().equals("")
										||subCategoryDetailsDTOs.get(0).getProductOfferCount().equals("0")) {
									tvBidPrice.setText("No offers yet");	
									tvChatToBuy.setText("view offers");
									tvChatToBuy.setVisibility(View.GONE);							
								} else {
									tvBidPrice.setText("You have "+subCategoryDetailsDTOs.get(0).getProductOfferCount()+" offer");		
									tvChatToBuy.setText("view offers");
								}													
							} else {
								if(subCategoryDetailsDTOs.get(0).getOfferItemDTOs().size()>0){
									for (int i = 0; i < subCategoryDetailsDTOs.get(0).getOfferItemDTOs().size(); i++) {
										if ( appSession.getUserId().equals(subCategoryDetailsDTOs.get(0).
												getOfferItemDTOs().get(i).getCategoryId())) {									
											tempBid=1;
											bidAmount=subCategoryDetailsDTOs.get(0).
													getOfferItemDTOs().get(i).getCategoryName();
											break;
										} else {
											tempBid=0;
										}	
									}
								}else{
									tempBid=0;
								}
								if (tempBid==1) {
									tvBidPrice.setText("You offered $"+bidAmount);
									tvChatToBuy.setText("view chat");
								} else {
									tvBidPrice.setText("$"+subCategoryDetailsDTOs.get(0).getProductPrice());
									tvChatToBuy.setText("Chat to buy");
								}

							}

						}else{						
							if(appSession.getUserId().equals(subCategoryDetailsDTOs.get(0).getProductUserId())) {
								tvBidPrice.setText("SOLD");	
								tvChatToBuy.setVisibility(View.GONE);
							}							
							else {
								tvBidPrice.setText("You sold this item");	
								tvChatToBuy.setText("view chat");
							}

						}

						if (!subCategoryDetailsDTOs.get(0).getProductUserImage().equals("")) {
							imageLoader.DisplayImage(new AppSession(SubCategoryDetails.this).getUserImageBaseUrl()+"thumb_"+subCategoryDetailsDTOs.get(0).getProductUserImage(),
									(Activity) SubCategoryDetails.this,ivUser,0, 0, 0, R.drawable.default_user);
						}else{
							ivUser.setImageResource(R.drawable.default_user);
						}

						if (subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size() <= 3) {
							commentListSize=subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size();
							moreCommentsLayout.setVisibility(View.GONE);
						} else {
							commentListSize=3;
							moreCommentsLayout.setVisibility(View.VISIBLE);
							tvCommentsNumber.setText("VIEW "+(subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size()-3)
									+" MORE EARLIER COMMENTS");
						}
						commentListAdapter=new CommentListAdapter(SubCategoryDetails.this, R.layout.listitem_comment,
								subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),commentListSize);
						commentsListView.setAdapter(commentListAdapter);
						Utility.getListViewSize(commentsListView);

						if (isNetworkAvailable()) {
							new TaskForImage().execute();
						} else {
							Toast.makeText(SubCategoryDetails.this, getResources().getString(R.string.NETWORK_ERROR),
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(SubCategoryDetails.this,subCategoryDetailsDTOs.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	

	//Task for add likes
	private class TaskAddLike extends AsyncTask<String, Void, Void> {
		//ProgressDialog pd = null;
		String[] arr=new String[3];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				Animation shake = AnimationUtils.loadAnimation(SubCategoryDetails.this, R.anim.wobbling);
				ivLikeCircle.startAnimation(shake);
				ivLikeCircle.setEnabled(false);
				//pd = ProgressDialog.show(ForgotPassword.this, "",getResources().getString(R.string.please_wait));
				//pd = ProgressDialog.show(SubCategoryDetails.this, null, null);
				//pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(SubCategoryDetails.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new OfferDAO(SubCategoryDetails.this).addLikes(appSession.getBaseUrl(), 
						getResources().getString(R.string.method_addLike), appSession.getUserId(),
						userDTOs.get(0).getUserName(), params[0], params[1], params[2], params[3], 
						params[4],userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						params[5]);
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
				ivLikeCircle.clearAnimation();
				ivLikeCircle.setEnabled(true);
				//pd.dismiss();
				if (arr != null) {
					if (arr[0].equals("1")) {
						// Toast.makeText(SubCategoryDetails.this,arr[1],Toast.LENGTH_LONG).show();						
						if (subCategoryDetailsDTOs.get(0).getLikeStatus().equals("1")) {
							subCategoryDetailsDTOs.get(0).setLikeStatus("0");
							tvProductLikes.setText(""+
									(Integer.parseInt(subCategoryDetailsDTOs.get(0).getProductLikeCount())-1));
							subCategoryDetailsDTOs.get(0).setProductLikeCount(""+
									(Integer.parseInt(subCategoryDetailsDTOs.get(0).getProductLikeCount())-1));

							ivLikeCircle.setImageResource(R.drawable.like_circle_icon);
							tvProductLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_border,
									0, 0, 0);
						} else {
							subCategoryDetailsDTOs.get(0).setLikeStatus("1");							
							tvProductLikes.setText(""+
									(Integer.parseInt(subCategoryDetailsDTOs.get(0).getProductLikeCount())+1));
							subCategoryDetailsDTOs.get(0).setProductLikeCount(""+
									(Integer.parseInt(subCategoryDetailsDTOs.get(0).getProductLikeCount())+1));

							ivLikeCircle.setImageResource(R.drawable.like_circle_icon_hover);
							tvProductLikes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_bred,
									0, 0, 0);
						}								
					} else {
						Toast.makeText(SubCategoryDetails.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	

	//Task for image of flippers
	public class TaskForImage extends AsyncTask<Void, Void, Void> {		
		Drawable drawable = null;
		//ProgressDialog pd=null;
		@Override
		protected void onPostExecute(Void result) {
			//pd.dismiss();
			try {
				Log.i(getClass().getSimpleName(), "listImagesUrl size= "+listImagesUrl.size());
				ImagePagerAdapter adapter = new ImagePagerAdapter();
				viewPager.setAdapter(adapter);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private float initialX;

		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				/*pd = ProgressDialog.show(SubCategoryDetails.this, null, null);
				pd.setContentView(R.layout.progressloader);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {				
				listImagesUrl.clear();
				for (int i = 0; i < subCategoryDetailsDTOs.get(0).getProductImageDtos().size(); i++) {
					if(!subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i).getCategoryImage().
							equals("")){
						//without thumb
						listImagesUrl.add(new AppSession(SubCategoryDetails.this).getProductBaseUrl()+subCategoryDetailsDTOs.get(0).getProductImageDtos()
								.get(i).getCategoryImage());
						//with thumb
						//listImagesUrl.add(new AppSession(SubCategoryDetails.this).getProductBaseUrl()+"thumbtwo_"+subCategoryDetailsDTOs.get(0).getProductImageDtos()
						//		.get(i).getCategoryImage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}


	/*//Task for image of flippers
	public class TaskForImage extends AsyncTask<Void, Void, Void> {		
		Drawable drawable = null;
		//ProgressDialog pd=null;
		@Override
		protected void onPostExecute(Void result) {
			//pd.dismiss();
			try {

				Log.i(getClass().getSimpleName(), "listDrawables size= "+listDrawables.size());
				for(int i=0; i<listDrawables.size(); i++ ) {
				     	ImageView imageView = new ImageView(SubCategoryDetails.this);
				     	LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(
								LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
						imageParam.gravity=Gravity.CENTER;
				     	imageView.setId(i);
				     	imageView.setLayoutParams(imageParam); 
				     	imageView.setImageDrawable(listDrawables.get(i));
				     	viewFlipper.addView(imageView);
			 }

			 viewFlipper.setInAnimation(SubCategoryDetails.this, android.R.anim.fade_in);
			 viewFlipper.setOutAnimation(SubCategoryDetails.this, android.R.anim.fade_out);						 
			 viewFlipper.setOnTouchListener(new View.OnTouchListener() {						 				 
			        @Override
			        public boolean onTouch(final View view, final MotionEvent touchevent) {		
			        	detector.onTouchEvent(touchevent);					        				        	
			            return false;	
			        }
			    });

			 viewFlipper.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {	
					if( subCategoryDetailsDTOs!=null){
						if(subCategoryDetailsDTOs.get(0).getProductImageDtos()!=null
								&&subCategoryDetailsDTOs.get(0).getProductImageDtos().size()>0){
							if(viewFlipper.getDisplayedChild()<subCategoryDetailsDTOs.get(0).getProductImageDtos().size()){
								PopupImage(v, new AppSession(SubCategoryDetails.this).getProductBaseUrl()+subCategoryDetailsDTOs.get(0).getProductImageDtos()
										.get(viewFlipper.getDisplayedChild()).getCategoryImage());
							}
						}
					}
				}
			});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private float initialX;

		@Override
		protected void onPreExecute() {		
			super.onPreExecute();			
			try {
				pd = ProgressDialog.show(SubCategoryDetails.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				listDrawables.clear();				
				for (int i = 0; i < subCategoryDetailsDTOs.get(0).getProductImageDtos().size(); i++) {
					if(!subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i).getCategoryImage().
							equals("")){
					drawable = downloadImage(new AppSession(SubCategoryDetails.this).getProductBaseUrl()+"thumbtwo_"+subCategoryDetailsDTOs.get(0).getProductImageDtos()
							.get(i).getCategoryImage());
					listDrawables.add(drawable);}
				}
		} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}*/
	/*class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.i(TAG, "onFling()");
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { Log.i(TAG, "next");
						viewFlipper.setInAnimation(AnimationUtils.loadAnimation(SubCategoryDetails.this,
								R.anim.view_transition_in_left));
						viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(SubCategoryDetails.this,
								R.anim.view_transition_out_left));
						viewFlipper.showNext();
						return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) { Log.i(TAG, "previous");
						viewFlipper.setInAnimation(AnimationUtils.loadAnimation(SubCategoryDetails.this,
								R.anim.view_transition_in_right));
						viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(SubCategoryDetails.this,
								R.anim.view_transition_out_right));
						viewFlipper.showPrevious();
						return true;
				}
				else {								
					if( subCategoryDetailsDTOs!=null){
						if(subCategoryDetailsDTOs.get(0).getProductImageDtos()!=null
								&&subCategoryDetailsDTOs.get(0).getProductImageDtos().size()>0){
							if(viewFlipper.getDisplayedChild()<subCategoryDetailsDTOs.get(0).getProductImageDtos().size()){
								PopupImage(viewFlipper.getChildAt(viewFlipper.getDisplayedChild()), new AppSession(SubCategoryDetails.this).getProductBaseUrl()+subCategoryDetailsDTOs.get(0).getProductImageDtos()
										.get(viewFlipper.getDisplayedChild()).getCategoryImage());
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}


	}
*/
		

	@Override
	protected void onResume() {
		super.onResume();		
		if(tmp==1){
			Log.i("SubCategoryDetails", "onResume");
			if (isNetworkAvailable()) {
				new TaskForSubCategoryDetails().execute();
			} else {
				Toast.makeText(SubCategoryDetails.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
			}
		}
	}


	//popup dialog for large image of flipper
	public void PopupImage(View v,String imageurl) {
		Log.i("PopupImage", "imageurl=="+imageurl);
		if (pview1 == null) {
			LayoutInflater inflater = (LayoutInflater) SubCategoryDetails.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			pview1 = inflater.inflate(R.layout.popup_image,(ViewGroup) findViewById(R.layout.forgot_password));
			if (pview1 != null)
				pw1 = new PopupWindow(pview1,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			pw1.setOutsideTouchable(false);
			pw1.setTouchable(true);
			int[] pos = new int[2];
			v.getLocationInWindow(pos);
			int x = pos[0];
			int y = pos[1];// + v.getHeight();
			// pw.showAtLocation(pview, Gravity.AXIS_SPECIFIED,100,-160);
			pw1.showAtLocation(pview1, Gravity.AXIS_SPECIFIED,x,y);
			//pw.showAtLocation(pview, Gravity.LEFT,0,80);
			//				pw.showAsDropDown(v);
			//pw.update(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

			pw1.setTouchInterceptor(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

						pw1.dismiss();
						pview1=null;
						return true;
					}
					return false;
				}

			});
			ImageView image = (ImageView) pview1.findViewById(R.id.fullimage);
			ImageLoader imageLoader = new ImageLoader(mContext);
			if(!imageurl.equals("")){
				imageLoader.DisplayImage(imageurl, (Activity)mContext, image, 0, 0, 
						0 , R.drawable.no_image);
			}
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pw1.dismiss();
					pview1 = null;
				}
			});
		} else {
			pw1.dismiss();
			pview1 = null;
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (pview1!=null) {
			pw1.dismiss();
			pview1 = null;
		} else {
			super.onBackPressed();
		}

	}
}
