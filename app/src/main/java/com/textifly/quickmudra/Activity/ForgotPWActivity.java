package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityForgotPwactivityBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPWActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityForgotPwactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPwactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();

        binding.tilPhoneNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(RegisterActivity.this, "count:"+count, Toast.LENGTH_SHORT).show();
                int length = binding.tilPhoneNo.getEditText().getText().length();
                String strLen = String.valueOf(length);
                if (length <= 10)
                    binding.txtCounter1.setText(strLen + "/10");

                /*if(count <= 10)
                    binding.txtCounter.setText(count+"/10");*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        if (getIntent().hasExtra("phno")) {
            binding.llMobile.setVisibility(View.GONE);
            binding.llPassword.setVisibility(View.VISIBLE);
            binding.tvSave.setText("Update Password");
        } else {
            binding.llMobile.setVisibility(View.VISIBLE);
            binding.llPassword.setVisibility(View.GONE);
            binding.tvSave.setText("Verify Mobile Number");
        }
    }

    private void BtnClick() {
        binding.tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSave:
                if (binding.tvSave.getText().toString().equalsIgnoreCase("Verify Mobile Number")) {
                    checkMobileNo(binding.tilPhoneNo.getEditText().getText().toString());
                } else if (binding.tvSave.getText().toString().equalsIgnoreCase("Update Password")) {
                    forgotPW(getIntent().getStringExtra("phno"));
                }
                break;
        }
    }

    private void checkMobileNo(String ph) {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.MOBILE_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("1")) {
                        //forgotPW(ph);
                        Toast.makeText(ForgotPWActivity.this, "Ph no.: " + ph, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPWActivity.this, OtpValidationActivity.class);
                        intent.putExtra("phno", "+91" + ph);
                        intent.putExtra("from", "forgotPW");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("mobile", "+91 " + ph);
                return body;
            }
        };
        Volley.newRequestQueue(ForgotPWActivity.this).add(sr);
    }

    private void forgotPW(String ph) {
        CustomProgressDialog.showDialog(ForgotPWActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.FORGET_PW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(ForgotPWActivity.this, false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("0")) {
                        Toast.makeText(ForgotPWActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPWActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(ForgotPWActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("mobile", "+91 " + ph);
                body.put("password", binding.tilNewPassword.getEditText().getText().toString());
                return body;
            }
        };
        Volley.newRequestQueue(ForgotPWActivity.this).add(sr);
    }
}