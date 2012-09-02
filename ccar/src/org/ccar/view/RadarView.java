package org.ccar.view;

import org.ccar.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.SensorManager;
import android.view.View;

/**
 * À×´ïÍ¼²ã
 * @author swansword
 *
 */
public class RadarView extends View implements Runnable {
	private static final int RADAR_PADDING = 30;
	SensorManager sensorManager;
	
	Bitmap radar;		// À×´ïÍ¼Æ¬
	Bitmap compass;		// Ö¸ÄÏÕëÍ¼Æ¬
	Matrix compassMatrix = new Matrix();	// Ö¸ÄÏÕëÐý×ª¾ØÕó

	public RadarView(Context context, SensorManager sensorManager) {
		super(context);

		this.sensorManager = sensorManager;
		
		radar = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_plain);
		compass = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_needle);
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		compassMatrix.reset();
		
//		compassMatrix.setRotate(90, RADAR_PADDING + radar.getWidth() / 2, RADAR_PADDING + radar.getHeight() / 2);
//		compassMatrix.postTranslate(75, 16);
		canvas.drawBitmap(radar, RADAR_PADDING, RADAR_PADDING, null);
		
//		System.out.println(radar.getWidth() + ", " + compass.getWidth());
//		Bitmap compass1 = Bitmap.createBitmap(compass, 0, 0, compass.getWidth(), compass.getHeight(), compassMatrix, true);
//		canvas.drawBitmap(compass, compassMatrix, null);
	}

	@Override
	public void run() {
		while (true) {
			this.postInvalidate();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOrientation(float[] orientationValue) {
		
	}

}
