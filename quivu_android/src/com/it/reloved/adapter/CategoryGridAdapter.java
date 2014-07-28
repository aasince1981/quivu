package com.it.reloved.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.it.reloved.R;
import com.it.reloved.RelovedPreference;
import com.it.reloved.dto.CategoryItemDTO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.lazyloader.ImageLoaderProgressBar;
import com.it.reloved.utils.AppSession;

public class CategoryGridAdapter extends ArrayAdapter<CategoryItemDTO> {

	ImageLoader imageLoader;
	ImageLoaderProgressBar imageLoaderProgressBar;
	Context context;
	int resource;
	LayoutInflater inflater;
	List<CategoryItemDTO> list;

	public CategoryGridAdapter(Context context, int resource,
			List<CategoryItemDTO> list) {
		super(context, resource, list);
		this.context = context;
		this.resource = resource;
		this.list = list;
		inflater = ((Activity) context).getLayoutInflater();
		imageLoader=new ImageLoader(context);
		imageLoaderProgressBar=new ImageLoaderProgressBar(context);
	}

	public static class ViewHolder {
		ImageView ivCategory;
		TextView tvCategoryName;
		ProgressBar progressBar;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
    // http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
		//View row = convertView;
		ViewHolder holder;			   		
		try {
			
			if (convertView == null) {
		        convertView = inflater.inflate(resource, parent, false);		
		//	row = inflater.inflate(resource, parent, false);
			holder = new ViewHolder();
			/* method for initialising init components */
			holder.ivCategory = (ImageView) convertView.findViewById(R.id.iv_category_item);
			holder.tvCategoryName = (TextView) convertView.findViewById(R.id.tv_category_item);
			holder.tvCategoryName.setTypeface(RelovedPreference.typefaceBold);
			holder.progressBar=(ProgressBar)convertView.findViewById(R.id.pb_profile_image);
			
			convertView.setTag(holder);
		    }//if (convertView == null) 
			else {
				holder = (ViewHolder) convertView.getTag();
		    }	
			
			CategoryItemDTO itm = list.get(position);
			holder.tvCategoryName.setText(itm.getCategoryName());
			
			if (!itm.getCategoryImage().equals("")) {
				/*imageLoader.DisplayImage(new AppSession(context).getCategoryBaseUrl()+itm.getCategoryImage(),
					(Activity) context, holder.ivCategory,0, 0, 0, R.drawable.no_image);*/				
				imageLoaderProgressBar.DisplayImage((Activity) context,new AppSession(context).getCategoryBaseUrl()+itm.getCategoryImage(),
						holder.ivCategory, holder.progressBar, 0, 0, 0, R.drawable.no_image,360);
			}else{
				holder.ivCategory.setImageResource(R.drawable.no_image);
				holder.progressBar.setVisibility(View.INVISIBLE);
			}	
			
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		

		return convertView;

	}
}
