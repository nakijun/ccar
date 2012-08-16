package org.ccar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;

/**
 * 主页
 * @author swansword
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
	Button btnSpotlist; 	// “景点列表”按钮
	Button btnAR;			// “增强现实”按钮
	Button btnNavigation;	// “景区导航”按钮
	Button btnExit;			// “退出”按钮
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setListener(); // 设置各个按钮的监听器
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
	 * 监听各按钮的点击事件，跳转到相应页面
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
	 * 按回退键退出应用程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exitApp();
		}
		return false;
	}
	
	/**
	 * 设置各按钮的监听器
	 */
	private void setListener() {
		btnSpotlist = (Button)findViewById(R.id.spotlist_button);
        btnSpotlist.setOnClickListener(this);
        btnAR = (Button)findViewById(R.id.ar_button);
        btnAR.setOnClickListener(this);
        btnNavigation = (Button)findViewById(R.id.navigation_button);
        btnNavigation.setOnClickListener(this);
        btnExit = (Button)findViewById(R.id.exit_button);
        btnExit.setOnClickListener(this);
	}

	/**
	 * 退出应用程序，退出前弹出确认框进行确认。
	 */
	private void exitApp() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确认退出吗？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}