package org.ccar.app;

import org.ccar.data.DatabaseManager;

import android.app.Application;

/**
 * CCAR Ӧ�ó���
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * ���ݿ�����ࡣ
	 */
	private DatabaseManager dbManager;

	/**
	 * ��ȡ���ݿ�����ࡣ
	 * 
	 * @return ���ݿ�����ࡣ
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
