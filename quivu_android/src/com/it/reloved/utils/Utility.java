package com.it.reloved.utils;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
	
	public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
      //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }
	
	/* public static void setListViewHeightBasedOnChildren(ListView listView) {
	        ListAdapter listAdapter = listView.getAdapter();
	        if (listAdapter == null) {
	            // pre-condition
	            return;
	        }

	        int totalHeight = 0;
	        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	        for (int i = 0; i < listAdapter.getCount(); i++) {
	            View listItem = listAdapter.getView(i, null, listView);
	            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	            totalHeight += listItem.getMeasuredHeight();
	        }

	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	        listView.setLayoutParams(params);
	        listView.requestLayout();
	    }*/
	 
	 
	 public static void getGridViewSize(GridView myGridView) {
	        ListAdapter myListAdapter = myGridView.getAdapter();
	        if (myListAdapter == null) {
	            //do nothing return null
	            return;
	        }
	        //set listAdapter in loop for getting final size
	        int totalHeight = 0;
	    //    int gridSize=(myListAdapter.getCount()/2)+(myListAdapter.getCount()%2);
	        int gridSize=(myListAdapter.getCount()/2)+(myListAdapter.getCount()%2);
	        Log.i("size of grid",""+gridSize);
	        Log.i("adapter item:", String.valueOf(gridSize));
	        for (int size = 0; size < gridSize; size++) {
	            View listItem = myListAdapter.getView(size, null, myGridView);
	            listItem.measure(0, 0);
	            totalHeight += listItem.getMeasuredHeight();
	        }
	      //setting listview item in adapter
	        ViewGroup.LayoutParams params = myGridView.getLayoutParams();
	        params.height = totalHeight+80 + (myGridView.getPaddingLeft() * gridSize);
	        myGridView.setLayoutParams(params);
	        // print height of adapter on log
	        Log.i("height of listItem:", String.valueOf(totalHeight));
	        
	    }
}
