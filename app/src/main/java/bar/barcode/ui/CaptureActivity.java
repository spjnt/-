package bar.barcode.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import bar.barcode.App;
import bar.barcode.R;
import bar.barcode.camera.CameraManager;
import bar.barcode.camera.PreviewFrameShotListener;
import bar.barcode.camera.Size;
import bar.barcode.decode.DecodeListener;
import bar.barcode.decode.DecodeThread;
import bar.barcode.decode.Lumin;
import bar.barcode.decode.PlanarYUVLuminanceSource;
import bar.barcode.decode.RGBLuminanceSource;
import bar.barcode.util.DocumentUtil;
import bar.barcode.util.ToastUtil;
import bar.barcode.view.CaptureView;


public class CaptureActivity extends Activity implements SurfaceHolder.Callback, PreviewFrameShotListener, OnClickListener, DecodeListener {

    private static final int REQUEST_CODE_ALBUM = 0;
    public static final String EXTRA_RESULT = "result";
    private SurfaceView previewSv;
    private CaptureView captureView;
    private ImageButton backBtn;
    private Button albumBtn;
    private DecodeThread mDecodeThread;
    private CameraManager mCameraManager;
    private Rect previewFrameRect = null;
    private boolean isDecoding = false;

    private PlanarYUVLuminanceSource luminanceSource;
    private CheckBox bulb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        previewSv = (SurfaceView) findViewById(R.id.sv_preview);
        captureView = (CaptureView) findViewById(R.id.cv_capture);
        backBtn = (ImageButton) findViewById(R.id.btn_back);
        bulb = findViewById(R.id.bulb);
        bulb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCameraManager.enableFlashlight();
                } else {
                    mCameraManager.disableFlashlight();
                }
            }
        });
        bulb.setEnabled(false);
        backBtn.setOnClickListener(this);
        albumBtn = (Button) findViewById(R.id.choice_photo);
        albumBtn.setOnClickListener(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            albumBtn.setVisibility(View.GONE);
        }
        previewSv.getHolder().addCallback(this);
        mCameraManager = new CameraManager(this);
        mCameraManager.setPreviewFrameShotListener(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCameraManager.initCamera(holder);
        if (!mCameraManager.isCameraAvailable()) {
            Toast.makeText(CaptureActivity.this, R.string.capture_camera_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (mCameraManager.isFlashlightAvailable()) {
            bulb.setEnabled(true);
        }
        mCameraManager.startPreview();
        mCameraManager.requestPreviewFrameShot();

        if (!isDecoding) {
            mCameraManager.requestPreviewFrameShot();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.stopPreview();
        if (mDecodeThread != null) {
            mDecodeThread.cancel();
        }
        mCameraManager.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPreviewFrame(byte[] data, Size dataSize) {

        if (previewFrameRect == null) {
            previewFrameRect = mCameraManager.getPreviewFrameRect(captureView.getFrameRect());
        }
        luminanceSource = new PlanarYUVLuminanceSource(data, dataSize, previewFrameRect);
        mDecodeThread = new DecodeThread(luminanceSource, this);
        mDecodeThread.execute();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.choice_photo:
                Intent intent = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK && data != null) {
            Bitmap cameraBitmap = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String path = DocumentUtil.getPath(CaptureActivity.this, data.getData());
                cameraBitmap = DocumentUtil.getBitmap(path);
            } else {
                // Not supported in SDK lower that KitKat
            }
            if (cameraBitmap != null) {
                if (mDecodeThread != null) {
                    mDecodeThread.cancel();
                }
                int width = cameraBitmap.getWidth();
                int height = cameraBitmap.getHeight();
                int[] pixels = new int[width * height];
                cameraBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                RGBLuminanceSource luminanceSource = new RGBLuminanceSource(pixels, new Size(width, height));
                mDecodeThread = new DecodeThread(luminanceSource, CaptureActivity.this);
                isDecoding = true;
                mDecodeThread.execute();
            }
        }
    }

    @Override
    public void onDecodeSuccess(byte[] bytes, Lumin source) {
        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        bundle.putByteArray(EXTRA_RESULT, bytes);
        resultData.putExtra("bundle", bundle);
        setResult(RESULT_OK, resultData);
        finish();


    }

    @Override
    public void onDecodeFailed(Lumin source) {
        if (source instanceof RGBLuminanceSource) {
//            ToastUtils.showStr(R.string.capture_decode_failed, ConfigToast.WARING);
            ToastUtil.showToast(CaptureActivity.this, "No Code found", ToastUtil.Showstate.FAIL);
        }
        isDecoding = false;
        mCameraManager.requestPreviewFrameShot();
    }


}
