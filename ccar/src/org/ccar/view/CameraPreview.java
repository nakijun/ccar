package org.ccar.view;

import java.io.IOException;

import org.ccar.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * ����ͷ��ӰԤ����
 * @author swansword
 *
 */
public class CameraPreview extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	public static Camera.Parameters camaraParam;

	public CameraPreview(Context context) {
		super(context);
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e){
        	
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e){
        	e.printStackTrace();
        	mCamera.release();
			mCamera = null;
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            mCamera.setPreviewCallback(new PreviewCallback() {
				
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {

				}
			});
        } catch (IOException e) {
        	e.printStackTrace();
        	mCamera.release();
			mCamera = null;
        }
		camaraParam = mCamera.getParameters();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.setPreviewCallback(null) ;
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
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
