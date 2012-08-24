package org.ccar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.ccar.app.CCARApplication;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class SpotInfoActivity extends Activity {
	DatabaseManager dm;
	TextView tvSpotInfo;
	ImageView imageSpotInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotinfo);
		
		// 初始化DatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		
		tvSpotInfo = (TextView)findViewById(R.id.spot_info);
		imageSpotInfo = (ImageView)findViewById(R.id.spot_image);
		
		Intent intent = getIntent();
		String spotID = intent.getStringExtra("spot_id");
		
		ScenicSpot spot = dm.getScenicSpot(Integer.parseInt(spotID));
		
		tvSpotInfo.setText("景点ID：" + spotID + 
				"\n景点名称：" + spot.getName() + 
				"\n景点介绍：" + spot.getDescription());
		
		String imageFiles = spot.getImageFiles();
		if (imageFiles != null) {
			String [] arrImageFiles = imageFiles.split("_");
			Bitmap bitmap = getLoacalBitmap("/sdcard/ccar/pic/" + arrImageFiles[0] + ".png");
			imageSpotInfo.setImageBitmap(bitmap);
		}
		
		
		
	}
	
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
