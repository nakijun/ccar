package org.ccar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * æ∞µ„¡–±Ì
 * @author swansword
 *
 */
public class SpotListActivity extends ListActivity {
	LocationManager locationManager = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotlist);
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);		        
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		DatabaseManager dm = ccarApplication.getDatabaseManager();
		List<ScenicSpot> spotList = dm.getScenicSpots();
		for (ScenicSpot spot : spotList) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("spot_id", String.valueOf(spot.getID()));
			map.put("spot_name", spot.getName());
			
			map.put("spot_dis", String.valueOf(spot.getLat())); 
			
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.scenicspot,
				new String[] { "spot_name", "spot_dis", "spot_id" }, new int[] {
						R.id.spot_name, R.id.spot_dis });
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		HashMap<String, String> map = (HashMap<String, String>) l.getItemAtPosition(position);

		Intent i = new Intent(this, SpotInfoActivity.class);
		i.putExtra("spot_id", map.get("spot_id"));
		i.putExtra("spot_name", map.get("spot_name"));
		startActivity(i);
	}

}
