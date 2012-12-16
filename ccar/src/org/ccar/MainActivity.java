package org.ccar;

import org.ccar.app.CCARApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * ��ҳ
 * 
 * @author swansword
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	ImageButton btnSpotlist; // �������б���ť
	ImageButton btnAR; // ����ǿ��ʵ����ť
	ImageButton btnNavigation; // ��������������ť
//	Button btnExit; // ���˳�����ť

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()   // or .detectAll() for all detectable problems
        .penaltyLog()
        .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedSqlLiteObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());

		setListener(); // ���ø�����ť�ļ�����
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// �ر����ݿ⡣
		CCARApplication ccarApplication = (CCARApplication) getApplication();
		ccarApplication.getDatabaseManager().closeDatabase();
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
		case R.id.imgbtn_spotlist:
			i = new Intent(this, SpotCatalogActivity.class);
			startActivity(i);
			break;
		case R.id.imgbtn_ar:
			i = new Intent(this, ARActivity.class);
			startActivity(i);
			break;
		case R.id.imgbtn_navigation:
			i = new Intent(this, NavigationActivity.class);
			startActivity(i);
			break;
//		case R.id.exit_button:
//			exitApp();
//			break;
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
	 * ���ø���ť�ļ�����
	 */
	private void setListener() {
		btnSpotlist = (ImageButton) findViewById(R.id.imgbtn_spotlist);
		btnSpotlist.setOnClickListener(this);
		btnAR = (ImageButton) findViewById(R.id.imgbtn_ar);
		btnAR.setOnClickListener(this);
		btnNavigation = (ImageButton) findViewById(R.id.imgbtn_navigation);
		btnNavigation.setOnClickListener(this);
//		btnExit = (Button) findViewById(R.id.exit_button);
//		btnExit.setOnClickListener(this);
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