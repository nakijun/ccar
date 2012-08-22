package org.ccar;

import java.util.HashMap;
import java.util.List;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.InfoTemplate;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
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
	
	private final OnSingleTapListener m_onSingleTapListener = new OnSingleTapListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void onSingleTap(float x, float y) {
			if (!mapView.isLoaded()) {
                return;
            }
			Graphic g = GetGraphicsFromLayer(x, y, gLayer);
			if (g != null) {
				Toast.makeText(NavigationActivity.this, g.getInfoTemplate().getContent(g), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	/**
	 * 显示景点Point
	 */
	private void showScenicSpot() {
		List<ScenicSpot> spotList = dm.getScenicSpots();
		for (ScenicSpot spot : spotList) {
			Graphic g = new Graphic(new Point(spot.getLon(), spot.getLat()), 
					new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE),
					new HashMap<String, Object>(),
					new InfoTemplate(String.valueOf(spot.getID()), spot.getName()));
			gLayer.addGraphic(g);
		}
	}
	
	/**
	 * 从一个图层里查找获得 Graphics对象，范围是50像素半径圆
	 * @param xScreen 屏幕x坐标
	 * @param yScreen 屏幕y坐标
	 * @param layer 目标图层
	 * @return
	 */
	private Graphic GetGraphicsFromLayer(double xScreen, double yScreen, GraphicsLayer layer) {
        Graphic resultGraphic = null;
        try {
            int[] graphicIDs = layer.getGraphicIDs(); // 所有Graphic的ID
            double x = xScreen;
            double y = yScreen;
            
            double distance = 0; // 点击位置与Graphic的距离
            
            // 所有Graphic循环一遍，找到点中的Graphic
            for (int i = 0; i < graphicIDs.length; i++) {
                Graphic graphic = layer.getGraphic(graphicIDs[i]);
                if (graphic != null) {
                    Point point = (Point) graphic.getGeometry();
                    point = mapView.toScreenPoint(point);
                    double x1 = point.getX();
                    double y1 = point.getY();
                    
                    if (distance == 0 || distance > Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1))) {
                    	distance = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
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
