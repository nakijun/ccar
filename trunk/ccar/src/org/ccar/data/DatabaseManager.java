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
	 * 指示排序优先级，默认为距离优先。
	 */
	private OrderPriority orderPriority = OrderPriority.Distance;

	/**
	 * 获取排序优先级。
	 * 
	 * @return 排序优先级。
	 */
	public OrderPriority getOrderPriority() {
		return orderPriority;
	}

	/**
	 * 设置排序优先级。
	 * 
	 * @param orderPriority
	 *            将排序优先级设置为该值。
	 */
	public void setOrderPriority(OrderPriority orderPriority) {
		this.orderPriority = orderPriority;
	}

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
			scenicSpot = constructInstance(cursor);
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
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from t_scenicspot order by ?", new String[] { "id" });
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor);
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
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from t_scenicspot order by ? limit ? offset ?",
				new String[] { "id", String.valueOf(limit),
						String.valueOf(offset) });
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = constructInstance(cursor);
			scenicSpots.add(scenicSpot);
		}
		cursor.close();
		return scenicSpots;
	}

	/**
	 * 从 Cursor 构造景点实例。
	 * 
	 * @param cursor
	 *            用于构造景点实例的 Cursor。
	 * @return 景点实例。
	 */
	private ScenicSpot constructInstance(Cursor cursor) {
		ScenicSpot scenicSpot = new ScenicSpot();
		scenicSpot.setID(cursor.getInt(cursor.getColumnIndex("ID")));
		scenicSpot.setName(cursor.getString(cursor.getColumnIndex("Name")));
		scenicSpot.setDescription(cursor.getString(cursor
				.getColumnIndex("Description")));
		scenicSpot.setLon(cursor.getDouble(cursor.getColumnIndex("Lon")));
		scenicSpot.setLat(cursor.getDouble(cursor.getColumnIndex("Lat")));
		scenicSpot.setX(cursor.getDouble(cursor.getColumnIndex("X")));
		scenicSpot.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
		return scenicSpot;
	}

	/**
	 * 关闭数据库。
	 */
	public void closeDatabase() {
		dbHelper.close();
	}

}
