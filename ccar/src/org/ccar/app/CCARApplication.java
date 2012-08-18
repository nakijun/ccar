package org.ccar.app;

import android.app.Application;
import android.os.Environment;

/**
 * CCAR 应用程序
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * 数据库文件名称。
	 */
	private String dbFile = "data.db";

	/**
	 * 数据库文件路径。
	 */
	private String dbPath = null;

	/**
	 * 获取数据库文件名称。
	 * 
	 * @return 数据文件库名称。
	 */
	public String getDbFile() {
		return dbFile;
	}

	/**
	 * 获取数据库文件路径。
	 * 
	 * @return 数据库文件路径。
	 */
	public String getDbPath() {
		if (dbPath == null) {
			dbPath = Environment.getDataDirectory() + "/data/" + getPackageName() + "/" + dbFile;
		}
		return dbPath;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
