package com.it.reloved.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.it.reloved.ProfileOther;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.CommentItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.NonUnderlinedClickbleSpan;

public class CommentListAdapter extends BaseAdapter {

	Context context;
	int layoutResourceId;
	List<CommentItemDTO> data = null;
	CommentItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null;
	int size=0;
	int len=0;
	//ImageLoaderProgressBar imageLoaderProgressBar;

	public CommentListAdapter(Context context, int layoutResourceId,
			List<CommentItemDTO> data,int size) {
		super();
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			//imageLoaderProgressBar=new ImageLoaderProgressBar(context);
			this.size=size;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		ImageView userImageView;
		TextView tvUsername,  tvComment, tvTime;	
		//ProgressBar progressBar;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;
		try {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.listitem_comment, parent, false);
			/* method for initialising init components */
			holder.userImageView = (ImageView) row.findViewById(R.id.iv_user_listitem_comment);
			holder.tvUsername = (TextView) row.findViewById(R.id.tv_username_listitem_comment);
			holder.tvComment = (TextView) row.findViewById(R.id.tv_comment_listitem_comment);
			holder.tvTime = (TextView) row.findViewById(R.id.tv_time_listitem_comment);
			//holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			dto = data.get(position);
			holder.tvUsername.setText(dto.getCommentUserName());			
			holder.tvTime.setText(RelovedPreference.getUpdateTime(dto.getCommentTime()));		
			
			if (dto.getCommentMessage().length()>0) {
				String firstChar=""+dto.getCommentMessage().charAt(0);
				Log.i("CommentListAdapter", "firstChar="+firstChar);
				if (firstChar.equals("@")) {
					len=dto.getCommentMessage().substring(0, dto.getCommentMessage().indexOf(" ")).length();
				} else {
					len=0;
				}
			} 			
			holder.tvComment.setText(dto.getCommentMessage(),BufferType.SPANNABLE);
			//holder.tvComment.setMovementMethod(LinkMovementMethod.getInstance());
			Spannable spannableName=(Spannable) holder.tvComment.getText();
			/*spannableName.setSpan(new NonUnderlinedClickbleSpan(){
	            @Override
	            public void onClick(View widget) {
	            	Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getCommentToReplyUserId());
					context.startActivity(intent);
	            }
	        }, 0,len, 0);*/
			spannableName.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_green)),
					0,len, 0);
			
			if(!dto.getCommentUserImage().equals("")){
			imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getCommentUserImage(),
				(Activity)context,holder.userImageView,6,4,context.getResources().getColor(R.color.gray),
				R.drawable.default_user);
			
			/*imageLoaderProgressBar.DisplayImage((Activity) context,
					new AppSession(context).getUserImageBaseUrl()+"thumb_"+dto.getCommentUserImage(),
					holder.userImageView, holder.progressBar, 0, 0, 0, R.drawable.default_user,360);*/
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user);
			}
			//click on user image
			holder.userImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getCommentUserId());
					context.startActivity(intent);
				}
			});
			
			//click on username
			holder.tvUsername.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getCommentUserId());
					context.startActivity(intent);
				}
			});
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
