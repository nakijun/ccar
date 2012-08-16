package org.ccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SpotInfoActivity extends Activity {
	TextView tvSpotInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotinfo);
		tvSpotInfo = (TextView)findViewById(R.id.spot_info);
		
		Intent intent = getIntent();
		String spotID = intent.getStringExtra("spot_id");
		String spotName = intent.getStringExtra("spot_name");
		tvSpotInfo.setText("æ∞µ„ID£∫" + spotID + "\n" + "æ∞µ„√˚≥∆£∫" + spotName);
	}

}
