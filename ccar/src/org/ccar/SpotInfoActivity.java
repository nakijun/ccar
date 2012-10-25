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
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class SpotInfoActivity extends Activity {
	DatabaseManager dm;
	TextView tvSpotName;
	TextView tvSpotInfo;
	ImageView imageSpotInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spotinfo);
		
		// ≥ı ºªØDatabaseManager
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		dm = ccarApplication.getDatabaseManager();
		
		tvSpotName = (TextView)findViewById(R.id.spot_spotname);
		tvSpotInfo = (TextView)findViewById(R.id.spot_info);
		imageSpotInfo = (ImageView)findViewById(R.id.spot_image);
		
		Intent intent = getIntent();
		String spotID = intent.getStringExtra("spot_id");
		
		ScenicSpot spot = dm.getScenicSpot(Integer.parseInt(spotID));
		
		tvSpotName.setText(spot.getName());
		tvSpotInfo.setText(spot.getDescription());
		
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
