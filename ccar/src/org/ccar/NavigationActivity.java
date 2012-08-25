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
	private Callout callout;

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
	 * ����Callout
	 * @param spotID ����ID
	 * @param spotname ��������
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
	 * ��ʾ����Point
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
	 * ��һ��ͼ������һ�� Graphics���󣬷�Χ��ָ�����50���ذ뾶Բ��
	 * @param xScreen ��Ļx����
	 * @param yScreen ��Ļy����
	 * @param layer Ŀ��ͼ��
	 * @return
	 */
	private Graphic getGraphicFromLayer(float xScreen, float yScreen, GraphicsLayer layer) {
        Graphic resultGraphic = null;
        try {
            int[] graphicIDs = layer.getGraphicIDs(); // ����Graphic��ID
            double x = xScreen;
            double y = yScreen;
            
            double distance = 0; // ���λ����Graphic�ľ���
            
            // ����Graphicѭ��һ�飬�ҵ����е�Graphic
            // TODO �㷨Ч�ʽϵͣ����Ľ�
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
