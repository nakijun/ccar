package net.ccar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Index extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View cameraButton = findViewById(R.id.btnCamera);
        cameraButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCamera:
			Intent i = new Intent(this, CameraView.class);
			startActivity(i);
			break;
		default:
			break;
		
		}
	}
}