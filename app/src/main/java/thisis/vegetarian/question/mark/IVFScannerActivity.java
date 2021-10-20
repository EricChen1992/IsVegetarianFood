package thisis.vegetarian.question.mark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Switch;

import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;



public class IVFScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener{
    private CaptureManager captureManager;
    private DecoratedBarcodeView barcodeView;
    private Switch switchFlashLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivf_scanner);

        barcodeView = findViewById(R.id.zxing_barcode_scanner_cf);
        barcodeView.setTorchListener(this);


        switchFlashLight = findViewById(R.id.switch_flashlight);
//        viewfinderView = findViewById(R.id.zxing_viewfinder_view);

        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            switchFlashLight.setVisibility(View.GONE);
        }

        captureManager = new CaptureManager(this, barcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.setShowMissingCameraPermissionDialog(false);
        captureManager.decode();

//        changeMaskColor(null);
        changeLaserVisibility(true);

        //set actionbar close button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ivf_close);

    }

    public void changeLaserVisibility(boolean visible) {
//        viewfinderView.setLaserVisibility(visible);
        Log.e("CF_Scanner",""+ visible);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /** Open Flashlight */
    public void switchFlashlight(View view) {
        if (getString(R.string.turn_off_flashlight).trim().toLowerCase().equals(switchFlashLight.getText().toString().trim().toLowerCase())) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }

    /** Flashlight ON*/
    @Override
    public void onTorchOn() {
        switchFlashLight.setText(R.string.turn_on_flashlight);
    }

    /** Flashlight OFF*/
    @Override
    public void onTorchOff() {
        switchFlashLight.setText(R.string.turn_off_flashlight);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}