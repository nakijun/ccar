package org.ccar;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/**
 * 导航，即地图界面
 * @author swansword
 *
 */
public class NavigationActivity extends Activity {
	private MapView mapView;
	private Button btnZoomIn;
	private Button btnZoomOut;
	private GraphicsLayer gLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.navigation);
		
		mapView = (MapView)findViewById(R.id.map);
		btnZoomIn = (Button)findViewById(R.id.map_zoomin_button);
		btnZoomOut = (Button)findViewById(R.id.map_zoomout_button);
		
		setControlProperty(); // 设置控件属性
		
		
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
	
	/**
	 * 设置控件属性
	 */
	private void setControlProperty() {
		// 设置地图及其Layer
		mapView.addLayer(new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer"));
		gLayer = new GraphicsLayer();
		mapView.addLayer(gLayer);
		
		mapView.setOnSingleTapListener(new OnSingleTapListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSingleTap(float x, float y) {
				Graphic g = new Graphic(mapView.toMapPoint(x, y), 
						new SimpleMarkerSymbol(Color.RED, 20, SimpleMarkerSymbol.STYLE.CIRCLE));
				gLayer.addGraphic(g);
			}
		});
		
		// 设置按钮属性
		btnZoomIn.getBackground().setAlpha(155);
		btnZoomOut.getBackground().setAlpha(155);
		
		// 设置按钮监听器
		btnZoomIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapView.zoomin();
			}
		});
		btnZoomOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapView.zoomout();
			}
		});
	}
	
	/**
	 * 显示景点Point
	 */
	private void showScenicSpot() {
		
	}

}
