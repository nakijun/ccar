package org.ccar.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.ccar.R;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 摄像头摄影预览类
 * @author swansword
 *
 */
public class CameraPreview extends SurfaceView implements Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	public static Camera.Parameters camaraParam;
	
	private int index = 0; 

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
            mCamera.getParameters().setPreviewFormat(ImageFormat.JPEG);
            mCamera.setPreviewCallback(new PreviewCallback() {
				
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					if (index == 0) {
						File file = new File(Environment.getExternalStorageDirectory().getPath()+"/ccar/preview.jpg");
						try {
							int length = data.length;
							Bitmap bitmap = rawByteArray2RGBABitmap2(data,mCamera.getParameters().getPreviewSize().width, mCamera.getParameters().getPreviewSize().height);
//							Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, length);
							FileOutputStream fout = new FileOutputStream(file);
							BufferedOutputStream bos = new BufferedOutputStream(fout);
							bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
							bos.flush();
							bos.close();
//							OutputStream os = new FileOutputStream(file);
//							os.write(bitmap.compress(format, quality, stream));
//							os.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						index++;
					}
				}
			});
        } catch (IOException e) {
        	e.printStackTrace();
        	mCamera.release();
			mCamera = null;
        }
		camaraParam = mCamera.getParameters();
	}
	
	public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {  
        int frameSize = width * height;  
        int[] rgba = new int[frameSize];  
  
            for (int i = 0; i < height; i++)  
                for (int j = 0; j < width; j++) {  
                    int y = (0xff & ((int) data[i * width + j]));  
                    int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));  
                    int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));  
                    y = y < 16 ? 16 : y;  
  
                    int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));  
                    int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));  
                    int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));  
  
                    r = r < 0 ? 0 : (r > 255 ? 255 : r);  
                    g = g < 0 ? 0 : (g > 255 ? 255 : g);  
                    b = b < 0 ? 0 : (b > 255 ? 255 : b);  
  
                    rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;  
                }  
  
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
        bmp.setPixels(rgba, 0 , width, 0, 0, width, height);  
        return bmp;  
    }

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.setPreviewCallback(null) ;
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
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
