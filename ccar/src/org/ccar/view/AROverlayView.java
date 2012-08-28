package org.ccar.view;

import java.util.ArrayList;
import java.util.List;

import org.ccar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
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
	private static final int RADAR_PADDING = 30;
	SensorManager sensorManager;
	List<Bitmap> iconList;
	int showIndex = 0;
	int i = 0, j = 0;
	
	Bitmap radar;
	Bitmap compass;
	Matrix compassMatrix = new Matrix();
	int rotate = 0;
	

	public AROverlayView(Context context, SensorManager sensorManager) {
		super(context);
		
		this.sensorManager = sensorManager;
		
		initIconList();
		
		radar = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_plain);
		compass = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_needle);
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		for (int i = 0; i < iconList.size(); i++) {
//			canvas.drawBitmap(iconList.get(i), 100 + i * 30, 100, null);
//		}
//		
		if (getWidth() != 0) {
			canvas.drawBitmap(iconList.get(0), 400, j, null);
		}
		
		compassMatrix.reset();
		
		compassMatrix.setRotate(90, RADAR_PADDING + radar.getWidth() / 2, RADAR_PADDING + radar.getHeight() / 2);
//		compassMatrix.postTranslate(75, 16);
		canvas.drawBitmap(radar, RADAR_PADDING, RADAR_PADDING, null);
		
//		System.out.println(radar.getWidth() + ", " + compass.getWidth());
//		Bitmap compass1 = Bitmap.createBitmap(compass, 0, 0, compass.getWidth(), compass.getHeight(), compassMatrix, true);
		canvas.drawBitmap(compass, compassMatrix, null);
	}

	@Override
	public void run() {
		while (true) {
//			showIndex = (showIndex + 1) % iconList.size();
//			if (getWidth() != 0 && getHeight() != 0) {
//				i = (i + 1) % getWidth();
//				j = (j + 1) % getHeight();
//			}
			rotate++;
			this.postInvalidate();
			try {
				Thread.sleep(100);
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
	 * 设置当前手机的方向
	 * @param orientationValue
	 */
	public void setOrientation(float[] orientationValue) {
		orientationToScreenXY(orientationValue);
	}
	
	private void orientationToScreenXY(float[] orientationValue) {
//		Camera.Parameters param =  CameraPreview.camaraParam;
//		System.out.println(orientationValue[0] + ", " + orientationValue[1] + ", " + orientationValue[2]);
//		if (param != null && Math.abs(orientationValue[2]) != 0) {
//			float vvAngle = param.getVerticalViewAngle();
		double alpha = 21.25;
		double beta = Math.PI / 2 + orientationValue[2];
		int h = 240;
		j = (int) (h * (Math.tan(Math.toRadians(alpha)) - Math.tan(beta)) / (2 * Math.tan(Math.toRadians(alpha))));
//			System.out.println(orientationValue[0] + ", " + orientationValue[1] + ", " + orientationValue[2]+ ", " + j);
//		}
	}
	

}
