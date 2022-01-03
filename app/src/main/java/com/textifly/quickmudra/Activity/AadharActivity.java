package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.Toast;

import com.textifly.quickmudra.ApiManager.ApiClient;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ResponseDataModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.WebService;
import com.textifly.quickmudra.databinding.ActivityAadharBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AadharActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityAadharBinding binding;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    File AadharFront, VoterBack;
    String position = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAadharBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();
    }

    private void BtnClick() {
        binding.ivAadharFont.setOnClickListener(this);
        binding.ivAadharBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            ActivityCompat.requestPermissions(AadharActivity.this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(AadharActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AadharActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(AadharActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(this);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (position.equals("front")) {
            if (resultCode != RESULT_CANCELED) {
                switch (requestCode) {
                    case 0://Camera
                        //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        binding.ivAadharFont.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(this, photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //File finalFile = new File(getRealPathFromURI(tempUri));
                        AadharFront = new File(getRealPathFromURI(tempUri));
                        Log.e("finalFile==", String.valueOf(AadharFront));
                        //listFile = new ArrayList<>();
                        //listFile.add(finalFile);
                        break;

                    case 1:
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                            String path = getPathFromURI(selectedImageUri);
                            Log.e("path==", path);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            binding.ivAadharFont.setImageBitmap(bitmap);
                            //File finalFile2 = new File(path);
                            AadharFront = new File(path);
                            /*listFile = new ArrayList<>();
                            listFile.add(finalFile2);*/
                        }
                        break;

                }
            }
        } else if (position.equals("back")) {
            if (resultCode != RESULT_CANCELED) {
                switch (requestCode) {
                    case 0://Camera
                        //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        binding.ivAadharBack.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(this, photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //File finalFile = new File(getRealPathFromURI(tempUri));
                        VoterBack = new File(getRealPathFromURI(tempUri));
                        Log.e("finalFile==", String.valueOf(VoterBack));
                        /*listFile = new ArrayList<>();
                        listFile.add(finalFile);*/
                        break;

                    case 1:
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                            String path = getPathFromURI(selectedImageUri);
                            Log.e("path==", path);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            binding.ivAadharBack.setImageBitmap(bitmap);
                            VoterBack = new File(path);
                            /*listFile = new ArrayList<>();
                            listFile.add(finalFile2);*/
                        }
                        break;

                }
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



    private void uploadVoterId() {
        Log.d("AadharFront",AadharFront.getName());
        Log.d("VoterBack",VoterBack.getName());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), YoDB.getPref().read(Constants.ID,""));
        RequestBody aadhar = RequestBody.create(MediaType.parse("text/plain"),binding.tilAadharNo.getEditText().getText().toString());
        RequestBody percentage = RequestBody.create(MediaType.parse("text/plain"),"60");

        RequestBody bodyVoterFront = RequestBody.create(MediaType.parse("image/*"), AadharFront);
        MultipartBody.Part aadhar_font = MultipartBody.Part.createFormData("aadhar_font", AadharFront.getName(), bodyVoterFront);

        RequestBody bodyVoterBack = RequestBody.create(MediaType.parse("image/*"), VoterBack);
        MultipartBody.Part aadhar_back = MultipartBody.Part.createFormData("aadhar_back", VoterBack.getName(), bodyVoterBack);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updateAadhar(user_id,aadhar,percentage,aadhar_font, aadhar_back);

        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                CustomProgressDialog.showDialog(AadharActivity.this, false);
                ResponseDataModel model = response.body();
                Log.d("RESPONSE",model.getStatus());
                if(model.getStatus().equals("0")){
                    Toast.makeText(AadharActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AadharActivity.this,PassportActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivAadharFont:
                //Toast.makeText(this, "front", Toast.LENGTH_SHORT).show();
                position = "front";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(AadharActivity.this);
                }
                break;
            case R.id.ivAadharBack:
                //Toast.makeText(this, "front", Toast.LENGTH_SHORT).show();
                position = "back";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(AadharActivity.this);
                }
                break;
            case R.id.btnSubmit:
                if (AadharFront == null) {
                    Toast.makeText(AadharActivity.this, "Please enter aadhar front image", Toast.LENGTH_SHORT).show();
                } else if (VoterBack == null) {
                    Toast.makeText(AadharActivity.this, "Please enter aadhar back image", Toast.LENGTH_SHORT).show();
                } else {
                    CustomProgressDialog.showDialog(AadharActivity.this, true);
                    uploadVoterId();
                }
                break;
        }
    }
}