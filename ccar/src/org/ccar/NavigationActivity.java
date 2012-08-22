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
 * ����������ͼ����
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
		
		// ��ʼ��DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		// ��ʼ���ؼ�
		mapView = (MapView)findViewById(R.id.map);
		btnZoomIn = (Button)findViewById(R.id.map_zoomin_button);
		btnZoomOut = (Button)findViewById(R.id.map_zoomout_button);
		
		setControlProperty(); // ���ÿؼ�����
		
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
	 * ���ÿؼ�����
	 */
	private void setControlProperty() {
		// ���õ�ͼ����Layer
		mapView.addLayer(new ArcGISLocalTiledLayer("file:///mnt/sdcard/ccar/Layers"));
		gLayer = new GraphicsLayer();
		mapView.addLayer(gLayer);
		
		mapView.setOnSingleTapListener(m_onSingleTapListener);
		
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
	 * ��ʾ����Point
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
	 * ��һ��ͼ������һ�� Graphics���󣬷�Χ��50���ذ뾶Բ
	 * @param xScreen ��Ļx����
	 * @param yScreen ��Ļy����
	 * @param layer Ŀ��ͼ��
	 * @return
	 */
	private Graphic GetGraphicsFromLayer(double xScreen, double yScreen, GraphicsLayer layer) {
        Graphic resultGraphic = null;
        try {
            int[] graphicIDs = layer.getGraphicIDs(); // ����Graphic��ID
            double x = xScreen;
            double y = yScreen;
            
            double distance = 0; // ���λ����Graphic�ľ���
            
            // ����Graphicѭ��һ�飬�ҵ����е�Graphic
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
