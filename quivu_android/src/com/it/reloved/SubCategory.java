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
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.CustomSpinnerAdapter;
import com.it.reloved.adapter.SubCategoryGridAdapter;
import com.it.reloved.dao.CategoryDetailsDAO;
import com.it.reloved.dto.CategoryDTO;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.utils.AppSession;
import com.it.reloved.utils.CurrencyTextWatcher;
import com.it.reloved.utils.GPSTracker;


public class SubCategory extends RelovedPreference{
	
	private Context mContext;
	private View pview = null;;
	private PopupWindow pw; 
	
	private List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
	private GridView categoryGridView;
	private SubCategoryGridAdapter subCategoryGridAdapter;
	private Intent intent=null;
	private String categoryId="",CategoryName="";
	private GPSTracker gps;
	double latitude = 0.0, longitude = 0.0;
	private TextView tvCategoryName, tvServerMsg;
	private ImageView ivBackHeader,ivFilter,ivSearchHeader; 
	private EditText filterEditText;	
	private String filterString="",shortBy="1",searchRange="",minPrice="",maxPrice="";
	private LinearLayout noDataLinLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_subcategory);		
	
		/* method for initialising init components */
		ivBackHeader=(ImageView)findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivFilter=(ImageView)findViewById(R.id.iv_filter_header);
		ivFilter.setOnClickListener(this);
		ivSearchHeader=(ImageView)findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		filterEditText=(EditText)findViewById(R.id.et_search_header);
		
		intent=getIntent();
		if (intent!=null) {
			categoryId=intent.getStringExtra("categoryId");
			CategoryName=intent.getStringExtra("CategoryName");			
			Log.i("SubCategory", "categoryId==" + categoryId+"---CategoryName="+CategoryName);
		}
	
		gps = new GPSTracker(SubCategory.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			Log.i("Your Location is -:", "\nLat:" + latitude + "\nLong: "+ longitude);
		}
		
		tvCategoryName=(TextView)findViewById(R.id.tv_category_name_subcategory);
		tvCategoryName.setText(CategoryName);
		categoryGridView=(GridView)findViewById(R.id.gridview_category);
		noDataLinLayout=(LinearLayout)findViewById(R.id.lin_layout_nodata);
		tvServerMsg=(TextView)findViewById(R.id.tv_server_msg);
		
		
		if (!categoryId.equals("0")) {
			if (isNetworkAvailable()) {
				new TaskForSubCategory().execute();
			} else {
				//Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG)
				//		.show();
				showMessageView( getResources().getString(R.string.NETWORK_ERROR) );
			}
		}
		
		//perform click on grid item 
		categoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent=new Intent(SubCategory.this, SubCategoryDetails.class);
				intent.putExtra("productId", categoryDTOs.get(0).getCategoryItemDTOs().get(arg2).getCategoryId());
				startActivity(intent);
				
			}
		});
		
		//searching from soft keyboard
		filterEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {		    
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					 filterString=filterEditText.getText().toString();
						if (isNetworkAvailable()) {
							new TaskForSearch().execute();
						} else {
							Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG)
									.show();
						}
			            return true;
			        }
				return false;
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
	
	//perform click
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.iv_filter_header:
			Popup(v);
			break;	
		case R.id.iv_search_header:
			filterString=filterEditText.getText().toString();
			if (isNetworkAvailable()) {
				new TaskForSearch().execute();
			} else {
				Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG)
						.show();
			}
			break;	
		}
	}
	
	//Task for sub category
	private class TaskForSubCategory extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(SubCategory.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(SubCategory.this);
				categoryDTOs=new CategoryDetailsDAO(SubCategory.this).getCategoryItems(appSession.getBaseUrl(),
						getResources().getString(R.string.method_getProduct),categoryId,""+latitude,""+longitude);
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
				if (categoryDTOs != null) {
					if (categoryDTOs.get(0).getSuccess().equals("1")) {
						if( categoryDTOs.get(0).getCategoryItemDTOs().size() != 0 ) {
							categoryGridView.setVisibility(View.VISIBLE);
							noDataLinLayout.setVisibility(View.GONE);
						subCategoryGridAdapter=new SubCategoryGridAdapter(SubCategory.this, R.layout.category_subitem,
								categoryDTOs.get(0).getCategoryItemDTOs());
						categoryGridView.setAdapter(subCategoryGridAdapter);	
						}
						else {
							showMessageView( categoryDTOs.get(0).getMsg() );
						}
					} else {
						//Toast.makeText(SubCategory.this,categoryDTOs.get(0).getMsg(),
						//		Toast.LENGTH_LONG).show();
						showMessageView( categoryDTOs.get(0).getMsg() );
					}
				} else {
					//Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR),
					//		Toast.LENGTH_LONG).show();
					showMessageView( getResources().getString(R.string.NETWORK_ERROR) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}	
	
	private void showMessageView( String msg ) {
		categoryGridView.setVisibility(View.GONE);
		noDataLinLayout.setVisibility(View.VISIBLE);
		tvServerMsg.setText(msg);
	}
	
	//Task for search
	private class TaskForSearch extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd = null;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				pd = ProgressDialog.show(SubCategory.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {		
				AppSession appSession=new AppSession(SubCategory.this);				
				categoryDTOs=new CategoryDetailsDAO(SubCategory.this).searchProducts(appSession.getBaseUrl(),
						getResources().getString(R.string.method_searchProduct), filterString, 
						appSession.getUserId(),shortBy,categoryId, searchRange,minPrice,
						maxPrice, ""+latitude, ""+longitude);				
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
				if (categoryDTOs != null) {
					if (categoryDTOs.get(0).getSuccess().equals("1")) {
						subCategoryGridAdapter=new SubCategoryGridAdapter(SubCategory.this, R.layout.category_subitem,
								categoryDTOs.get(0).getCategoryItemDTOs());
						categoryGridView.setAdapter(subCategoryGridAdapter);
					} else {
						Toast.makeText(SubCategory.this,categoryDTOs.get(0).getMsg(),
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR),
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
	
	//pop up window for slide filter
	 public void Popup(View v) {
		 Display display = getWindow().getWindowManager().getDefaultDisplay();
		 int width=display.getWidth();
		 int height=display.getHeight();
			if (pview == null) {
				LayoutInflater inflater = (LayoutInflater) SubCategory.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				pview = inflater.inflate(R.layout.slide_filter,
						(ViewGroup)findViewById(R.layout.activity_subcategory));
				if (pview != null)
					pw = new PopupWindow(pview);
			
				
				pw.setOutsideTouchable(true);
				pw.setTouchable(true);	
				pw.setFocusable(true);
				pw.update();
				pw.showAtLocation(pview, Gravity.RIGHT|Gravity.TOP,0,0);
				//pw.showAsDropDown(v);
				pw.update(width,height);
				pw.setTouchInterceptor(new OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {							
							pw.dismiss();							
							return false;
						}
						return true;
					}
				});
				
				pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				
				pview.setOnTouchListener(new OnTouchListener() {
				     int orgX, orgY;
				     int offsetX, offsetY;
				     
				     int orgWidth, orgHeight;

				     @Override
				     public boolean onTouch(View v, MotionEvent event) {
				      switch (event.getAction()) {
				      case MotionEvent.ACTION_DOWN:
				       orgX = (int) event.getRawX();
				       orgY = (int) event.getRawY();

				       orgWidth = v.getMeasuredWidth();
				       orgHeight = v.getMeasuredHeight();
				       
				       break;
				      case MotionEvent.ACTION_MOVE:
				       offsetX = (int)event.getRawX() - orgX;
				       offsetY = (int)event.getRawY() - orgY;

				       //resize PopWindow
				       pw.update(
				         orgWidth + offsetX, 
				         orgHeight + offsetY);
				       break;
				      }
				      return true;
				     }});
				
				
				ImageView ivRight=(ImageView)pview.findViewById(R.id.iv_right_slide);
				TextView tvReset = (TextView) pview.findViewById(R.id.tv_reset_slide);
				final TextView tvCountryWide = (TextView) pview.findViewById(R.id.tv_countrywide_slide);
				final Spinner orderBySpinner=(Spinner)pview.findViewById(R.id.orderby_spinner_slide);
				final Spinner categorySpinner=(Spinner)pview.findViewById(R.id.category_spinner_slide);
				final SeekBar seekBar=(SeekBar)pview.findViewById(R.id.seekBar_slide);
				final EditText minEditText=(EditText)pview.findViewById(R.id.et_min_slide);
				final EditText maxEditText=(EditText)pview.findViewById(R.id.et_max_slide);				
				LinearLayout layout=(LinearLayout)pview.findViewById(R.id.blank_layout);
				
				minEditText.addTextChangedListener(new CurrencyTextWatcher());
				maxEditText.addTextChangedListener(new CurrencyTextWatcher());
						
				layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pw.dismiss();
						pview = null;
					}
				});
				
				ivRight.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {	
						minPrice=minEditText.getText().toString();
						maxPrice=maxEditText.getText().toString();
						filterString=filterEditText.getText().toString();
						if (isNetworkAvailable()) {
							new TaskForSearch().execute();
						} else {
							Toast.makeText(SubCategory.this,getString(R.string.NETWORK_ERROR), Toast.LENGTH_LONG)
									.show();
						}
						pw.dismiss();
						pview = null;
					}
				});				
				tvReset.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						minEditText.setText("");
						maxEditText.setText("");
						orderBySpinner.setSelection(0);
						categorySpinner.setSelection(0);
						seekBar.setProgress(0);						
					}
				});
				
				final List<CategoryItemDTO > orderByList=new ArrayList<CategoryItemDTO>();
				orderByList.add(new CategoryItemDTO("1", "Popular", null));
				orderByList.add(new CategoryItemDTO("2", "Recent", null));
				orderByList.add(new CategoryItemDTO("3", "Nearest", null));
				orderByList.add(new CategoryItemDTO("4", "LowPrice", null));
				orderByList.add(new CategoryItemDTO("5", "HighPrice", null));
				
				CustomSpinnerAdapter orderByAdapter=new CustomSpinnerAdapter(SubCategory.this, R.layout.spinner,
						orderByList);
				orderBySpinner.setAdapter(orderByAdapter);
				
				final List<CategoryItemDTO > categoryList=new ArrayList<CategoryItemDTO>();
				categoryList.add(new CategoryItemDTO("0", "All Categories", null));
				if (Category.categoryDTO.getCategoryItemDTOs()!=null && 
						Category.categoryDTO.getCategoryItemDTOs().size()>0) {
					for (int i = 0; i < Category.categoryDTO.getCategoryItemDTOs().size(); i++) {
						categoryList.add(new CategoryItemDTO(Category.categoryDTO.getCategoryItemDTOs()
								.get(i).getCategoryId(), Category.categoryDTO.getCategoryItemDTOs()
								.get(i).getCategoryName(), null));
					}
					
				}
				CustomSpinnerAdapter categoryAdapter=new CustomSpinnerAdapter(SubCategory.this, R.layout.spinner,
						categoryList);
				categorySpinner.setAdapter(categoryAdapter);
				
				orderBySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						shortBy=orderByList.get(arg2).getCategoryId();
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						}
				});
				
				categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub						
							categoryId=categoryList.get(arg2).getCategoryId();						
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						}
				});
				
				
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {					
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}					
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						
					}					
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {
						// TODO Auto-generated method stub
						searchRange=""+progress;
						if (progress==0) {
							tvCountryWide.setText("Countrywide");
						} else {
							tvCountryWide.setText(""+progress+"km from currrent location");
						}
					}
				});
							

			} else {
				pw.dismiss();
				pview = null;
			}
		}
}
