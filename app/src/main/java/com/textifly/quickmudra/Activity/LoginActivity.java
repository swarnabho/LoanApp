package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.Helper.ManageLoginData;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.Model.ContactListModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding binding;
    String IMEINumber;
    String mobile, password;
    String contact = "";

    List<ContactListModel> contactListModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();

        binding.tilPhone.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(RegisterActivity.this, "count:"+count, Toast.LENGTH_SHORT).show();
                int length = binding.tilPhone.getEditText().getText().length();
                String strLen = String.valueOf(length);
                if (length <= 10)
                    binding.txtCounter.setText(strLen + "/10");

                /*if(count <= 10)
                    binding.txtCounter.setText(count+"/10");*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void BtnClick() {
        IMEINumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        binding.tvLogin.setOnClickListener(this);
        binding.llRegister.setOnClickListener(this);
        binding.llForgotPW.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                checkLoginData();
                break;
            case R.id.llForgotPW:
                startActivity(new Intent(LoginActivity.this, ForgotPWActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llRegister:
                startActivity(new Intent(LoginActivity.this, MobileNoVerificationActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
        }
    }

    private void checkLoginData() {
        mobile = "+91 " + binding.tilPhone.getEditText().getText().toString();
        password = binding.tilPassword.getEditText().getText().toString();

        if (binding.tilPhone.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            binding.tilPhone.getEditText().requestFocus();
        } else if (binding.tilPhone.getEditText().getText().toString().length() != 10) {
            Toast.makeText(LoginActivity.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
            binding.tilPhone.getEditText().requestFocus();
        } else if (binding.tilPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            binding.tilPassword.getEditText().requestFocus();
        } else {
            checkPermission();
        }
    }

    private void login(String contact) {
        Log.d("Contact_List", contact);

        CustomProgressDialog.showDialog(LoginActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(LoginActivity.this, false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("0")) {
                        String id = object.getString("id");
                        String name = object.getString("fname");
                        String mobile = object.getString("mobile");
                        String email = object.getString("email");
                        ManageLoginData.addLoginData(id, name, email, mobile);
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        redirect();
                    } else {
                        Toast.makeText(LoginActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.getMessage());
                Toast.makeText(getApplicationContext(), "Getting some troubles", Toast.LENGTH_SHORT).show();
                CustomProgressDialog.showDialog(LoginActivity.this, false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("BODY", mobile + "," + password + "," + IMEINumber + "," + contact.toString());
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("password", password);
                map.put("device_id", IMEINumber);
                map.put("contact_list", contact);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(sr);
    }

    private void redirect() {
        //if(YoDB.getPref().read(Constants.UploadNextDoc))
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.GET_PERMISSIONS) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContacts();
        }
    }

    private void getContacts() {
        contactListModels = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + "ASC";

        Cursor cursor = getContentResolver().query(uri, null, null, null, sort);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";

                Cursor phoneCursor = getContentResolver().query(phoneUri, null, selection,
                        new String[]{id}, null);

                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d("PH_NUMBER", number);

                    contactListModels.add(new ContactListModel(number));
                    phoneCursor.close();
                }
            }
            cursor.close();

            Log.d("contactList_size", String.valueOf(contactListModels.size()));

            int size;
            if (contactListModels.size() <= 50)
                size = contactListModels.size();
            else {
                size = 50;
            }
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                if (contact.equals("")) {
                    //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
                    contact = contactListModels.get(i).getPhno();
                } else {
                    contact = contact + "," + contactListModels.get(i).getPhno();
                }
                array.put(contactListModels.get(i).getPhno());
            }
            Log.d("Phone_NUMBER", contact);
            login(contact);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        } else {
            Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            checkPermission();
        }
    }

}