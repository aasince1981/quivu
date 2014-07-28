package com.it.reloved;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

public class CustomCamera extends Activity implements SensorEventListener,
		OnClickListener {
	String TAG = getClass().getName();
	Context context = this;
	private Camera mCamera;
	private CameraPreview mPreview;
	private SensorManager sensorManager = null;
	private int orientation;
	private ExifInterface exif;
	private int deviceHeight, deviceWidth;
	// private Button ibRetake;
	// private Button ibUse;
	// private Button ibCapture;
	// private Button btnCancel, btnCameraOption;
	private RelativeLayout flBtnContainer;
	// private File sdRoot;
	// private String dir;
	// private String fileName;
	private ImageView rotatingImage, ivFlashOption, ivCameraOption,ivCrossCamera,ivDivider;
	private int degrees = -1, numCameras = 1;
	public static int cameraId = 0;
	public static String flashType = Camera.Parameters.FLASH_MODE_AUTO;
	private ProgressBar pbCapture;
	//ZoomControls zoomControls;
	boolean linesFlag=false;
	Paint paint;
	Intent intent=null;
	 String imageCount="";
	File outputfile=null;
	
   // SeekBar seekBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_preview);
		try {
			intent=getIntent();
			if (intent!=null) {
				imageCount=intent.getStringExtra("imageCount");
			}
			/* method for initialising init components */
			paint=new Paint();
			String fileName = "reloved_item" + System.currentTimeMillis()+ ".jpg";
			String root = Environment.getExternalStorageDirectory().toString();
			new File(root + "/Reloved").mkdirs();
			outputfile = new File(root + "/Reloved/", fileName);
			
			// Getting all the needed elements from the layout
			rotatingImage = (ImageView) findViewById(R.id.iv_capture);
			// ibRetake = (Button) findViewById(R.id.ibRetake);
			// ibUse = (Button) findViewById(R.id.ibUse);
			// ibCapture = (Button) findViewById(R.id.ibCapture);
			ivFlashOption = (ImageView) findViewById(R.id.iv_flash_option);
			ivCameraOption = (ImageView) findViewById(R.id.iv_camera_option);
			ivCameraOption.setVisibility(View.GONE);
			flBtnContainer = (RelativeLayout) findViewById(R.id.flBtnContainer);
			pbCapture = (ProgressBar) findViewById(R.id.pb_capture);
			pbCapture.setVisibility(8);
		//	zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
		   // seekBar=(SeekBar)findViewById(R.id.seekBar);
			// Getting the sensor service.
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

			// Selecting the resolution of the Android device so we can create a
			// proportional preview
			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			deviceHeight = display.getHeight();
			deviceWidth = display.getWidth();
			// Add a listener to the Capture button
			rotatingImage.setOnClickListener(this);
			rotatingImage.setOnClickListener(this);
			
			ivCrossCamera = (ImageView) findViewById(R.id.iv_cross_camera);
			ivCrossCamera.setOnClickListener(this);
			ivFlashOption.setOnClickListener(this);
			ivCameraOption.setOnClickListener(this);
			
			ivDivider = (ImageView) findViewById(R.id.iv_divider_camera);
			ivDivider.setOnClickListener(this);

			if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
				numCameras = Camera.getNumberOfCameras();
			// Toast.makeText(context, " " + numCameras, Toast.LENGTH_LONG)
			// .show();
			if (numCameras == 2)
				ivCameraOption.setVisibility(View.VISIBLE);
			else
				ivCameraOption.setVisibility(View.GONE);
			
			
			
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("CLOSE_ALL");
			registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					finish();
				}
			}, intentFilter);

		} catch (Exception e) {
			Toast.makeText(this,
					"Something is happen unexpectedly. Please try again.",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
			finish();
		}
	}

	/*perform click*/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_cross_camera:
			finish();
			break;
		case R.id.iv_divider_camera:
			float widthFloat = (float) (deviceHeight) * 4 / 3;
			int width = Math.round(widthFloat);			
			FrameLayout preview1 = (FrameLayout) findViewById(R.id.camera_preview);
			if (linesFlag) {
				linesFlag=false;
				preview1.removeViewAt(1);
			} else {
				linesFlag=true;
				preview1.addView(new Lines(CustomCamera.this, width, deviceHeight),1);
			}						
			break;
		case R.id.iv_flash_option:
			// finish();
			if (mCamera != null)
				setFlashChange(mCamera);
			break;
		case R.id.iv_camera_option:
			if (cameraId == CameraInfo.CAMERA_FACING_BACK)
				cameraId = CameraInfo.CAMERA_FACING_FRONT;
			else
				cameraId = CameraInfo.CAMERA_FACING_BACK;
			// Toast.makeText(context, " " + cameraId,
			// Toast.LENGTH_LONG)
			// .show();
			releaseCamera();
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.removeViewAt(0);
			createCamera(cameraId);
			break;
		case R.id.iv_capture:
			 //seekBar.setVisibility(8);
			//zoomControls.setVisibility(8);
			flBtnContainer.setVisibility(8);
			pbCapture.setVisibility(0);
			mCamera.autoFocus(new AutoFocusCallback() {
				Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
					public void onShutter() {
						// If you want to play your own sound
						// (otherwise, it
						// plays the sound by default)
					}
				};

				@Override
				public void onAutoFocus(boolean arg0, Camera arg1) {
					mCamera.takePicture(shutterCallback, myPictureCallback_RAW,
							myPictureCallback_JPG);
				}
			});

			break;
		}
	}

	int maxZoomLevel = 0, currentZoomLevel = 0;

	private void createCamera(int camreId) {
		try { // Create an instance of Camera
			mCamera = getCameraInstance(cameraId);

			// Setting the right parameters in the camera
			final Camera.Parameters params = mCamera.getParameters();
			// params.setPictureSize(1600, 1200);
			ivFlashOption.setVisibility(8);
			if (params.getSupportedFlashModes() == null)
				ivFlashOption.setVisibility(8);
			else {
				ivFlashOption.setVisibility(8);
				if (flashType
						.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
					if (params.getSupportedFlashModes().contains(
							Camera.Parameters.FLASH_MODE_AUTO)) {
						params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
						ivFlashOption.setImageResource(R.drawable.flash_auto);
						ivFlashOption.setVisibility(0);
					} else
						ivFlashOption.setVisibility(8);
				} else if (flashType
						.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
					if (params.getSupportedFlashModes().contains(
							Camera.Parameters.FLASH_MODE_ON)) {
						params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
						ivFlashOption.setImageResource(R.drawable.flash_on);
						ivFlashOption.setVisibility(0);
					} else
						ivFlashOption.setVisibility(8);
				} else if (flashType.equals(Camera.Parameters.FLASH_MODE_OFF)) {
					if (params.getSupportedFlashModes().contains(
							Camera.Parameters.FLASH_MODE_OFF)) {
						params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						ivFlashOption.setImageResource(R.drawable.flash_off);
						ivFlashOption.setVisibility(0);
					} else
						ivFlashOption.setVisibility(8);
				} else
					ivFlashOption.setVisibility(8);
			}

			if (params.getSupportedSceneModes() != null
					&& params.getSupportedSceneModes().contains(
							Camera.Parameters.SCENE_MODE_AUTO))
				params.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
			if (params.getSupportedFocusModes() != null
					&& params.getSupportedFocusModes().contains(
							Camera.Parameters.FOCUS_MODE_AUTO))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			else if (params.getSupportedFocusModes() != null
					&& params.getSupportedFocusModes().contains(
							Camera.Parameters.FOCUS_MODE_AUTO))
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			if (params.getSupportedWhiteBalance() != null
					&& params.getSupportedWhiteBalance().contains(
							Camera.Parameters.WHITE_BALANCE_AUTO))
				params.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
			// params.setExposureCompensation(0);

			params.setPictureFormat(PixelFormat.JPEG);
			params.setJpegQuality(100);
			// params.setPictureSize(deviceWidth, deviceHeight);
			// List<Size> sizes = params.getSupportedPreviewSizes();
			// Size optimalSize = getOptimalPreviewSize(sizes,
			// getResources().getDisplayMetrics().widthPixels,
			// getResources().getDisplayMetrics().heightPixels);
			// params.setPreviewSize(optimalSize.width, optimalSize.height);

			// //////////////////////////////////////////////////////////////////////////

			 //seekBar.setVisibility(0);
//			if (params.isZoomSupported() && params.isSmoothZoomSupported()) {
//				// most phones
//				maxZoomLevel = params.getMaxZoom();
////				 Toast.makeText(this, "SmoothZoomSupported : " + maxZoomLevel,
////				 Toast.LENGTH_LONG).show();
//				 seekBar.setMax(maxZoomLevel);
//				 seekBar.setOnSeekBarChangeListener(new
//				 OnSeekBarChangeListener() {
//				
//				 @Override
//				 public void onStopTrackingTouch(SeekBar seekBar) {
//				
//				 }
//				
//				 @Override
//				 public void onStartTrackingTouch(SeekBar seekBar) {
//				
//				 }
//				
//				 @Override
//				 public void onProgressChanged(SeekBar seekBar, int progress,
//				 boolean fromUser) {
////				 Toast.makeText(context,"SmoothZoomSupported progress : "+progress,
////				 Toast.LENGTH_LONG).show();
//				 mCamera.startSmoothZoom(progress);
//				 mCamera.setParameters(params);
//				 }
//				 });
//
////				zoomControls.setIsZoomInEnabled(true);
////				zoomControls.setIsZoomOutEnabled(true);
////
////				zoomControls.setOnZoomInClickListener(new OnClickListener() {
////					public void onClick(View v) {
////						if (currentZoomLevel < maxZoomLevel) {
////							currentZoomLevel++;
////							mCamera.startSmoothZoom(currentZoomLevel);
////							mCamera.setParameters(params);
////							zoomControls.setIsZoomInEnabled(true);
////							zoomControls.setIsZoomOutEnabled(true);
////						} else {
////							zoomControls.setIsZoomInEnabled(false);
////							zoomControls.setIsZoomOutEnabled(true);
////						}
////					}
////				});
//
////				zoomControls.setOnZoomOutClickListener(new OnClickListener() {
////					public void onClick(View v) {
////						if (currentZoomLevel > 0) {
////							currentZoomLevel--;
////							mCamera.startSmoothZoom(currentZoomLevel);
////							mCamera.setParameters(params);
////							zoomControls.setIsZoomInEnabled(true);
////							zoomControls.setIsZoomOutEnabled(true);
////						} else {
////							zoomControls.setIsZoomInEnabled(true);
////							zoomControls.setIsZoomOutEnabled(false);
////						}
////					}
////				});
//			} else
				if (params.isZoomSupported()
					/*&& !params.isSmoothZoomSupported()*/) {
				// stupid HTC phones
				maxZoomLevel = params.getMaxZoom();
//				 Toast.makeText(this, "ZoomSupported : " + maxZoomLevel,
//				 Toast.LENGTH_LONG).show();
				// seekBar.setMax(maxZoomLevel);
				/* seekBar.setOnSeekBarChangeListener(new
				 OnSeekBarChangeListener() {
				
				 @Override
				 public void onStopTrackingTouch(SeekBar seekBar) {
				
				 }
				
				 @Override
				 public void onStartTrackingTouch(SeekBar seekBar) {
				
				 }
				
				 @Override
				 public void onProgressChanged(SeekBar seekBar, int progress,
				 boolean fromUser) {
//				 Toast.makeText(context,"ZoomSupported progress : "+progress,
//				 Toast.LENGTH_LONG).show();
				 params.setZoom(progress);
				 mCamera.setParameters(params);
				 }
				 });*/
//				zoomControls.setIsZoomInEnabled(true);
//				zoomControls.setIsZoomOutEnabled(true);
//
//				zoomControls.setOnZoomInClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						if (currentZoomLevel < maxZoomLevel) {
//							currentZoomLevel++;
//							params.setZoom(currentZoomLevel);
//							mCamera.setParameters(params);
//							zoomControls.setIsZoomInEnabled(true);
//							zoomControls.setIsZoomOutEnabled(true);
//						} else {
//							zoomControls.setIsZoomInEnabled(false);
//							zoomControls.setIsZoomOutEnabled(true);
//						}
//					}
//				});

//				zoomControls.setOnZoomOutClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						if (currentZoomLevel > 0) {
//							currentZoomLevel--;
//							params.setZoom(currentZoomLevel);
//							mCamera.setParameters(params);
//							zoomControls.setIsZoomInEnabled(true);
//							zoomControls.setIsZoomOutEnabled(true);
//						} else {
//							zoomControls.setIsZoomInEnabled(true);
//							zoomControls.setIsZoomOutEnabled(false);
//						}
//					}
//				});
			} else {
				// no zoom on phone
			//	zoomControls.setVisibility(8);
				// seekBar.setVisibility(8);
			}
			// /////////////////////////////////////////////////////////////////////////
			mCamera.setParameters(params);

			// Create our Preview view and set it as the content of our
			// activity.
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

			// Calculating the width of the preview so it is proportional.
			float widthFloat = (float) (deviceHeight) * 4 / 3;
			int width = Math.round(widthFloat);
			
			// Resizing the LinearLayout so we can make a proportional preview.
			// This
			// approach is not 100% perfect because on devices with a really
			// small
			// screen the the image will still be distorted - there is place for
			// improvment.
			// Toast.makeText(getApplicationContext(),
			// width + " - " + deviceHeight + " - " + deviceWidth,
			// Toast.LENGTH_LONG).show();

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					width-50, deviceHeight);
			// RelativeLayout.LayoutParams layoutParams = new
			// RelativeLayout.LayoutParams(
			// deviceWidth, deviceHeight);
			preview.setLayoutParams(layoutParams);

			// Adding the camera preview after the FrameLayout and before the
			// button
			// as a separated element.
			preview.addView(mPreview, 0);
		} catch (Exception e) {
			Toast.makeText(this,
					"Something is happen unexpectedly. Please try again.",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Creating the camera
		createCamera(cameraId);
		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// release the camera immediately on pause event
		releaseCamera();
		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.removeViewAt(0);
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	// /** Check if this device has a camera */
	// private boolean checkCameraHardware(Context context) {
	// if (context.getPackageManager().hasSystemFeature(
	// PackageManager.FEATURE_CAMERA)) {
	// // this device has a camera
	// return true;
	// } else {
	// // no camera on this device
	// return false;
	// }
	// }
	//
	// private boolean checkSDCard() {
	// boolean state = false;
	// String sd = Environment.getExternalStorageState();
	// if (Environment.MEDIA_MOUNTED.equals(sd)) {
	// state = true;
	// }
	// return state;
	// }

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance(int cameraId) {
		Camera c = null;
		try {
			// attempt to get a Camera instance
			c = Camera.open(cameraId);
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}

		// returns null if camera is unavailable
		return c;
	}

	PictureCallback myPictureCallback_RAW = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {

		}
	};
	PictureCallback myPictureCallback_JPG = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			try {				
				FileOutputStream purge = new FileOutputStream(outputfile);
				purge.write(data);
				purge.close();
			} catch (FileNotFoundException e) {
				Toast.makeText(context, "Error! Picture is not capture.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
				//finish();
			} catch (IOException e) {
				Toast.makeText(context, "Error! Picture is not capture.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
				//finish();
			}

			// Adding Exif data for the orientation. For some strange reason the
			// ExifInterface class takes a string instead of a file.
			try {
				// Toast.makeText(getApplicationContext(),
				// orientation+" "+degrees,
				// Toast.LENGTH_LONG).show();
				if (cameraId == CameraInfo.CAMERA_FACING_FRONT) {
					if (degrees == 270)
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
					else if (degrees == 90)
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
				}

				exif = new ExifInterface(outputfile.getPath());
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
						+ orientation);
				exif.saveAttributes();
				setResult(RESULT_OK);
				linesFlag=false;
				
				//Go back on AddProduct with Result
				Intent intent = new Intent();	
				intent.setData(Uri.fromFile(outputfile)) ;
				setResult(RESULT_OK, intent);
				finish();
				
				/*Intent intent=new Intent(CustomCamera.this, CameraEffects.class);
				intent.putExtra("imageCount", imageCount);				
				startActivity(intent);*/
				//finish();
			} catch (IOException e) {
				Toast.makeText(context, "Error! Picture is not capture.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
				//finish();
			}
			// Toast.makeText(getApplicationContext(), "SAVE",
			// Toast.LENGTH_LONG).show();
		}
	};
	
	/**
	 * Putting in place a listener so we can get the sensor data only when
	 * something changes.
	 */
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
						// Toast.makeText(getApplicationContext(), "1",
						// Toast.LENGTH_LONG).show();
					} else if (event.values[1] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
						// Toast.makeText(getApplicationContext(), "2",
						// Toast.LENGTH_LONG).show();
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0
							&& orientation != ExifInterface.ORIENTATION_NORMAL) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
						// Toast.makeText(getApplicationContext(), "3",
						// Toast.LENGTH_LONG).show();
					} else if (event.values[0] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
						// Toast.makeText(getApplicationContext(), "4",
						// Toast.LENGTH_LONG).show();
					}
				}
				if (animation != null) {
					//rotatingImage.startAnimation(animation);
					//ivCameraOption.startAnimation(animation);
					//ivFlashOption.startAnimation(animation);
				}
				// Toast.makeText(getApplicationContext(),
				// orientation+" "+degrees,
				// Toast.LENGTH_LONG).show();

			}

		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 * 
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation(float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees
				- compensation, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	/**
	 * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w,
			int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) h / w;

		if (sizes == null)
			return null;

		Camera.Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		for (Camera.Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Camera.Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	void setFlashChange(Camera mCamera) {
		final Camera.Parameters params = mCamera.getParameters();
		if (params.getSupportedFlashModes() == null)
			ivFlashOption.setVisibility(8);
		else {
			if (flashType.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
				flashType = Camera.Parameters.FLASH_MODE_ON;
				if (params.getSupportedFlashModes().contains(
						Camera.Parameters.FLASH_MODE_ON)) {
					params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
					ivFlashOption.setImageResource(R.drawable.flash_on);
					ivFlashOption.setVisibility(0);
					//showCustomToast(context, "SET FLASH ON");
				} else
					ivFlashOption.setVisibility(8);
			} else if (flashType
					.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
				flashType = Camera.Parameters.FLASH_MODE_OFF;
				if (params.getSupportedFlashModes().contains(
						Camera.Parameters.FLASH_MODE_OFF)) {
					params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					ivFlashOption.setImageResource(R.drawable.flash_off);
					ivFlashOption.setVisibility(0);
					//showCustomToast(context, "SET FLASH OFF");
				} else
					ivFlashOption.setVisibility(8);
			} else if (flashType.equals(Camera.Parameters.FLASH_MODE_OFF)) {
				flashType = Camera.Parameters.FLASH_MODE_AUTO;
				if (params.getSupportedFlashModes().contains(
						Camera.Parameters.FLASH_MODE_AUTO)) {
					params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
					ivFlashOption.setImageResource(R.drawable.flash_auto);
					ivFlashOption.setVisibility(0);
					//showCustomToast(context, "SET FLASH AUTO");
				} else
					ivFlashOption.setVisibility(8);
			} else
				ivFlashOption.setVisibility(8);
			mCamera.setParameters(params);
		}
	}

	public void showCustomToast(Context context, String message) {
		// Create layout inflator object to inflate toast.xml file
		LayoutInflater inflater = getLayoutInflater();
		// Call toast.xml file for toast layout
		View toastRoot;
		TextView textView;
		if (degrees == 90) {  
			/*toastRoot = inflater.inflate(R.layout.toast_vertical_reverse, null);
			textView = (TextView) toastRoot.findViewById(R.id.tv_toast_message);
			textView.setText("" + message);*/
			
			Toast.makeText(CustomCamera.this, message, Toast.LENGTH_LONG).show();

		} else if (degrees == 270) {
			/*toastRoot = inflater.inflate(R.layout.toast_vertical, null);
			textView = (TextView) toastRoot.findViewById(R.id.tv_toast_message);
			textView.setText("" + message);*/
			Toast.makeText(CustomCamera.this, message, Toast.LENGTH_LONG).show();
		} else {
			/*toastRoot = inflater.inflate(R.layout.toast, null);
			textView = (TextView) toastRoot.findViewById(R.id.tv_toast_message);
			textView.setText("" + message);
			textView.startAnimation(getRotateAnimation(degrees));*/
			Toast.makeText(CustomCamera.this, message, Toast.LENGTH_LONG).show();
		}

		/*Toast toast = new Toast(context);
		// Set layout to toast
		toast.setView(toastRoot);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();*/

	}
	
	public class Lines extends View{
		int screenWidth=0,screenHeight=0;
		public Lines(Context context,int width,int height) {
			super(context);
			// TODO Auto-generated constructor stub
			screenWidth=width;
			screenHeight=height;
			this.setWillNotDraw(false);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			
			 if(linesFlag){
			        //  Set paint options  
			        paint.setAntiAlias(true);  
			        paint.setStrokeWidth(3);  
			        paint.setStyle(Paint.Style.STROKE);  
			        paint.setColor(Color.WHITE/*argb(255, 255, 255, 255)*/);  

			        canvas.drawLine((screenWidth/3)*2,0,(screenWidth/3)*2,screenHeight,paint);
			        canvas.drawLine((screenWidth/3),0,(screenWidth/3),screenHeight,paint);
			        canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,paint);
			        canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),paint);
			}
		}
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		pbCapture.setVisibility(8);
		flBtnContainer.setVisibility(0);
	}
}