package org.ccar;

import java.util.ArrayList;
import java.util.HashMap;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpotCatalog;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SpotCatalogActivity extends ListActivity {
	DatabaseManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotcatalog);
		
		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		setSpotCatalogList();
	}
	
	/**
	 * 设置景点类别列表
	 */
	private void setSpotCatalogList() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(); // 景点类别列表
		
//		List<String> catalog = dm.getScenicSpotCatalog();
		
		for (ScenicSpotCatalog catalog : ScenicSpotCatalog.getCatalog()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("code", catalog.getCode());
			map.put("name", catalog.getName());
			list.add(map);
		}
		
		// 设置景点列表的Adapter
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.spotcatalog_item, new String[] { "name" },
				new int[] { R.id.spotcatalog_name });
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		HashMap<String, String> map = (HashMap<String, String>) l.getItemAtPosition(position);
		Intent i = new Intent(this, SpotListActivity.class);
		i.putExtra("spot_type", map.get("code"));
		startActivity(i);
	}

}
