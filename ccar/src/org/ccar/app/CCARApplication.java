package org.ccar.app;

import org.ccar.data.DatabaseManager;

import android.app.Application;

/**
 * CCAR 应用程序。
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * 数据库管理类。
	 */
	private DatabaseManager dbManager;

	/**
	 * 获取数据库管理类。
	 * 
	 * @return 数据库管理类。
	 */
	public DatabaseManager getDatabaseManager() {
		if (dbManager == null) {
			dbManager = new DatabaseManager(this);
		}
		return dbManager;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

}
