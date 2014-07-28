package com.it.reloved.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.MessageItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class MessageListAdapter extends ArrayAdapter<MessageItemDTO> {

	Context context;
	int layoutResourceId;
	List<MessageItemDTO> data = null;
	MessageItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null;
	//ImageLoaderProgressBar imageLoaderProgressBar;

	public MessageListAdapter(Context context, int layoutResourceId,
			List<MessageItemDTO> data) {
		super(context, layoutResourceId, data);
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			//imageLoaderProgressBar=new ImageLoaderProgressBar(context);
			Log.i("MessageListAdapter", ""+data.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		ImageView userImageView,ivSent;
		TextView tvUsername, tvComment, tvTime;
		//ProgressBar progressBar;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;
		try {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.listitem_msg, parent, false);
			/* method for initialising init components */
			holder.userImageView = (ImageView) row.findViewById(R.id.iv_user_listitem_msg);
			holder.ivSent = (ImageView) row.findViewById(R.id.iv_sent_listitem_msg);
			holder.tvUsername = (TextView) row.findViewById(R.id.tv_username_listitem_msg);
			holder.tvComment = (TextView) row.findViewById(R.id.tv_comment_listitem_msg);
			holder.tvTime = (TextView) row.findViewById(R.id.tv_time_listitem_msg);
			//holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			dto = data.get(position);
			holder.tvUsername.setText(dto.getMessageFromUserName());
			holder.tvTime.setText(RelovedPreference.getUpdateTime(dto.getMessageAddTime()));
			holder.tvComment.setText(dto.getMessage());
			
			Log.i("MessageListAdapter", "MessageType="+dto.getMessageType());
			if (dto.getMessageType().equals("1")) {
				holder.tvComment.setVisibility(View.GONE);
				holder.ivSent.setVisibility(View.VISIBLE);
				Log.i("MessageListAdapter", "Message="+dto.getMessage());
				if (!dto.getMessage().equals("")){
					AppSession appSession=new AppSession(context);
					imageLoader.DisplayImage(appSession.getMessageImageBaseUrl()+dto.getMessage(),(Activity)context,
					holder.ivSent, 6, 4,context.getResources().getColor(R.color.gray),R.drawable.default_user_image);
				}
			} else {
				holder.ivSent.setVisibility(View.GONE);
				holder.tvComment.setVisibility(View.VISIBLE);
			}

			if (!dto.getMessageFromUserImage().equals("")){
				imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getMessageFromUserImage(),(Activity) context, holder.userImageView,
					6, 4, context.getResources().getColor(R.color.gray),R.drawable.default_user);
				
				/*imageLoaderProgressBar.DisplayImage((Activity) context,
						new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getMessageFromUserImage(),
						holder.userImageView, holder.progressBar, 0, 0, 0, R.drawable.default_user,360);*/
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;

	}	

}
