package org.ccar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.ccar.app.CCARApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * ��ҳ
 * 
 * @author swansword
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	Button btnSpotlist; // �������б���ť
	Button btnAR; // ����ǿ��ʵ����ť
	Button btnNavigation; // ��������������ť
	Button btnExit; // ���˳�����ť

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		copyDatabase(); // �������ݿ��ļ��������ݿ��ļ��� assets Ŀ¼������ sd ���С�
		setListener(); // ���ø�����ť�ļ�����
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * ��������ť�ĵ���¼�����ת����Ӧҳ��
	 */
	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.spotlist_button:
			i = new Intent(this, SpotListActivity.class);
			startActivity(i);
			break;
		case R.id.ar_button:
			i = new Intent(this, ARActivity.class);
			startActivity(i);
			break;
		case R.id.navigation_button:
			i = new Intent(this, NavigationActivity.class);
			startActivity(i);
			break;
		case R.id.exit_button:
			exitApp();
			break;
		default:
			break;
		}
	}

	/**
	 * �����˼��˳�Ӧ�ó���
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitApp();
		}
		return false;
	}

	/**
	 * �������ݿ��ļ��������ݿ��ļ��� assets Ŀ¼������ sd ���С�
	 * 
	 * @return �Ƿ񿽱��ɹ���
	 */
	private boolean copyDatabase() {

		// ��ȡ CCAR Ӧ�ó���
		CCARApplication ccarApplication = (CCARApplication) getApplication();

		// sd ���е����ݿ��ļ�·�����������򴴽�.
		String dbPath = Environment.getExternalStorageDirectory() + "/"
				+ ccarApplication.getDbPath();
		File path = new File(dbPath);
		if (!path.exists() && !path.mkdirs()) {
			Toast.makeText(this, R.string.mkdirs_error, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// sd ���е����ݿ��ļ�������������п�����
		String dbFile = dbPath + "/" + ccarApplication.getDbFile();
		File file = new File(dbFile);
		if (!file.exists()) {
			try {
				// �� assets Ŀ¼�е����ݿ��ļ���
				InputStream inputStream = getResources().getAssets().open(
						ccarApplication.getDbFile());

				// �����ݿ��ļ������� sd ���С�
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
				Toast.makeText(this, R.string.copy_database_error,
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * ���ø���ť�ļ�����
	 */
	private void setListener() {
		btnSpotlist = (Button) findViewById(R.id.spotlist_button);
		btnSpotlist.setOnClickListener(this);
		btnAR = (Button) findViewById(R.id.ar_button);
		btnAR.setOnClickListener(this);
		btnNavigation = (Button) findViewById(R.id.navigation_button);
		btnNavigation.setOnClickListener(this);
		btnExit = (Button) findViewById(R.id.exit_button);
		btnExit.setOnClickListener(this);
	}

	/**
	 * �˳�Ӧ�ó����˳�ǰ����ȷ�Ͽ����ȷ�ϡ�
	 */
	private void exitApp() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("ȷ���˳���");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}