package com.textifly.quickmudra.Activity;

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
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.textifly.quickmudra.ApiManager.ApiClient;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ResponseDataModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.Utils.WebService;
import com.textifly.quickmudra.databinding.ActivityPanCardBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanCardActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityPanCardBinding binding;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    File PanFront, PanBack;
    String position = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPanCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();
        binding.tilPanNo.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    private void initView() {
        CustomProgressDialog.showDialog(PanCardActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE_COMPLETE_CHECK, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(PanCardActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        String file_type = object.getString("file_type");
                        String frontImg = object.getString("image1");
                        String backImg = object.getString("image2");
                        String id_no = object.getString("id_no");

                        binding.percentPD.setText("100%");
                        binding.tilPanNo.getEditText().setText(id_no);
                        Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+frontImg).into(binding.ivPanFront);
                        //Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+backImg).into(binding.ivPanBack);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(PanCardActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("type", "pan");

                return body;
            }
        };

        Volley.newRequestQueue(PanCardActivity.this).add(sr);

    }

    private void BtnClick() {
        binding.ivPanFront.setOnClickListener(this);
        binding.ivPanBack.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivPanFront:
                position = "front";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(PanCardActivity.this);
                }
                break;
            case R.id.ivPanBack:
                position = "back";
                if (checkAndRequestPermissions(this)) {
                    chooseImage(PanCardActivity.this);
                }
                break;
            case R.id.btnSubmit:
                if(binding.tilPanNo.getEditText().toString().isEmpty()){
                    Toast.makeText(PanCardActivity.this, "Please enter pan number", Toast.LENGTH_SHORT).show();
                    binding.tilPanNo.getEditText().requestFocus();
                }else if (PanFront == null) {
                    Toast.makeText(PanCardActivity.this, "Please enter voter id front image", Toast.LENGTH_SHORT).show();
                } /*else if (PanBack == null) {
                    Toast.makeText(PanCardActivity.this, "Please enter voter id back image", Toast.LENGTH_SHORT).show();
                }*/ else {
                    CustomProgressDialog.showDialog(PanCardActivity.this, true);
                    uploadVoterId();
                }
                break;
        }
    }

    private void uploadVoterId() {
        Log.d("VoterFront",PanFront.getName());
        Log.d("VoterBack",PanBack.getName());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), YoDB.getPref().read(Constants.ID,""));
        RequestBody pan = RequestBody.create(MediaType.parse("text/plain"),binding.tilPanNo.getEditText().getText().toString());
        RequestBody percentage = RequestBody.create(MediaType.parse("text/plain"),"100");

        RequestBody bodyVoterFront = RequestBody.create(MediaType.parse("image/*"), PanFront);
        MultipartBody.Part pan_font = MultipartBody.Part.createFormData("pan_front", PanFront.getName(), bodyVoterFront);

        RequestBody bodyVoterBack = RequestBody.create(MediaType.parse("image/*"), PanBack);
        MultipartBody.Part pan_back = MultipartBody.Part.createFormData("pan_rear", PanBack.getName(), bodyVoterBack);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updatePan(user_id,pan,percentage,pan_font, pan_back);

        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                CustomProgressDialog.showDialog(PanCardActivity.this, false);
                ResponseDataModel model = response.body();
                Log.d("RESPONSE",model.getStatus());
                if(model.getStatus().equals("0")){
                    Toast.makeText(PanCardActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PanCardActivity.this,DetailsListActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });

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
            ActivityCompat.requestPermissions(PanCardActivity.this, listPermissionsNeeded
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
                if (ContextCompat.checkSelfPermission(PanCardActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PanCardActivity.this.getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(PanCardActivity.this,
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
                        binding.ivPanFront.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(this, photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //File finalFile = new File(getRealPathFromURI(tempUri));
                        PanFront = new File(getRealPathFromURI(tempUri));
                        Log.e("finalFile==", String.valueOf(PanFront));
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
                            binding.ivPanFront.setImageBitmap(bitmap);
                            //File finalFile2 = new File(path);
                            PanFront = new File(path);
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
                        binding.ivPanBack.setImageBitmap(photo);
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(this, photo);
                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        //File finalFile = new File(getRealPathFromURI(tempUri));
                        PanBack = new File(getRealPathFromURI(tempUri));
                        Log.e("finalFile==", String.valueOf(PanBack));
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
                            binding.ivPanBack.setImageBitmap(bitmap);
                            PanBack = new File(path);
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
    
}