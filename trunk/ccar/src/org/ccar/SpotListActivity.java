package org.ccar;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

/**
 * 景点列表
 * @author swansword
 *
 */
public class SpotListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotlist);
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new HashMap<String, String>();
		HashMap<String, String> map2 = new HashMap<String, String>();
		HashMap<String, String> map3 = new HashMap<String, String>();
		map1.put("spot_name", "洪氏宗祠");
		map1.put("spot_dis", "25m");
		map2.put("spot_name", "钱塘望族");
		map2.put("spot_dis", "100m");
		map3.put("spot_name", "五常人家");
		map3.put("spot_dis", "200m");

		list.add(map1);
		list.add(map2);
		list.add(map3);
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.scenicspot,
				new String[] { "spot_name", "spot_dis" }, new int[] {
						R.id.spot_name, R.id.spot_dis });
		setListAdapter(adapter);
	}

}
