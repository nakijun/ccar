package org.ccar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ�����ࡣ
 * 
 * @author FG
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * ���캯����
	 * 
	 * @param context
	 *            ���ڴ����ݿ�������ġ�
	 * @param name
	 *            ���ݿ����ơ�
	 */
	public DatabaseHelper(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
