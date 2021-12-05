package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityCameraPermissionBinding;

import java.util.List;

public class CameraPermissionActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityCameraPermissionBinding binding;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();

    }

    private void BtnClick() {
        binding.chkbox.setOnClickListener(this);
        binding.tvAgree.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CameraPermissionActivity.this, OnBoardScreenActivity.class));
        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkbox:
                if (binding.chkbox.isChecked())
                    isChecked = true;
                else if (!binding.chkbox.isChecked())
                    isChecked = false;
                break;
            case R.id.tvAgree:
                if (isChecked == false) {
                    Toast.makeText(CameraPermissionActivity.this, "Please accept terms and conditions", Toast.LENGTH_SHORT).show();
                } else {
                    checkPermissions();
                }
                break;
        }
    }

    private void checkPermissions() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    // do you work now
                    Toast.makeText(CameraPermissionActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CameraPermissionActivity.this, MobileNoVerificationActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                }
                // check for permanent denial of any permission
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    // permission is denied permanently,
                    // we will show user a dialog message.
                    //showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError dexterError) {
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }
}