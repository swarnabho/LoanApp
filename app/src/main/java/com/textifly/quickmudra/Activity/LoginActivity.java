package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLoginBinding binding;
    String mobile, password;

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
        mobile = "+91 "+binding.tilPhone.getEditText().getText().toString();
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
            login();
        }
    }

    private void login() {
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
                        ManageLoginData.addLoginData(id,name,email,mobile);
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        redirect();
                    }else{
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
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", mobile);
                map.put("password", password);
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
}