package org.ccar;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SpotInfoActivity extends Activity {
	DatabaseManager dm;
	TextView tvSpotInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotinfo);
		
		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		
		tvSpotInfo = (TextView)findViewById(R.id.spot_info);
		
		Intent intent = getIntent();
		String spotID = intent.getStringExtra("spot_id");
		
		ScenicSpot spot = dm.getScenicSpot(Integer.parseInt(spotID));
		
		tvSpotInfo.setText("景点ID：" + spotID + "\n" + "景点名称：" + spot.getName());
	}

}
