package org.ccar.view;

import java.util.ArrayList;
import java.util.List;

import org.ccar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;

/**
 * AR�ϵĸ��ǲ�
 * @author swansword
 *
 */
public class AROverlayView extends View implements Runnable {
	SensorManager sensorManager;
	List<Bitmap> iconList;
	int showIndex = 0;
	int i = 0, j = 0;
	

	public AROverlayView(Context context, SensorManager sensorManager) {
		super(context);
		
		this.sensorManager = sensorManager;
		
		initIconList();
		
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
	}

	@Override
	public void run() {
		while (true) {
//			showIndex = (showIndex + 1) % iconList.size();
//			if (getWidth() != 0 && getHeight() != 0) {
//				i = (i + 1) % getWidth();
//				j = (j + 1) % getHeight();
//			}
			this.postInvalidate();
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	/**
	 * ��ʼ��POIͼ��
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
	 * ���õ�ǰ�ֻ��ķ���
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
			System.out.println(orientationValue[0] + ", " + orientationValue[1] + ", " + orientationValue[2]+ ", " + j);
//		}
	}
	

}
