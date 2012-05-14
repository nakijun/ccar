package net.ccar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CanvasActivity extends Activity {
	LinearLayout mLinearLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mLinearLayout = new LinearLayout(this);
		
		CustomView view = new CustomView(this);

		mLinearLayout.addView(view);
		setContentView(mLinearLayout);
	}

}

class CustomView extends View {
	private ShapeDrawable mDrawable;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		mDrawable.draw(canvas);
	}

	public CustomView(Context context) {
		super(context);
		int x = 10;
	    int y = 10;
	    int width = 300;
	    int height = 50;

	    mDrawable = new ShapeDrawable(new OvalShape());
	    mDrawable.getPaint().setColor(0xff74AC23);
	    mDrawable.setBounds(x, y, x + width, y + height);
	}
	
}
