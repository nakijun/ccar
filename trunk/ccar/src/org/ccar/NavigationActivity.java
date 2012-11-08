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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ����������ͼ����
 * 
 * @author swansword
 * 
 */
public class NavigationActivity extends Activity {
	private DatabaseManager dm;
	private LocationManager locationManager;
	private MapView mapView;
	private Button btnZoomIn;
	private Button btnZoomOut;
	private ImageButton imgbtnAR;
	private Callout callout;

	/**
	 * ����ͼ�㡣
	 */
	private GraphicsLayer scenicSpotsLayer;
	
	/**
	 * �������ֱ�עͼ��
	 */
	private GraphicsLayer scenicSoptsLabelLayer;

	/**
	 * ·��ͼ�㡣
	 */
	private GraphicsLayer routeLayer;
	
	/**
	 * ��ǰλ��ͼ��
	 */
	private GraphicsLayer currentLocationLayer;
	
	/**
	 * ��ǰλ�õ�
	 */
	private Graphic currentLocationPoint;

	/**
	 * ��ǰ��ѡ�еľ��㡣
	 */
	private Graphic selectedScenicSpot;

	/**
	 * ָʾ�Ƿ����ڽ��е�����
	 */
	private boolean isNavigating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.navigation);
		
		
		
		// ��ʼ��DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();

		// ��ʼ���ؼ�
		mapView = (MapView) findViewById(R.id.map);
		btnZoomIn = (Button) findViewById(R.id.map_zoomin_button);
		btnZoomOut = (Button) findViewById(R.id.map_zoomout_button);
		imgbtnAR = (ImageButton)findViewById(R.id.ar_imgbutton);

		setControlProperty(); // ���ÿؼ�����

		showScenicSpot();
		
		// ��ȡ��ǰλ��
		receiveCurrentLocation();
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
	 * ���ÿؼ�����
	 */
	private void setControlProperty() {
		setMapView();

		// ���ð�ť����
		btnZoomIn.getBackground().setAlpha(155);
		btnZoomOut.getBackground().setAlpha(155);

		// ���ð�ť������
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
		imgbtnAR.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(NavigationActivity.this, ARActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * ���õ�ͼ��ͼ�Լ���ͼͼ�㡣
	 */
	private void setMapView() {
		// ��ӱ��ػ���ͼ�㡣
		mapView.addLayer(new ArcGISLocalTiledLayer(
				"file:///mnt/sdcard/ccar/Layers"));

		// ��Ӿ���ͼ�㡣
		scenicSpotsLayer = new GraphicsLayer();
		mapView.addLayer(scenicSpotsLayer);
		
		// ��Ӿ������ֱ�עͼ��
		scenicSoptsLabelLayer = new GraphicsLayer();
		mapView.addLayer(scenicSoptsLabelLayer);

		// ���·��ͼ�㡣
		routeLayer = new GraphicsLayer();
		SimpleLineSymbol symbol = new SimpleLineSymbol(Color.BLUE, 2);
		SimpleRenderer renderer = new SimpleRenderer(symbol);
		routeLayer.setRenderer(renderer);
		mapView.addLayer(routeLayer);
		
		// ��ӵ�ǰλ��ͼ��
		currentLocationLayer = new GraphicsLayer();
		mapView.addLayer(currentLocationLayer);

		// ���õ�ͼ��ͼ�¼���
		mapView.setOnSingleTapListener(m_onSingleTapListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigation_menu, menu); // ����Դ�����˵�
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
	}

	/**
	 * ʵ�ָ��˵���Ĺ���
	 * 
	 * @param item
	 *            �˵���
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
	 * ��ͼ�����¼�������
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
	 * ����Callout
	 * 
	 * @param spotID
	 *            ����ID
	 * @param spotname
	 *            ��������
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
	 * ��ʾ����Point
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
				scenicSoptsLabelLayer.addGraphic(g);
			}
		}
	}
	
	/**
	 * ��ȡ��ǰλ��
	 */
	private void receiveCurrentLocation() {
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
		
		//  ���Ի�ȡ֮ǰ�õ���λ�����ݣ�����У���ʹ�ø�����
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			showCurrentLocation(lastKnownLocation);
		} else {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lastKnownLocation != null) {
				showCurrentLocation(lastKnownLocation);
			}
		}
		
		// �ֱ�ͨ����վ��GPS��ȡ��ǰλ��
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 10, locationListener);
	}
	
	private void showCurrentLocation(Location location) {
		Symbol symbol = new SimpleMarkerSymbol(Color.BLACK, 10,
				SimpleMarkerSymbol.STYLE.CIRCLE);
		currentLocationPoint = new Graphic(new Point(location.getLongitude(), location.getLatitude()), symbol);
		if (currentLocationLayer.getNumberOfGraphics() == 0) {
			currentLocationLayer.addGraphic(currentLocationPoint);
		}
	}
	
	/**
	 *  λ�ü�����
	 */
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onProviderDisabled(String provider) {}
		
		@Override
		public void onLocationChanged(Location location) {
			// λ�øı�ʱ���¾����б�
			showCurrentLocation(location);
		}
	};
}
