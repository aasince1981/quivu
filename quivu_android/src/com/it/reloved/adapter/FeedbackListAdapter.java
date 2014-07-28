package com.it.reloved.adapter;

import java.util.List;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.it.reloved.AddFeedback;
import com.it.reloved.AddReply;
import com.it.reloved.EditProduct;
import com.it.reloved.Feedback;
import com.it.reloved.ProfileOther;
import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.FeedbackItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.NonUnderlinedClickbleSpan;

public class FeedbackListAdapter extends ArrayAdapter<FeedbackItemDTO> {

	Context context;
	int layoutResourceId;
	List<FeedbackItemDTO> data = null;
	FeedbackItemDTO dto;
	ViewHolder holder = null;
	ImageLoader imageLoader = null;
	int type=0,poss=0;
	boolean flag=false;

	public FeedbackListAdapter(Context context, int layoutResourceId,
			List<FeedbackItemDTO> data,int type) {
		super(context, layoutResourceId, data);
		// TODO Auto-generated constructor stub
		try {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
			imageLoader = new ImageLoader(context);
			this.type=type;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ViewHolder {
		ImageView userImageView,ivType,ivEditFeedback,ivReplyFeedback,ivEditReply;
		TextView tvFeedback, tvFeedbackTime, tvEditedFeedback, tvReply,tvTimeReply;
		LinearLayout replyLayout;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = null;

		try {
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.listitem_feedback, parent, false);
			/* method for initialising init components */
			holder.userImageView = (ImageView) row.findViewById(R.id.iv_user_feedback);
			holder.ivType= (ImageView) row.findViewById(R.id.iv_type_feedback);
			holder.ivEditFeedback= (ImageView) row.findViewById(R.id.iv_edit_feedback);
			holder.ivReplyFeedback= (ImageView) row.findViewById(R.id.iv_reply_feedback);
			holder.ivEditReply= (ImageView) row.findViewById(R.id.iv_edit_reply_feedback);		
			holder.tvFeedback = (TextView) row.findViewById(R.id.tv_feedback_feedback);
			holder.tvFeedbackTime = (TextView) row.findViewById(R.id.tv_time_feedback);
			holder.tvEditedFeedback = (TextView) row.findViewById(R.id.tv_edited_feedback);
			holder.tvReply = (TextView) row.findViewById(R.id.tv_reply_feedback);
			holder.tvTimeReply = (TextView) row.findViewById(R.id.tv_replytime_feedback);			
			holder.replyLayout=(LinearLayout)row.findViewById(R.id.layout_reply);

			dto = data.get(position);		
			holder.tvFeedback.setText(dto.getFeedbackByUserName()+" "+dto.getFeedbackDescription(),BufferType.SPANNABLE);
			holder.tvFeedback.setMovementMethod(LinkMovementMethod.getInstance());
			Spannable spannableName=(Spannable) holder.tvFeedback.getText();
			spannableName.setSpan(new NonUnderlinedClickbleSpan(){
	            @Override
	            public void onClick(View widget) {
	            	Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getFeedbackByUserId());
					context.startActivity(intent);
	            }
	        }, 0,dto.getFeedbackByUserName().length(), 0);
			spannableName.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_green)),0, 
					dto.getFeedbackByUserName().length(), 0);
			
			holder.tvReply.setText(dto.getFeedbackToUserName()+" "+dto.getFeedbackReplyMessage(),BufferType.SPANNABLE);
			holder.tvReply.setMovementMethod(LinkMovementMethod.getInstance());
			Spannable spannableName1=(Spannable) holder.tvReply.getText();
			spannableName1.setSpan(new NonUnderlinedClickbleSpan(){
	            @Override
	            public void onClick(View widget) {
	            	Intent intent=new Intent(context, ProfileOther.class);					
					intent.putExtra("userId",dto.getFeedbackToUserId());
					context.startActivity(intent);
	            }
	        }, 0,dto.getFeedbackToUserName().length(), 0);
			spannableName1.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.app_green)),0, 
					dto.getFeedbackToUserName().length(), 0);
	
			if (dto.getFeedbackExperience().equals("1")) {
				holder.ivType.setImageResource(R.drawable.positive_icon);
			}else if (dto.getFeedbackExperience().equals("2")) {
				holder.ivType.setImageResource(R.drawable.negative_icon);
			}else {
				holder.ivType.setImageResource(R.drawable.neutral_ico);
			}
			
			AppSession appSession=new AppSession(context);
			if (dto.getFeedbackToUserId().equals(appSession.getUserId())) {
				holder.ivEditReply.setVisibility(View.VISIBLE);
				holder.ivReplyFeedback.setVisibility(View.GONE);
			} else {
				holder.ivEditReply.setVisibility(View.GONE);				
				holder.ivReplyFeedback.setVisibility(View.VISIBLE);
			}
			
			if (dto.getFeedbackByUserId().equals(appSession.getUserId())) {
				holder.ivEditFeedback.setVisibility(View.VISIBLE);
				holder.ivReplyFeedback.setVisibility(View.GONE);
			} else {
				holder.ivEditFeedback.setVisibility(View.GONE);
				holder.ivReplyFeedback.setVisibility(View.VISIBLE);
			}
			
			if (dto.getFeedbackEditStatus().equals("1")) {
				holder.tvEditedFeedback.setVisibility(View.VISIBLE);
				holder.tvFeedbackTime.setText(RelovedPreference.getUpdateTime(dto.getFeedbackEditTime()));
			} else {
				holder.tvEditedFeedback.setVisibility(View.GONE);
				holder.tvFeedbackTime.setText(RelovedPreference.getUpdateTime(dto.getFeedbackTime()));
			}			
			
			if (dto.getFeedbackReplyStatus().equals("1")) {
				holder.replyLayout.setVisibility(View.VISIBLE);
				holder.tvTimeReply.setText(RelovedPreference.getUpdateTime(dto.getFeedbackEditReplyTime()));
			} else {
				holder.replyLayout.setVisibility(View.GONE);
				holder.tvTimeReply.setText(RelovedPreference.getUpdateTime(dto.getFeedbackReplyTime()));
			}					
			
			if (!dto.getFeedbackByUserImage().equals("")){
				imageLoader.DisplayImage(new AppSession(context).getUserImageBaseUrl()+dto.getFeedbackByUserImage(),
					(Activity) context, holder.userImageView,0, 0,color.transparent, R.drawable.default_user_image);
			}else{
				holder.userImageView.setImageResource(R.drawable.default_user_image);
			}
			
			//click on edit feedback
			holder.ivEditFeedback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, AddFeedback.class);
					intent.putExtra("UserId", dto.getFeedbackToUserId());
					intent.putExtra("UserName",dto.getFeedbackToUserName());
					intent.putExtra("UserImage",dto.getFeedbackToUserImage());
					intent.putExtra("FeedbackId", dto.getFeedbackId());
					intent.putExtra("feedbackType",dto.getFeedbackType());
					intent.putExtra("feedbackExp",dto.getFeedbackExperience());
					intent.putExtra("feedback",dto.getFeedbackDescription());
					context.startActivity(intent);
				}
			});
			//click on reply fedback
			holder.ivReplyFeedback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*if(data!=null){
						for (int i = 0; i < data.size(); i++) {
							String uId=data.get(i).getFeedbackByUserId();
							if (uId.equals(new AppSession(context).getUserId())) {
								poss=i;
								flag=true;
								break;
							}
						}
					}*/
					if(!dto.getFeedbackReplyStatus().equals("1")){
						Intent intent=new Intent(context, AddReply.class);
						intent.putExtra("UserId", dto.getFeedbackByUserId());
						intent.putExtra("UserName",dto.getFeedbackToUserName());
						intent.putExtra("UserImage",dto.getFeedbackToUserImage());
						intent.putExtra("replyMsg", "");
						intent.putExtra("FeedbackId","");					
						context.startActivity(intent);
					}else{
						Intent intent=new Intent(context, AddReply.class);
						intent.putExtra("UserId", dto.getFeedbackByUserId());
						intent.putExtra("UserName",dto.getFeedbackToUserName());
						intent.putExtra("UserImage",dto.getFeedbackToUserImage());
						intent.putExtra("replyMsg", dto.getFeedbackReplyMessage());
						intent.putExtra("FeedbackId",dto.getFeedbackId());					
						context.startActivity(intent);
					}
				}
			});
			
			//click on edit reply
			holder.ivEditReply.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context, AddReply.class);
					intent.putExtra("UserId", dto.getFeedbackByUserId());
					intent.putExtra("UserName",dto.getFeedbackToUserName());
					intent.putExtra("UserImage",dto.getFeedbackToUserImage());
					intent.putExtra("replyMsg", dto.getFeedbackReplyMessage());
					intent.putExtra("FeedbackId",dto.getFeedbackId());					
					context.startActivity(intent);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return row;

	}
	
	
}
