package org.ccar.app;

import android.app.Application;

/**
 * CCAR 应用程序
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * 数据库文件路径。
	 */
	private String dbPath = "ccar";

	/**
	 * 数据库文件名称。
	 */
	private String dbFile = "data.db";

	/**
	 * 获取数据文件库路径。
	 * 
	 * @return 数据文件库路径。
	 */
	public String getDbPath() {
		return dbPath;
	}

	/**
	 * 获取数据库文件名称。
	 * 
	 * @return 数据文件库名称。
	 */
	public String getDbFile() {
		return dbFile;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
