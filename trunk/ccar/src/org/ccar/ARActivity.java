package org.ccar;

import org.ccar.view.AROverlayView;
import org.ccar.view.CameraPreview;
import org.ccar.view.RadarView;
import org.codehaus.jackson.JsonParser;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
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
    private CameraPreview mPreview; 		// 摄像头预览界面
    private AROverlayView overlayView; 		// 叠加层
    private RadarView radarView;			// 雷达图
    private SensorManager sensorManager;	// 传感器管理器
    LocationManager locationManager;		// 位置管理器
    
    float[] gravity = new float[3]; 	// 加速度计测量值
    float[] geomagnetic = new float[3];	// 磁场传感器测量值
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.ar);
		
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		
//		mCamera = getCameraInstance();
		
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        mPreview = new CameraPreview(this);
        preview.addView(mPreview);
        
        FrameLayout overlay = (FrameLayout) findViewById(R.id.ar_overlay);
        overlayView = new AROverlayView(this, sensorManager);
        overlay.addView(overlayView);
        
        FrameLayout radar = (FrameLayout) findViewById(R.id.ar_radar);
        radarView = new RadarView(this, sensorManager);
        radar.addView(radarView);
        
        
        sensorManager.registerListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorEventListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(sensorEventListener, 
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 传感器监听器
	 */
	private final SensorEventListener sensorEventListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
	        case Sensor.TYPE_ACCELEROMETER:
	        	gravity = event.values.clone();
	        case Sensor.TYPE_MAGNETIC_FIELD:
	        	geomagnetic = event.values.clone();
	            break;
	        default:
	            break;
			}
			
			float[] values = new float[3]; 
	        float[] R = new float[9]; 
	        SensorManager.getRotationMatrix(R, null, gravity, geomagnetic); 
	        SensorManager.getOrientation(R, values); 
	        
//	        values[0] = (float) Math.toDegrees(values[0]); 
//	        values[1] = (float) Math.toDegrees(values[1]); 
//	        values[2] = (float) Math.toDegrees(values[2]); 
//	        System.out.println(values[0] + ", " + values[1] + ", " + values[2]);
//	        if ((int)values[0] != 0)
	        overlayView.setOrientation(values);
	        radarView.setOrientation(values);
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 获取当前位置
	 */
	private void receiveCurrentLocation() {
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
		
		// 分别通过基站、GPS获取当前位置
//		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 100, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
	}
	
	private final LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		
		@Override
		public void onProviderEnabled(String provider) {
		}
		
		@Override
		public void onProviderDisabled(String provider) {
		}
		
		@Override
		public void onLocationChanged(Location location) {
			radarView.setLocation(location);
		}
	};
}
