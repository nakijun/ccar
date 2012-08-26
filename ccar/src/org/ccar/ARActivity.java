package org.ccar;

import org.ccar.view.AROverlayView;
import org.ccar.view.CameraPreview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 增强现实
 * @author swansword
 * 
 */
public class ARActivity extends Activity {
	private Camera mCamera;
    private CameraPreview mPreview;
    AROverlayView overlayView;
    SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ar);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
		mCamera = getCameraInstance();
		
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        FrameLayout overlay = (FrameLayout) findViewById(R.id.ar_overlay);
        overlayView = new AROverlayView(this, sensorManager);
        Thread t = new Thread(overlayView);
        t.start();
        overlay.addView(overlayView);
        
        
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	/**
	 * 检测设备是否有摄像头
	 * @param context
	 * @return
	 */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // 有摄像头
	        return true;
	    } else {
	        // 没有摄像头
	        return false;
	    }
	}
	
	/**
	 * 获取摄像头
	 * @return 返回一个Camera实例，如果没有Camera或当前不可用，则返回null
	 */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open();
	    }
	    catch (Exception e){
	        e.printStackTrace();
	    }
	    return c;
	}
	
private final SensorEventListener sensorEventListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			float azimuth_angle = event.values[0];
		    float pitch_angle = event.values[1];
		    float roll_angle = event.values[2];
		    
		    Log.v("sensor", String.valueOf(azimuth_angle));
		    Log.v("sensor", String.valueOf(pitch_angle));
		    Log.v("sensor", String.valueOf(roll_angle));
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
}
