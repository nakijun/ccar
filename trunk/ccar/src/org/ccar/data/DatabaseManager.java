package org.ccar.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.ccar.R;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.widget.Toast;

/**
 * 数据库管理类。
 * 
 * @author FG
 * 
 */
public class DatabaseManager {

	/**
	 * 用于打开数据库的上下文。
	 */
	private Context context;

	/**
	 * 数据库文件。
	 */
	private String dbFile;

	/**
	 * 数据库帮助类。
	 */
	private DatabaseHelper dbHelper;

	/**
	 * 指示数据库是否初始化。
	 */
	private boolean initialized;

	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            用于打开数据库的上下文。
	 * @param name
	 *            数据库名称。
	 */
	public DatabaseManager(Context context) {
		this.context = context;
		if (copyDatabase()) {
			dbHelper = new DatabaseHelper(context, dbFile);
			initialized = true;
		}
	}

	/**
	 * 拷贝数据库文件，将数据库文件从 assets 目录拷贝到手机内存中。
	 * 
	 * @return 是否拷贝成功。
	 */
	private boolean copyDatabase() {

		// 如果数据库目录不存在，则创建。
		String dbPath = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/databases";
		File path = new File(dbPath);
		if (!path.exists() && !path.mkdirs()) {
			Toast.makeText(context, R.string.mkdirs_error, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// 如果数据库文件不存在，则拷贝。
		dbFile = dbPath + "/data.db";
		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				// 打开 assets 目录中的数据库文件。
				InputStream inputStream = context.getResources().getAssets()
						.open("data.db");

				// 将数据库文件拷贝到手机内存中。
				OutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
				outputStream.flush();

				// 关闭文件。
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				Toast.makeText(context, R.string.copy_database_error,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 * 获取指定 ID 的景点记录。
	 * 
	 * @param id
	 *            景点的 ID。
	 * @return 指定 ID 的景点记录。
	 */
	public ScenicSpot getScenicSpot(int id) {
		if (!initialized) {
			Toast.makeText(context, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ScenicSpot scenicSpot = null;
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from t_scenicspot where id = ?",
				new String[] { String.valueOf(id) });
		if (cursor.moveToNext()) {
			scenicSpot = constructInstance(cursor, false);
		}
		cursor.close();
		return scenicSpot;
	}

	/**
	 * 获取所有景点记录。
	 * 
	 * @return 所有景点记录。
	 */
	public List<ScenicSpot> getScenicSpots() {
		if (!initialized) {
			Toast.makeText(context, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();
		String sql = "select * from t_scenicspot order by name";
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor, false);
			scenicSpots.add(scenicSpot);
		}
		cursor.close();
		return scenicSpots;
	}

	/**
	 * 获取从指定位置开始指定条数的景点记录。
	 * 
	 * @param offset
	 *            指定位置。
	 * @param limit
	 *            指定条数。
	 * @return 从指定位置开始指定条数的景点记录。
	 */
	public List<ScenicSpot> getScenicSpots(int offset, int limit) {
		if (!initialized) {
			Toast.makeText(context, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();
		String sql = "select * from t_scenicspot order by name limit "
				+ String.valueOf(limit) + " offset " + String.valueOf(offset);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor, false);
			scenicSpots.add(scenicSpot);
		}
		cursor.close();
		return scenicSpots;
	}

	/**
	 * 根据景点的名称以及当前位置获取所有符合条件景点记录。
	 * 
	 * @param name
	 *            景点的名称。
	 * @param x
	 *            当前位置的 X 坐标。
	 * @param y
	 *            当前位置的 Y 坐标。
	 * @return 所有符合条件景点记录。
	 */
	public List<ScenicSpot> getScenicSpots(String name, String x, String y) {
		if (!initialized) {
			Toast.makeText(context, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();
		boolean hasDistance = false;
		String sql = "select *";
		if (x != null && y != null) {
			hasDistance = true;
			sql = sql + ", (x - " + x + ") * (x - " + x + ") + (y - " + y
					+ ") * (y - " + y + ") as sod";
		}
		sql = sql + " from t_scenicspot";
		String[] selectionArgs = null;
		if (name != null) {
			sql = sql + " where name = ?";
			selectionArgs = new String[] { name };
		}
		sql = sql + " order by " + getOrderBy(name, x, y);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql,
				selectionArgs);
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor, hasDistance);
			scenicSpots.add(scenicSpot);
		}
		cursor.close();
		return scenicSpots;
	}

	/**
	 * 根据景点的名称以及当前位置获取符合条件的从指定位置开始指定条数的景点记录。
	 * 
	 * @param name
	 *            景点的名称。
	 * @param x
	 *            当前位置的 X 坐标。
	 * @param y
	 *            当前位置的 Y 坐标。
	 * @param offset
	 *            指定位置。
	 * @param limit
	 *            指定条数。
	 * @return 符合条件的从指定位置开始指定条数的景点记录。
	 */
	public List<ScenicSpot> getScenicSpots(String name, String x, String y,
			int offset, int limit) {
		if (!initialized) {
			Toast.makeText(context, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();
		boolean hasDistance = false;
		String sql = "select *";
		if (x != null && y != null) {
			hasDistance = true;
			sql = sql + ", (x - " + x + ") * (x - " + x + ") + (y - " + y
					+ ") * (y - " + y + ") as sod";
		}
		sql = sql + " from t_scenicspot";
		String[] selectionArgs = null;
		if (name != null) {
			sql = sql + " where name = ?";
			selectionArgs = new String[] { name };
		}
		sql = sql + " order by " + getOrderBy(name, x, y);
		sql = sql + " limit " + String.valueOf(limit) + " offset "
				+ String.valueOf(offset);
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(sql,
				selectionArgs);
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor, hasDistance);
			scenicSpots.add(scenicSpot);
		}
		cursor.close();
		return scenicSpots;
	}

	/**
	 * 根据景点的名称以及当前位置获取 order by 语句。
	 * 
	 * @param name
	 *            景点的名称。
	 * @param x
	 *            当前位置的 X 坐标。
	 * @param y
	 *            当前位置的 Y 坐标。
	 * @return order by 语句。
	 */
	private String getOrderBy(String name, String x, String y) {
		String orderby = null;
		if (name != null) {
			orderby = "name";
		}
		if (x != null && y != null) {
			if (orderby == null) {
				orderby = orderby + ", ";
			}
			orderby = orderby + "(x - " + x + ") * (x - " + x + ") + (y - " + y
					+ ") * (y - " + y + ")";
		}
		if (orderby == null) {
			orderby = "name";
		}
		return orderby;
	}

	/**
	 * 从 Cursor 构造景点实例。
	 * 
	 * @param cursor
	 *            用于构造景点实例的 Cursor。
	 * @param hasDistance
	 *            是否存在距离。
	 * @return 景点实例。
	 */
	private ScenicSpot constructInstance(Cursor cursor, boolean hasDistance) {
		ScenicSpot scenicSpot = new ScenicSpot();
		scenicSpot.setID(cursor.getInt(cursor.getColumnIndex("ID")));
		scenicSpot.setName(cursor.getString(cursor.getColumnIndex("Name")));
		scenicSpot.setDescription(cursor.getString(cursor
				.getColumnIndex("Description")));
		scenicSpot.setImageFiles(cursor.getString(cursor.getColumnIndex("ImageFiles")));
		scenicSpot.setLon(cursor.getDouble(cursor.getColumnIndex("Lon")));
		scenicSpot.setLat(cursor.getDouble(cursor.getColumnIndex("Lat")));
		if (hasDistance) {
			scenicSpot.setDistance(Math.sqrt(cursor.getDouble(cursor
					.getColumnIndex("sod"))));
		}
		return scenicSpot;
	}

	/**
	 * 关闭数据库。
	 */
	public void closeDatabase() {
		dbHelper.close();
	}

}
