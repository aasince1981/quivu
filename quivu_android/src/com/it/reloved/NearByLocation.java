package com.it.reloved;

import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.adapter.MyArrayAdapter;
import com.it.reloved.dto.Place;
import com.it.reloved.utils.GPSTracker;

public class NearByLocation extends RelovedPreference implements
		OnClickListener {

	Intent intent;
	private String TAG = "NearByLocation";
	double latitude = 0.0, longitude = 0.0;
	ListView listView;
	TextView header_tv, tv_near;
	String placeSearch = "", title = "", loc = "";
	static ConnectivityManager cm;
	Context context = this;
	public List<Place> findPlaces;
	MyArrayAdapter adapter;
	GPSTracker gps;

	ImageView ivBackHeader, ivFilter, ivSearchHeader;
	EditText filterEditText;
	String fromClass="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_location);
		
		intent=getIntent();
		if (intent!=null) {
			fromClass=intent.getStringExtra("fromClass");
		}
		
		/*get lat long*/
		gps = new GPSTracker(context);
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {
			gps.showSettingsAlert();
		}
		try {
			intent = getIntent();
			if (intent != null) {
				placeSearch = intent.getStringExtra("search");
				title = intent.getStringExtra("title");
				Log.i(TAG, "placeSearch=" + placeSearch);
				loc = intent.getStringExtra("loc");
				Log.i(TAG, "loc=" + loc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* method for initialising init components */
		ivBackHeader = (ImageView) findViewById(R.id.iv_back_header);
		ivBackHeader.setOnClickListener(this);
		ivFilter = (ImageView) findViewById(R.id.iv_filter_header);
		ivFilter.setOnClickListener(this);
		ivFilter.setVisibility(View.GONE);
		ivSearchHeader = (ImageView) findViewById(R.id.iv_search_header);
		ivSearchHeader.setOnClickListener(this);
		filterEditText = (EditText) findViewById(R.id.et_search_header);
		filterEditText.setHint("Search by name");
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		listView = (ListView) findViewById(R.id.listview);

		if (isNetworkAvailable())
			new TaskGetPlaces(this, listView).execute();
		else
			Toast.makeText(context,
					getResources().getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_SHORT).show();

		/*perform click on list item*/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Place itemPlace = (Place) arg0.getItemAtPosition(position);
				if (fromClass.equals("EditProduct")) {
					EditProduct.place=itemPlace;
				} else {
					AddProduct.place=itemPlace;
				}				
				finish();
			}
		});
		
		
		filterEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {		    
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				 if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					 try {
							if (adapter != null)
								adapter.getFilter().filter(filterEditText.getText());
						} catch (Exception e) {
							e.printStackTrace();
						} catch (Error e) {
							e.printStackTrace();
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
	
/*perform click*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.iv_search_header:
			try {
				if (adapter != null)
					adapter.getFilter().filter(filterEditText.getText());
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/*Task for getting near by places*/
	class TaskGetPlaces extends AsyncTask<Void, Void, List<Place>> {
		Context context;
		private ListView listView;
		private ProgressDialog pd;

		public TaskGetPlaces(Context context, ListView listView) {
			this.context = context;
			this.listView = listView;
		}

		@Override
		protected void onPostExecute(List<Place> nearByPlaceList) {
			super.onPostExecute(nearByPlaceList);
			try {
				pd.dismiss();
				if (nearByPlaceList != null) {
					if (nearByPlaceList.size() > 0) {
						listView.setVisibility(View.VISIBLE);
						adapter = new MyArrayAdapter(context, nearByPlaceList);
						this.listView.setAdapter(adapter);
					} else {
						listView.setVisibility(View.GONE);
					}
				} else {
					listView.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(NearByLocation.this, null, null);
			pd.setContentView(R.layout.progressloader);
		}
		@Override
		protected List<Place> doInBackground(Void... arg0) {
			List<Place> nearByPlaceList = null;
			try {
				nearByPlaceList = findNearLocation();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return nearByPlaceList;
		}
	}

	public List<Place> findNearLocation() {
		try {
			PlacesService service = new PlacesService(//
					"AIzaSyCXa5R4ZyUXmQjVhq_J82mAQyQIdQQKaxE");
			Log.i("findNearLocation", "lat::" + latitude + "lon::" + longitude);
			findPlaces = service.findPlaces(latitude, longitude, placeSearch);
			for (int i = 0; i < findPlaces.size(); i++) {
				Place placeDetail = findPlaces.get(i);
				placeDetail.getIcon();
				System.out.println(placeDetail.getName());
			}
			return findPlaces;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
