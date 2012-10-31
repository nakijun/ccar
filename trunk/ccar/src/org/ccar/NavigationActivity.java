package org.ccar;

import java.util.HashMap;
import java.util.List;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;
import org.ccar.task.RouteTask;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.InfoTemplate;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 导航，即地图界面
 * 
 * @author swansword
 * 
 */
public class NavigationActivity extends Activity {
	private DatabaseManager dm;
	private MapView mapView;
	private Button btnZoomIn;
	private Button btnZoomOut;
	private Callout callout;

	/**
	 * 景点图层。
	 */
	private GraphicsLayer scenicSpotsLayer;

	/**
	 * 路径图层。
	 */
	private GraphicsLayer routeLayer;

	/**
	 * 当前被选中的景点。
	 */
	private Graphic selectedScenicSpot;

	/**
	 * 指示是否正在进行导航。
	 */
	private boolean isNavigating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.navigation);

		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();

		// 初始化控件
		mapView = (MapView) findViewById(R.id.map);
		btnZoomIn = (Button) findViewById(R.id.map_zoomin_button);
		btnZoomOut = (Button) findViewById(R.id.map_zoomout_button);

		setControlProperty(); // 设置控件属性

		showScenicSpot();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.unpause();
	}

	/**
	 * 设置控件属性
	 */
	private void setControlProperty() {
		setMapView();

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
	 * 设置地图视图以及地图图层。
	 */
	private void setMapView() {
		// 添加本地缓存图层。
		mapView.addLayer(new ArcGISLocalTiledLayer(
				"file:///mnt/sdcard/ccar/Layers"));

		// 添加景点图层。
		scenicSpotsLayer = new GraphicsLayer();
		mapView.addLayer(scenicSpotsLayer);

		// 添加路径图层。
		routeLayer = new GraphicsLayer();
		SimpleLineSymbol symbol = new SimpleLineSymbol(Color.BLUE, 2);
		SimpleRenderer renderer = new SimpleRenderer(symbol);
		routeLayer.setRenderer(renderer);
		mapView.addLayer(routeLayer);

		// 设置地图视图事件。
		mapView.setOnSingleTapListener(m_onSingleTapListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigation_menu, menu); // 从资源创建菜单
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
	}

	/**
	 * 实现各菜单项的功能
	 * 
	 * @param item
	 *            菜单项
	 * @return
	 */
	private boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.clear_results:
			routeLayer.removeAll();
			return true;
		}
		return false;
	}

	/**
	 * 地图单击事件监听器
	 * 
	 */
	private final OnSingleTapListener m_onSingleTapListener = new OnSingleTapListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void onSingleTap(float x, float y) {
			if (!mapView.isLoaded()) {
				return;
			}

			if (isNavigating) {
				routeLayer.removeAll();

				RouteTask routeTask = new RouteTask(
						"http://218.108.83.172/ArcGIS/rest/services/pathline/NAServer/Route",
						mapView.toMapPoint(x, y), (Point) selectedScenicSpot
								.getGeometry());
				int errorCode = routeTask.Solve();
				if (errorCode > 0) {
					Toast.makeText(NavigationActivity.this, errorCode,
							Toast.LENGTH_SHORT);
				} else {
					Graphic route = new Graphic(routeTask.getResult(), null);
					routeLayer.addGraphic(route);
				}

				selectedScenicSpot = null;
				isNavigating = false;
				return;
			}

			int[] uids = scenicSpotsLayer.getGraphicIDs(x, y, 10);
			if (uids != null && uids.length > 0) {
				Graphic g = scenicSpotsLayer.getGraphic(uids[0]);
				selectedScenicSpot = g;
				callout = mapView.getCallout();
				callout.setStyle(R.xml.spotinfo_callout);
				callout.setContent(loadCalloutView(g.getInfoTemplate()
						.getTitle(g), g.getInfoTemplate().getContent(g)));
				callout.show((Point) g.getGeometry());
			} else {
				selectedScenicSpot = null;
				if (callout != null && callout.isShowing()) {
					callout.hide();
				}
			}
		}
	};

	/**
	 * 加载Callout
	 * 
	 * @param spotID
	 *            景点ID
	 * @param spotname
	 *            景点名称
	 * @return
	 */
	private View loadCalloutView(String spotID, String spotname) {
		View view = LayoutInflater.from(NavigationActivity.this).inflate(
				R.layout.spotinfo_callout, null);

		final TextView tvSpotname = (TextView) view
				.findViewById(R.id.callout_spotname);
		tvSpotname.setText(spotname);
		tvSpotname.setTag(spotID);
		tvSpotname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(NavigationActivity.this,
						SpotInfoActivity.class);
				i.putExtra("spot_id", (String) view.getTag());
				startActivity(i);
				// Toast.makeText(NavigationActivity.this, tvSpotname.getText(),
				// Toast.LENGTH_SHORT).show();
			}
		});

		final Button btnNav = (Button) view
				.findViewById(R.id.callout_nav_button);
		btnNav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callout = mapView.getCallout();
				callout.hide();
				isNavigating = true;
			}
		});

		return view;
	}

	/**
	 * 显示景点Point
	 */
	private void showScenicSpot() {
		List<ScenicSpot> spotList = dm.getScenicSpots();
		for (ScenicSpot spot : spotList) {
			Symbol symbol = null;
			TextSymbol textsymbol = null;
			if (spot.getCode().equals("RK")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.rk_s));
			} else if (spot.getCode().equals("CK")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.ck_s));
			} else if (spot.getCode().equals("JD")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.jd_s));
			} else if (spot.getCode().equals("JDCR")) {
//				symbol = new SimpleMarkerSymbol(Color.GREEN, 6,
//						SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("JZ")) {
				symbol = new SimpleMarkerSymbol(Color.CYAN, 6,
						SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("QL")) {
				symbol = new SimpleMarkerSymbol(Color.YELLOW, 6,
						SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("CY")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.cy_s));
			} else if (spot.getCode().equals("FW")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.fw_s));
			} else if (spot.getCode().equals("ZXC")) {
				symbol = new SimpleMarkerSymbol(Color.RED, 8,
						SimpleMarkerSymbol.STYLE.CROSS);
			} else if (spot.getCode().equals("CS")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.cs_s));
			} else if (spot.getCode().equals("TCC")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.tcc_s));
			} else if (spot.getCode().equals("MT")) {
				symbol = new PictureMarkerSymbol(this.getResources()
						.getDrawable(R.drawable.mt_s));
			} else if (spot.getCode().equals("HC")) {
				symbol = new SimpleMarkerSymbol(Color.YELLOW, 6,
						SimpleMarkerSymbol.STYLE.SQUARE);
			}

			if (symbol != null) {
				Graphic g = new Graphic(
						new Point(spot.getLon(), spot.getLat()), symbol,
						new HashMap<String, Object>(), new InfoTemplate(
								String.valueOf(spot.getID()), spot.getName()));
				scenicSpotsLayer.addGraphic(g);
			}
			
			if (!spot.getCode().equals("JDCR")) {
				textsymbol = new TextSymbol(10, spot.getName(), Color.BLACK);
				textsymbol.setHorizontalAlignment(HorizontalAlignment.LEFT);
			}
			if (textsymbol != null) {
				Graphic g = new Graphic(new Point(spot.getLon() + 0.000015, spot.getLat()), textsymbol);
				scenicSpotsLayer.addGraphic(g);
			}
		}
	}
}
