package com.it.reloved.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.ChatToBuy;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.SubCategory;
import com.it.reloved.SubCategoryDetails;
import com.it.reloved.adapter.FollowersListAdapter.ViewHolder;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class SubCategoryGridAdapter extends ArrayAdapter<CategoryItemDTO> {

	private  final String TAG = getClass().getSimpleName();
	ImageLoader imageLoader;
	ImageLoaderProgressBar imageLoaderProgressBar;
	 Context context;
	int resource;
	LayoutInflater inflater;
	List<CategoryItemDTO> list;
	int poss=0;
	//private TaskAddLike taskAddLike = null;

	public SubCategoryGridAdapter(Context context, int resource,
			List<CategoryItemDTO> list) {
		super(context, resource, list);
		this.context = context;
		this.resource = resource;
		this.list = list;
		inflater = ((Activity) context).getLayoutInflater();
		imageLoader=new ImageLoader(context);
		imageLoaderProgressBar=new ImageLoaderProgressBar(context);
	}

	public static class ViewHolder {
		ImageView ivProduct,ivShare,ivUser;
		TextView tvProductName,tvPrice,tvLike,tvUserName,tvTime;
		ProgressBar progressBar;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//View row = convertView;
		ViewHolder holder;			   		
		try {			
			if (convertView == null) {
		        convertView = inflater.inflate(resource, parent, false);
			//row = inflater.inflate(resource, parent, false);
			holder = new ViewHolder();
			/* method for initialising init components */
			holder.ivProduct = (ImageView) convertView.findViewById(R.id.iv_product_item);
			holder.ivShare = (ImageView) convertView.findViewById(R.id.iv_share_product_item);
			holder.ivUser = (ImageView) convertView.findViewById(R.id.iv_user_product_item);
			
			holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_name_product_item);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price_product_item);
			holder.tvLike = (TextView) convertView.findViewById(R.id.tv_like_product_item);
			holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username_product_item);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time_product_item);
			holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			Log.i("SubCategoryGridAdapter", "likeStatus"+list.get(position).getLikeStatus());			
			if (list.get(position).getLikeStatus().equals("1")) {
				holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_heart_icon,0,0,0);
				Log.i("SubCategoryGridAdapter", "likeStatus1111="+list.get(position).getLikeStatus());
			} else {
				holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_white_icon,0,0,0);
			}
			
			convertView.setTag(holder);
		    }//if (convertView == null) 
			else {
				holder = (ViewHolder) convertView.getTag();
		    }	
			
			holder.tvProductName.setTypeface(RelovedPreference.typeface);
			holder.tvPrice.setTypeface(RelovedPreference.typeface);
			holder.tvLike.setTypeface(RelovedPreference.typeface);
			holder.tvUserName.setTypeface(RelovedPreference.typeface);
			holder.tvTime.setTypeface(RelovedPreference.typeface);
			
			final CategoryItemDTO itm = list.get(position);
			holder.tvProductName.setText(itm.getCategoryName());
			holder.tvPrice.setText("$"+itm.getCategoryPrice());
			holder.tvLike.setText(itm.getCategoryLikeCount());
			holder.tvUserName.setText(itm.getProductUserName());
			holder.tvTime.setText(RelovedPreference.getUpdateTime(itm.getProductAddDate()));
			
			holder.tvLike.setTag(holder);
			
			if (!itm.getCategoryImage().equals("")) {
				/*imageLoader.DisplayImage(new AppSession(context).getProductBaseUrl()+itm.getCategoryImage(), (Activity) context, holder.ivProduct,
						0, 0, 0, R.drawable.no_image);*/
				imageLoaderProgressBar.DisplayImage((Activity) context,new AppSession(context).getProductBaseUrl()+"thumbtwo_"+itm.getCategoryImage(),
						holder.ivProduct, holder.progressBar, 0, 0, 0, R.drawable.no_image,360);
			}else{
				holder.ivProduct.setImageResource(R.drawable.no_image);
			}
			
			if (!itm.getProductUserImage().equals("")) {
				imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+"thumb_"+itm.getProductUserImage(), (Activity) context, holder.ivUser,
						0, 0, 0, R.drawable.default_user);
			}else{
				holder.ivUser.setImageResource(R.drawable.default_user);
			}
			//click on share
			holder.ivShare.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					share("Reloved", itm.getProductWebsiteUrl());
				}
			});
			//click on like
			holder.tvLike.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {									
					poss=position;
					// ToUserId, ToUserName, LikeProductId, LikeProductName, LikeStatus,ProductImage
					String LikeStatus="";
					String ToUserId=list.get(position).getProductUserId();
					String ToUserName=list.get(position).getProductUserName();
					String LikeProductId=list.get(position).getCategoryId();
					String LikeProductName=list.get(position).getCategoryName();
					AppSession appSession1=new AppSession(context);
					String ProductImage=list.get(position).getCategoryImage().
							replace(appSession1.getProductBaseUrl(),"");									
					if (list.get(position).getLikeStatus().equals("1")) {
						LikeStatus="0";
					} else {
						LikeStatus="1";
					}
					if (isNetworkAvailable()) {
						ViewHolder holder1 = (ViewHolder)v.getTag();
						new TaskAddLike(holder1).execute(ToUserId, ToUserName, LikeProductId, LikeProductName,
								LikeStatus,ProductImage);																	
					} else {
						Toast.makeText(context,context.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}
			});
			
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;

	}
	//Task for add likes
	private  class TaskAddLike extends AsyncTask<String, Void, Void> {
		//ProgressDialog pd = null;
		String[] arr=new String[3];
		ViewHolder vh;
		
		TaskAddLike(ViewHolder _vh){
			vh = _vh;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				Animation shake = AnimationUtils.loadAnimation(context, R.anim.wobbling);
				vh.tvLike.startAnimation(shake);
				vh.tvLike.setEnabled(false);			
				//pd = ProgressDialog.show(ForgotPassword.this, "",getResources().getString(R.string.please_wait));
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
				arr=new OfferDAO(context).addLikes(appSession.getBaseUrl(), 
						context.getResources().getString(R.string.method_addLike), appSession.getUserId(),
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
				//pd.dismiss();
				vh.tvLike.clearAnimation();
				vh.tvLike.setEnabled(true);
				if (arr != null) {
					if (arr[0].equals("1")) {
						//Toast.makeText(context,arr[1],Toast.LENGTH_LONG).show();
						if (list.get(poss).getLikeStatus().equals("1")) {				
							vh.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_white_icon,0,0, 0);		
							list.get(poss).setLikeStatus("0");
							list.get(poss).setCategoryLikeCount(""+
									(Integer.parseInt(list.get(poss).getCategoryLikeCount())-1) );
						} else {	
							vh.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_heart_icon,0,0, 0);
							list.get(poss).setLikeStatus("1");
							list.get(poss).setCategoryLikeCount(""+
									(Integer.parseInt(list.get(poss).getCategoryLikeCount())+1) );
						}
						vh.tvLike.setText(list.get(poss).getCategoryLikeCount());
					//	notifyDataSetChanged();						
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
	
	/*public String getUpdateTime(String updateTime) {
		Date currentDate = new Date();
		long diff, second, minute, hour, day, year;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			// dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
			Date updateDate = dateFormat.parse(updateTime);
			diff = currentDate.getTime() - updateDate.getTime();
			second = diff / 1000;
			minute = second / 60;
			hour = minute / 60;
			day = hour / 24;
			year = day / 365;
			if (second <= 59) {
				if (second <= 1)
					return "a moment ago";
				return second + " seconds ago";
			} else if (minute <= 59) {
				if (minute == 1)
					return minute + " minute ago";
				else
					return minute + " minutes ago";
			} else if (hour <= 23) {
				if (hour == 1)
					return hour + " hour ago";
				else
					return hour + " hours ago";
			} else if (day <= 364) {
				if (day == 1)
					return day + " day ago";
				else
					return day + " days ago";
			} else {
				if (year == 1)
					return year + " year ago";
				else
					return year + " year ago";
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return " ";
	}
	*/
	
	//method for sharing
	public void share(String subject,String text) {
		Log.i(TAG, "website link: "+text);
	     final Intent intent = new Intent(Intent.ACTION_SEND);
	     intent.setType("text/plain");
	     intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	     intent.putExtra(Intent.EXTRA_TEXT, text);
	     context.startActivity(Intent.createChooser(intent, "Share this via"));
	}
	//method for network check
	public boolean isNetworkAvailable() {
		try {
			ConnectivityManager cm;
			cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);	
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
