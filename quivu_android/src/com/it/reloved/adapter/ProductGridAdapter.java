package com.it.reloved.adapter;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.adapter.SubCategoryGridAdapter.ViewHolder;
import com.it.reloved.dao.OfferDAO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class ProductGridAdapter extends ArrayAdapter<CategoryItemDTO> {

	ImageLoader imageLoader;
	Context context;
	int resource;
	LayoutInflater inflater;
	List<CategoryItemDTO> list;
	int poss=0;
	ImageLoaderProgressBar imageLoaderProgressBar;

	public ProductGridAdapter(Context context, int resource,
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
		ImageView ivProduct,ivShare,ivSold;
		TextView tvProductName,tvPrice,tvLike;
		ProgressBar progressBar;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//View row = convertView;
		ViewHolder holder;			   		
		try {			
			if (convertView == null) {
		        convertView = inflater.inflate(resource, parent, false);
			// row = inflater.inflate(resource, parent, false);
			holder = new ViewHolder();
			/* method for initialising init components */
			holder.ivProduct = (ImageView) convertView.findViewById(R.id.iv_product_item);
			holder.ivSold = (ImageView) convertView.findViewById(R.id.iv_sold_item);
			holder.ivShare = (ImageView) convertView.findViewById(R.id.iv_share_product_item);
			holder.tvProductName = (TextView) convertView.findViewById(R.id.tv_name_product_item);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price_product_item);
			holder.tvLike = (TextView) convertView.findViewById(R.id.tv_like_product_item);
			holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			holder.tvLike.setTag(holder);
			convertView.setTag(holder);
		    }//if (convertView == null) 
			else {
				holder = (ViewHolder) convertView.getTag();
		    }				
			
			holder.tvProductName.setTypeface(RelovedPreference.typeface);
			holder.tvPrice.setTypeface(RelovedPreference.typeface);
			holder.tvLike.setTypeface(RelovedPreference.typeface);
			
			if (list.get(position).getProductSoldStatus().equals("1")) {
				holder.ivSold.setVisibility(View.VISIBLE);
			} else {
				holder.ivSold.setVisibility(View.GONE);
			}
			
			if (list.get(position).getLikeStatus().equals("1")) {
				holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_heart_icon,0,0, 0);
			} else {
				holder.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon,0,0, 0);
			}
			
			final CategoryItemDTO itm = list.get(position);
			holder.tvProductName.setText(itm.getCategoryName());
			holder.tvPrice.setText("$"+itm.getCategoryPrice());
			holder.tvLike.setText(itm.getCategoryLikeCount());
			
			if (!itm.getCategoryImage().equals("")) {
			/*	imageLoader.DisplayImage(new AppSession(context).getProductBaseUrl()+"thumbtwo_"+itm.getCategoryImage(),
					(Activity) context, holder.ivProduct,0, 0, 0, R.drawable.no_image);*/
				
				imageLoaderProgressBar.DisplayImage((Activity) context,
						new AppSession(context).getProductBaseUrl()+"thumbtwo_"+itm.getCategoryImage(),
						holder.ivProduct, holder.progressBar, 0, 0, 0, R.drawable.no_image,360);
			}else{
				holder.ivProduct.setImageResource(R.drawable.no_image);
			}
					
			//click on share
			holder.ivShare.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
	
	public void share(String subject,String text) {
	     final Intent intent = new Intent(Intent.ACTION_SEND);
	     intent.setType("text/plain");
	     intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	     intent.putExtra(Intent.EXTRA_TEXT, text);
	     context.startActivity(Intent.createChooser(intent, "Share this via"));
	}
	
	//Task for add like
	private class TaskAddLike extends AsyncTask<String, Void, Void> {
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
						Toast.makeText(context,arr[1],Toast.LENGTH_LONG).show();
						if (list.get(poss).getLikeStatus().equals("1")) {
							vh.tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon,0,0, 0);
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
						//notifyDataSetChanged();						
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
