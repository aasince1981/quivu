package com.it.reloved.adapter;

import java.util.List;

import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.EditProduct;
import com.it.reloved.ProfileOther;
import com.it.reloved.R;
import com.it.reloved.SubCategoryDetails;
import com.it.reloved.dao.FollowersDAO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class FollowersListAdapter extends ArrayAdapter<CategoryItemDTO> {

	Context context;
	int layoutResourceId;
	List<CategoryItemDTO> data = null;
	CategoryItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null;
	String type="";
	int poss=0;
	//ImageLoaderProgressBar imageLoaderProgressBar;

	public FollowersListAdapter(Context context, int layoutResourceId,
			List<CategoryItemDTO> data,String type) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			//imageLoaderProgressBar=new ImageLoaderProgressBar(context);
			this.type=type;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		ImageView userImageView;
		TextView tvUsername,tvDesc,tvTime,tvFollow;
		//ProgressBar progressBar;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;
	//	if (convertView == null) {
		try {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.listitem_follower, parent, false);
			/* method for initialising init components */
			holder.userImageView = (ImageView) row.findViewById(R.id.iv_user__followers);
			holder.tvUsername = (TextView) row.findViewById(R.id.tv_username_followers);			
			holder.tvDesc = (TextView) row.findViewById(R.id.tv_desc_followers);
			holder.tvTime = (TextView) row.findViewById(R.id.tv_time_followers);
			holder.tvFollow = (TextView) row.findViewById(R.id.tv_follow_followers);
			holder.tvFollow.setTag(holder);
			//holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			dto = data.get(position);
			holder.tvUsername.setText(dto.getCategoryName());			
			holder.tvDesc.setText("");
			holder.tvTime.setText("");
			
			if (dto.getFollowStatus().equals("0")) {
				holder.tvFollow.setText("Follow");
			} else {
				holder.tvFollow.setText("Following");
			}
			AppSession appSession=new AppSession(context);
			if (dto.getCategoryId().equals(appSession.getUserId())) {
				holder.tvFollow.setVisibility(View.GONE);
			} else {
				holder.tvFollow.setVisibility(View.VISIBLE);			}
			
			if(!dto.getCategoryImage().equals("")){
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getCategoryImage(),(Activity)context,holder.userImageView,0,0,color.transparent,
					R.drawable.default_user_image);
			
			/*imageLoaderProgressBar.DisplayImage((Activity) context,
					new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getCategoryImage(),
					holder.userImageView, holder.progressBar, 0, 0, 0, R.drawable.default_user_image,360);*/
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user_image);
			}
			
			//click on follow
			holder.tvFollow.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					poss=position;
					// ToUserId, ToUserName, ToUserImage, FollowStatus
					AppSession appSession=new AppSession(context);
					String ToUserId=""+dto.getCategoryId();
					String ToUserName=""+dto.getCategoryName();
					String ToUserImage=""+dto.getCategoryImage().replace(appSession.getUserImageBaseUrl(),"");
					String FollowStatus="";
					if (holder.tvFollow.getText().toString().equals("Follow")) {
						FollowStatus="1";
					} else {
						FollowStatus="0";
					}
					
					if (isNetworkAvailable()) {
						ViewHolder holder1 = (ViewHolder)v.getTag();			
		                //Access the Textview from holder1 like below		              						
						new TaskAddFollow(holder1).execute(ToUserId, ToUserName, ToUserImage, FollowStatus);
					} else {
						Toast.makeText(context,context.getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}
				}
			});
			
			//click on username
			holder.tvUsername.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AppSession appSession=new AppSession(context);
					if(!dto.getCategoryId().equals(appSession.getUserId())){
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",data.get(position).getCategoryId());
					context.startActivity(intent);
					}
				}
			});
			
			//click on userimage
			holder.userImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AppSession appSession=new AppSession(context);
					if(!dto.getCategoryId().equals(appSession.getUserId())){					
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",data.get(position).getCategoryId());
					context.startActivity(intent);
					}
				}
			});
			

		} catch (Exception e) {
			e.printStackTrace();
		}
/*		convertView.setTag(holder);
	} else {
        holder = (ViewHolder) convertView.getTag();
    }*/
		
		return row;
	}
	
	//Task for add follow
	private class TaskAddFollow extends AsyncTask<String, Void, Void> {
		// ProgressDialog pd = null;
		String[] arr=new String[2];
		ViewHolder vh = null;
		
		TaskAddFollow(ViewHolder _vh) {
			vh = _vh;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				vh.tvFollow.setText("Loading..");
				vh.tvFollow.setClickable(false);
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
				vh.tvFollow.setClickable(true);
				//pd.dismiss();
				if (arr != null) {
					if (arr[0].equals("1")) {
					//	Toast.makeText(context,arr[1],Toast.LENGTH_LONG).show();
						if (data.get(poss).getFollowStatus().equals("1")) {
							data.get(poss).setFollowStatus("0");							
						} else {
							data.get(poss).setFollowStatus("1");							
						}
						notifyDataSetChanged();						
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
	
	//method for cheking network
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
