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
 * �����б�
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
		
		// ��ʼ��DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		// ��ȡ��ǰλ��
		receiveCurrentLocation();
	}
	
	/**
	 * ���ĳ���㣬��ʾ�þ�����ϸ��Ϣ
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
	 * ���þ����б�
	 * @param location ��ǰ��λ�ã����ڸ��ݾ�����������
	 */
	private void setScenicSpotList(Location location) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); // �����б�
		
		// ����γ��ת��Ϊƽ������
		double[] xy = GeoCalcUtil.WGS2flat(location.getLongitude(), location.getLatitude());
		
		// ��ѯ���ݿⷵ�ؾ����б�����������
		List<ScenicSpot> spotList = dm.getScenicSpots(null, String.valueOf(xy[0]), String.valueOf(xy[1]));
		
		// ���쾰���б�����
		for (ScenicSpot spot : spotList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("spot_id", String.valueOf(spot.getID()));
			map.put("spot_name", spot.getName());
			
			// ���ݷ��صľ���ľ�γ�ȼ����뵱ǰλ�õľ���
			double dis = GeoCalcUtil.CalcDistance(location.getLongitude(), location.getLatitude(), spot.getLon(), spot.getLat());
			
			// �������ݾ��ȱ�����10��
			map.put("spot_dis", String.valueOf(Math.round(dis / 10) * 10)+"m"); 
			
			list.add(map);
		}

		// ���þ����б��Adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.scenicspot,
				new String[] { "spot_name", "spot_dis", "spot_id" }, new int[] {
						R.id.spot_name, R.id.spot_dis });
		setListAdapter(adapter);
	}
	
	/**
	 * �˳���Activityʱȡ��λ�ø���
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * ��ȡ��ǰλ��
	 */
	private void receiveCurrentLocation() {
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);	
		
		//  ���Ի�ȡ֮ǰ�õ���λ�����ݣ�����У���ʹ�ø����ݲ�ѯ�����б�
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			setScenicSpotList(lastKnownLocation);
		} else {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lastKnownLocation != null) {
				setScenicSpotList(lastKnownLocation);
			}
		}
		
		// �ֱ�ͨ����վ��GPS��ȡ��ǰλ��
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
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
			setScenicSpotList(location);
		}
	};

}
