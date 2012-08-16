package org.ccar;

import org.ccar.view.CameraPreview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * ��ǿ��ʵ
 * @author swansword
 * 
 */
public class ARActivity extends Activity {
	private Camera mCamera;
    private CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.ar);
		
		mCamera = getCameraInstance();
		
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
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
