package com.it.reloved;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.it.reloved.lazyloader.ImageLoader;

public class ViewPagerActivity extends Activity {
	private Intent intent;
	private String picId;
	private int pic_id;
	private ViewPager viewPager;
	ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
		imageLoader =new ImageLoader(this);
		intent=getIntent();
		if (intent!=null) {
			picId = intent.getStringExtra("PIC_ID");
			pic_id = Integer.parseInt(picId);
		}

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(pic_id);

	}

	private class ImagePagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return SubCategoryDetails.listImagesUrl.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			Context context = ViewPagerActivity.this;
			ImageView  imageView = new ImageView(context);
			for(int i=0; i<SubCategoryDetails.listImagesUrl.size(); i++ ) {	
				imageLoader.DisplayImage(SubCategoryDetails.listImagesUrl.get(position), (Activity)context, imageView, 0, 0, 
						0 , R.drawable.no_image);
			}
			viewPager.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}