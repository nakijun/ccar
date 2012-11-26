package org.ccar.ar;

import org.ccar.R;

import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AugmentedActivity extends SensorsActivity implements
		OnTouchListener {
	private static final String TAG = "AugmentedActivity";

	protected static WakeLock wakeLock = null;
	protected static CameraSurface camScreen = null;
	protected static AugmentedView augmentedView = null;

	public static boolean useCollisionDetection = false;
	public static boolean showRadar = true;
	public static boolean showZoomBar = true;
	public static boolean showMarkerInfo = false;

	/**
	 * 缩放栏布局。
	 */
	protected static LinearLayout zoomBarLayout = null;

	/**
	 * 缩放栏。
	 */
	protected static SeekBar zoomBar = null;

	/**
	 * 范围值。
	 */
	protected static TextView rangeValue = null;

	protected static LinearLayout markerInfoLayout = null;

	protected static TextView titleValue = null;

	protected static TextView infoValue = null;

	protected static ImageButton go = null;

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

		LayoutInflater layoutInflater = getLayoutInflater();

		zoomBarLayout = (LinearLayout) layoutInflater.inflate(R.layout.zoombar,
				null);
		zoomBarLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE
				: LinearLayout.GONE);
		zoomBar = (SeekBar) zoomBarLayout.findViewById(R.id.sb_zoombar);
		zoomBar.setOnSeekBarChangeListener(myZoomBarOnSeekBarChangeListener);
		rangeValue = (TextView) zoomBarLayout
				.findViewById(R.id.tv_zoombar_range);
		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.TOP);
		addContentView(zoomBarLayout, frameLayoutParams);

		markerInfoLayout = (LinearLayout) layoutInflater.inflate(
				R.layout.markerinfo, null);
		markerInfoLayout.setVisibility((showMarkerInfo) ? LinearLayout.VISIBLE
				: LinearLayout.GONE);
		titleValue = (TextView) markerInfoLayout
				.findViewById(R.id.tv_markerinfo_title);
		infoValue = (TextView) markerInfoLayout
				.findViewById(R.id.tv_markerinfo_info);
		go = (ImageButton) markerInfoLayout.findViewById(R.id.ib_markerinfo_go);
		Button close = (Button) markerInfoLayout
				.findViewById(R.id.btn_markerinfo_close);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markerInfoLayout.setVisibility(LinearLayout.GONE);
			}
		});
		frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM);
		addContentView(markerInfoLayout, frameLayoutParams);

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

	/**
	 * 根据缩放值计算范围（最小值为 100，0-10 的步长为 50，11-20 的步长为 100，21-30 的步长为 200，最大值为 3600）。
	 * 
	 * @return 范围。
	 */
	private float calcRange() {
		float range = 0;
		int progress = zoomBar.getProgress();
		if (progress <= 10) {
			range = progress * 50 + 100;
		} else if (progress > 10 && progress <= 20) {
			range = progress * 100 - 400;
		} else {
			range = progress * 200 - 2400;
		}
		return range;
	}

	protected void updateDataOnZoom() {
		float range = calcRange();
		String value = GeoCalcUtil.formatDistance(range);
		rangeValue.setText(value);
		ARData.setRadius(range);
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