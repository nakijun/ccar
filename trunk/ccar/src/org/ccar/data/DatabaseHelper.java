package org.ccar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类。
 * 
 * @author FG
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * 构造函数。
	 * 
	 * @param context
	 *            用于打开数据库的上下文。
	 * @param name
	 *            数据库名称。
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
