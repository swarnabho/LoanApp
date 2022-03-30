package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.Helper.ManageLoginData;
import com.textifly.quickmudra.Model.ContactListModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityRegistrationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityRegistrationBinding binding;
    String name, email, phno, password, referral, IMEINumber;
    private static final int REQUEST_CODE = 101;


    TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BtnClick();
        getIemiNo();

        //initView();

    }

    /*private void getContactList() {
        checkPermission();
    }*/


    //@SuppressLint("MissingPermission")
    private void getIemiNo() {
        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.READ_PHONE_STATE);
        if (ActivityCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            return;
        }

        IMEINumber = telephonyManager.getDeviceId();*/

        /*int permission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        if(permission == PackageManager.PERMISSION_GRANTED){
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEINumber = tm.getDeviceId().toString() ;
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},123);
        }*/

        IMEINumber = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("IMEINumber", IMEINumber);

    }

    private void initView() {
        name = binding.tilName.getEditText().getText().toString();
        email = binding.tilEmail.getEditText().getText().toString();
        phno = getIntent().getStringExtra("phno");  //7439638404
        password = binding.tilPassword.getEditText().getText().toString();
        referral = binding.tilRefferal.getEditText().getText().toString();
    }

    private void BtnClick() {
        binding.btnSignup.setOnClickListener(this);
        binding.btnGenerate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                //Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_SHORT).show();
                checkData();
                break;
            case R.id.btnGenerate:
                String pw = passwordGenerator();
                binding.tilPassword.getEditText().setText(pw);
                binding.tilConfirmPassword.getEditText().setText(pw);
                break;
        }
    }

    private String passwordGenerator() {
        Random random = new Random();
        String data = "";
        int length = 10;
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(26) + 97);
            if (random.nextBoolean()) {
                c = Character.toUpperCase(c);
            }
            data += c;
        }
        return data;
    }

    private void checkData() {
        /*name = binding.tilName.getEditText().getText().toString();
        email = binding.tilEmail.getEditText().getText().toString();
        phno = getIntent().getStringExtra("phno");  //7439638404*/
        referral = binding.tilRefferal.getEditText().getText().toString();
        String password = binding.tilPassword.getEditText().getText().toString();
        String cnfrmPassword = binding.tilConfirmPassword.getEditText().getText().toString();
        Log.d("RES", "p: " + password + ", cP: " + cnfrmPassword);
        //Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
        if (binding.tilName.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_SHORT).show();
            binding.tilName.getEditText().requestFocus();
        } else if (binding.tilEmail.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_SHORT).show();
            binding.tilEmail.getEditText().requestFocus();
        } else if (binding.tilPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_SHORT).show();
            binding.tilPassword.getEditText().requestFocus();
        } else if (binding.tilConfirmPassword.getEditText().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
            binding.tilConfirmPassword.getEditText().requestFocus();
        } else if (!password.equals(cnfrmPassword)) {
            Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
        } else {
            registration();
        }
    }

    private void registration() {
        CustomProgressDialog.showDialog(RegistrationActivity.this, true);
        //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(RegistrationActivity.this, false);
                Log.d("RESPONSE", response);
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("RESPONSE", object.toString());
                    if (object.getString("status").equals("0")) {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("RES NAME", object.getString("fname"));
                        String id = object.getString("userid");
                        String name = object.getString("fname");
                        String mobile = object.getString("mobile");
                        String email = object.getString("email");

                        //saveInFirestore();

                        ManageLoginData.addLoginData(id, name, email, mobile);
                        startActivity(new Intent(RegistrationActivity.this, ApplyActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);

                    } else {
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
                CustomProgressDialog.showDialog(RegistrationActivity.this, false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email", binding.tilEmail.getEditText().getText().toString());
                map.put("mobile", "+91 "+getIntent().getStringExtra("phno"));
                map.put("fname", binding.tilName.getEditText().getText().toString());
                map.put("confirm_password", binding.tilPassword.getEditText().getText().toString());
                map.put("promo_code", binding.tilRefferal.getEditText().getText().toString());
                map.put("imei_no", IMEINumber);
                return map;
            }
        };//email,mobile,fname,confirm_password,promo_code,imei_no

        Volley.newRequestQueue(getApplicationContext()).add(sr);
    }

    private void saveInFirestore() {
        Toast.makeText(RegistrationActivity.this, "firestore", Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("name", binding.tilName.getEditText().getText().toString());
        user.put("email", binding.tilEmail.getEditText().getText().toString());
        user.put("mobile", getIntent().getStringExtra("phno"));

        db.collection("users").document("USER")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success_RES", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure_RES", "Error writing document", e);
                    }
                });
    }
}