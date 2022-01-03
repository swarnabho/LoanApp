package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityMailVerificationBinding;
import com.textifly.quickmudra.databinding.ActivityWhatsAppVerificationBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WhatsAppVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityWhatsAppVerificationBinding binding;
    String[] spinner;
    String data;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private ArrayList<File> listFile = new ArrayList<>();
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsAppVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();
    }

    private void chooseFile(int requestCode) {
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        @SuppressLint("IntentReset")
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("*/*");
        //intent.setType("image/*|application/pdf|application/doc|application/docx");
        //intent.setType("pdf/application");
        intent.setType("*/images");
        /*String[] extraMimeTypes = {"Image/*","application/pdf", "application/doc"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);*/
        startActivityForResult(intent, requestCode);
    }

    private void initView() {
        binding.llVoterId.setVisibility(View.GONE);
        binding.llPanCard.setVisibility(View.GONE);
        binding.llAadhar.setVisibility(View.GONE);
        binding.llDrivingLicence.setVisibility(View.GONE);
        binding.llPassport.setVisibility(View.GONE);

        spinner = new String[]{"Select Document", "Voter ID", "PAN Card", "Passport", "Driving License", "Aadhaar"};
        ArrayAdapter<String> obj = new ArrayAdapter<String>(WhatsAppVerificationActivity.this, android.R.layout.simple_list_item_1, spinner);
        binding.spn.setAdapter(obj);

        binding.spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data = binding.spn.getSelectedItem().toString();
                if (data.equalsIgnoreCase("PAN Card")) {
                    startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                   /* binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.VISIBLE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);*/
                } else if (data.equalsIgnoreCase("Voter ID")) {
                    startActivity(new Intent(WhatsAppVerificationActivity.this, VoterActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    /*binding.llVoterId.setVisibility(View.VISIBLE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);*/
                } else if (data.equalsIgnoreCase("Aadhaar")) {
                    startActivity(new Intent(WhatsAppVerificationActivity.this, AadharActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    /*binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.VISIBLE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);*/
                } else if (data.equalsIgnoreCase("Passport")) {
                    startActivity(new Intent(WhatsAppVerificationActivity.this, PassportActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    /*binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.VISIBLE);*/
                } else if (data.equalsIgnoreCase("Driving License")) {
                    startActivity(new Intent(WhatsAppVerificationActivity.this, DrivingLisenceActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    /*binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.VISIBLE);
                    binding.llPassport.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void BtnClick() {
        binding.btnContinue.setOnClickListener(this);
        binding.ivVoterFont.setOnClickListener(this);
        binding.ivVoterBack.setOnClickListener(this);
        binding.ivPanFont.setOnClickListener(this);
        binding.ivPanBack.setOnClickListener(this);
        binding.ivAadharFont.setOnClickListener(this);
        binding.ivAadharBack.setOnClickListener(this);
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                //overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                saveVoterID();
                break;
            case R.id.ivVoterFont:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 101);
                    }
                }
                break;
            case R.id.ivVoterBack:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 102);
                    }
                }
                break;
            case R.id.ivPanFont:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 103);
                    }
                }
                break;
            case R.id.ivPanBack:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 104);
                    }
                }
                break;
            case R.id.ivAadharFont:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 105);
                    }
                }
                break;
            case R.id.ivAadharBack:
                if (checkAndRequestPermissions(WhatsAppVerificationActivity.this)) {
                    if (checkAndRequestPermissions(getApplicationContext())) {
                        chooseImage(WhatsAppVerificationActivity.this, 106);
                    }
                }
                break;
        }
    }

    private void saveVoterID() {

    }

    private void chooseImage(Context context, int requestCode) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (optionsMenu[i].equals("Choose from Gallery")) {
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    private boolean checkAndRequestPermissions(Context context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(WhatsAppVerificationActivity.this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(WhatsAppVerificationActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(WhatsAppVerificationActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(WhatsAppVerificationActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(WhatsAppVerificationActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //chooseImage(WhatsAppVerificationActivity.this);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0://Camera
                    //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    binding.ivVoterFont.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(this, photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    Log.e("finalFile==", String.valueOf(finalFile));
                    listFile = new ArrayList<>();
                    listFile.add(finalFile);
                    break;

                case 1:
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        String path = getPathFromURI(selectedImageUri);
                        Log.e("path==", path);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        binding.ivVoterFont.setImageBitmap(bitmap);
                        File finalFile2 = new File(path);
                        listFile = new ArrayList<>();
                        listFile.add(finalFile2);
                    }
                    break;

            }
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, "", null, "");
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WhatsAppVerificationActivity.this, DetailsListActivity.class));
        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
    }
}