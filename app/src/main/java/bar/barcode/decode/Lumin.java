package bar.barcode.decode;

import android.graphics.Bitmap;



public abstract class Lumin extends LuminanceSource {

    protected Lumin(int width, int height) {
        super(width, height);
    }

    public abstract Bitmap renderCroppedGreyScaleBitmap();
}
