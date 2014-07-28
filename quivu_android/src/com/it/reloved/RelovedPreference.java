package com.it.reloved;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.it.reloved.utils.AppSession;

public class RelovedPreference extends GetGCMID implements OnClickListener{

	public static final long UPDATE_INTERVAL = 2; // In Minutes
	public static String  Notification_Id ,selectedImagePath="";
	private static ConnectivityManager cm;
	public static String GCMSenderId = "1090612974530";	
	public static String imgPath="";
	public static Typeface typeface,typefaceBold;
	
	// protected FacebookConnector fConnector=null;
	protected static final String[] PERMS = new String[] { "email" };
	
	public static final int PICK_IMAGE = 1;
	protected static final String TAG = "RelovedPreference";
	private static Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext=RelovedPreference.this;
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);	
		typeface = Typeface.createFromAsset(getAssets(), "fonts/Lato_Regular.ttf");
		typefaceBold = Typeface.createFromAsset(getAssets(), "fonts/Lato_Bold.ttf");
	}
	
	public static Bitmap loadBitmap(String imgUrlStr) {
		Log.i(TAG, "imggg url: " + imgUrlStr);
		Bitmap bitmap = null;
		try {
			URL url = new URL(imgUrlStr);
			bitmap = BitmapFactory.decodeStream((InputStream) url.getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	@Override
	public void onClick(View v) { }
	
	public void share(String subject,String text) {
	     final Intent intent = new Intent(Intent.ACTION_SEND);
	     intent.setType("text/plain");
	     intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	     intent.putExtra(Intent.EXTRA_TEXT, text);
	     startActivity(Intent.createChooser(intent, "Share this via"));
	}

	public final static boolean checkEmail(CharSequence target) {
        if (target == null) {
        return false;
        } else {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }   
	
	public static String getImagePath() {
		return imgPath;
	}
	
	private final static Pattern NAME_PATTERN = Pattern
			.compile(".[a-zA-Z0-9_.]+");

	/* method for username validation */
	public static boolean checkName(String name) {
		try {
			System.out.println("chek name  :  "
					+ NAME_PATTERN.matcher(name).matches());
			return NAME_PATTERN.matcher(name).matches();
		} catch (NullPointerException exception) {
			return false;
		}
	}

	/* Method for checking network availability */
	public static boolean isNetworkAvailable() {
		try {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getAbsolutePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}
	
	public static Bitmap decodeFile(File f, int REQUIRED_WIDTH,
			int REQUIRED_HEIGHT) {
		try {
			ExifInterface exif = new ExifInterface(f.getPath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			// Log.i("exif.getAttribute(ExifInterface.TAG_ORIENTATION)",
			// exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}
			Log.i("path & orientation & angle", f.getPath() + " & "
					+ orientation + " & " + angle);
			Matrix mat = new Matrix();
			// if(checkRotate)
			mat.postRotate(angle);
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_SIZE = 100; // 70
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			Log.i("W*H Before.....................", scale + " " + width_tmp
					+ "*" + height_tmp);
			if (width_tmp > height_tmp) {
				REQUIRED_SIZE = REQUIRED_HEIGHT;
				REQUIRED_HEIGHT = REQUIRED_WIDTH;
				REQUIRED_WIDTH = REQUIRED_SIZE;
			}
			while (true) {
				if (width_tmp / 2 < REQUIRED_WIDTH
						&& height_tmp / 2 < REQUIRED_HEIGHT)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			Log.i("W*H After.....................", scale + " " + width_tmp
					+ "*" + height_tmp);
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPurgeable = true;
			// return BitmapFactory.decodeStream(new FileInputStream(f), null,
			// o2);
			Bitmap correctBmp = BitmapFactory.decodeStream(new FileInputStream(
					f), null, o2);
			// Log.i("W*H bitmap.....................",
			// bmp.getWidth() + "*" + bmp.getHeight());
			correctBmp = Bitmap.createBitmap(correctBmp, 0, 0,
					correctBmp.getWidth(), correctBmp.getHeight(), mat, true);
			return correctBmp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			// Toast.makeText(this, "Image Size is too large",
			// Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			// Toast.makeText(this, "Image Size is too large",
			// Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
	public static String getUpdateTime(String updateTime) {
		Date currentDate = new Date();
		long diff, second, minute, hour, day, year;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TimeZone tz = TimeZone.getTimeZone(new AppSession(mContext).getTimeZone()/*"America/Chicago"*/);
			dateFormat.setTimeZone(tz);
			Date updateDate = dateFormat.parse(updateTime);
			Log.i("getUpdateTime", "current time="+dateFormat.format(currentDate.getTime()).toString());
			diff = currentDate.getTime() - updateDate.getTime();
			second = diff / 1000;
			minute = second / 60;
			hour = minute / 60;
			day = hour / 24;
			year = day / 365;
			if (second <= 59) {
				if (second <= 1)
					return "a moment ago";
				return second + " seconds ago";
			} else if (minute <= 59) {
				if (minute == 1)
					return minute + " minute ago";
				else
					return minute + " minutes ago";
			} else if (hour <= 23) {
				if (hour == 1)
					return hour + " hour ago";
				else
					return hour + " hours ago";
			} else if (day <= 364) {
				if (day == 1)
					return day + " day ago";
				else
					return day + " days ago";
			} else {
				if (year == 1)
					return year + " year ago";
				else
					return year + " year ago";
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return " ";
	}
	
	public String getAppVersion() {
	String appVersion = "";
	try {
		appVersion = getPackageManager()
				.getPackageInfo(getPackageName(), 0).versionName;
	} catch (NameNotFoundException e) {
		e.printStackTrace();
	}
	return appVersion;
   }
	
	public static Drawable downloadImage(String imageUrl) {
		Log.i("imggg url   ", imageUrl);
		Drawable drawable = null;
		try {
			Bitmap bitmap = null;

			URL url = new URL(imageUrl);
			HttpGet httpRequest = null;

			httpRequest = new HttpGet(url.toURI());

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpRequest);

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
			InputStream input = b_entity.getContent();

			bitmap = BitmapFactory.decodeStream(input);
			drawable = (Drawable) new BitmapDrawable(bitmap);
		} catch (MalformedURLException e) {
			Log.v("log", "bad url", e);
		} catch (IOException e) {
			Log.v("log", "io error", e);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		return drawable;

	}
	public static Uri setImageUri() {
		// Store image in dcim

		File file = new File(Environment.getExternalStorageDirectory()
				+ "/quivu/", "image" + new Date().getTime() + ".jpg");
		Uri imgUri = Uri.fromFile(file);
		imgPath = file.getAbsolutePath();
		return imgUri;
	}
	
	public static String getFilePath(Bitmap bitmap, String pictureType) {
		String filepath = "";
		String IMAGE_PATH = Environment.getExternalStorageDirectory()+ "/quivu/";
		File myDir = new File(IMAGE_PATH);		
		myDir.mkdirs();
		String FILE_NAME = pictureType + ".jpg";
		File file = new File(myDir, FILE_NAME);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(file);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file1 = new File(IMAGE_PATH + FILE_NAME);
		filepath = file1.getAbsolutePath();
		return filepath;
	}
	
	public static boolean isAndroidEmulator() {
	    String model = Build.MODEL;
	    Log.d("isAndroidEmulator", "model=" + model);
	    String product = Build.PRODUCT;
	    Log.d("isAndroidEmulator", "product=" + product);
	    boolean isEmulator = false;
	    if (product != null) {
	        isEmulator = product.equals("sdk") || product.contains("_sdk") || product.contains("sdk_");
	    }
	    Log.d("isAndroidEmulator", "isEmulator=" + isEmulator);
	    if(isEmulator)
	    {
	    	Log.d("isAndroidEmulator", "Kya ye Emulator he =" + "Ha ye Emulator he");
	    }
	    else
	    {
	    	Log.d("isAndroidEmulator", "Kya ye Emulator he =" + "Ye Emulator nahi he");
	    }
	    return isEmulator;
	}
	
	
	public String getIMEIorDeviceId() {
		String imei = "";
		try {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			imei = tm.getDeviceId();
			if(isAndroidEmulator())
			{
				imei ="420420420420420420420";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imei;
	}
	
	public void createConfirmDialog(final String marketLink,Context mContext) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		alertDialogBuilder
				.setMessage("Your device does not have this app, want to install this app!")
				.setCancelable(false)
				.setPositiveButton("Now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent i = new Intent(Intent.ACTION_VIEW);
								i.setData(Uri.parse(marketLink));
								startActivity(i);
								finish();
							}
						})
				.setNegativeButton("Later",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		String time = dateFormat.format(new Date().getTime());
		Log.i("CurrentDateTime: ", time);
		return time;		  
	}
	
	
	public static long getTimeDiferenceMinute(String lastSeenTime) {
		long diff = 0, second, minute, hour;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			Date lastDate = dateFormat.parse(lastSeenTime);
			Date currentDate = new Date();
			diff = currentDate.getTime() - lastDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		second = diff / 1000;
		minute = second / 60;
		hour = minute / 60;
		Log.i("TimeDiferenceMinute: ", minute+"");
		return minute;
	}
	
}
