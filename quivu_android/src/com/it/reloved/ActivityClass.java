package com.it.reloved;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.ActivityListAdapter;
import com.it.reloved.dao.ProfileDAO;
import com.it.reloved.dto.ActivityDTO;
import com.it.reloved.dto.ActivityItemDTO;
import com.it.reloved.utils.AppSession;

public class ActivityClass extends RelovedPreference{

	protected static final String TAG = "ActivityClass";
	private ImageView ivSearchHeader,ivRefreshHeader,ivAddUserHeader;
	private ActivityDTO  activityDTO = null;
	private ListView listViewActivity;
	private ActivityListAdapter activityListAdapter=null;
	private Intent intent=null;
	private int page = 1;
	private boolean bLoadMore = false;
	private List<ActivityItemDTO>  activityItemDTOs=new ArrayList<ActivityItemDTO>();
	
	private LinearLayout noDataLinLayout;
	private TextView tvServerMsg;
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitys);
		
		/* method for initialising init components */
		ivSearchHeader=(ImageView)findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		ivRefreshHeader=(ImageView)findViewById(R.id.iv_refresh_header);		
		ivRefreshHeader.setOnClickListener(this);
		ivAddUserHeader=(ImageView)findViewById(R.id.iv_adduser_header);		
		ivAddUserHeader.setOnClickListener(this);
		ivAddUserHeader.setVisibility(View.GONE);		
		listViewActivity=(ListView)findViewById(R.id.list_activity);
		noDataLinLayout=(LinearLayout)findViewById(R.id.lin_layout_nodata);
		tvServerMsg=(TextView)findViewById(R.id.tv_server_msg);
		
		if (isNetworkAvailable()) {			
			//--------------------------------------------------------------------------------------		
			if ( new AppSession(ActivityClass.this).getJson(AppSession.ACTIVITY_JSON).equals("") ) {
				// Get data from server
				new TaskForActivity().execute(""+page);
			}
			else if(!(RelovedPreference.getTimeDiferenceMinute(new AppSession(ActivityClass.this).getLastSeenTime(AppSession.ACTIVITY_SEEN_TIME)) 
					< RelovedPreference.UPDATE_INTERVAL) ) { // if time limit is over
				// Note:- Don't put this condition with above condition with OR operator 
				// this same body repetition is required to get last seen time first time. 
				new TaskForActivity().execute(""+page);
			}
			else {
				// Just show the saved activity data
				activityDTO = ProfileDAO.parseActivities(new AppSession(ActivityClass.this).getJson(AppSession.ACTIVITY_JSON));
				showActivityList( activityDTO );
			}
			//--------------------------------------------------------------------------------------
						
		} else {
			//Toast.makeText(ActivityClass.this,getString(R.string.NETWORK_ERROR),
			//	Toast.LENGTH_LONG).show();
			showMessageView(getResources().getString(R.string.NETWORK_ERROR));
		}			
		
		/*click on  list item*/
		listViewActivity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//if(activityDTO.getActivityItemDTOs()!=null){
				if (activityItemDTOs.get(arg2).getActivityTypeId().equals("1")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("2")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("3")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("4")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("5")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("6")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("7")) {
					intent=new Intent(ActivityClass.this, Feedback.class);			
					if(activityDTO!=null){
						intent.putExtra("UserId", activityItemDTOs.get(arg2).getActivityFromUserId());
						intent.putExtra("UserName",activityItemDTOs.get(arg2).getActivityFromUserName());
						intent.putExtra("UserImage",activityItemDTOs.get(arg2).getActivityFromUserImage());
					}
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("8")) {
					intent=new Intent(ActivityClass.this, Feedback.class);			
					if(activityDTO!=null){
						intent.putExtra("UserId", activityItemDTOs.get(arg2).getActivityFromUserId());
						intent.putExtra("UserName",activityItemDTOs.get(arg2).getActivityFromUserName());
						intent.putExtra("UserImage",activityItemDTOs.get(arg2).getActivityFromUserImage());
					}
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("9")) {
					intent=new Intent(ActivityClass.this, ProfileOther.class);
					if(activityDTO!=null)
					intent.putExtra("userId",activityItemDTOs.get(arg2).getActivityFromUserId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("10")) {
					intent=new Intent(ActivityClass.this, ProfileOther.class);
					if(activityDTO!=null)
					intent.putExtra("userId",activityItemDTOs.get(arg2).getActivityFromUserId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("11")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("12")) {
					intent=new Intent(ActivityClass.this, SubCategoryDetails.class);
					intent.putExtra("productId", activityItemDTOs.get(arg2).getActivityProductId());
					startActivity(intent);
				}else if (activityItemDTOs.get(arg2).getActivityTypeId().equals("13")) {
					
				}
			 // }	
			}
		});		
		
		/*calling webservice on list scroll*/
		listViewActivity.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// Log.i("",""+visibleItemCount+"  "+firstVisibleItem+"  "+totalItemCount);
				if (bLoadMore&& ((visibleItemCount + firstVisibleItem) >= totalItemCount)) {
					if (isNetworkAvailable()) {
						new TaskForActivity().execute(""+page);
					} else {
						Toast.makeText(ActivityClass.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
					}	
					bLoadMore = false;
				}
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
		case R.id.iv_search_header:
			intent=new Intent(ActivityClass.this, SubCategory.class);
			intent.putExtra("categoryId", "0");
			intent.putExtra("CategoryName", "");
			startActivity(intent);		
			break;		
		case R.id.iv_refresh_header:
			if (isNetworkAvailable()) {
				new TaskForActivity().execute(""+page);
			} else {
				Toast.makeText(ActivityClass.this,getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
	
	/*Task for getting activity*/
	private class TaskForActivity extends AsyncTask<String, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(ActivityClass.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(ActivityClass.this);
				activityDTO = new ProfileDAO(ActivityClass.this).getActivity(ActivityClass.this, appSession.getBaseUrl(),
						getResources().getString(R.string.method_getActivities),appSession.getUserId(),
						params[0]);
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
				if (activityDTO != null) {
					if (activityDTO.getSuccess().equals("1")) {
						try {
							if (activityDTO.getActivityItemDTOs() != null) {								
								if (page == 1) {
									activityItemDTOs = null;
									activityItemDTOs = activityDTO.getActivityItemDTOs();
									showActivityList(activityDTO);
								//	activityListAdapter=new ActivityListAdapter(ActivityClass.this, R.layout.listitem_activity,
								//			activityItemDTOs);
								//	listViewActivity.setAdapter(activityListAdapter);

								} else {
									activityItemDTOs.addAll(activityDTO.getActivityItemDTOs());
									activityListAdapter.notifyDataSetChanged();
								}
								if (activityDTO.getActivityItemDTOs().size() >= 10) {
									bLoadMore = true;
									page++;
								}
							}
						} catch (Exception e) {
							System.gc();
							Runtime.getRuntime().gc();
							e.printStackTrace();
						}	
											
					} else {
						//Toast.makeText(ActivityClass.this,activityDTO.getMsg(),
						//		Toast.LENGTH_LONG).show();
						showMessageView(activityDTO.getMsg());
						if (page == 1) {							
						} else {						
							bLoadMore = false;
						}
					}
				} else {
					if (page == 1) {
					} else {
						bLoadMore = true;
					}
					//Toast.makeText(ActivityClass.this,getString(R.string.NETWORK_ERROR),
					//		Toast.LENGTH_LONG).show();
					showMessageView(getResources().getString(R.string.NETWORK_ERROR));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	private void showMessageView( String msg ) {
		listViewActivity.setVisibility(View.GONE);
		noDataLinLayout.setVisibility(View.VISIBLE);
		tvServerMsg.setText(msg);
	}
	
	/*method for setting adapter*/
	private void showActivityList(ActivityDTO dto) {
		if( dto.getActivityItemDTOs().size() != 0 ) {
			listViewActivity.setVisibility(View.VISIBLE);
			noDataLinLayout.setVisibility(View.GONE);
		activityListAdapter=new ActivityListAdapter(ActivityClass.this, R.layout.listitem_activity,
				dto.getActivityItemDTOs());
		listViewActivity.setAdapter(activityListAdapter);
		}
		else {
			showMessageView(dto.getMsg());
		}
	}
}
