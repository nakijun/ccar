package org.ccar;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.ccar.app.CCARApplication;
import org.ccar.app.GeoCalcUtil;
import org.ccar.ar.ARData;
import org.ccar.ar.AugmentedActivity;
import org.ccar.ar.LocalDataSource;
import org.ccar.ar.Marker;
import org.ccar.data.DatabaseManager;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ARActivity extends AugmentedActivity {
	private static final String TAG = "MainActivity";

	private LocalDataSource localData;
	private DatabaseManager dm;

	private int currentSpotID = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ImageButton map = new ImageButton(this);
		map.setImageDrawable(this.getResources().getDrawable(R.drawable.map));
		map.setBackgroundColor(this.getResources()
				.getColor(R.color.transparent));
		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ARActivity.this,
						NavigationActivity.class);
				startActivity(intent);
			}
		});
		FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				Gravity.RIGHT);
		frameLayoutParams.setMargins(0, 60, 0, 0);
		addContentView(map, frameLayoutParams);

		go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(ARActivity.this, SpotInfoActivity.class);
				i.putExtra("spot_id", String.valueOf(currentSpotID));
				startActivity(i);

			}
		});

		localData = new LocalDataSource(this.getResources());
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
	}

	@Override
	public void onStart() {
		super.onStart();

		Location last = ARData.getCurrentLocation();
		updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v(TAG, "onOptionsItemSelected() item=" + item);
		switch (item.getItemId()) {
		case R.id.showRadar:
			showRadar = !showRadar;
			item.setTitle(((showRadar) ? "Òþ²Ø" : "ÏÔÊ¾") + "À×´ïÍ¼");
			break;
		case R.id.showZoomBar:
			showZoomBar = !showZoomBar;
			item.setTitle(((showZoomBar) ? "Òþ²Ø" : "ÏÔÊ¾") + "Ëõ·ÅÀ¸");
			zoomBarLayout.setVisibility((showZoomBar) ? LinearLayout.VISIBLE
					: LinearLayout.GONE);
			break;
		}
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		super.onLocationChanged(location);

		updateData(location.getLatitude(), location.getLongitude(),
				location.getAltitude());
	}

	@Override
	protected void markerTouched(Marker marker) {
		if (marker.getCode().equalsIgnoreCase("JD")) {
			StringBuilder title = new StringBuilder(marker.getName());
			title.append(" (");
			double distance = marker.getDistance();
			if (distance < 1000) {
				title.append((int) distance);
				title.append("Ã×");
			} else {
				DecimalFormat format = new DecimalFormat("#.#");
				title.append(format.format(distance / 1000));
				title.append("Ç§Ã×");
			}
			title.append(")");
			titleValue.setText(title.toString());
			infoValue.setText(marker.getInfo());
			markerInfoLayout.setVisibility(LinearLayout.VISIBLE);

			currentSpotID = marker.getID();
		}
	}

	@Override
	protected void updateDataOnZoom() {
		super.updateDataOnZoom();
		Location last = ARData.getCurrentLocation();
		updateData(last.getLatitude(), last.getLongitude(), last.getAltitude());
	}

	private void updateData(final double lat, final double lon, final double alt) {
		try {
			if (localData == null) {
				return;
			}

			// double[] xy = GeoCalcUtil.WGS2flat(lon, lat);
			double[] xy = GeoCalcUtil.WGS2flat(lon - 0.077639, lat - 0.008553);
			List<Marker> markers = localData.getMarkers(dm, xy[0], xy[1],
					ARData.getRadius());
			ARData.addMarkers(markers);
		} catch (RejectedExecutionException rej) {
			Log.w(TAG, "Not running new download Runnable, queue is full.");
		} catch (Exception e) {
			Log.e(TAG, "Exception running download Runnable.", e);
		}
	}
}