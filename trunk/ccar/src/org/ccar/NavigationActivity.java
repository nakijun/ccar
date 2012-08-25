package org.ccar;

import java.util.HashMap;
import java.util.List;

import org.ccar.app.CCARApplication;
import org.ccar.app.GraphicCalcUtil;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.InfoTemplate;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 导航，即地图界面
 * @author swansword
 *
 */
public class NavigationActivity extends Activity {
	private static final int THRESHOLD_FEATURE_SELECTION = 50;
	
	private DatabaseManager dm;
	private MapView mapView;
	private Button btnZoomIn;
	private Button btnZoomOut;
	private GraphicsLayer gLayer;
	private Callout callout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.navigation);
		
		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		// 初始化控件
		mapView = (MapView)findViewById(R.id.map);
		btnZoomIn = (Button)findViewById(R.id.map_zoomin_button);
		btnZoomOut = (Button)findViewById(R.id.map_zoomout_button);
		
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
		// 设置地图及其Layer
		mapView.addLayer(new ArcGISLocalTiledLayer("file:///mnt/sdcard/ccar/Layers"));
		gLayer = new GraphicsLayer();
		mapView.addLayer(gLayer);
		mapView.setOnSingleTapListener(m_onSingleTapListener);
		
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
			Graphic g = getGraphicFromLayer(x, y, gLayer);
			callout = mapView.getCallout();
			if (g != null) {
				callout.setStyle(R.xml.spotinfo_callout);
				callout.setContent(loadCalloutView(g.getInfoTemplate().getTitle(g), g.getInfoTemplate().getContent(g)));
				callout.show((Point)g.getGeometry());
			} else {
				if (callout.isShowing())
					callout.hide();
			}
		}
	};
	
	/**
	 * 加载Callout
	 * @param spotID 景点ID
	 * @param spotname 景点名称
	 * @return
	 */
	private View loadCalloutView(String spotID, String spotname) {
		View view = LayoutInflater.from(NavigationActivity.this).inflate(
				R.layout.spotinfo_callout, null);

		final TextView tvSpotname = (TextView) view.findViewById(R.id.callout_spotname);
		tvSpotname.setText(spotname);
		tvSpotname.setTag(spotID);
		tvSpotname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent i = new Intent(NavigationActivity.this, SpotInfoActivity.class);
				i.putExtra("spot_id", (String)view.getTag());
				startActivity(i);
//				Toast.makeText(NavigationActivity.this, tvSpotname.getText(), Toast.LENGTH_SHORT).show();
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
			if (spot.getCode().equals("RK") || spot.getCode().equals("CK")) {
				symbol = new PictureMarkerSymbol(this.getResources().getDrawable(R.drawable.rk));
			} else if (spot.getCode().equals("JD")) {
				symbol = new SimpleMarkerSymbol(Color.BLUE, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("JDCR")) {
				symbol = new SimpleMarkerSymbol(Color.GREEN, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("JZ")) {
				symbol = new SimpleMarkerSymbol(Color.CYAN, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("QL")) {
				symbol = new SimpleMarkerSymbol(Color.YELLOW, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("CY")) {
				symbol = new SimpleMarkerSymbol(Color.GRAY, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("FW")) {
				symbol = new SimpleMarkerSymbol(Color.MAGENTA, 6, SimpleMarkerSymbol.STYLE.CIRCLE);
			} else if (spot.getCode().equals("ZXC")) {
				symbol = new SimpleMarkerSymbol(Color.RED, 8, SimpleMarkerSymbol.STYLE.CROSS);
			} else if (spot.getCode().equals("CS")) {
				symbol = new SimpleMarkerSymbol(Color.BLUE, 6, SimpleMarkerSymbol.STYLE.DIAMOND);
			} else if (spot.getCode().equals("TCC")) {
				symbol = new SimpleMarkerSymbol(Color.CYAN, 6, SimpleMarkerSymbol.STYLE.SQUARE);
			} else if (spot.getCode().equals("MT")) {
				symbol = new SimpleMarkerSymbol(Color.GREEN, 6, SimpleMarkerSymbol.STYLE.DIAMOND);
			} else if (spot.getCode().equals("HC")) {
				symbol = new SimpleMarkerSymbol(Color.YELLOW, 6, SimpleMarkerSymbol.STYLE.SQUARE);
			}
			
			if (symbol != null) {
				Graphic g = new Graphic(new Point(spot.getLon(), spot.getLat()), 
						symbol,
						new HashMap<String, Object>(),
						new InfoTemplate(String.valueOf(spot.getID()), spot.getName()));
				gLayer.addGraphic(g);
			}
		}
	}
	
	/**
	 * 从一个图层里查找获得 Graphics对象，范围是指定点的50像素半径圆内
	 * @param xScreen 屏幕x坐标
	 * @param yScreen 屏幕y坐标
	 * @param layer 目标图层
	 * @return
	 */
	private Graphic getGraphicFromLayer(float xScreen, float yScreen, GraphicsLayer layer) {
        Graphic resultGraphic = null;
        try {
            int[] graphicIDs = layer.getGraphicIDs(); // 所有Graphic的ID
            double x = xScreen;
            double y = yScreen;
            
            double distance = 0; // 点击位置与Graphic的距离
            
            // 所有Graphic循环一遍，找到点中的Graphic
            // TODO 算法效率较低，待改进
            for (int i = 0; i < graphicIDs.length; i++) {
                Graphic graphic = layer.getGraphic(graphicIDs[i]);
                if (graphic != null) {
                    Point point = (Point) graphic.getGeometry();
                    point = mapView.toScreenPoint(point);
                    double x1 = point.getX();
                    double y1 = point.getY();

                    if (distance == 0 || distance > GraphicCalcUtil.getDistance(x, y, x1, y1)) {
                    	distance = GraphicCalcUtil.getDistance(x, y, x1, y1);
                    	if (distance < THRESHOLD_FEATURE_SELECTION) {
                            resultGraphic = graphic;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return resultGraphic;
    }

}
