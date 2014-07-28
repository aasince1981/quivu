package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import com.it.reloved.adapter.ProductGridAdapter;
import com.it.reloved.dao.FollowersDAO;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileOther extends RelovedPreference{

	private TextView tvName,tvCity,tvHappy,tvNormal,tvSad,tvFollowers,tvFollowing,
			tvJoinDate,tvVerified,tvFollow;
	private ExpandableTextView tvDescription;
	private ImageView ivUser;
	private LinearLayout promoteLayout;
	private GridView gridViewProduct;
	private Context context=this;
	private ImageLoader imageLoader;
	private ProductGridAdapter productGridAdapter;
	private Intent intent=null;
	private ImageView ivBackHeader,ivBlockHeader;
	private String userId="";
	private LinearLayout layoutSmiley;
	private List<ProfileDTO> profileDTOs=new ArrayList<ProfileDTO>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_other);
		imageLoader=new ImageLoader(context);
		
		intent=getIntent();
		if (intent!=null) {
			userId=intent.getStringExtra("userId");
			Log.i("ProfileOther", "userId==="+userId);
		}
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBlockHeader=(ImageView)findViewById(R.id.iv_block_header);		
		ivBackHeader.setOnClickListener(this);
		ivBlockHeader.setOnClickListener(this);
		
		ivUser=(ImageView)findViewById(R.id.iv_user_profile_me);
		tvName=(TextView)findViewById(R.id.tv_name_profile_me);
		tvCity=(TextView)findViewById(R.id.tv_city_profile_me);
		tvHappy=(TextView)findViewById(R.id.tv_happy_profile_me);
		tvNormal=(TextView)findViewById(R.id.tv_normal_profile_me);
		tvSad=(TextView)findViewById(R.id.tv_sad_profile_me);
		tvFollowers=(TextView)findViewById(R.id.tv_followers_profile_me);
		tvFollowing=(TextView)findViewById(R.id.tv_following_profile_me);		
		tvJoinDate=(TextView)findViewById(R.id.tv_joindate_profile_me);
		tvVerified=(TextView)findViewById(R.id.tv_verified_profile_me);
		tvDescription=(ExpandableTextView)findViewById(R.id.tv_desc_profile_me);
		tvFollow=(TextView)findViewById(R.id.tv_promote_profile_me);
		layoutSmiley=(LinearLayout)findViewById(R.id.layout_smiley);
		layoutSmiley.setOnClickListener(this);
		tvFollowers.setOnClickListener(this);
		tvFollowing.setOnClickListener(this);
		tvFollow.setOnClickListener(this);
		
		tvName.setTypeface(typeface);
		tvCity.setTypeface(typeface);
		tvHappy.setTypeface(typeface);
		tvNormal.setTypeface(typeface);
		tvSad.setTypeface(typeface);
		tvFollowers.setTypeface(typeface);
		tvFollowing.setTypeface(typeface);		
		tvJoinDate.setTypeface(typeface);
		tvVerified.setTypeface(typeface);
		tvDescription.setTypeface(typeface);
		tvFollow.setTypeface(typeface);
		
		promoteLayout=(LinearLayout)findViewById(R.id.layout_promote);		
		gridViewProduct=(GridView)findViewById(R.id.gridview_profile_me);	
				
		if (isNetworkAvailable()) {
			new TaskForProfile().execute();
		} else {
			Toast.makeText(context,getString(R.string.NETWORK_ERROR),Toast.LENGTH_LONG).show();
		}
		
		/*perform click on grid item*/
		gridViewProduct.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent=new Intent(ProfileOther.this, SubCategoryDetails.class);
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
	
	//perform click on grid item
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.iv_block_header:
			break;
		case R.id.layout_smiley:
			intent=new Intent(ProfileOther.this, Feedback.class);			
			if(profileDTOs!=null){
				intent.putExtra("UserId", profileDTOs.get(0).getUserId());
				intent.putExtra("UserName",profileDTOs.get(0).getUserName());
				intent.putExtra("UserImage",profileDTOs.get(0).getUserImage());
			}
			startActivity(intent);
			break;
		case R.id.tv_followers_profile_me:
			intent=new Intent(ProfileOther.this, Followers.class);
			intent.putExtra("type", "followers");
			intent.putExtra("UserId",profileDTOs.get(0).getUserId());
			startActivity(intent);
			break;
		case R.id.tv_following_profile_me:
			intent=new Intent(ProfileOther.this, Followers.class);
			intent.putExtra("type", "following");
			intent.putExtra("UserId",profileDTOs.get(0).getUserId());
			startActivity(intent);
			break;
		case R.id.tv_promote_profile_me:
			AppSession appSession=new AppSession(context);
			String ToUserId=""+profileDTOs.get(0).getUserId();
			String ToUserName=""+profileDTOs.get(0).getUserName();
			String ToUserImage=""+profileDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),"");
			String FollowStatus="";
			if (tvFollow.getText().toString().equals("Follow")) {
				FollowStatus="1";
			} else {
				FollowStatus="0";
			}			
			if (isNetworkAvailable()) {
				new TaskAddFollow().execute(ToUserId, ToUserName, ToUserImage, FollowStatus);
			} else {
				Toast.makeText(context,context.getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}	
	
	/*Task for Profile data*/
	private class TaskForProfile extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(context, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(ProfileOther.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {			
				AppSession appSession=new AppSession(ProfileOther.this);
				profileDTOs=new ProfileDAO(ProfileOther.this).getProfileDetails(ProfileOther.this, appSession.getBaseUrl()
						,getResources().getString(R.string.method_profile_me),userId,appSession.getUserId());
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
				if (profileDTOs != null) {
					if (profileDTOs.get(0).getSuccess().equals("1")) {
						tvHappy.setText(profileDTOs.get(0).getUserPositiveFeedBCount());
						tvNormal.setText(profileDTOs.get(0).getUserNeutralFeedBCount());
						tvSad.setText(profileDTOs.get(0).getUserNegativeFeedBCount());
						tvFollowers.setText(profileDTOs.get(0).getUserFollowerCount()+" followers");
						tvFollowing.setText(profileDTOs.get(0).getUserFollowingCount()+" following");
						
						tvJoinDate.setText("Join date "+profileDTOs.get(0).getUserRegistationDate());
						tvDescription.setText(profileDTOs.get(0).getUserBio());
						
						tvName.setText(profileDTOs.get(0).getUserName());
						
						if (profileDTOs.get(0).getFollowStatus().equals("0")) {
							tvFollow.setText("Follow");
						} else {
							tvFollow.setText("Following");
						}
						
						if(profileDTOs.get(0).getUserCity().equals("")||profileDTOs.get(0).getUserCity().equals("0"))
							tvCity.setText(profileDTOs.get(0).getUserDefaultCity());
						else
							tvCity.setText(profileDTOs.get(0).getUserCityName());
						
						Log.i("ProfileMe", "userDTOs.get(0).getUserImage()="+profileDTOs.get(0).getUserImage());
						if (!profileDTOs.get(0).getUserImage().equals("")) {
							imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+profileDTOs.get(0).getUserImage(), (Activity) context, ivUser,
									0, 0, 0, R.drawable.default_user_image);
						}else{
							ivUser.setImageResource(R.drawable.default_user_image);
						}
						
						if(profileDTOs.get(0).getUserEmailVerificationStatus().equals("1"))
							tvVerified.setText("Verified");
						else
							tvVerified.setText("Not Verified");
						
						productGridAdapter=new ProductGridAdapter(context, R.layout.grid_item_product,
								profileDTOs.get(0).getProductItemDTOs());
						gridViewProduct.setAdapter(productGridAdapter);
						Utility.getGridViewSize(gridViewProduct);
					} else {
						Toast.makeText(context,profileDTOs.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	/*Task for add follow*/
	private class TaskAddFollow extends AsyncTask<String, Void, Void> {		
		//ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				tvFollow.setText("Loading");
				tvFollow.setClickable(false);
				//pd = ProgressDialog.show(context, null, null);
				//pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(context);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new FollowersDAO(context).addFollowers(appSession.getBaseUrl(),
						context.getResources().getString(R.string.method_addFollow),
						appSession.getUserId(), userDTOs.get(0).getUserName(),
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						params[0], params[1], params[2], params[3]);
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
				if (arr != null) {
					if (arr[0].equals("1")) {
						Toast.makeText(context,arr[1],Toast.LENGTH_LONG).show();
						if (profileDTOs.get(0).getFollowStatus().equals("1")) {
							profileDTOs.get(0).setFollowStatus("0");	
							tvFollow.setText("Follow");
						} else {
							profileDTOs.get(0).setFollowStatus("1");
							tvFollow.setText("Following");
						}	
						tvFollow.setClickable(true);
					} else {
						Toast.makeText(context,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(context,context.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
