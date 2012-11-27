package org.ccar.ar;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class IconMarker extends Marker {
    private Bitmap bitmap = null;

    public IconMarker(int id, String name, String info, String code, double latitude, double longitude, double altitude, int color, Bitmap bitmap) {
        super(id, name, info, code, latitude, longitude, altitude, color);
        this.bitmap = bitmap;
    }

    @Override
    public void drawIcon(Canvas canvas) {
    	if (canvas==null || bitmap==null) throw new NullPointerException();

        if (gpsSymbol==null) gpsSymbol = new PaintableIcon(bitmap,64,74);

        textXyzRelativeToCameraView.get(textArray);
        symbolXyzRelativeToCameraView.get(symbolArray);

        float currentAngle = Utilities.getAngle(symbolArray[0], symbolArray[1], textArray[0], textArray[1]);
        float angle = currentAngle + 90;

        if (symbolContainer==null) symbolContainer = new PaintablePosition(gpsSymbol, symbolArray[0], symbolArray[1], angle, 1);
        else symbolContainer.set(gpsSymbol, symbolArray[0], symbolArray[1], angle, 1);

        symbolContainer.paint(canvas);
    }
}