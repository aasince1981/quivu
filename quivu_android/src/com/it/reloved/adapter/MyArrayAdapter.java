package com.it.reloved.adapter;

import java.util.ArrayList;
import java.util.List;

import com.it.reloved.R;
import com.it.reloved.dto.Place;
import com.it.reloved.lazyloader.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<Place>{

	 private final Context context;
	  private  List<Place> nearByPlaceList,originalList;
	
		MyFilter filter ;
	  ImageLoader imageLoader;
	  public MyArrayAdapter(Context context, List<Place> nearByPlaceList) {
	    super(context, R.layout.listview_item, nearByPlaceList);
	    this.context = context;
	    this.nearByPlaceList = nearByPlaceList;
	    imageLoader=new ImageLoader(context);
	    originalList = new ArrayList<Place>();   
		originalList.addAll(nearByPlaceList);
	  }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listview_item, parent, false);
		
		final Place nEARYPlace=(Place)this.getItem(position);
		/* method for initialising init components */
		TextView textView = (TextView) rowView.findViewById(R.id.tv_name_of_place_NEARBY_LIST);
		TextView textViewAddress = (TextView) rowView.findViewById(R.id.tv_address_of_place_NEARBY_LIST);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.locImage_NEARBY_LIST);

		if(nEARYPlace.getIcon()!=null&&!nEARYPlace.equals(null)&&!nEARYPlace.equals(""))
		{
			Log.i("Icon", "|_____| Icon" + nEARYPlace.getIcon());
			imageLoader.DisplayImage(nEARYPlace.getIcon(), ((Activity)context), imageView, 10, 0, 0, R.drawable.icon_location);
		}
		
		Log.i("values length", "!!!!!!!" + nearByPlaceList.size());
		textView.setText(nEARYPlace.getName());
		textViewAddress.setText(nEARYPlace.getVicinity());
		return rowView;
	}
	
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new MyFilter();
		}
		return filter;
	}
	private class MyFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();
		//	Log.i("constraint", "" + constraint);
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<Place> filteredItems = new ArrayList<Place>();
				for (int i = 0, l = originalList.size(); i < l; i++) {
					Place contact = originalList.get(i) ;
					if (contact.getName().toString().toLowerCase()
							.contains(constraint))
						filteredItems.add(contact);
				}
				result.count = filteredItems.size();
				result.values = filteredItems;
			} else {
				synchronized (this) {
					result.values = originalList;
					result.count = originalList.size();
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {

			nearByPlaceList = (ArrayList<Place>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = nearByPlaceList.size(); i < l; i++)
				add(nearByPlaceList.get(i));
			notifyDataSetInvalidated();
		}
	}

	
	
	
}
