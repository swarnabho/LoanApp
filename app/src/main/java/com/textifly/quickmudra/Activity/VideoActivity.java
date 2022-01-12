package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.textifly.quickmudra.ApiManager.ApiClient;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ResponseDataModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.Utils.WebService;
import com.textifly.quickmudra.databinding.ActivityVideoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityVideoBinding binding;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private static final int VIDEO_RECORD_CODE = 102;
    private Uri videoPath;
    File SelfieVideoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.percentPD.setText(YoDB.getPref().read(Constants.UploadPercentage, "") + "%");
        initView();
        BtnClick();
        showToast();
    }

    private void initView() {
        CustomProgressDialog.showDialog(VideoActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.UPLOADED_DOCUMENT_FETCH, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(VideoActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("files");
                        Log.d("jsonArray", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (object.getString("file_type").equalsIgnoreCase("profilevideo")) {
                                //Toast.makeText(VideoActivity.this, "hi", Toast.LENGTH_SHORT).show();
                                //Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+object.getString("image1")).into(binding.ivVideo);
                                /*Uri video = Uri.parse(object.getString("image1"));
                                Bitmap photo = ThumbnailUtils.createVideoThumbnail(getRealPathFromURI(video), MediaStore.Images.Thumbnails.MINI_KIND);
                                binding.ivVideo.setImageBitmap(photo);*/
                                RequestOptions requestOptions = new RequestOptions();
                                //requestOptions.placeholder(R.drawable.placeholder_card_view);
                                //requestOptions.error(R.drawable.placeholder_card_view);


                                Glide.with(getApplicationContext())
                                        .load(Urls.IMAGE_URL+object.getString("image1"))
                                        .apply(requestOptions)
                                        .thumbnail(Glide.with(getApplicationContext()).load(Urls.IMAGE_URL+object.getString("image1")))
                                        .into(binding.ivVideo);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(VideoActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(VideoActivity.this).add(sr);
    }

    private void showToast() {
        final Handler h = new Handler();
        final Runnable r = new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                //Toast.makeText(getBaseContext(), "please told on video:My name is.... and I am responsible to repay my loans on Dopfin", Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(R.id.llVideo), "please told on video:My name is.... and I am responsible to repay my loans on Dopfin", Snackbar.LENGTH_LONG)
                        .setDuration(10000)
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            }
        };

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                h.post(r);
            }
        }, 2000, 5000);
    }

    private void BtnClick() {
        binding.tvContinue.setOnClickListener(this);
        binding.ivVideo.setOnClickListener(this);
        binding.tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_video:
                if (checkAndRequestPermissions(VideoActivity.this)) {
                    recordVideo();
                }
                break;
            case R.id.tvNext:
                startActivity(new Intent(VideoActivity.this, DetailsListActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.tvContinue:
                loadPercentage();
                YoDB.getPref().write(Constants.UploadNextDoc, "", "complete");
                if (SelfieVideoFile != null) {
                    CustomProgressDialog.showDialog(VideoActivity.this, true);
                    uploadVoterId();
                } else {
                    Toast.makeText(VideoActivity.this, "Please add selfie video", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
            ActivityCompat.requestPermissions(VideoActivity.this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    private void loadPercentage() {
        int percentage = (600 / 6);
        YoDB.getPref().write(Constants.UploadPercentage, "", String.valueOf(percentage));
        /*Log.d("Percentage", percentage + "%");
        Constants.UploadPercentage = String.valueOf(percentage);*/
        binding.percentPD.setText(percentage + "%");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_RECORD_CODE) {
            if (resultCode == RESULT_OK) {
                videoPath = data.getData();
                Bitmap photo = ThumbnailUtils.createVideoThumbnail(getRealPathFromURI(videoPath), MediaStore.Images.Thumbnails.MINI_KIND);
                binding.ivVideo.setImageBitmap(photo);
                Log.d("videoPath", videoPath.toString());
                SelfieVideoFile = new File(getRealPathFromURI(videoPath));
                Log.e("finalFile==", String.valueOf(SelfieVideoFile));

            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void uploadVoterId() {
        Log.d("AadharFront", SelfieVideoFile.getName());

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), YoDB.getPref().read(Constants.ID, ""));
        RequestBody percentage = RequestBody.create(MediaType.parse("text/plain"), "100");

        RequestBody bodyVoterFront = RequestBody.create(MediaType.parse("video/*"), SelfieVideoFile);
        MultipartBody.Part selfieVideo = MultipartBody.Part.createFormData("profile_video", SelfieVideoFile.getName(), bodyVoterFront);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updateSelfieVideo(user_id, percentage, selfieVideo);

        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, Response<ResponseDataModel> response) {
                CustomProgressDialog.showDialog(VideoActivity.this, false);
                ResponseDataModel model = response.body();
                Log.d("RESPONSE", model.getStatus());
                if (model.getStatus().equals("0")) {
                    Toast.makeText(VideoActivity.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VideoActivity.this, DetailsListActivity.class));
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });

    }

}