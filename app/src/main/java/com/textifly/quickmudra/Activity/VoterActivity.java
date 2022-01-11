package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.textifly.quickmudra.ApiManager.ApiClient;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ResponseDataModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.Utils.WebService;
import com.textifly.quickmudra.databinding.ActivityVoterBinding;

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
import retrofit2.http.Url;

public class VoterActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityVoterBinding binding;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    File VoterFront, VoterBack;
    String position = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();
    }

    private void initView() {
        if(getIntent().hasExtra("frontImg")){
            binding.percentPD.setText("100%");
            Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+getIntent().getStringExtra("frontImg")).into(binding.ivVoterFont);
            Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+getIntent().getStringExtra("backImg")).into(binding.ivVoterBack);
        }
    }

    private void BtnClick() {
        binding.ivVoterFont.setOnClickListener(this);
        binding.ivVoterBack.setOnClickListener(this);
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
            ActivityCompat.requestPermissions(VoterActivity.this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(VoterActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(VoterActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(VoterActivity.this,
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (position.equals("front")) {
            if (resultCode != RESULT_CANCELED) {
                switch (requestCode) {
                    case 0://Camera
                        //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        binding.ivVoterFont.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(this, photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //File finalFile = new File(getRealPathFromURI(tempUri));
                        VoterFront = new File(getRealPathFromURI(tempUri));
                        Log.e("finalFile==", String.valueOf(VoterFront));
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
                            binding.ivVoterFont.setImageBitmap(bitmap);
                            //File finalFile2 = new File(path);
                            VoterFront = new File(path);
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
                        binding.ivVoterBack.setImageBitmap(photo);
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
                            binding.ivVoterBack.setImageBitmap(bitmap);
                            VoterBack = new File(path);
                            /*listFile = new ArrayList<>();
                            listFile.add(finalFile2);*/
                        }
                        break;

                }
            }
        }
        //Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivVoterFont:
                position = "front";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(VoterActivity.this);
                }
                break;
            case R.id.ivVoterBack:
                position = "back";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(VoterActivity.this);
                }
                break;
            case R.id.btnSubmit:
                if (VoterFront == null) {
                    Toast.makeText(VoterActivity.this, "Please enter voter id front image", Toast.LENGTH_SHORT).show();
                } else if (VoterBack == null) {
                    Toast.makeText(VoterActivity.this, "Please enter voter id back image", Toast.LENGTH_SHORT).show();
                } else {
                    CustomProgressDialog.showDialog(VoterActivity.this, true);
                    uploadVoterId();
                }
                break;
        }
    }

    private void uploadVoterId() {
        Log.d("VoterFront",VoterFront.getName());
        Log.d("VoterBack",VoterBack.getName());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),YoDB.getPref().read(Constants.ID,""));
        RequestBody percentage = RequestBody.create(MediaType.parse("text/plain"),"100");

        RequestBody bodyVoterFront = RequestBody.create(MediaType.parse("image/*"), VoterFront);
        MultipartBody.Part multi_VoterFront = MultipartBody.Part.createFormData("voter_front", VoterFront.getName(), bodyVoterFront);

        RequestBody bodyVoterBack = RequestBody.create(MediaType.parse("image/*"), VoterBack);
        MultipartBody.Part multi_VoterBack = MultipartBody.Part.createFormData("voter_back", VoterBack.getName(), bodyVoterBack);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updateVoter(user_id,percentage,multi_VoterFront, multi_VoterBack);

        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                CustomProgressDialog.showDialog(VoterActivity.this, false);
                ResponseDataModel model = response.body();
                Log.d("RESPONSE",model.getStatus());
                if(model.getStatus().equals("0")){
                    Toast.makeText(VoterActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(VoterActivity.this,PanCardActivity.class));
                    startActivity(new Intent(VoterActivity.this,DetailsListActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(VoterActivity.this,DetailsListActivity.class));
        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
    }
}