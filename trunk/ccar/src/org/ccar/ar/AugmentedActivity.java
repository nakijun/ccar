package org.ccar.ar;

import java.text.DecimalFormat;
import java.util.zip.Inflater;

import org.ccar.R;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AugmentedActivity extends SensorsActivity implements
		OnTouchListener {
	private static final String TAG = "AugmentedActivity";
	private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

	protected static WakeLock wakeLock = null;
	protected static CameraSurface camScreen = null;
	protected static SeekBar myZoomBar = null;
	protected static AugmentedView augmentedView = null;
	protected static LinearLayout zoomLayout = null;

	public static final float MAX_ZOOM = 100; // in KM
	public static final float ONE_PERCENT = MAX_ZOOM / 100f;
	public static final float TEN_PERCENT = 10f * ONE_PERCENT;
	public static final float TWENTY_PERCENT = 2f * TEN_PERCENT;
	public static final float EIGHTY_PERCENTY = 4f * TWENTY_PERCENT;

	public static boolean useCollisionDetection = true;
	public static boolean showRadar = true;
	public static boolean showZoomBar = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		camScreen = new CameraSurface(this);
		setContentView(camScreen);

		augmentedView = new AugmentedView(this);
		augmentedView.setOnTouchListener(this);
		LayoutParams augLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		addContentView(augmentedView, augLayout);

		LayoutInflater inflater = getLayoutInflater();
		zoomLayout = (LinearLayout) inflater.inflate(R.layout.scalebar, null);
		zoomLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE
				: LinearLayout.GONE);

		myZoomBar = (SeekBar) zoomLayout.findViewById(R.id.scalebar);
		myZoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);

		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.TOP);
		addContentView(zoomLayout, frameLayoutParams);

		updateDataOnZoom();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"DimScreen");
	}

	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();
	}

	@Override
	public void onPause() {
		super.onPause();

		wakeLock.release();
	}

	@Override
	public void onSensorChanged(SensorEvent evt) {
		super.onSensorChanged(evt);

		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER
				|| evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			augmentedView.postInvalidate();
		}
	}

	private OnSeekBarChangeListener myZoomBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			updateDataOnZoom();
			camScreen.invalidate();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
			// Not used
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			updateDataOnZoom();
			camScreen.invalidate();
		}
	};

	private static float calcZoomLevel() {
		int myZoomLevel = myZoomBar.getProgress();
		float out = 0;

		float percent = 0;
		if (myZoomLevel <= 25) {
			percent = myZoomLevel / 25f;
			out = ONE_PERCENT * percent;
		} else if (myZoomLevel > 25 && myZoomLevel <= 50) {
			percent = (myZoomLevel - 25f) / 25f;
			out = ONE_PERCENT + (TEN_PERCENT * percent);
		} else if (myZoomLevel > 50 && myZoomLevel <= 75) {
			percent = (myZoomLevel - 50f) / 25f;
			out = TEN_PERCENT + (TWENTY_PERCENT * percent);
		} else {
			percent = (myZoomLevel - 75f) / 25f;
			out = TWENTY_PERCENT + (EIGHTY_PERCENTY * percent);
		}
		return out;
	}

	protected void updateDataOnZoom() {
		float zoomLevel = calcZoomLevel();
		ARData.setRadius(zoomLevel);
		ARData.setZoomLevel(FORMAT.format(zoomLevel));
		ARData.setZoomProgress(myZoomBar.getProgress());
	}

	public boolean onTouch(View view, MotionEvent me) {
		for (Marker marker : ARData.getMarkers()) {
			if (marker.handleClick(me.getX(), me.getY())) {
				if (me.getAction() == MotionEvent.ACTION_UP)
					markerTouched(marker);
				return true;
			}
		}
		return super.onTouchEvent(me);
	};

	protected void markerTouched(Marker marker) {
		Log.w(TAG, "markerTouched() not implemented.");
	}
}