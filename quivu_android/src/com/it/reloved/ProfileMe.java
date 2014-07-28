package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import com.it.reloved.adapter.ProductGridAdapter;
import com.it.reloved.dao.ProfileDAO;
import com.it.reloved.dto.ProfileDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.ExpandableTextView;
import com.it.reloved.utils.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileMe extends RelovedPreference{
	
	private final static String TAG = "ProfileMe";
	private final static String WEBSITE_BASE_URL =  "http://mobilitytesting.com/";  

	private TextView tvName,tvCity, tvHappy, tvNormal, tvSad, tvFollowers, tvFollowing, tvOfferMade,tvItemLiked,
			tvJoinDate,tvVerified,tvPromote,tvWebsite;
	private ExpandableTextView tvDescription;
	private ImageView ivUser;
	private LinearLayout promoteLayout, infoLayout;
	private GridView gridViewProduct;
	private Context context=this;
	private ImageLoader imageLoader;
	private ProductGridAdapter productGridAdapter;
	private Intent intent=null;
	private ImageView ivSettingHeader,ivEditHeader,ivRefresh;
	private List<ProfileDTO> profileDTOs = new ArrayList<ProfileDTO>();
	private LinearLayout layoutSmiley;
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_me);
		
		/* method for initialising init components */
		imageLoader=new ImageLoader(context);
		ivSettingHeader=(ImageView)findViewById(R.id.iv_setting_header);
		ivEditHeader=(ImageView)findViewById(R.id.iv_edit_header);		
		ivSettingHeader.setOnClickListener(this);
		ivEditHeader.setOnClickListener(this);
		ivRefresh = (ImageView) findViewById(R.id.iv_refresh_header);
		ivRefresh.setOnClickListener(this);
		
		ivUser=(ImageView)findViewById(R.id.iv_user_profile_me);
		tvName=(TextView)findViewById(R.id.tv_name_profile_me);
		tvCity=(TextView)findViewById(R.id.tv_city_profile_me);
		tvHappy=(TextView)findViewById(R.id.tv_happy_profile_me);
		tvNormal=(TextView)findViewById(R.id.tv_normal_profile_me);
		tvSad=(TextView)findViewById(R.id.tv_sad_profile_me);
		tvFollowers=(TextView)findViewById(R.id.tv_followers_profile_me);
		tvFollowing=(TextView)findViewById(R.id.tv_following_profile_me);
		tvOfferMade=(TextView)findViewById(R.id.tv_offers_made_profile_me);
		tvItemLiked=(TextView)findViewById(R.id.tv_items_liked_profile_me);
		tvJoinDate=(TextView)findViewById(R.id.tv_joindate_profile_me);
		tvVerified=(TextView)findViewById(R.id.tv_verified_profile_me);
		tvDescription=(ExpandableTextView)findViewById(R.id.tv_desc_profile_me);
		tvPromote=(TextView)findViewById(R.id.tv_promote_profile_me);
		tvWebsite=(TextView)findViewById(R.id.tv_website_profile_me);
		tvWebsite.setOnClickListener(this);
		layoutSmiley=(LinearLayout)findViewById(R.id.layout_smiley);
		layoutSmiley.setOnClickListener(this);
		
		tvPromote.setOnClickListener(this);
		tvFollowers.setOnClickListener(this);
		tvFollowing.setOnClickListener(this);
		tvOfferMade.setOnClickListener(this);
		tvItemLiked.setOnClickListener(this);
		
		tvName.setTypeface(typeface);
		tvCity.setTypeface(typeface);
		tvHappy.setTypeface(typeface);
		tvNormal.setTypeface(typeface);
		tvSad.setTypeface(typeface);
		tvFollowers.setTypeface(typeface);
		tvFollowing.setTypeface(typeface);
		tvOfferMade.setTypeface(typeface);
		tvItemLiked.setTypeface(typeface);
		tvJoinDate.setTypeface(typeface);
		tvVerified.setTypeface(typeface);
		tvDescription.setTypeface(typeface);
		tvPromote.setTypeface(typeface);
		
		promoteLayout = (LinearLayout)findViewById(R.id.layout_promote);	
		infoLayout = (LinearLayout)findViewById(R.id.lin_layout_info);	
		gridViewProduct=(GridView)findViewById(R.id.gridview_profile_me);		
		
		AppSession appSession=new AppSession(ProfileMe.this);
		List<UserDTO> userDTOs=appSession.getConnections();
		tvName.setText(userDTOs.get(0).getUserName());
		
		if(userDTOs.get(0).getUserCity().equals("")||userDTOs.get(0).getUserCity().equals("0"))
			tvCity.setText(userDTOs.get(0).getUserDefaultCity());
		else
			tvCity.setText(userDTOs.get(0).getUserCityName());
		
		Log.i("ProfileMe", "userDTOs.get(0).getUserImage()="+userDTOs.get(0).getUserImage());
		if (!userDTOs.get(0).getUserImage().equals("")) {
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+userDTOs.get(0).getUserImage(), (Activity) context, ivUser,
					0, 0, 0, R.drawable.default_user_image);
		}else{
			ivUser.setImageResource(R.drawable.default_user_image);
		}
				
		if (isNetworkAvailable()) {
			//--------------------------------------------------------------------------------------		
			if ( new AppSession(ProfileMe.this).getJson(AppSession.PROFILE_ME_JSON).equals("") ) {
				// Get data from server
				new TaskForProfile().execute();	
			}
			else if(!(RelovedPreference.getTimeDiferenceMinute(new AppSession(ProfileMe.this).getLastSeenTime(AppSession.PROFILE_ME_SEEN_TIME)) 
					< RelovedPreference.UPDATE_INTERVAL) ) { // if time limit is over
				// Note:- Don't put this condition with above condition with OR operator 
				// this same body repetition is required to get last seen time first time. 
				new TaskForProfile().execute();	
			}
			else {
				// Just show the saved activity data
				//showProfileData( ProfileDAO.parseCategory(AppSession.getJson(AppSession.PROFILE_ME_JSON)) );
				profileDTOs = ProfileDAO.parseCategory(new AppSession(ProfileMe.this).getJson(AppSession.PROFILE_ME_JSON));
				showProfileData( profileDTOs );
			}
			//--------------------------------------------------------------------------------------
		} else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}
		
		//Perform click on grid item
		gridViewProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent=new Intent(ProfileMe.this, SubCategoryDetails.class);
				if(profileDTOs.get(0).getProductItemDTOs()!=null
						&&profileDTOs.get(0).getProductItemDTOs().size()>0)
				intent.putExtra("productId", profileDTOs.get(0).getProductItemDTOs().get(arg2).getCategoryId());
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
	
	@Override
	protected void onRestart() {
		Log.i("onRestart", "onRestart");
		super.onRestart();
		AppSession appSession=new AppSession(ProfileMe.this);
		List<UserDTO> userDTOs = appSession.getConnections();
		tvName.setText(userDTOs.get(0).getUserName());
		
		if(userDTOs.get(0).getUserCity().equals("")||userDTOs.get(0).getUserCity().equals("0"))
			tvCity.setText(userDTOs.get(0).getUserDefaultCity());
		else
			tvCity.setText(userDTOs.get(0).getUserCityName());
		
		if (!userDTOs.get(0).getUserImage().equals("")) {
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+userDTOs.get(0).getUserImage(), (Activity) context, ivUser,
					0, 0, 0, R.drawable.default_user_image);
		}else{
			ivUser.setImageResource(R.drawable.default_user_image);
		}
		
		/*if (isNetworkAvailable()) {
			new TaskForProfile().execute();
		} else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}*/
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i("onPause", "onPause");
		super.onPause();
		AppSession appSession=new AppSession(ProfileMe.this);
		List<UserDTO> userDTOs=appSession.getConnections();
		tvName.setText(userDTOs.get(0).getUserName());
		
		if(userDTOs.get(0).getUserCity().equals("")||userDTOs.get(0).getUserCity().equals("0"))
			tvCity.setText(userDTOs.get(0).getUserDefaultCity());
		else
			tvCity.setText(userDTOs.get(0).getUserCityName());
		
		if (!userDTOs.get(0).getUserImage().equals("")) {
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+userDTOs.get(0).getUserImage(), (Activity) context, ivUser,
					0, 0, 0, R.drawable.default_user_image);
		}else{
			ivUser.setImageResource(R.drawable.default_user_image);
		}
		/*if (isNetworkAvailable()) {
			new TaskForProfile().execute();
		} else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}*/
	}
	
	
	//Perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_promote_profile_me:
			intent =new Intent(ProfileMe.this, Promote.class);
			startActivity(intent);
			break;
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskForProfile().execute();
			} else {
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.iv_setting_header:
			intent=new Intent(ProfileMe.this, Settings.class);
			startActivity(intent);
			break;
		case R.id.iv_edit_header:
			intent=new Intent(ProfileMe.this, EditProfile.class);
			intent.putExtra("email_verif_status", profileDTOs.get(0).getUserEmailVerificationStatus() );
			startActivity(intent);
			break;
		case R.id.tv_followers_profile_me:
			if( !profileDTOs.get(0).getUserFollowerCount().equals("0") ){
				intent = new Intent(ProfileMe.this, Followers.class);
				intent.putExtra("type", "followers");
				AppSession appSession = new AppSession(ProfileMe.this);
				intent.putExtra("UserId",appSession.getUserId());
				startActivity(intent);
			}			
			break;
		case R.id.tv_following_profile_me:
			if( !profileDTOs.get(0).getUserFollowingCount().equals("0") ){
			intent = new Intent(ProfileMe.this, Followers.class);
			intent.putExtra("type", "following");
			AppSession appSession1 = new AppSession(ProfileMe.this);
			intent.putExtra("UserId",appSession1.getUserId());
			startActivity(intent);
			}
			break;
		case R.id.tv_offers_made_profile_me:
			if( !profileDTOs.get(0).getOfferMadeByMe().equals("0") ){
			intent=new Intent(ProfileMe.this, StuffLiked.class);
			AppSession appSession2=new AppSession(ProfileMe.this);
			intent.putExtra("UserId", appSession2.getUserId());
			intent.putExtra("fromClass", "Offer");
			startActivity(intent);
			}
			break;
		case R.id.tv_items_liked_profile_me:
			if( !profileDTOs.get(0).getStuffLikes().equals("0") ){
			intent=new Intent(ProfileMe.this, StuffLiked.class);
			AppSession appSession3=new AppSession(ProfileMe.this);
			intent.putExtra("UserId", appSession3.getUserId());
			intent.putExtra("fromClass", "Stuff");
			startActivity(intent);
			}
			break;
		case R.id.layout_smiley:
			intent=new Intent(ProfileMe.this, Feedback.class);
			AppSession appSession4=new AppSession(ProfileMe.this);
			List<UserDTO> userDTOs=appSession4.getConnections();
			intent.putExtra("UserId",appSession4.getUserId());
			if(userDTOs!=null){
			intent.putExtra("UserName",userDTOs.get(0).getUserName());
			intent.putExtra("UserImage",userDTOs.get(0).getUserImage());
			}
			startActivity(intent);
			break;
		case R.id.tv_website_profile_me:			
			// Toast.makeText(ProfileMe.this, "OK", 1).show();
			if (isNetworkAvailable()) {
				if(!profileDTOs.get(0).getUserWebsiteUrl().equals("")) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(profileDTOs.get(0).getUserWebsiteUrl()));
				startActivity(browserIntent);
				}
			} else {
				Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
			}
			
			break;
		default:
			break;
		}
	}	
	
	//Task for Profile
	private class TaskForProfile extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(ProfileMe.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {			
				AppSession appSession=new AppSession(ProfileMe.this);
				profileDTOs = new ProfileDAO(ProfileMe.this).getProfileDetails(ProfileMe.this, appSession.getBaseUrl(),getResources().getString(R.string.method_profile_me),
						appSession.getUserId(),"");
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
				if (profileDTOs != null) {
					showProfileData(profileDTOs);	
					} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	//Show profile data
	private void showProfileData( List<ProfileDTO> dtos ) {
		if( dtos!=null && dtos.size() != 0  ){
		if (dtos.get(0).getSuccess().equals("1")) {
			tvHappy.setText(dtos.get(0).getUserPositiveFeedBCount());
			tvNormal.setText(dtos.get(0).getUserNeutralFeedBCount());
			tvSad.setText(dtos.get(0).getUserNegativeFeedBCount());
			tvFollowers.setText(dtos.get(0).getUserFollowerCount()+" followers");
			tvFollowing.setText(dtos.get(0).getUserFollowingCount()+" following");
			String inShort = dtos.get(0).getUserWebsiteUrl().replace(WEBSITE_BASE_URL, "");
			tvWebsite.setText(inShort);
			
			tvJoinDate.setText("Join date "+dtos.get(0).getUserRegistationDate());
			tvDescription.setText(dtos.get(0).getUserBio());
			
			if(dtos.get(0).getUserEmailVerificationStatus().equals("1")) {
				tvVerified.setText("Verified");
				//invisible view
				infoLayout.setVisibility(View.GONE);
			}
			else {
				tvVerified.setText("Not Verified");
				if(dtos.get(0).getProductItemDTOs().size() != 0) {
					//visible view
					infoLayout.setVisibility(View.VISIBLE);
				}
				else {
					//invisible view
					infoLayout.setVisibility(View.GONE);
				}
			}
			
			productGridAdapter = new ProductGridAdapter(context, R.layout.grid_item_product,
					dtos.get(0).getProductItemDTOs());
			gridViewProduct.setAdapter(productGridAdapter);
			Utility.getGridViewSize(gridViewProduct);			
			
		} else {
			Toast.makeText(context,dtos.get(0).getMsg(),
					Toast.LENGTH_LONG).show();
		}
		}
	
	}
}
