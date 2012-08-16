package org.ccar;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;

/**
 * 增强现实
 * @author swansword
 * 
 */
public class ARActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ar);
	}

	/**
	 * 检测设备是否有摄像头
	 * @param context
	 * @return
	 */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // 有摄像头
	        return true;
	    } else {
	        // 没有摄像头
	        return false;
	    }
	}
	
	/**
	 * 获取摄像头
	 * @return 返回一个Camera实例，如果没有Camera或当前不可用，则返回null
	 */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open();
	    }
	    catch (Exception e){
	        e.printStackTrace();
	    }
	    return c;
	}
}
