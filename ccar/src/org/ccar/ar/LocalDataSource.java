package org.ccar.ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ccar.R;
import org.ccar.data.DatabaseManager;
import org.ccar.data.ScenicSpot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class LocalDataSource extends DataSource {
	private List<Marker> cachedMarkers = new ArrayList<Marker>();
	private static Bitmap icon = null;

	private static HashMap<String, Bitmap> icons = new HashMap<String, Bitmap>();

	public LocalDataSource(Resources res) {
		if (res == null)
			throw new NullPointerException();

		createIcon(res);
	}

	protected void createIcon(Resources res) {
		if (res == null)
			throw new NullPointerException();

		icon = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);

		icons.put("CS", BitmapFactory.decodeResource(res, R.drawable.cs));
		icons.put("CY", BitmapFactory.decodeResource(res, R.drawable.cy));
		icons.put("HC", BitmapFactory.decodeResource(res, R.drawable.hc));
		icons.put("JD", BitmapFactory.decodeResource(res, R.drawable.jd));
		icons.put("MT", BitmapFactory.decodeResource(res, R.drawable.mt));
		icons.put("QL", BitmapFactory.decodeResource(res, R.drawable.ql));
		icons.put("RK", BitmapFactory.decodeResource(res, R.drawable.rk));
		icons.put("TCC", BitmapFactory.decodeResource(res, R.drawable.tcc));
		icons.put("ZXC", BitmapFactory.decodeResource(res, R.drawable.zxc));
	}

	public List<Marker> getMarkers() {
		Marker atl = new IconMarker(1, "ATL", "", "", 39.931269, -75.051261, 0,
				Color.DKGRAY, icon);
		cachedMarkers.add(atl);

		Marker home = new Marker(2, "Mt Laurel", "", "", 39.95, -74.9, 0,
				Color.YELLOW);
		cachedMarkers.add(home);

		return cachedMarkers;
	}

	public List<Marker> getMarkers(DatabaseManager dm, double x, double y,
			double radius) {
		cachedMarkers.clear();
		List<ScenicSpot> scenicSpots = dm.getScenicSpots(String.valueOf(x),
				String.valueOf(y), radius);
		for (ScenicSpot scenicSpot : scenicSpots) {
			Bitmap bitmap = icons.get(scenicSpot.getCode());
			if (bitmap == null) {
				continue;
			}
			Marker marker = new IconMarker(scenicSpot.getID(),
					scenicSpot.getName(), scenicSpot.getDescription(),
					scenicSpot.getCode(), scenicSpot.getLat(),
					scenicSpot.getLon(), 0, Color.YELLOW, bitmap);
//			Marker marker = new IconMarker(scenicSpot.getID(),
//					scenicSpot.getName(), scenicSpot.getDescription(),
//					scenicSpot.getCode(), scenicSpot.getLat() + 0.008553,
//					scenicSpot.getLon() + 0.077639, 0, Color.YELLOW, bitmap);
			cachedMarkers.add(marker);
		}
		return cachedMarkers;
	}
}