package com.it.reloved.adapter;

import java.util.List;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.internal.co;
import com.it.reloved.ProfileOther;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.SubCategoryDetails;
import com.it.reloved.dto.OfferItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class OffersListAdapter extends ArrayAdapter<OfferItemDTO> {

	Context context;
	int layoutResourceId;
	List<OfferItemDTO> data = null;
	OfferItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null;
	//ImageLoaderProgressBar imageLoaderProgressBar;

	public OffersListAdapter(Context context, int layoutResourceId,
			List<OfferItemDTO> data) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			//imageLoaderProgressBar=new ImageLoaderProgressBar(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		ImageView userImageView;
		TextView tvUsername,  tvPrice, tvDesc,tvTime;
		//ProgressBar progressBar;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;

		try {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.offer_list_item, parent, false);
			/* method for initialising init components */
			holder.userImageView = (ImageView) row.findViewById(R.id.iv_user_offer_listitem);
			holder.tvUsername = (TextView) row.findViewById(R.id.tv_username_offer_listitem);
			holder.tvPrice = (TextView) row.findViewById(R.id.tv_price_offer_listitem);
			holder.tvDesc = (TextView) row.findViewById(R.id.tv_desc_offer_listitem);
			holder.tvTime = (TextView) row.findViewById(R.id.tv_time_offer_listitem);
			//holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			dto = data.get(position);

			holder.tvUsername.setText(""+dto.getOfferUserName());
			holder.tvPrice.setText("$ "+dto.getOfferAmount());
			holder.tvDesc.setText(""+dto.getOfferMessage());
			holder.tvTime.setText(""+RelovedPreference.getUpdateTime(dto.getOfferAddTime()));
			
			if(!dto.getOfferUserImage().equals("")){
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getOfferUserImage(),
				(Activity)context,holder.userImageView,0,0,color.transparent,R.drawable.default_user_image);
			
			/*imageLoaderProgressBar.DisplayImage((Activity) context,
					new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getOfferUserImage(),
					holder.userImageView, holder.progressBar, 0, 0, 0, R.drawable.default_user_image,360);*/
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user_image);
			}
			
			//click on username
			holder.tvUsername.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getOfferUserId());
					context.startActivity(intent);
				}
			});
			//click on userimage
			holder.userImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getOfferUserId());
					context.startActivity(intent);
				}
			});
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;

	}

}
