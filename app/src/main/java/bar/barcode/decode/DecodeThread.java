package bar.barcode.decode;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.synqe.Barcode.Barcode;
import com.synqe.Barcode.DecodeImage;
import com.synqe.Barcode.ImageType.EarMark;
import com.synqe.Barcode.ResultObject.DecodeImageData_Result;
import com.synqe.Barcode.ResultObject.decode_16x16_data_Result;

import bar.barcode.util.ImageDispose;

public class DecodeThread extends AsyncTask<Void, Void, Integer> {
    private Lumin luminanceSource;
    private DecodeListener listener;
    private boolean isStop = false;
    private byte[] pout;
    private int aByte;
    private int result;


    public DecodeThread(Lumin luminanceSource, DecodeListener listener) {
        this.luminanceSource = luminanceSource;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        long start = System.currentTimeMillis();
        ImageDispose imageDispose = new ImageDispose();
        Bitmap mBitmap = luminanceSource.renderCroppedGreyScaleBitmap();
        byte[] bytes = imageDispose.saveBmp(mBitmap);
        pout = new byte[256];
        aByte = CodeBar.getByte(bytes, pout);
        if (aByte == 0) {
            decode_16x16_data_Result DR = DecodeImage.DecodeSmallImage(pout);
            result = DR.Result;
            long end = System.currentTimeMillis();
            Log.e("Decode Success ", "Time:  " + (end - start) + "ms");
            return result;
        } else {
            long end = System.currentTimeMillis();
            Log.e("Decode Fail ", "Time:  " + (end - start) + "ms");
            return aByte;
        }


    }


    @Override
    protected void onPostExecute(Integer a) {
        if (listener != null && !isStop) {
            if (a != 0) {
                listener.onDecodeFailed(luminanceSource);
            } else {
                listener.onDecodeSuccess(pout, luminanceSource);
            }
        }
    }


    public void cancel() {
        isStop = true;
        cancel(true);
    }


}
