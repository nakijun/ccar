package org.ccar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

/**
 * ��ҳ
 * @author swansword
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	Button btnSpotlist; 	// �������б�����ť
	Button btnAR;			// ����ǿ��ʵ����ť
	Button btnNavigation;	// ��������������ť
	Button btnExit;			// ���˳�����ť
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // ���ø�����ť�ļ�����
        btnSpotlist = (Button)findViewById(R.id.spotlist_button);
        btnSpotlist.setOnClickListener(this);
        btnAR = (Button)findViewById(R.id.ar_button);
        btnAR.setOnClickListener(this);
        btnNavigation = (Button)findViewById(R.id.navigation_button);
        btnNavigation.setOnClickListener(this);
        btnExit = (Button)findViewById(R.id.exit_button);
        btnExit.setOnClickListener(this);
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
		switch (v.getId()) {
		case R.id.spotlist_button:
			Toast.makeText(this, "spotlist", Toast.LENGTH_SHORT).show();
			break;
		case R.id.ar_button:
			Toast.makeText(this, "ar", Toast.LENGTH_SHORT).show();
			break;
		case R.id.navigation_button:
			Toast.makeText(this, "navigation", Toast.LENGTH_SHORT).show();
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