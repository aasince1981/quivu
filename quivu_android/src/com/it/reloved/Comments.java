package com.it.reloved;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.CommentListAdapter;
import com.it.reloved.dao.CommentDAO;
import com.it.reloved.dao.FeedbackDAO;
import com.it.reloved.dto.CommentDTO;
import com.it.reloved.dto.CommentItemDTO;
import com.it.reloved.dto.UserDTO;
import com.it.reloved.utils.AppSession;

public class Comments extends RelovedPreference{

	private ImageView ivBackHeader,ivSend;
	private ListView listViewComment;
	private EditText msgEditText;
	private CommentListAdapter commentListAdapter;
	private String CommentMessage="",productImage="",OwnerStatus="",ReplyStaus="0";
	private int poss=0;
	
	@Override
	public void onBackPressed() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comments);
	
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
	
		listViewComment=(ListView)findViewById(R.id.list_comment);
		msgEditText=(EditText)findViewById(R.id.et_comment_comment);
		ivSend=(ImageView)findViewById(R.id.iv_send_comment);
		ivSend.setOnClickListener(this);
		
		commentListAdapter=new CommentListAdapter(Comments.this, R.layout.listitem_comment,
				SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),
				SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size());
		listViewComment.setAdapter(commentListAdapter);
		
		/*perform click on list item*/
		listViewComment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				poss=arg2;
				Log.i("listViewComment","setOnItemClickListener");
				ReplydailogBox(Comments.this,SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getCommentItemDTOs().get(arg2).getCommentId(),SubCategoryDetails.subCategoryDetailsDTOs.get(0)
						.getCommentItemDTOs().get(arg2).getCommentUserName());
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
	
	/*perform click*/
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			setResult(RESULT_OK);
			  finish();
			break;
		case R.id.iv_send_comment:
			AppSession appSession=new AppSession(Comments.this);
			CommentMessage=msgEditText.getText().toString();			
			if (appSession.getUserId().equals(SubCategoryDetails.subCategoryDetailsDTOs.get(0)
					.getProductUserId())) {
				OwnerStatus="1";
			} else {
				OwnerStatus="0";
			}		
			
			if(SubCategoryDetails.subCategoryDetailsDTOs!=null){
				for (int i = 0; i < SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductImageDtos().size(); i++) {
					if (SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductImageDtos().get(i)
						.getCategoryName().equals("1")) {
						productImage=SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductImageDtos()
								.get(i).getCategoryImage();
						break;
					}
				}
			}
			
			if (!CommentMessage.equals("")) {
				if (CommentMessage.length()>0) {
					String firstChar=""+CommentMessage.charAt(0);
					Log.i("firstChar", "firstChar="+firstChar);
					if (firstChar.equals("@")) {
						ReplyStaus="1";
					} else {
						ReplyStaus="0";
					}
				} 
				if (isNetworkAvailable()) {
					new TaskAddComments().execute();					
					List<UserDTO> userDTOs=appSession.getConnections();	
					msgEditText.setText("");
					Calendar c = Calendar.getInstance();
			        System.out.println("Current time => "+c.getTime());
			        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        String formattedDate = df.format(c.getTime());
					CommentItemDTO commentItemDTO=new CommentItemDTO(appSession.getUserId(),
							userDTOs.get(0).getUserName(),userDTOs.get(0).getUserImage(), 
							CommentMessage, SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductUserId(),
							SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductUserName(),
							formattedDate, OwnerStatus, ReplyStaus, "");	
					// Update getCommentItemDTOs with new added comments
					SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().add(0, commentItemDTO);
					// Update current comments list view
					commentListAdapter=new CommentListAdapter(Comments.this, R.layout.listitem_comment,
							SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),
							SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size());
					listViewComment.setAdapter(commentListAdapter);
					//Toast.makeText(this, SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size()+"", 1).show();
					commentListAdapter.notifyDataSetChanged();
					// Set focus on newly added comment
					listViewComment.setSelection(SubCategoryDetails.subCategoryDetailsDTOs
							.get(0).getCommentItemDTOs().size() - 1);
				} else {
					Toast.makeText(Comments.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}				
			}
			break;

		default:
			break;
		}
	}
	
	/*Task for add comments*/
	private class TaskAddComments extends AsyncTask<Void, Void, Void> {
		//ProgressDialog pd = null;
		List<CommentDTO> commentDTOs=new ArrayList<CommentDTO>();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				/*pd = ProgressDialog.show(Comments.this, null, null);
				pd.setContentView(R.layout.progressloader);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {				
				AppSession appSession=new AppSession(Comments.this);
				List<UserDTO> userDTOs=appSession.getConnections();							
				commentDTOs=new CommentDAO(Comments.this).addComment(appSession.getBaseUrl(),
						getResources().getString(R.string.method_addcomment),SubCategoryDetails.productId, 
						appSession.getUserId(), userDTOs.get(0).getUserName(),
						userDTOs.get(0).getUserImage().replace(appSession.getUserImageBaseUrl(),""),
						CommentMessage,productImage.replace(appSession.getProductBaseUrl(),""),
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductName(),
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductUserId(),
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductUserName(),
						OwnerStatus, ReplyStaus,SubCategoryDetails.subCategoryDetailsDTOs.get(0).
						getProductUserId(),
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).getProductUserName());					
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);			
			try {
				//pd.dismiss();
				if (commentDTOs != null) {
					if (commentDTOs.get(0).getSuccess().equals("1")) {
						// Just update the getCommentItemDTOs of SubCategoryDetails class 
						//SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs()
						//commentDTOs.get(0).getCommentItemDTOs()
						
						/*msgEditText.setText("");
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).
							getCommentItemDTOs().addAll(commentDTOs.get(0).getCommentItemDTOs());
						commentListAdapter=new CommentListAdapter(Comments.this, R.layout.listitem_comment,
								SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),
								SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size());
						listViewComment.setAdapter(commentListAdapter);
						commentListAdapter.notifyDataSetChanged();*/
					} else {
						Toast.makeText(Comments.this,commentDTOs.get(0).getMsg(),Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(Comments.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	private Dialog dialog = null;
	/*method for reply dialog*/
	private void ReplydailogBox(Context mCtx,final String commetId,final String commentUserName) {
		dialog = new Dialog(mCtx);
		Window window = dialog.getWindow();
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_reply);
		window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
		window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		
		TextView replyComment = (TextView) window.findViewById(R.id.reply_comment_dialog);
		TextView deleteComment = (TextView) window.findViewById(R.id.delete_comment_dialog);

		replyComment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				msgEditText.setText("@"+commentUserName+" ");
				dialog.dismiss();
				dialog = null;
			}
		});
		deleteComment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isNetworkAvailable()) {
					new TaskDeleteComment().execute(commetId);
				} else {
					Toast.makeText(Comments.this,getString(R.string.NETWORK_ERROR),
						Toast.LENGTH_LONG).show();
				}
				dialog.dismiss();
				dialog = null;
			}
		});
		dialog.show();
	}
	
	/*Task for delete comment*/
	private class TaskDeleteComment extends AsyncTask<String, Void, Void> {
		ProgressDialog pd = null;
		String[] arr=new String[2];
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(Comments.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			try {				
				AppSession appSession=new AppSession(Comments.this);
				List<UserDTO> userDTOs=appSession.getConnections();
				arr=new CommentDAO(Comments.this).deleteComment(appSession.getBaseUrl(),
						getResources().getString(R.string.method_deleteComment), params[0]);					
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
				if (arr != null) {
					if (arr[0].equals("1")) {
						Toast.makeText(Comments.this,arr[1],Toast.LENGTH_LONG).show();
						// Remove comment from getCommentItemDTOs
						SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().remove(poss);
						// Update current comments list view
						commentListAdapter=new CommentListAdapter(Comments.this, R.layout.listitem_comment,
								SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs(),
								SubCategoryDetails.subCategoryDetailsDTOs.get(0).getCommentItemDTOs().size());
						listViewComment.setAdapter(commentListAdapter);
						commentListAdapter.notifyDataSetChanged();
						// Set focus on last camment
						listViewComment.setSelection(SubCategoryDetails.subCategoryDetailsDTOs
								.get(0).getCommentItemDTOs().size() - 1);
					} else {
						Toast.makeText(Comments.this,arr[1],Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(Comments.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
}
