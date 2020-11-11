package ca.kylegray.vanvin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import static com.google.android.gms.vision.barcode.Barcode.CODE_128;

public class ScanActivity extends AppCompatActivity {

    public final String TAG = " 👻 SCANNING";
    SurfaceView cameraPreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_scan);
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        createCameraSource();
        Log.d(TAG, " ACTIVITY CREATED");
    }

    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(CODE_128 | Barcode.CODE_39 | Barcode.CODE_93)
                .build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            private static final int MY_PERMISSION_REQUEST_CAMERA = 2525;

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
                return;
            } try {
                cameraSource.start(cameraPreview.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("👻 CAMERA ", "Barcode Scan Error: " + e);
            }

        }



            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0)); //get latest barcode from the array
                    Log.d(TAG, "Barcode Value Is: " + barcodes.valueAt(0)); //returns object reference not string
                    //for (int i=0; i < barcodes.size(); i++){
                        //Log.d(TAG, barcodes.get(i).toString());
                    //}

                    setResult(CommonStatusCodes.SUCCESS,intent);
                    finish();
                /*}else{ //this code seems to bypass the app even trying to get a barcode
                    Intent intent = new Intent();
                    setResult(CommonStatusCodes.ERROR, intent);
                    finish();// added 07.28.20*/
                }
            }
        });
    }
    /*@Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        createCameraSource();
    }*/

}
