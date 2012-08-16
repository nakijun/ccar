package org.ccar;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 景点列表
 * @author swansword
 *
 */
public class SpotListActivity extends ListActivity {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotlist);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		HashMap<String, String> map3 = new HashMap<String, String>();
		map1.put("spot_id", "0001");
		map1.put("spot_name", "洪氏宗祠");
		map1.put("spot_dis", "25m");
		map2.put("spot_id", "0002");
		map2.put("spot_name", "钱塘望族");
		map2.put("spot_dis", "100m");
		map3.put("spot_id", "0003");
		map3.put("spot_name", "五常人家");
		map3.put("spot_dis", "200m");

		list.add(map1);
		list.add(map2);
		list.add(map3);
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.scenicspot,
				new String[] { "spot_name", "spot_dis", "spot_id" }, new int[] {
						R.id.spot_name, R.id.spot_dis });
		setListAdapter(adapter);
	}

}
