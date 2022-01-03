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
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityMobileNoVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileNoVerificationActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityMobileNoVerificationBinding binding;
    int length = 0;
    String phno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMobileNoVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();

        binding.etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                length = binding.etMobileNumber.getText().length();
                if(length == 10){
                    binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg));
                }else{
                    binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void BtnClick() {
        binding.btnContinue.setOnClickListener(this);
        binding.llLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnContinue:
                if(length == 10){
                    phno = "+"+binding.ccp.getSelectedCountryCode()+" "+binding.etMobileNumber.getText().toString();
                    checkMobileNumber();
                    //otpCheck();
                }else{
                    Toast.makeText(MobileNoVerificationActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    binding.etMobileNumber.requestFocus();
                }
                break;
            case R.id.llLogin:
                startActivity(new Intent(MobileNoVerificationActivity.this,LoginActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }

    private void checkMobileNumber() {
        CustomProgressDialog.showDialog(MobileNoVerificationActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.MOBILE_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(MobileNoVerificationActivity.this,false);
                //CustomProgressDialog.showDialog(MobileNoVerificationActivity.this, false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("0")) {
                        //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        otpCheck();

                    }else if (object.getString("status").equals("1")) {
                        CustomProgressDialog.showDialog(MobileNoVerificationActivity.this,false);
                        Toast.makeText(MobileNoVerificationActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(MobileNoVerificationActivity.this,false);
                Log.d("ERROR", error.getMessage());
                Toast.makeText(getApplicationContext(), "Getting some troubles", Toast.LENGTH_SHORT).show();
                //CustomProgressDialog.showDialog(MobileNoVerificationActivity.this, false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("mobile", binding.etMobileNumber.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(sr);
    }

    private void otpCheck() {
        Toast.makeText(MobileNoVerificationActivity.this, "Ph no.: "+phno, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MobileNoVerificationActivity.this, OtpValidationActivity.class);
        intent.putExtra("phno",phno);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}