package com.it.reloved.lazyloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;



public class ImageLoaderProgressBar {
	private String TAG="lazyloader <---> ImageLoader";
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<String, ProgressBarAndImage> imageViews = Collections
			.synchronizedMap(new WeakHashMap<String, ProgressBarAndImage>());
	ExecutorService executorService;
	Handler handler = new Handler();// handler to display images in UI thread
	// ProgressBar progress;
	ProgressBarAndImage pbAndImage;
	Context context;
	int cornerPixels, borderPixels, borderColor,defaultImage;
	
	int REQUIRED_SIZE = 70; // 70
	
	public ImageLoaderProgressBar(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(10);
		
	}

	// final int stub_id=R.drawable.stub;
	public void DisplayImage(Activity activity, String url,
			ImageView imageView, ProgressBar progressBar, int cornerPixels,
			int borderPixels, int borderColor,int defaultImage,int size) {
		try {
			// progress=progressBar;
			this.cornerPixels = cornerPixels;
			this.borderPixels = borderPixels;
			this.borderColor = borderColor;
			this.defaultImage = defaultImage;
			REQUIRED_SIZE=size;
			this.context = activity;
			pbAndImage = new ProgressBarAndImage(url, imageView, progressBar,
					cornerPixels, borderPixels, borderColor,defaultImage);
			imageViews.put(url, pbAndImage);
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				imageView.setVisibility(View.VISIBLE);
				/* pbAndImage.getImg() */
//				imageView.setImageBitmap(getRoundedCornerBitmap(context,
//						bitmap, cornerPixels, borderPixels, borderColor));
				imageView.setImageBitmap(bitmap);
				/* pbAndImage.getPb() */
				progressBar.setVisibility(View.GONE);
				//setAdapterChanged(className);
			} else {
				queuePhoto(url, pbAndImage);
				imageView.setImageResource(defaultImage);
				imageView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void queuePhoto(String url, ProgressBarAndImage pbAndImage) {
		PhotoToLoad p = new PhotoToLoad(url, pbAndImage);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			memoryCache.clear();
			return null;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			
			
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
//Log.i("exif.getAttribute(ExifInterface.TAG_ORIENTATION)", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}
           Log.i("path & orientation & angle",f.getPath()+" & "+ orientation+" & "+angle);
			Matrix mat = new Matrix();
			mat.postRotate(angle);		
			
			
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inTempStorage=new byte[32 * 1024];
			Bitmap bitmap=BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			
			return  Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), mat, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in decodeFile(File f) ");
			return null;
		}catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in decodeFile(File f) ");
			return null;
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;
		public ProgressBar progressBar;
		public int cPixels, bPixels, bColor;

		public PhotoToLoad(String u, ProgressBarAndImage pbAndImage) {
			url = u;
			imageView = pbAndImage.getImageView();
			progressBar = pbAndImage.getProgressBar();
			cPixels = pbAndImage.getCornerPixels();
			bPixels = pbAndImage.getBorderPixels();
			bColor = pbAndImage.getBorderColor();
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		ProgressBarAndImage tag = imageViews.get(photoToLoad.url);
		if (tag == null || !tag.getImagePath().equals(photoToLoad.url)) {
			return true;
		}

		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			try {
				if (bitmap != null) {
					photoToLoad.imageView.setVisibility(View.VISIBLE);
//					photoToLoad.imageView
//							.setImageBitmap(getRoundedCornerBitmap(context,
//									bitmap, photoToLoad.cPixels,
//									photoToLoad.bPixels, photoToLoad.bColor));
					photoToLoad.imageView
					.setImageBitmap(bitmap);
					photoToLoad.progressBar.setVisibility(View.GONE);
					//setAdapterChanged(className);
				} else {
					photoToLoad.imageView.setVisibility(View.VISIBLE);
					photoToLoad.imageView
							.setImageResource(defaultImage);
					photoToLoad.progressBar.setVisibility(View.GONE);
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

//	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap,
//			int cornerDips, int borderDips, int borderColor) {
//		Bitmap original = bitmap;
//		try {
//			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
//					bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//			Canvas canvas = new Canvas(output);
//
//			final int borderSizePx = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, (float) borderDips, context
//							.getResources().getDisplayMetrics());
//			final int cornerSizePx = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips, context
//							.getResources().getDisplayMetrics());
//			final Paint paint = new Paint();
//			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
//					bitmap.getHeight());
//			final RectF rectF = new RectF(rect);
//
//			// prepare canvas for transfer
//			paint.setAntiAlias(true);
//			paint.setColor(0xFFFFFFFF);
//			paint.setStyle(Paint.Style.FILL);
//			canvas.drawARGB(0, 0, 0, 0);
//			canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
//
//			// draw bitmap
//			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//			canvas.drawBitmap(bitmap, rect, rect, paint);
//
//			// draw border
//			paint.setColor(borderColor);
//			paint.setStyle(Paint.Style.STROKE);
//			paint.setStrokeWidth((float) borderSizePx);
//			canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
//			return output;
//		} catch (OutOfMemoryError e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return original;
//	}

}
