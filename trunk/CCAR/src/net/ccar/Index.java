package net.ccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/** 
 * 系统主界面 
 * @author swansword
 * @createdate 2012-3-19
 * */
public class Index extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button cameraButton = (Button) findViewById(R.id.btnCamera);
        cameraButton.setOnClickListener(this);
        Button canvasButton = (Button) findViewById(R.id.btnCanvas);
        canvasButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btnCamera:
			i = new Intent(this, CameraActivity.class);
			startActivity(i);
			break;
		case R.id.btnCanvas:
			i = new Intent(this, CanvasActivity.class);
			startActivity(i);
			break;
		default:
			break;
		
		}
	}
}