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
	private Context ctx;

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
		ctx = context;
		if (copyDatabase()) {
			dbHelper = new DatabaseHelper(ctx, dbFile);
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
				+ ctx.getPackageName() + "/databases";
		File path = new File(dbPath);
		if (!path.exists() && !path.mkdirs()) {
			Toast.makeText(ctx, R.string.mkdirs_error, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// 如果数据库文件不存在，则拷贝。
		dbFile = dbPath + "/data.db";
		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				// 打开 assets 目录中的数据库文件。
				InputStream inputStream = ctx.getResources().getAssets()
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
				Toast.makeText(ctx, R.string.copy_database_error,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 * 获取所有景点。
	 * 
	 * @return 所有景点。
	 */
	public List<ScenicSpot> getScenicSpots() {

		if (!initialized) {
			Toast.makeText(ctx, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();

		// 查询
		Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
				"select * from t_scenicspot", null);
		while (cursor.moveToNext()) {
			ScenicSpot scenicSpot = new ScenicSpot();
			scenicSpot.setID(cursor.getInt(cursor.getColumnIndex("ID")));
			scenicSpot.setName(cursor.getString(cursor.getColumnIndex("Name")));
			scenicSpot.setDescription(cursor.getString(cursor
					.getColumnIndex("Description")));
			scenicSpot.setX(cursor.getDouble(cursor.getColumnIndex("X")));
			scenicSpot.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
			scenicSpots.add(scenicSpot);
		}
		cursor.close();

		return scenicSpots;

	}

	/**
	 * 关闭数据库。
	 */
	public void closeDatabase() {
		dbHelper.close();
	}

}
