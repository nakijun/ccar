package org.ccar.view;

import java.util.List;

import org.ccar.R;
import org.ccar.app.CCARApplication;
import org.ccar.app.GeoCalcUtil;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.location.Location;
import android.view.View;

/**
 * �״�ͼ��
 * @author swansword
 *
 */
public class RadarView extends View implements Runnable {
	private static final int RADAR_PADDING = 30;	// �״����/�ϵľ���
	SensorManager sensorManager;		// ������������
	DatabaseManager dm;					// ���ݿ������
	
	Bitmap radar;						// �״�ͼƬ
	Bitmap compass;						// ָ����ͼƬ
	Matrix compassMatrix = new Matrix();// ָ������ת����
	Paint radarPaint = new Paint();		// �״�������������
	RectF rect;							// �״�������
	
	int rotate = 0; 					// ָ������ת�Ƕȣ�����Ϊ0����˳ʱ���0��360��
	boolean isLocationChanged = false;	// ��ʶ��ǰλ���Ѹ���

	public RadarView(Context context, SensorManager sensorManager) {
		super(context);

		this.sensorManager = sensorManager;
		
		Activity activity = (Activity)getContext();
		CCARApplication ccarApplication = (CCARApplication) activity.getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		radar = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_plain);
		compass = BitmapFactory.decodeResource(this.getResources(), R.drawable.compass_needle);
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// �����״�ͼ
		drawRadar(canvas);
		
		// ����ָ����
		drawCompass(canvas);
		
		// ���ƾ���
		drawScenicSpot(canvas);
	}
	
	/**
	 * �����״�
	 * @param canvas Canvas����
	 */
	private void drawRadar(Canvas canvas) {
		//����Բ
		canvas.drawBitmap(radar, RADAR_PADDING, RADAR_PADDING, null);
		
		//������������
		rect = new RectF(RADAR_PADDING, RADAR_PADDING, 
				RADAR_PADDING + radar.getWidth(), 
				RADAR_PADDING + radar.getHeight());
		radarPaint.setColor(Color.argb(100, 0, 0, 0));
		float cameraHorizontalViewAngle = CameraPreview.camaraParam.getHorizontalViewAngle();
		
		// drawArc�����ĽǶ�����������Ϊ0�ȣ�˳ʱ����ת
		canvas.drawArc(rect, 
				270 - cameraHorizontalViewAngle / 2, 
				cameraHorizontalViewAngle, 
				true, radarPaint);
	}
	
	/**
	 * ����ָ����
	 * @param canvas Canvas����
	 */
	private void drawCompass(Canvas canvas) {
		compassMatrix.reset();
		
		// �Ƚ�ָ����ƽ�Ƶ��״�����Ϸ�
		compassMatrix.setTranslate(RADAR_PADDING + radar.getWidth() / 2 - compass.getWidth() / 2, 
				RADAR_PADDING - compass.getHeight());
		
		// �ٽ�ָ�������״�����ĵ���ת
		compassMatrix.postRotate(rotate, 
				RADAR_PADDING + radar.getWidth() / 2, 
				RADAR_PADDING + radar.getHeight() / 2);
		
		// ��ƽ�ƺ���ת���ָ������Ƴ���
		canvas.drawBitmap(compass, compassMatrix, null);
	}
	
	/**
	 * ���ƾ���
	 * @param canvas Canvas����
	 */
	private void drawScenicSpot(Canvas canvas) {
		if (isLocationChanged) {
			
		}
	}

	@Override
	public void run() {
		while (true) {
//			rotate = (rotate + 1) % 360;
			this.postInvalidate();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ���÷���ֵ
	 * @param orientationValue
	 */
	public void setOrientation(float[] orientationValue) {
		// ˮƽ����ǣ���Ϊ0������Ϊ180/-180������Ϊ90����Ϊ-90
		int horizontalOrientation = (int) Math.toDegrees(orientationValue[0]); 
		rotate = horizontalOrientation + 90;
	}
	
	/**
	 * ����λ��
	 * @param location �ֻ���ǰλ��
	 */
	public void setLocation(Location location) {
		double[] xy = GeoCalcUtil.WGS2flat(location.getLongitude(), location.getLatitude());
		List<ScenicSpot> spotList = dm.getScenicSpots(null, String.valueOf(xy[0]), String.valueOf(xy[1]));
		
		// �������о���
		for (ScenicSpot spot : spotList) {
			double deltaX = xy[0] - spot.getX();
			double deltaY = xy[1] - spot.getY();
			double distance = spot.getDistance();
			
		}
		isLocationChanged = true;
	}

}
