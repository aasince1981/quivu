package com.it.reloved.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.it.reloved.R;
import com.it.reloved.dto.CategoryItemDTO;

public class CustomSpinnerAdapter extends ArrayAdapter<CategoryItemDTO> {
	private final String TAG = "CustomSpinnerAdapter";
	Context mContext;
	List<CategoryItemDTO> listData;
	int layoutResourceId;
	View row = null;

	public CustomSpinnerAdapter(Context mContext, int layoutResourceId,
			List<CategoryItemDTO> listData) {
		super(mContext, layoutResourceId, listData);
		this.mContext = mContext;
		this.listData = listData;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomDropDownView(position, convertView, parent);
	}

	@Override
	public int getCount() {

		try {
			return (listData.size());
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in getCount");
			return 0;
		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in getCount ");
			return 0;
		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		try {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.spinner, parent, false);
			TextView label = (TextView) row.findViewById(R.id.textView_spinner);
			label.setText(listData.get(position).getCategoryName());
			label.setTextColor(mContext.getResources().getColor(R.color.black));
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			return row;
		}
	}

	public View getCustomDropDownView(int position, View convertView,
			ViewGroup parent) {
		try {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(R.layout.text_layout, parent, false);
			TextView label = (TextView) row.findViewById(R.id.listtv);
			label.setText(listData.get(position).getCategoryName());
			if (position == 0) {
				label.setBackgroundColor(mContext.getResources().getColor(
						R.color.black));
				label.setTextColor(mContext.getResources().getColor(
						R.color.white));
			} else {
				label.setBackgroundColor(mContext.getResources().getColor(
						R.color.white));
				label.setTextColor(mContext.getResources().getColor(
						R.color.black));
			}
			return row;
		} catch (Exception e) {
			e.printStackTrace();
			return row;
		}
	}
}
