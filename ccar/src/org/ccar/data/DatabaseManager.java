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
	private Context context;

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
		this.context = context;
		if (copyDatabase()) {
			dbHelper = new DatabaseHelper(context, dbFile);
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
				+ context.getPackageName() + "/databases";
		File path = new File(dbPath);
		if (!path.exists() && !path.mkdirs()) {
			Toast.makeText(context, R.string.mkdirs_error, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// ������ݿ��ļ������ڣ��򿽱���
		dbFile = dbPath + "/data.db";
		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				// �� assets Ŀ¼�е����ݿ��ļ���
				InputStream inputStream = context.getResources().getAssets()
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
				Toast.makeText(context, R.string.copy_database_error,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 * ��ȡָ�� ID �ľ����¼��
	 * 
	 * @param id
	 *            ����� ID��
	 * @return ָ�� ID �ľ����¼��
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
	 * ��ȡ���о����¼��
	 * 
	 * @return ���о����¼��
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
	 * ��ȡ��ָ��λ�ÿ�ʼָ�������ľ����¼��
	 * 
	 * @param offset
	 *            ָ��λ�á�
	 * @param limit
	 *            ָ��������
	 * @return ��ָ��λ�ÿ�ʼָ�������ľ����¼��
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
	 * ���ݾ���������Լ���ǰλ�û�ȡ���з������������¼��
	 * 
	 * @param name
	 *            ��������ơ�
	 * @param x
	 *            ��ǰλ�õ� X ���ꡣ
	 * @param y
	 *            ��ǰλ�õ� Y ���ꡣ
	 * @return ���з������������¼��
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
	 * ���ݾ���������Լ���ǰλ�û�ȡ���������Ĵ�ָ��λ�ÿ�ʼָ�������ľ����¼��
	 * 
	 * @param name
	 *            ��������ơ�
	 * @param x
	 *            ��ǰλ�õ� X ���ꡣ
	 * @param y
	 *            ��ǰλ�õ� Y ���ꡣ
	 * @param offset
	 *            ָ��λ�á�
	 * @param limit
	 *            ָ��������
	 * @return ���������Ĵ�ָ��λ�ÿ�ʼָ�������ľ����¼��
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
	 * ���ݾ���������Լ���ǰλ�û�ȡ order by ��䡣
	 * 
	 * @param name
	 *            ��������ơ�
	 * @param x
	 *            ��ǰλ�õ� X ���ꡣ
	 * @param y
	 *            ��ǰλ�õ� Y ���ꡣ
	 * @return order by ��䡣
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
	 * �� Cursor ���쾰��ʵ����
	 * 
	 * @param cursor
	 *            ���ڹ��쾰��ʵ���� Cursor��
	 * @param hasDistance
	 *            �Ƿ���ھ��롣
	 * @return ����ʵ����
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
	 * �ر����ݿ⡣
	 */
	public void closeDatabase() {
		dbHelper.close();
	}

}
