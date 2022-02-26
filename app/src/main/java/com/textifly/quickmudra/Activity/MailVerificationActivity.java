package com.textifly.quickmudra.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.CustomPreference;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityAlternativeContactBinding;
import com.textifly.quickmudra.databinding.ActivityMailVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MailVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMailVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMailVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkMailVerify();
        BtnClick();
    }

    private void checkMailVerify() {
        CustomProgressDialog.showDialog(MailVerificationActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(MailVerificationActivity.this,false);
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("mail_verify").isEmpty() || object.getString("mail_verify").equals("0")){
                        //Toast.makeText(MailVerificationActivity.this, "hi", Toast.LENGTH_SHORT).show();
                        binding.btnContinue.setVisibility(View.VISIBLE);
                        binding.txtVerify.setVisibility(View.GONE);
                        binding.percentPD.setText("0%");
                    }else if(object.getString("mail_verify").equals("1")){
                        //Toast.makeText(MailVerificationActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        binding.btnContinue.setVisibility(View.GONE);
                        binding.txtVerify.setVisibility(View.VISIBLE);
                        binding.percentPD.setText("100%");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(MailVerificationActivity.this,false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", YoDB.getPref().read(Constants.ID, ""));
                return map;
            }
        };

        Volley.newRequestQueue(MailVerificationActivity.this).add(sr);
    }

    private void BtnClick() {
        binding.etEmailAddress.setText(YoDB.getPref().read(Constants.EMAIL, ""));
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                verifyEmail(binding.etEmailAddress.getText().toString());
                break;
        }
    }

    private void verifyEmail(String email) {
        CustomProgressDialog.showDialog(MailVerificationActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.MAIL_VERIFY_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(MailVerificationActivity.this, false);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("0")) {
                        Toast.makeText(MailVerificationActivity.this, "A verification link has been sent to " + email, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MailVerificationActivity.this,DetailsListActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(MailVerificationActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("email_id", email);
                return body;
            }
        };
        Volley.newRequestQueue(MailVerificationActivity.this).add(sr);
    }

}