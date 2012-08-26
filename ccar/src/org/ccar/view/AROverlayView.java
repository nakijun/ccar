package org.ccar.view;

import java.util.ArrayList;
import java.util.List;

import org.ccar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;

/**
 * AR上的覆盖层
 * @author swansword
 *
 */
public class AROverlayView extends View implements Runnable {
	SensorManager sensorManager;
	List<Bitmap> iconList;
	int showIndex = 0;
	

	public AROverlayView(Context context, SensorManager sensorManager) {
		super(context);
		
		this.sensorManager = sensorManager;
		
		initSensorManager();
		
		initIconList();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < iconList.size(); i++) {
			canvas.drawBitmap(iconList.get(i), 100 + i * 30, 100, null);
		}
		
		canvas.drawBitmap(iconList.get(showIndex), 200, 200, null);
	}

	@Override
	public void run() {
		while (true) {
			showIndex = (showIndex + 1) % iconList.size();
			this.postInvalidate();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 初始化POI图标
	 */
	private void initIconList() {
		iconList = new ArrayList<Bitmap>();
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.ck));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cs));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cy));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.fw));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.hc));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.jd));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.jz));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.mt));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.ql));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.rk));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.tcc));
		iconList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.zxc));
	}
	
	/**
	 * 初始化传感器
	 */
	private void initSensorManager() {
//		sensorManager.registerListener(sensorEventListener, 
//				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), 
//				SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	

}
