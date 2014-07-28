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
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImageLoader {
	private String TAG = "lazyloader <---> ImageLoader";
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	int cornerPixels, borderPixels, borderColor;
	int stub_id;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	Context context;

	public ImageLoader(Context context) {
		// Make the background thead low priority. This way it will not affect
		// the UI performance
		try {
			photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
			fileCache = new FileCache(context);

		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in ImageLoader(Context context)");

		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in ImageLoader(Context context)");
		}
	}

	// final int stub_id = R.drawable.ic_launcher;

	public void DisplayImage(String url, Activity activity,
			ImageView imageView, int cornerPixels, int borderPixels,
			int borderColor, int defaultIconId) {
		try {
			if (url == null)
				url = "";
			this.cornerPixels = cornerPixels;
			this.borderPixels = borderPixels;
			this.borderColor = borderColor;
			this.context = activity;
			this.stub_id = defaultIconId;
			imageViews.put(imageView, url);
			Bitmap bitmap = memoryCache.get(url);
			if (bitmap != null) {
				// Log.i("image view w*h",imageView.getWidth()+"*"+imageView.getHeight());
				imageView.setImageBitmap(/*new BitmapDrawable(*/
						getRoundedCornerBitmap(context, bitmap, cornerPixels,
							borderPixels, borderColor));
				//imageView.setScaleType(ScaleType.CENTER_CROP);
				//imageView.setImageBitmap(bitmap);			
				// imageView.setImageBitmap(getRoundedCornerBitmap(context,
				// bitmap,cornerPixels, borderPixels, borderColor));
			} else {
				queuePhoto(url, activity, imageView);
				if (activity.getLocalClassName().equals("SplashGetStarted"))
					// imageView.setImageResource(stub_id);
					imageView.setImageResource(stub_id);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in DisplayImage()");
		}
	}

	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap,
			int cornerDips, int borderDips, int borderColor) {
		Bitmap original = bitmap;
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int borderSizePx = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, (float) borderDips, context
							.getResources().getDisplayMetrics());
			final int cornerSizePx = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips, context
							.getResources().getDisplayMetrics());
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);

			// prepare canvas for transfer
			paint.setAntiAlias(true);
			paint.setColor(0xFFFFFFFF);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

			// draw bitmap
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			// draw border
			paint.setColor(borderColor);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth((float) borderSizePx);
			canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);
			return output;
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e1) {
			e1.printStackTrace();
			Log.e("lazyloader <---> ImageLoader", "Error in DisplayImage()");
		}
		return original;
	}

	public Bitmap getOldBitmap(String url) {

		try {
			//Log.i("Image Loader", "url=" + url);
			File f = fileCache.getFile(url);

			// from SD cache
			Bitmap b = decodeFile(f);
			if (b != null) {
				//Log.i("Image Loader", "b=" + b);
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView) {
		try {
			// This ImageView may be used for other images before. So there may
			// be
			// some old tasks in the queue. We need to discard them.
			photosQueue.Clean(imageView);
			PhotoToLoad p = new PhotoToLoad(url, imageView);
			synchronized (photosQueue.photosToLoad) {
				photosQueue.photosToLoad.push(p);
				photosQueue.photosToLoad.notifyAll();
			}

			// start thread if it's not started yet
			if (photoLoaderThread.getState() == Thread.State.NEW)
				photoLoaderThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e1) {
			e1.printStackTrace();
			Log.e("lazyloader <---> ImageLoader", "Error in DisplayImage()");
		}
	}

	private Bitmap getBitmap(String url) {
		try {
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
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				Utils.CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
				return bitmap;
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				e.printStackTrace();
				return null;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in getBitmap(String url)");
			return null;
		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in getBitmap(String url)");
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 120; // 70
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
			o2.inTempStorage = new byte[32 * 1024];
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);

			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in decodeFile(File f) ");
			return null;
		} catch (Error e1) {
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

		public PhotoToLoad(String u, ImageView i) {
			try {
				url = u;
				imageView = i;
			} catch (Exception e1) {
				e1.printStackTrace();
				Log.e(TAG, "Exception in PhotoToLoad(String u, ImageView i)");

			} catch (Error e1) {
				e1.printStackTrace();
				Log.e(TAG, "Error in PhotoToLoad(String u, ImageView i)");

			}
		}

	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread() {
		try {
			photoLoaderThread.interrupt();
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in stopThread()");

		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in stopThread()");

		}
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			try {
				for (int j = 0; j < photosToLoad.size();) {
					if (photosToLoad.get(j).imageView == image)
						photosToLoad.remove(j);
					else
						++j;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				Log.e(TAG, "Exception in Clean(ImageView image)");

			} catch (Error e1) {
				e1.printStackTrace();
				Log.e(TAG, "Error in Clean(ImageView image)");

			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Bitmap bmp = getBitmap(photoToLoad.url);
						memoryCache.put(photoToLoad.url, bmp);
						String tag = imageViews.get(photoToLoad.imageView);
						if (tag != null && tag.equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp,
									photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
									.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e1) {
				e1.printStackTrace();
				Log.e(TAG, "Error in PhotosLoader");

			}
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageView;

		public BitmapDisplayer(Bitmap b, ImageView i) {
			bitmap = b;
			imageView = i;
		}

		public void run() {
			try {
				// Log.i("image view w*h",imageView.getWidth()+"*"+imageView.getHeight());
				if (bitmap != null) {
					//imageView.setBackgroundDrawable(null);
					//imageView.setScaleType(ImageView.ScaleType.FIT_XY);
					imageView.setImageBitmap(getRoundedCornerBitmap(context,
							bitmap, cornerPixels, borderPixels, borderColor));
				} else
					imageView.setImageResource(stub_id);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e1) {
				e1.printStackTrace();
				Log.e(TAG, "Error in BitmapDisplayer");

			}
		}
	}

	public void clearCache() {
		try {
			memoryCache.clear();
			fileCache.clear();
		} catch (Exception e1) {
			e1.printStackTrace();
			Log.e(TAG, "Exception in clearCache()");

		} catch (Error e1) {
			e1.printStackTrace();
			Log.e(TAG, "Error in clearCache()");

		}
	}

}
