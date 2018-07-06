
package bar.barcode.decode;

import android.graphics.Bitmap;
import android.graphics.Rect;

import bar.barcode.camera.Size;
public class PlanarYUVLuminanceSource extends Lumin {
	private byte[] yuvData;
	private Size dataSize;
	private Rect previewRect;

	/**
	 * @param yuvData
	 *                  YUV数据，包含Y信息和UV信息
	 * @param dataSize
	 *                  图像大小
	 * @param previewRect
	 *                  要处理的图像区域
	 */
	public PlanarYUVLuminanceSource(byte[] yuvData, Size dataSize, Rect previewRect) {
		super(previewRect.width(), previewRect.height());

		if (previewRect.left + previewRect.width() > dataSize.width || previewRect.top + previewRect.height() > dataSize.height) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}

		this.yuvData = yuvData;
		this.dataSize = dataSize;
		this.previewRect = previewRect;
	}

	@Override
	public byte[] getRow(int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		int offset = (y + previewRect.top) * dataSize.width + previewRect.left;
		System.arraycopy(yuvData, offset, row, 0, width);
		return row;
	}

	@Override
	public byte[] getMatrix() {
		int width = getWidth();
		int height = getHeight();
		if (width == dataSize.width && height == dataSize.height) {
			return yuvData;
		}
		int area = width * height;
		byte[] matrix = new byte[area];
		int inputOffset = previewRect.top * dataSize.width + previewRect.left;

		if (width == dataSize.width) {
			System.arraycopy(yuvData, inputOffset, matrix, 0, area);
			return matrix;
		}

		byte[] yuv = yuvData;
		for (int y = 0; y < height; y++) {
			int outputOffset = y * width;
			System.arraycopy(yuv, inputOffset, matrix, outputOffset, width);
			inputOffset += dataSize.width;
		}
		return matrix;
	}

	@Override
	public boolean isCropSupported() {
		return true;
	}

	public int getDataWidth() {
		return dataSize.width;
	}

	public int getDataHeight() {
		return dataSize.height;
	}

	/**
	 * 根据扫描结果，生成一个灰度图像
	 * 
	 * @return
	 */
	public Bitmap renderCroppedGreyScaleBitmap() {
		int width = getWidth();
		int height = getHeight();
		int[] pixels = new int[width * height];
		byte[] yuv = yuvData;
		int inputOffset = previewRect.top * dataSize.width + previewRect.left;
		for (int y = 0; y < height; y++) {
			int outputOffset = y * width;
			for (int x = 0; x < width; x++) {
				int grey = yuv[inputOffset + x] & 0xff;
				pixels[outputOffset + x] = 0xFF000000 | (grey * 0x00010101);
			}
			inputOffset += dataSize.width;
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
