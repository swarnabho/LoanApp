package com.textifly.quickmudra.UI;

import static android.app.Activity.RESULT_CANCELED;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.textifly.quickmudra.Activity.DetailsListActivity;
import com.textifly.quickmudra.Activity.LoginActivity;
import com.textifly.quickmudra.Activity.SelfieActivity;
import com.textifly.quickmudra.Activity.VideoActivity;
import com.textifly.quickmudra.ApiManager.ApiClient;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.Helper.ManageLoginData;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ResponseDataModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.CustomPreference;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.Utils.WebService;
import com.textifly.quickmudra.databinding.FragmentProfileBinding;
import com.textifly.quickmudra.databinding.PwChangeLayoutBinding;

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

public class ProfileFragment extends Fragment implements View.OnClickListener{
    FragmentProfileBinding binding;
    private String getEditData = "";
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    File imgFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        if(YoDB.getPref().read(Constants.isFullyDocumented,"").equals("false")){
            startActivity(new Intent(getActivity(), DetailsListActivity.class));
            getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
            getActivity().finish();
        }
        BtnClick();
        loadProfile();
        return binding.getRoot();
    }

    private void BtnClick() {
        binding.tvName.setOnClickListener(this);
        binding.tvContact.setOnClickListener(this);
        binding.tvEmail.setOnClickListener(this);
        binding.tvAddress.setOnClickListener(this);
        binding.changePw.setOnClickListener(this);
        binding.fl1.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.btnUpdate.setOnClickListener(this);
    }

    private void loadProfile() {
        CustomProgressDialog.showDialog(getActivity(),true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PERSONAL_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(),false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("0")){
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            binding.tvReferalCode.setText(object.getString("refferal_code"));
                            CustomPreference cp = new CustomPreference(getActivity());
                            cp.write(Constants.REFERRAL_CODE,"",object.getString("refferal_code"));
                            binding.tvName.setText(object.getString("fname")/*+" "+object.getString("lname")*/);
                            binding.tvAddress.setText(object.getString("permanent_address"));
                            binding.tvContact.setText(object.getString("mobile"));
                            binding.tvEmail.setText(object.getString("email"));
                            Glide.with(binding.getRoot()).load(Urls.IMAGE_URL+object.getString("image")).into(binding.ivProfile);
                        }
                    }else{
                        CustomProgressDialog.showDialog(getActivity(),false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(getActivity(),false);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }


    private void inputDilog(String getEditData) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final TextView dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);

        if (getEditData.equalsIgnoreCase(Constants.NAME)) {
            dialogTitle.setText("Enter Name");
            if (binding.tvName.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Name");
            } else {
                userInputDialogEditText.setText(binding.tvName.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.PHONE)) {
            dialogTitle.setText("Enter Phone No");
            if (binding.tvName.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Phone Number");
            } else {
                userInputDialogEditText.setText(binding.tvContact.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.EMAIL)) {
            dialogTitle.setText("Enter Email Id");
            if (binding.tvName.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Email Address");
            } else {
                userInputDialogEditText.setText(binding.tvEmail.getText().toString());
            }
        }else if (getEditData.equalsIgnoreCase(Constants.ADDRESS)) {
            dialogTitle.setText("Enter Address");
            if (binding.tvName.getText().toString().isEmpty()) {
                userInputDialogEditText.setHint("Enter Address");
            } else {
                userInputDialogEditText.setText(binding.tvAddress.getText().toString());
            }
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int which) {
                        if (userInputDialogEditText.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"Please Input Valid Info!",Toast.LENGTH_LONG).show();
                        }else {
                            if (getEditData.equalsIgnoreCase(Constants.NAME)){
                                binding.tvName.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.EMAIL)){
                                binding.tvEmail.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.PHONE)){
                                binding.tvContact.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }else if (getEditData.equalsIgnoreCase(Constants.ADDRESS)){
                                binding.tvAddress.setText(userInputDialogEditText.getText().toString());
                                dialogBox.cancel();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvName:
                getEditData = Constants.NAME;
                inputDilog(getEditData);
                break;
            case R.id.tvContact:
                Log.d("Clicked", "Phone");
                getEditData = Constants.PHONE;
                inputDilog(getEditData);
                break;
            case R.id.tvEmail:
                Log.d("Clicked", "Email");
                getEditData = Constants.EMAIL;
                inputDilog(getEditData);
                break;
            case R.id.tvAddress:
                Log.d("Clicked", "Address");
                getEditData = Constants.ADDRESS;
                inputDilog(getEditData);
                break;
            case R.id.changePw:
                showPasswordChangeDialog();
                break;
            case R.id.fl1:
                //Toast.makeText(getActivity(), "pressed", Toast.LENGTH_SHORT).show();
                if (checkAndRequestPermissions(getActivity())) {
                    chooseImage(getActivity());
                }
                break;
            case R.id.btnUpdate:
                updateProfile();
                break;
            case R.id.llMenu:
                ((MainActivity)getActivity()).openDrawer();
                break;
            case R.id.llLogout:
                ManageLoginData.clearLoginData();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                getActivity().finish();
                break;
        }
    }

    private void updateProfile() {
        Toast.makeText(getActivity(), "user_id: "+YoDB.getPref().read(Constants.ID,""), Toast.LENGTH_SHORT).show();
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), YoDB.getPref().read(Constants.ID,""));
        RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"), binding.tvName.getText().toString());
        RequestBody phone_no = RequestBody.create(MediaType.parse("text/plain"), binding.tvContact.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.tvEmail.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), binding.tvAddress.getText().toString());

        Log.d("image",imgFile.getName());
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), imgFile);
        MultipartBody.Part user_image = MultipartBody.Part.createFormData("profile_photo", imgFile.getName(), surveyBody);

        WebService service = ApiClient.getRetrofitInstance().create(WebService.class);
        Call<ResponseDataModel> call = service.updateProfile(user_id,user_name,address,phone_no,email,user_image);
        call.enqueue(new Callback<ResponseDataModel>() {
            @Override
            public void onResponse(Call<ResponseDataModel> call, retrofit2.Response<ResponseDataModel> response) {
                /*CustomProgressDialog.showDialog(getActivity(), false);
                Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                loadProfile();*/
                ResponseDataModel model = response.body();
                Log.d("RESPONSE", model.getStatus());
                if (model.getStatus().equals("0")) {
                    Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show();
                    loadProfile();
                }else{
                    Toast.makeText(getActivity(), "Profile not updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataModel> call, Throwable t) {

            }
        });

    }

    private void showPasswordChangeDialog(){
        final Dialog dialog = new Dialog(getActivity());
        PwChangeLayoutBinding binding1 = PwChangeLayoutBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding1.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        dialog.setCancelable(false);
        binding1.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding1.newPw.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter new password", Toast.LENGTH_SHORT).show();
                    binding1.newPw.requestFocus();
                }else{
                    updatePassword(dialog,binding1.newPw.getText().toString());
                }
            }
        });

        binding1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void updatePassword(Dialog dialog, String password) {
        Toast.makeText(getActivity(), "password: "+password, Toast.LENGTH_SHORT).show();
        CustomProgressDialog.showDialog(getActivity(),true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(),false);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("status").equals("0")){
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Password does not update", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(getActivity(),false);
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("confirm_password",password);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
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
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded
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
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(getActivity());

                }
                break;
        }
    }

    private void chooseImage(Context context) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0://Camera
                    //Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    binding.ivProfile.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    /*File finalFile = new File(getRealPathFromURI(tempUri));
                    Log.e("finalFile==", String.valueOf(finalFile));*/
                    /*listFile = new ArrayList<>();
                    listFile.add(finalFile);*/
                    imgFile = new File(getRealPathFromURI(tempUri));
                    Log.e("finalFile==", String.valueOf(imgFile));
                    break;

                case 1:
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        //Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        String path = getPathFromURI(selectedImageUri);
                        Log.e("path==", path);
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        binding.ivProfile.setImageBitmap(bitmap);
                        /*File finalFile2 = new File(path);
                        listFile = new ArrayList<>();
                        listFile.add(finalFile2);*/

                        imgFile = new File(path);
                        Log.e("finalFile==", String.valueOf(imgFile));
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, "", null, "");
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}