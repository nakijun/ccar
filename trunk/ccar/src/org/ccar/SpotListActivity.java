package org.ccar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ccar.app.CCARApplication;
import org.ccar.app.GeoCalcUtil;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 景点列表
 * @author swansword
 *
 */
public class SpotListActivity extends ListActivity {
	DatabaseManager dm;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotlist);
		
		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		// 获取当前位置
		receiveCurrentLocation();
	}
	
	/**
	 * 点击某景点，显示该景点详细信息
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		HashMap<String, String> map = (HashMap<String, String>) l.getItemAtPosition(position);
		Intent i = new Intent(this, SpotInfoActivity.class);
		i.putExtra("spot_id", map.get("spot_id"));
		startActivity(i);
	}
	
	/**
	 * 设置景点列表
	 * @param location 当前的位置，用于根据距离排序数据
	 */
	private void setScenicSpotList(Location location) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); // 景点列表
		
		// 将经纬度转换为平面坐标
		double[] xy = GeoCalcUtil.WGS2flat(location.getLongitude(), location.getLatitude());
		
		// 查询数据库返回景点列表，按距离排序
		List<ScenicSpot> spotList = dm.getScenicSpots(null, String.valueOf(xy[0]), String.valueOf(xy[1]));
		
		// 构造景点列表数据
		for (ScenicSpot spot : spotList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("spot_id", String.valueOf(spot.getID()));
			map.put("spot_name", spot.getName());
			
			// 根据返回的景点的经纬度计算与当前位置的距离
			double dis = GeoCalcUtil.CalcDistance(location.getLongitude(), location.getLatitude(), spot.getLon(), spot.getLat());
			
			// 距离数据精度保留到10米
			map.put("spot_dis", String.valueOf(Math.round(dis / 10) * 10)+"m"); 
			
			list.add(map);
		}

		// 设置景点列表的Adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.scenicspot,
				new String[] { "spot_name", "spot_dis", "spot_id" }, new int[] {
						R.id.spot_name, R.id.spot_dis });
		setListAdapter(adapter);
	}
	
	/**
	 * 退出该Activity时取消位置更新
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * 获取当前位置
	 */
	private void receiveCurrentLocation() {
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
		
		//  尝试获取之前得到的位置数据，如果有，先使用该数据查询景点列表
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			setScenicSpotList(lastKnownLocation);
		} else {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lastKnownLocation != null) {
				setScenicSpotList(lastKnownLocation);
			}
		}
		
		// 分别通过基站、GPS获取当前位置
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
	}
	
	/**
	 *  位置监听器
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
			// 位置改变时更新景点列表
			setScenicSpotList(location);
		}
	};

}
