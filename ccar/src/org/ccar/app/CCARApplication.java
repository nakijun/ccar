package org.ccar.app;

import android.app.Application;

/**
 * CCAR Ӧ�ó���
 * 
 * @author FG
 * 
 */
public class CCARApplication extends Application {

	/**
	 * ���ݿ��ļ�·����
	 */
	private String dbPath = "ccar";

	/**
	 * ���ݿ��ļ����ơ�
	 */
	private String dbFile = "data.db";

	/**
	 * ��ȡ�����ļ���·����
	 * 
	 * @return �����ļ���·����
	 */
	public String getDbPath() {
		return dbPath;
	}

	/**
	 * ��ȡ���ݿ��ļ����ơ�
	 * 
	 * @return �����ļ������ơ�
	 */
	public String getDbFile() {
		return dbFile;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
