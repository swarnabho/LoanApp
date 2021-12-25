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
import com.textifly.quickmudra.databinding.ActivityUploadCollegeIdBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadCollegeIdActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityUploadCollegeIdBinding binding;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    File CollegeIdFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadCollegeIdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.percentPD.setText(YoDB.getPref().read(Constants.UploadPercentage, "") + "%");
        BtnClick();
    }

    private void BtnClick() {
        binding.tvContinue.setOnClickListener(this);
        binding.ivCollegeId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCollegeId:
                if (checkAndRequestPermissions(this)) {
                    chooseImage(UploadCollegeIdActivity.this);
                }
                break;
            case R.id.tvContinue:
                loadPercentage();
                YoDB.getPref().write(Constants.UploadNextDoc, "", "signature");
                if (CollegeIdFile != null) {
                    CustomProgressDialog.showDialog(UploadCollegeIdActivity.this, true);
                    uploadVoterId();
                } else {
                    Toast.makeText(UploadCollegeIdActivity.this, "Please add College Id", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loadPercentage() {
        int percentage = (300 / 6);
        YoDB.getPref().write(Constants.UploadPercentage, "", String.valueOf(percentage));
        /*Log.d("Percentage", percentage + "%");
        Constants.UploadPercentage = String.valueOf(percentage);*/
        binding.percentPD.setText(percentage + "%");
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
            ActivityCompat.requestPermissions(UploadCollegeIdActivity.this, listPermissionsNeeded
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
                if (ContextCompat.checkSelfPermission(UploadCollegeIdActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UploadCollegeIdActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(UploadCollegeIdActivity.this,
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
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0://Camera
                    //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    binding.ivCollegeId.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(this, photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    //File finalFile = new File(getRealPathFromURI(tempUri));
                    CollegeIdFile = new File(getRealPathFromURI(tempUri));
                    Log.e("finalFile==", String.valueOf(CollegeIdFile));
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
                        binding.ivCollegeId.setImageBitmap(bitmap);
                        //File finalFile2 = new File(path);
                        CollegeIdFile = new File(path);
                            /*listFile = new ArrayList<>();
                            listFile.add(finalFile2);*/
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


    private void uploadVoterId() {
        Log.d("AadharFront", CollegeIdFile.getName());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), "57" /*YoDB.getPref().read(Constants.ID,"")*/);

        RequestBody bodyVoterFront = RequestBody.create(MediaType.parse("image/*"), CollegeIdFile);
        MultipartBody.Part collegeId = MultipartBody.Part.createFormData("collegeId", CollegeIdFile.getName(), bodyVoterFront);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updateCollegeId(user_id, collegeId);

        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                CustomProgressDialog.showDialog(UploadCollegeIdActivity.this, false);
                ResponseDataModel model = response.body();
                Log.d("RESPONSE", model.getStatus());
                if (model.getStatus().equals("0")) {
                    Toast.makeText(UploadCollegeIdActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadCollegeIdActivity.this, UploadSignaturePhotoActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });

    }

}