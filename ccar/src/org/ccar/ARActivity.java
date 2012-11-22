package org.ccar;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.ccar.R;
import org.ccar.app.CCARApplication;
import org.ccar.app.GeoCalcUtil;
import org.ccar.ar.ARData;
import org.ccar.ar.AugmentedActivity;
import org.ccar.ar.LocalDataSource;
import org.ccar.ar.Marker;
import org.ccar.data.DatabaseManager;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ARActivity extends AugmentedActivity {
	private static final String TAG = "MainActivity";

	private LocalDataSource localData;
	private DatabaseManager dm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		Toast t = Toast.makeText(getApplicationContext(), marker.getName(),
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
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
			double[] xy = GeoCalcUtil
					.WGS2flat(lon - 0.077639, lat - 0.008553);
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