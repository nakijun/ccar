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
 * ���ݿ�����ࡣ
 * 
 * @author FG
 * 
 */
public class DatabaseManager {
	
	/**
	 * ���ڴ����ݿ�������ġ�
	 */
	private Context ctx;

	/**
	 * ���ݿ��ļ���
	 */
	private String dbFile;

	/**
	 * ���ݿ�����ࡣ
	 */
	private DatabaseHelper dbHelper;

	/**
	 * ָʾ���ݿ��Ƿ��ʼ����
	 */
	private boolean initialized;

	/**
	 * ���캯����
	 * 
	 * @param context
	 *            ���ڴ����ݿ�������ġ�
	 * @param name
	 *            ���ݿ����ơ�
	 */
	public DatabaseManager(Context context) {
		ctx = context;
		if (copyDatabase()) {
			dbHelper = new DatabaseHelper(ctx, dbFile);
			initialized = true;
		}
	}

	/**
	 * �������ݿ��ļ��������ݿ��ļ��� assets Ŀ¼�������ֻ��ڴ��С�
	 * 
	 * @return �Ƿ񿽱��ɹ���
	 */
	private boolean copyDatabase() {

		// ������ݿ�Ŀ¼�����ڣ��򴴽���
		String dbPath = Environment.getDataDirectory() + "/data/"
				+ ctx.getPackageName() + "/databases";
		File path = new File(dbPath);
		if (!path.exists() && !path.mkdirs()) {
			Toast.makeText(ctx, R.string.mkdirs_error, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// ������ݿ��ļ������ڣ��򿽱���
		dbFile = dbPath + "/data.db";
		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				// �� assets Ŀ¼�е����ݿ��ļ���
				InputStream inputStream = ctx.getResources().getAssets()
						.open("data.db");

				// �����ݿ��ļ��������ֻ��ڴ��С�
				OutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
				outputStream.flush();

				// �ر��ļ���
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
	 * ��ȡ���о��㡣
	 * 
	 * @return ���о��㡣
	 */
	public List<ScenicSpot> getScenicSpots() {

		if (!initialized) {
			Toast.makeText(ctx, R.string.database_not_initialized,
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<ScenicSpot> scenicSpots = new ArrayList<ScenicSpot>();

		// ��ѯ
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
	 * �ر����ݿ⡣
	 */
	public void closeDatabase() {
		dbHelper.close();
	}

}
