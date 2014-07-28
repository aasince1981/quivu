//package com.it.reloved;
//
//import java.io.File;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.PorterDuff.Mode;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//
//public class CameraEffects extends RelovedPreference implements OnClickListener {
//
//	ImageView ivBack, ivMagic, ivRotate, ivNext, ivCaptured, ivBrightness,
//			ivSaturation, ivContrast, ivSharpening, ivVigenneting,ivRight,ivCancel;
//	LinearLayout layoutFooter,layoutSeekbaar;
//	TextView tvName;
//	SeekBar seekBar;
//	Bitmap bitmapMaster;
//	public static File outputfile=null;
//	public static int progressBrightness=0,progressSaturation=0,progressContrast=0,
//			progressSharpening=0,progressVignetting=0;
//	public static int temp=0,tempMagic=0;
//	int angle=0;
//	Intent intent=null;
//	String imageCount="";
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.camera_image);
//		
//		intent=getIntent();
//		if (intent!=null) {
//			imageCount=intent.getStringExtra("imageCount");
//		}
//
//		/* method for initialising init components */
//		ivBack = (ImageView) findViewById(R.id.iv_back_camera_image);
//		ivMagic = (ImageView) findViewById(R.id.iv_magic_camera_image);
//		ivRotate = (ImageView) findViewById(R.id.iv_rotate_camera_image);
//		ivNext = (ImageView) findViewById(R.id.iv_next_camera_image);
//		ivCaptured = (ImageView) findViewById(R.id.iv_captured_camera_image);
//		ivBrightness = (ImageView) findViewById(R.id.iv_brightness_camera_image);
//		ivSaturation = (ImageView) findViewById(R.id.iv_saturation_camera_image);
//		ivContrast = (ImageView) findViewById(R.id.iv_contrast_camera_image);
//		ivSharpening = (ImageView) findViewById(R.id.iv_sharpning_camera_image);
//		ivVigenneting = (ImageView) findViewById(R.id.iv_vignetting_camera_image);
//		ivRight= (ImageView) findViewById(R.id.iv_right_camera_image);
//		ivCancel= (ImageView) findViewById(R.id.iv_cancel_camera_image);
//		
//		layoutFooter=(LinearLayout)findViewById(R.id.layout_footer_camera_image);
//		layoutSeekbaar=(LinearLayout)findViewById(R.id.layout_seekbaar);
//		layoutSeekbaar.setVisibility(View.GONE);
//		
//		tvName=(TextView)findViewById(R.id.tv_name);
//		seekBar=(SeekBar)findViewById(R.id.seekbar_camera_image);
//		seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
//		
//		ivBack.setOnClickListener(this);
//		ivMagic.setOnClickListener(this);
//		ivRotate.setOnClickListener(this);
//		ivNext.setOnClickListener(this);
//		ivBrightness.setOnClickListener(this);
//		ivSaturation.setOnClickListener(this);
//		ivContrast.setOnClickListener(this);
//		ivSharpening.setOnClickListener(this);
//		ivVigenneting.setOnClickListener(this);
//		ivRight.setOnClickListener(this);
//		ivCancel.setOnClickListener(this);
//		
//		if(outputfile.exists()){
//			bitmapMaster = decodeFile(outputfile,640, 960);		   
//			ivCaptured.setImageBitmap(bitmapMaster);
//		}
//	
//		/*setting progress of seekbaar*/
//		if (bitmapMaster != null) {			
//			//for brightness
//			progressBrightness=50;						
//			ivCaptured.setImageBitmap(doBrightness(bitmapMaster, progressBrightness));
//			
//			//for Saturation
//			progressSaturation=50;		
//			float sat = (float) progressSaturation/ 256;
//			ivCaptured.setImageBitmap(updateSat(bitmapMaster, sat));				
//			
//			//for contrast
//			progressContrast=50;	
//			ivCaptured.setImageBitmap(createContrast(bitmapMaster,progressContrast ));
//			
//			//for Sharpen
//			progressSharpening=0;
//			ivCaptured.setImageBitmap(sharpen(bitmapMaster,progressSharpening ));
//			
//			//for Rounded corner
//			progressVignetting=0;	
//			ivCaptured.setImageBitmap(roundCorner(bitmapMaster,progressVignetting));
//		}
//		
//		
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("CLOSE_ALL");
//		registerReceiver(new BroadcastReceiver() {
//			@Override
//			public void onReceive(Context context, Intent intent) {
//				finish();
//			}
//		}, intentFilter);
//		
//	}
//	
//	/*change value on seekbar progress change*/
//	OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
//
//		@Override
//		public void onProgressChanged(SeekBar seekBar, int progress,
//				boolean fromUser) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onStartTrackingTouch(SeekBar seekBar) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onStopTrackingTouch(SeekBar seekBar) {
//			if (bitmapMaster != null) {
//				if(temp==1){
//				//for brightness							
//				ivCaptured.setImageBitmap(doBrightness(bitmapMaster, seekBar.getProgress()));
//				}else if(temp==2){
//				//for Saturation					
//				float sat = (float) (seekBar.getProgress())/ 256;
//				ivCaptured.setImageBitmap(updateSat(bitmapMaster, sat));				
//				}else if(temp==3){
//				//for contrast				
//				ivCaptured.setImageBitmap(createContrast(bitmapMaster,seekBar.getProgress() ));
//				}else if(temp==4){
//				//for Sharpen				
//				ivCaptured.setImageBitmap(sharpen(bitmapMaster,seekBar.getProgress()));
//				}else if(temp==5){
//				//for Rounded corner				
//				ivCaptured.setImageBitmap(roundCorner(bitmapMaster,seekBar.getProgress()));
//				}
//				/*//For shining
//				applyMeanRemoval(bitmapMaster);*/				
//			}
//		}
//	};
//
//	/*perform click*/
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.iv_back_camera_image:
//				finish();
//			break;
//		case R.id.iv_magic_camera_image:
//			if(tempMagic==0){
//				ivCaptured.setImageBitmap(applyMeanRemoval(bitmapMaster));
//				tempMagic=1;
//			}else {
//				ivCaptured.setImageBitmap(bitmapMaster);
//				tempMagic=0;
//			}
//			break;
//		case R.id.iv_rotate_camera_image:
//			angle=angle+90;
//			ivCaptured.setImageBitmap(rotate(bitmapMaster, angle));
//			if(angle==360)
//				angle=0;
//			break;
//		case R.id.iv_next_camera_image:
//			intent=new Intent(CameraEffects.this, AddProduct.class);
//			intent.putExtra("imagePath", outputfile.getAbsolutePath());
//			intent.putExtra("imageCount",imageCount);
//			startActivity(intent);
//			break;
//		case R.id.iv_brightness_camera_image:
//			temp=1;
//			tvName.setText("BRIGHTNESS");
//			seekBar.setProgress(progressBrightness);
//			layoutFooter.setVisibility(View.GONE);
//			layoutSeekbaar.setVisibility(View.VISIBLE);
//			break;
//		case R.id.iv_saturation_camera_image:
//			temp=2;
//			tvName.setText("SATURATION");
//			seekBar.setProgress(progressSaturation);
//			layoutFooter.setVisibility(View.GONE);
//			layoutSeekbaar.setVisibility(View.VISIBLE);
//			break;
//		case R.id.iv_contrast_camera_image:
//			temp=3;
//			tvName.setText("CONTRAST");
//			seekBar.setProgress(progressContrast);
//			layoutFooter.setVisibility(View.GONE);
//			layoutSeekbaar.setVisibility(View.VISIBLE);
//			break;
//		case R.id.iv_sharpning_camera_image:
//			temp=4;
//			tvName.setText("SHARPENING");
//			seekBar.setProgress(progressSharpening);
//			layoutFooter.setVisibility(View.GONE);
//			layoutSeekbaar.setVisibility(View.VISIBLE);
//			break;
//		case R.id.iv_vignetting_camera_image:
//			temp=5;
//			tvName.setText("VIGNETTING");
//			seekBar.setProgress(progressVignetting);
//			layoutFooter.setVisibility(View.GONE);
//			layoutSeekbaar.setVisibility(View.VISIBLE);
//			break;
//		case R.id.iv_cancel_camera_image:
//			layoutFooter.setVisibility(View.VISIBLE);
//			layoutSeekbaar.setVisibility(View.GONE);
//		break;
//		case R.id.iv_right_camera_image:
//			layoutFooter.setVisibility(View.VISIBLE);
//			layoutSeekbaar.setVisibility(View.GONE);
//			if (temp==1) {
//				progressBrightness=seekBar.getProgress();
//			} else if (temp==2){
//				progressSaturation=seekBar.getProgress();
//			}else if (temp==3){
//				progressContrast=seekBar.getProgress();
//			}else if (temp==4){
//				progressSharpening=seekBar.getProgress();
//			}else if (temp==5){
//				progressVignetting=seekBar.getProgress();
//			}
//		break;
//
//		default:
//			break;
//		}
//	}
//	
//	/*method for magic effect*/
//	public static Bitmap smooth(Bitmap src, double value) {
//	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
//	    convMatrix.setAll(1);
//	    convMatrix.Matrix[1][1] = value;
//	    convMatrix.Factor = value + 8;
//	    convMatrix.Offset = 1;
//	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
//	}
//	
//	public static Bitmap applyMeanRemoval(Bitmap src) {
//	    double[][] MeanRemovalConfig = new double[][] {
//	        { -1 , -1, -1 },
//	        { -1 ,  9, -1 },
//	        { -1 , -1, -1 }
//	    };
//	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
//	    convMatrix.applyConfig(MeanRemovalConfig);
//	    convMatrix.Factor = 1;
//	    convMatrix.Offset = 0;
//	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
//	}
//	
//	/*method for round corner of bitmap*/
//	public static Bitmap roundCorner(Bitmap src, float round) {
//	    // image size
//	    int width = src.getWidth();
//	    int height = src.getHeight();
//	    // create bitmap output
//	    Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//	    // set canvas for painting
//	    Canvas canvas = new Canvas(result);
//	    canvas.drawARGB(0, 0, 0, 0);
//	 
//	    // config paint
//	    final Paint paint = new Paint();
//	    paint.setAntiAlias(true);
//	    paint.setColor(Color.BLACK);
//	 
//	    // config rectangle for embedding
//	    final Rect rect = new Rect(0, 0, width, height);
//	    final RectF rectF = new RectF(rect);
//	 
//	    // draw rect to canvas
//	    canvas.drawRoundRect(rectF, round, round, paint);
//	 
//	    // create Xfer mode
//	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//	    // draw source image to canvas
//	    canvas.drawBitmap(src, rect, rect, paint);
//	 
//	    // return final image
//	    return result;
//	}
//	
//	/*method for sharpening*/
//	public static Bitmap sharpen(Bitmap src, double weight) {
//	    double[][] SharpConfig = new double[][] {
//	        { 0 , -2    , 0  },
//	        { -2, weight, -2 },
//	        { 0 , -2    , 0  }
//	    };
//	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
//	    convMatrix.applyConfig(SharpConfig);
//	    convMatrix.Factor = weight - 8;
//	    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
//	}
//	
//	/*method for contrast*/
//	public static Bitmap createContrast(Bitmap src, double value) {
//	    // image size
//	    int width = src.getWidth();
//	    int height = src.getHeight();
//	    // create output bitmap
//	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
//	    // color information
//	    int A, R, G, B;
//	    int pixel;
//	    // get contrast value
//	    double contrast = Math.pow((100 + value) / 100, 2);
//	 
//	    // scan through all pixels
//	    for(int x = 0; x < width; ++x) {
//	        for(int y = 0; y < height; ++y) {
//	            // get pixel color
//	            pixel = src.getPixel(x, y);
//	            A = Color.alpha(pixel);
//	            // apply filter contrast for every channel R, G, B
//	            R = Color.red(pixel);
//	            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	            if(R < 0) { R = 0; }
//	            else if(R > 255) { R = 255; }
//	 
//	            G = Color.red(pixel);
//	            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	            if(G < 0) { G = 0; }
//	            else if(G > 255) { G = 255; }
//	 
//	            B = Color.red(pixel);
//	            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
//	            if(B < 0) { B = 0; }
//	            else if(B > 255) { B = 255; }
//	 
//	            // set new pixel color to output bitmap
//	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
//	        }
//	    }
//	 
//	    // return final image
//	    return bmOut;
//	}
//
//	/*method for rotate bitmap*/
//	public static Bitmap rotate(Bitmap src, float degree) {
//	    // create new matrix
//	    Matrix matrix = new Matrix();
//	    // setup rotation degree
//	    matrix.postRotate(degree);
//	 
//	    // return new bitmap rotated using matrix
//	    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
//	}
//	
//	/*method for saturation*/
//	private Bitmap updateSat(Bitmap src, float settingSat) {
//
//		int w = src.getWidth();
//		int h = src.getHeight();
//
//		Bitmap bitmapResult = 
//				Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//		Canvas canvasResult = new Canvas(bitmapResult);
//		Paint paint = new Paint();
//		ColorMatrix colorMatrix = new ColorMatrix();
//		colorMatrix.setSaturation(settingSat);
//		ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
//		paint.setColorFilter(filter);
//		canvasResult.drawBitmap(src, 0, 0, paint);
//
//		return bitmapResult;
//	}
//	
//	/*method for brightness*/
//	public static Bitmap doBrightness(Bitmap src, int value) {
//	    // image size
//	    int width = src.getWidth();
//	    int height = src.getHeight();
//	    // create output bitmap
//	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
//	    // color information
//	    int A, R, G, B;
//	    int pixel;
//	 
//	    // scan through all pixels
//	    for(int x = 0; x < width; ++x) {
//	        for(int y = 0; y < height; ++y) {
//	            // get pixel color
//	            pixel = src.getPixel(x, y);
//	            A = Color.alpha(pixel);
//	            R = Color.red(pixel);
//	            G = Color.green(pixel);
//	            B = Color.blue(pixel);
//	 
//	            // increase/decrease each channel
//	            R += value;
//	            if(R > 255) { R = 255; }
//	            else if(R < 0) { R = 0; }
//	 
//	            G += value;
//	            if(G > 255) { G = 255; }
//	            else if(G < 0) { G = 0; }
//	 
//	            B += value;
//	            if(B > 255) { B = 255; }
//	            else if(B < 0) { B = 0; }
//	 
//	            // apply new pixel color to output bitmap
//	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
//	        }
//	    }
//	 
//	    // return final image
//	    return bmOut;
//	}
//}
