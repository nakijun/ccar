package org.ccar.app;

import android.app.Application;
import android.os.Environment;

/**
 * CCAR Ӧ�ó���
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * ���ݿ��ļ����ơ�
	 */
	private String dbFile = "data.db";

	/**
	 * ���ݿ��ļ�·����
	 */
	private String dbPath = null;

	/**
	 * ��ȡ���ݿ��ļ����ơ�
	 * 
	 * @return �����ļ������ơ�
	 */
	public String getDbFile() {
		return dbFile;
	}

	/**
	 * ��ȡ���ݿ��ļ�·����
	 * 
	 * @return ���ݿ��ļ�·����
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
