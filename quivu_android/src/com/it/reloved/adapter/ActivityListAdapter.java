package com.it.reloved.adapter;

import java.util.List;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.it.reloved.ProfileOther;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.adapter.CategoryGridAdapter.ViewHolder;
import com.it.reloved.dto.ActivityItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.NonUnderlinedClickbleSpan;


public class ActivityListAdapter extends ArrayAdapter<ActivityItemDTO> {

	Context context;
	int layoutResourceId;
	List<ActivityItemDTO> data = null;
	ActivityItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null,imageLoader1 = null;
	LayoutInflater inflater;
	ImageLoaderProgressBar imageLoaderProgressBar;

	public ActivityListAdapter(Context context, int layoutResourceId,
			List<ActivityItemDTO> data) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			imageLoader1 = new ImageLoader(context);
			imageLoaderProgressBar=new ImageLoaderProgressBar(context);
			inflater = ((Activity) context).getLayoutInflater();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static class ViewHolder {
		ImageView userImageView,productImageView;
		TextView tvUsername,tvTime;		
		ProgressBar progressBar;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		//View row = null;
		ViewHolder holder;			   				
		try {
			if (convertView == null) {
		    convertView = inflater.inflate(R.layout.listitem_activity, parent, false);
			holder = new ViewHolder();
			inflater = ((Activity) context).getLayoutInflater();
			/* method for initialising init components */
			holder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_activity);
			holder.productImageView = (ImageView) convertView.findViewById(R.id.iv_product_activity);			
			holder.tvUsername = (TextView) convertView.findViewById(R.id.tv_username_activity);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time_activity);
			holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			convertView.setTag(holder);
		    }//if (convertView == null) 
			else {
				holder = (ViewHolder) convertView.getTag();
		    }				
			dto = data.get(position);
									
			//holder.tvUsername.setText(dto.getActivityFromUserName()+" "+dto.getActivityMessage());
			holder.tvTime.setText(""+RelovedPreference.getUpdateTime(dto.getActivityTime()));					
			holder.tvUsername.setText(dto.getActivityFromUserName()+" "+dto.getActivityMessage(),BufferType.SPANNABLE);
			//holder.tvUsername.setMovementMethod(LinkMovementMethod.getInstance());
			Spannable spannableName=(Spannable) holder.tvUsername.getText();
			/*spannableName.setSpan(new NonUnderlinedClickbleSpan(){
	            @Override
	            public void onClick(View widget) {
	            	Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getActivityFromUserId());
					context.startActivity(intent);
	            }
	        }, 0,dto.getActivityFromUserName().length(), 0);*/
			spannableName.setSpan(new ForegroundColorSpan(Color.BLACK),0, 
					dto.getActivityFromUserName().length(), 0);
			
			if(!dto.getActivityFromUserImage().equals("")){
//			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+dto.getActivityFromUserImage(),
//					(Activity)context,holder.userImageView,0,0,color.transparent,R.drawable.default_user_image);
		
			imageLoaderProgressBar.DisplayImage((Activity) context,
					new AppSession(context).getUserImageBaseUrl()+dto.getActivityFromUserImage(),
					holder.userImageView, holder.progressBar, 0, 0, 0, R.drawable.default_user,360);
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user);
				holder.progressBar.setVisibility(View.INVISIBLE);
			}
			
			if(!dto.getActivityProductImage().equals("")){
				imageLoader1.DisplayImage(new AppSession(context).getProductBaseUrl()+"thumbone_"+dto.getActivityProductImage(),
						(Activity)context,holder.productImageView,0,0,color.transparent,R.drawable.no_image);
			}else{
				holder.productImageView.setImageResource(R.drawable.no_image);
			}
			
			if (dto.getActivityTypeId().equals("1")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_like_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("2")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_arrow_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("3")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_message_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("4")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_small_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("5")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_small_arrow,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("6")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_chat_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("7")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_small_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);
				if(!dto.getActivityFeedbackImage().equals("positive_icon.png")){
					holder.productImageView.setImageResource(R.drawable.postive_icon_large);
				}else if(!dto.getActivityFeedbackImage().equals("neutral_icon.png")){
					holder.productImageView.setImageResource(R.drawable.neutral_icon_large);
				}else if(!dto.getActivityFeedbackImage().equals("negative_icon.png")){
					holder.productImageView.setImageResource(R.drawable.negative_icon_large);
				}
				
			}else if (dto.getActivityTypeId().equals("8")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_small_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.INVISIBLE);				
			}else if (dto.getActivityTypeId().equals("9")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_like_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.INVISIBLE);				
			}else if (dto.getActivityTypeId().equals("10")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_chat_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("11")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.small_chat_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("12")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_small_icon,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.VISIBLE);				
			}else if (dto.getActivityTypeId().equals("13")) {
				holder.tvUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete_icon_small,0,0,0);
				holder.userImageView.setVisibility(View.VISIBLE);
				holder.productImageView.setVisibility(View.INVISIBLE);				
			}
			
			
			/*holder.tvUsername.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getActivityFromUserId());
					context.startActivity(intent);
				}
			});*/
			
			//click on userimage
			holder.userImageView.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getActivityFromUserId());
					context.startActivity(intent);
				}
			});	

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	

}
