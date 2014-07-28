package com.it.reloved;

import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

public class TabSample extends TabActivity {
	
	@Override
	public void onBackPressed() {
	}

	TabHost tabHost;
	Intent intent=null;
	String fromClass="";

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);
		intent=getIntent();
		if (intent!=null) {
			fromClass=intent.getStringExtra("fromClass");
			Log.i("TabSample", "fromClass="+fromClass);
		}
		
		setTabs();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
		
	}

	private void setTabs() {
		addTab("", R.drawable.tab_browse, Category.class);
		addTab("", R.drawable.tab_following, Followings.class);
		addTab("", R.drawable.tab_sell, Sell.class);
		addTab("", R.drawable.tab_activity, ActivityClass.class);
		addTab("", R.drawable.tab_me, ProfileMe.class);
		if(fromClass != null ) {		
		 if(fromClass.equals("Notification"))
				tabHost.setCurrentTab(3);		 
		}
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		//TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		//title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		Drawable drawable=getResources().getDrawable(drawableId);
		icon.setBackgroundDrawable(drawable);//(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}