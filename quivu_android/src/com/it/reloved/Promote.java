package com.it.reloved;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.it.reloved.dao.UtilityDAO;
import com.it.reloved.lazyloader.ImageLoader;
import com.it.reloved.utils.AppSession;

public class Promote extends RelovedPreference {
	
	private TextView tvPromteNow,tvHeaderText,tvUserName;
	private ImageView ivFacebook,ivTwitter,ivInstagram,ivBack,ivUserImage,ivMain;
	private ImageView ivFirst,ivSecond,ivthird;
	private Button btnPromoteNow;
	private LinearLayout layout3Images;
	private RelativeLayout layoutCapture;
	private AppSession appSession;
	private ImageLoader imageLoader ;
	private static String type="i";
	private String f="f",t="t",i="i";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promote);
		
		initLayout();
		tvHeaderText.setText("Promote");
		tvPromteNow.setText("PROMOTE NOW");
		
		appSession =new AppSession(this);
		imageLoader =new ImageLoader(this);
		if(appSession.getConnections()!=null)
		{
			tvUserName.setText(appSession.getConnections().get(0).getUserName());
			if(appSession.getUserImageBaseUrl()!=null)
			{
				String url= appSession.getUserImageBaseUrl()+appSession.getConnections().get(0).getUserImage();
				Log.v(getClass().getSimpleName(), "UserImage URL="+url);
				
				imageLoader.DisplayImage(url, ((Activity)Promote.this), ivUserImage, ivUserImage.getMeasuredHeight(), 0, 0, R.drawable.user_small_icon);
			}
		}
		
		if(type.equals("i"))
		{
			ivInstagram.setEnabled(false);
		}
		
		if(isNetworkAvailable())
		{
			new TaskForgetPromoteImages().execute();
		}
		
		

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CLOSE_ALL");
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		}, intentFilter);
	}
	
	/* method for initialising init components */
	private void initLayout()
	{
		tvPromteNow =(TextView)findViewById(R.id.tv_next_header);
		tvHeaderText =(TextView)findViewById(R.id.tv_header);
		tvUserName =(TextView)findViewById(R.id.tv_username);
		
		ivMain =(ImageView)findViewById(R.id.user_image);
		ivFirst =(ImageView)findViewById(R.id.imageView1);
		ivSecond =(ImageView)findViewById(R.id.imageView2);
		ivthird =(ImageView)findViewById(R.id.imageView3);
		ivUserImage =(ImageView)findViewById(R.id.iv_user_image);
		ivFacebook =(ImageView)findViewById(R.id.facebook_promote);
		ivTwitter =(ImageView)findViewById(R.id.twitter_promote);
		ivInstagram =(ImageView)findViewById(R.id.instagram_promote);
		ivBack =(ImageView)findViewById(R.id.iv_back_header);
		
		btnPromoteNow =(Button)findViewById(R.id.btn_promote_now);
		
		layout3Images =(LinearLayout)findViewById(R.id.layout3images);
		layoutCapture =(RelativeLayout)findViewById(R.id.rl_post_view);
		
		tvPromteNow.setOnClickListener(this);
		ivFacebook.setOnClickListener(this);
		ivTwitter.setOnClickListener(this);
		ivInstagram.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		btnPromoteNow.setOnClickListener(this);
		
		ivInstagram.setEnabled(true);
		ivTwitter.setEnabled(true);
		ivFacebook.setEnabled(true);
		
	}

	/*perform click*/
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_next_header:
			promote();
			break;
		case R.id.facebook_promote:
			
			if(v.isEnabled())
			{
				type=f;
			}
			ivInstagram.setEnabled(true);
			ivTwitter.setEnabled(true);
			ivFacebook.setEnabled(false);
			break;
		case R.id.twitter_promote:
			if(v.isEnabled())
			{
				type=t;
			}
			ivInstagram.setEnabled(true);
			ivTwitter.setEnabled(false);
			ivFacebook.setEnabled(true);
			break;
		case R.id.instagram_promote:
			if(v.isEnabled())
			{
				type=i;
			}
			ivInstagram.setEnabled(false);
			ivTwitter.setEnabled(true);
			ivFacebook.setEnabled(true);
			break;
		case R.id.iv_back_header:
			finish();
			break;
		case R.id.btn_promote_now:
			promote();
			break;
		}
	}
	
	
	private void promote(){
		if(isNetworkAvailable()){
		if(type.equals(f))
		{
			shareOnFacebook(captureImage(), "");
		}
		else if(type.equals(t))
		{
			shareonTwitter(captureImage(), "");
		}
		else if(type.equals(i))
		{
			shareInstagram(captureImage(), "");
		}}
		else
		{
			Toast.makeText(Promote.this,
					getString(R.string.NETWORK_ERROR),
					Toast.LENGTH_LONG).show();
		}
	}
	private Bitmap captureImage()
	{
		Bitmap bmp;
		View v1 = layoutCapture;
		v1.setDrawingCacheEnabled(true);
		bmp = Bitmap.createBitmap(v1.getDrawingCache());
		v1.setDrawingCacheEnabled(false);
		return bmp;
	}
	


	/*method for share on fb*/
	public void shareOnFacebook(Bitmap bmp ,String url) {
		String urlToShare = "YOUR_URL";
		boolean hasThirdpartyApp = false;
		Intent shareIntent = new Intent();
		shareIntent.setClassName("com.facebook.katana","com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
		shareIntent.setAction("android.intent.action.SEND");
		shareIntent.setType("image/*");
		Bundle b = new Bundle();
//		shareIntent.setType("text/plain");
//		shareIntent.putExtra("android.intent.extra.TEXT", url);
		b.putParcelable("android.intent.extra.STREAM",Uri.fromFile(storeToSDCard(bmp)));
		shareIntent.putExtras(b);
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		for (final ResolveInfo app : activityList) {
			if ((app.activityInfo.name).contains("facebook")) {
				hasThirdpartyApp = true;
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				shareIntent.setComponent(name);
				startActivity(shareIntent);
				break;
			}
		}
		if (!hasThirdpartyApp)
			createConfirmDialog("https://play.google.com/store/apps/details?id=com.facebook.katana",Promote.this);
	}

	/*method for store file to sdcard*/
	private File storeToSDCard(Bitmap bitmap ) {
	
		
		// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		// byte data1[]=bitmap.getRowBytes();
		FileOutputStream fo;
		try {
			fo = new FileOutputStream("/mnt/sdcard/reloved_ll.jpg");
			fo.write(bytes.toByteArray());
			// remember close de FileOutput
			fo.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = new File("/mnt/sdcard/reloved_ll.jpg");

		return file;

	}
	
	/*method for share on twitter*/
	private void shareonTwitter(Bitmap bmp ,String url)
	{
	 if (verificaTwitter()) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("image/*"); 				
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(storeToSDCard(bmp)));
			shareIntent.putExtra(Intent.EXTRA_TEXT, "QUIVU");
			shareIntent.setPackage("com.twitter.android");
			startActivity(shareIntent);
		} else {
			createConfirmDialog("https://play.google.com/store/apps/details?id=com.twitter.android",Promote.this);
					
		}}
	
	/*method for share on instagram*/
	private void shareInstagram(Bitmap bmp ,String url)
	{
		if (verificaInstagram()) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("image/*"); 				
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(storeToSDCard(bmp)));
			shareIntent.putExtra(Intent.EXTRA_TEXT, "QUIVU");
			shareIntent.setPackage("com.instagram.android");
			startActivity(shareIntent);
		} else {
			createConfirmDialog("https://play.google.com/store/apps/details?id=com.instagram.android&hl=en",Promote.this);
				
		}
	}
	private boolean verificaInstagram() {
		boolean instalado = false;
		try {
			@SuppressWarnings("unused")
			ApplicationInfo info = getPackageManager().getApplicationInfo("com.instagram.android", 0);
			instalado = true;
		} catch (NameNotFoundException e) {
			instalado = false;
		}
		return instalado;
	}
	private boolean verificaTwitter() {
		boolean instalado = false;
		try {
			@SuppressWarnings("unused")
			ApplicationInfo info = getPackageManager().getApplicationInfo("com.twitter.android", 0);
			instalado = true;
		} catch (NameNotFoundException e) {
			instalado = false;
		}
		return instalado;
	}
	
	//Task for promote images
	private class TaskForgetPromoteImages extends AsyncTask<Void, Void, String[]> {
		ProgressDialog pd = null;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try {
				//pd = ProgressDialog.show(context, "",getResources().getString(R.string.please_wait));
				pd = ProgressDialog.show(Promote.this, null, null);
				pd.setContentView(R.layout.progressloader);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected String[] doInBackground(Void... params) {
			String[] responde=null;
			try {			
				AppSession appSession=new AppSession(Promote.this);
				responde=new UtilityDAO(Promote.this).getPromote(appSession.getBaseUrl(),getResources().getString(R.string.method_getPromotion),
						appSession.getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return responde;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);			
			try {
				pd.dismiss();
		
				if(result!=null)
				{
					if(result.length>0)
					{
						if(result.length==3)
						{
							layout3Images.setVisibility(View.VISIBLE);
							String url =appSession.getProductBaseUrl();
							if(!result[0].equals(""))
							{
							imageLoader.DisplayImage(url+result[0], ((Activity)Promote.this), ivFirst, 0, 0, 0, R.drawable.no_image);
							}
							if(!result[1].equals(""))
							{
							imageLoader.DisplayImage(url+result[1], ((Activity)Promote.this), ivSecond, 0, 0, 0, R.drawable.no_image);
							}
							if(!result[2].equals(""))
							{
							imageLoader.DisplayImage(url+result[2], ((Activity)Promote.this), ivthird, 0, 0, 0, R.drawable.no_image);
							}
							
							}
						else
						{
							layout3Images.setVisibility(View.GONE);
							String url =appSession.getProductBaseUrl();
							if(!result[0].equals(""))
							{
							imageLoader.DisplayImage(url+result[0], ((Activity)Promote.this), ivMain, 0, 0, 0, R.drawable.no_image);
							}
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
