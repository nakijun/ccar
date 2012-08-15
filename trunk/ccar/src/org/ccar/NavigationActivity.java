package org.ccar;

import com.esri.android.map.MapView;

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
	}

}
