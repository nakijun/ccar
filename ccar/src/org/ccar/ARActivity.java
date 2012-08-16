package org.ccar;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;

/**
 * ��ǿ��ʵ
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
	 * ����豸�Ƿ�������ͷ
	 * @param context
	 * @return
	 */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // ������ͷ
	        return true;
	    } else {
	        // û������ͷ
	        return false;
	    }
	}
	
	/**
	 * ��ȡ����ͷ
	 * @return ����һ��Cameraʵ�������û��Camera��ǰ�����ã��򷵻�null
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
