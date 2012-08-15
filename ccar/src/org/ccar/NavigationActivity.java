package org.ccar;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

import android.app.Activity;
import android.os.Bundle;

/**
 * 导航，即地图界面
 * @author swansword
 *
 */
public class NavigationActivity extends Activity {
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation);
		
		mapView = (MapView)findViewById(R.id.map);
		mapView.addLayer(new ArcGISTiledMapServiceLayer("" +
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.unpause();
	}

}
