package bar.barcode.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageDispose {


    private byte[] bmpData;


    /**
     * 将Bitmap转为.bmp格式图片
     *
     * @param bitmap
     */
    public byte[] saveBmp(Bitmap bitmap) {
        // 位图大小
        int nBmpWidth = bitmap.getWidth();
        int nBmpHeight = bitmap.getHeight();
        // 图像数据大小
        int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

        // bmp文件头
        int bfSize = 14 + 40 + bufferSize;
        bmpData = new byte[bfSize];
        bmpData[0] = 0x42;
        bmpData[1] = 0x4d;

        for (int x = 2; x < 6; x++) {
            bmpData[x] = (byte) (bfSize >> (8 * (x - 2)));
        }
        for (int i = 6; i < 10; i++) {
            bmpData[i] = 0;
        }
        long bfOffBits = 14 + 40;
        for (int x = 10; x < 14; x++) {
            bmpData[x] = (byte) (bfOffBits >> (8 * (x - 10)));
        }
        // bmp信息头
        for (int x = 14; x < 18; x++) {
            bmpData[x] = (byte) (40 >> (8 * (x - 14)));
        }
        long biWidth = nBmpWidth;
        for (int i = 18; i < 22; i++) {
            bmpData[i] = (byte) (biWidth >> (8 * (i - 18)));
        }

        long biHeight = nBmpHeight;
        for (int i = 22; i < 26; i++) {
            bmpData[i] = (byte) (biHeight >> (8 * (i - 22)));
        }
        for (int i = 26; i < 28; i++) {
            bmpData[i] = (byte) (1 >> (8 * (i - 26)));
        }
        for (int i = 28; i < 30; i++) {
            bmpData[i] = (byte) (24 >> (8 * (i - 28)));
        }

        for (int i = 30; i < 54; i++) {
            bmpData[i] = 0;
        }

        // 像素扫描
        int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
        for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
            for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                int clr = bitmap.getPixel(wRow, nCol);

                bmpData[nRealCol * wWidth + wByteIdex + 54] = (byte) Color.blue(clr);
                bmpData[nRealCol * wWidth + wByteIdex + 1 + 54] = (byte) Color.green(clr);
                bmpData[nRealCol * wWidth + wByteIdex + 2 + 54] = (byte) Color.red(clr);
            }

        return bmpData;
    }



}
