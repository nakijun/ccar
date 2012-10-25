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
 * 雷达图层
 * @author swansword
 *
 */
public class RadarView extends View implements Runnable {
	private static final int RADAR_PADDING = 30;	// 雷达距左/上的距离
	SensorManager sensorManager;		// 传感器管理器
	DatabaseManager dm;					// 数据库管理器
	
	Bitmap radar;						// 雷达图片
	Bitmap compass;						// 指南针图片
	Matrix compassMatrix = new Matrix();// 指南针旋转矩阵
	Paint radarPaint = new Paint();		// 雷达扇形区域的外观
	RectF rect;							// 雷达的外包框
	
	int rotate = 0; 					// 指南针旋转角度，向上为0，按顺时针从0到360。
	boolean isLocationChanged = false;	// 标识当前位置已更改

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
		// 绘制雷达图
		drawRadar(canvas);
		
		// 绘制指南针
		drawCompass(canvas);
		
		// 绘制景点
		drawScenicSpot(canvas);
	}
	
	/**
	 * 绘制雷达
	 * @param canvas Canvas对象
	 */
	private void drawRadar(Canvas canvas) {
		//绘制圆
		canvas.drawBitmap(radar, RADAR_PADDING, RADAR_PADDING, null);
		
		//绘制视域扇形
		rect = new RectF(RADAR_PADDING, RADAR_PADDING, 
				RADAR_PADDING + radar.getWidth(), 
				RADAR_PADDING + radar.getHeight());
		radarPaint.setColor(Color.argb(100, 0, 0, 0));
		float cameraHorizontalViewAngle = CameraPreview.camaraParam.getHorizontalViewAngle();
		
		// drawArc方法的角度以正东方向为0度，顺时针旋转
		canvas.drawArc(rect, 
				270 - cameraHorizontalViewAngle / 2, 
				cameraHorizontalViewAngle, 
				true, radarPaint);
	}
	
	/**
	 * 绘制指南针
	 * @param canvas Canvas对象
	 */
	private void drawCompass(Canvas canvas) {
		compassMatrix.reset();
		
		// 先将指南针平移到雷达的正上方
		compassMatrix.setTranslate(RADAR_PADDING + radar.getWidth() / 2 - compass.getWidth() / 2, 
				RADAR_PADDING - compass.getHeight());
		
		// 再将指南针绕雷达的中心点旋转
		compassMatrix.postRotate(rotate, 
				RADAR_PADDING + radar.getWidth() / 2, 
				RADAR_PADDING + radar.getHeight() / 2);
		
		// 将平移和旋转后的指南针绘制出来
		canvas.drawBitmap(compass, compassMatrix, null);
	}
	
	/**
	 * 绘制景点
	 * @param canvas Canvas对象
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
	 * 设置方向值
	 * @param orientationValue
	 */
	public void setOrientation(float[] orientationValue) {
		// 水平方向角，向东为0，向西为180/-180，向南为90，向北为-90
		int horizontalOrientation = (int) Math.toDegrees(orientationValue[0]); 
		rotate = horizontalOrientation + 90;
	}
	
	/**
	 * 设置位置
	 * @param location 手机当前位置
	 */
	public void setLocation(Location location) {
		double[] xy = GeoCalcUtil.WGS2flat(location.getLongitude(), location.getLatitude());
		List<ScenicSpot> spotList = dm.getScenicSpots(null, String.valueOf(xy[0]), String.valueOf(xy[1]));
		
		// 遍历所有景点
		for (ScenicSpot spot : spotList) {
			double deltaX = xy[0] - spot.getX();
			double deltaY = xy[1] - spot.getY();
			double distance = spot.getDistance();
			
		}
		isLocationChanged = true;
	}

}
