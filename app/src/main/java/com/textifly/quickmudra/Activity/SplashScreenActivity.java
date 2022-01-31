package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.textifly.quickmudra.databinding.ActivitySplashScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySplashScreenBinding binding;
    Animation animation;
    Handler mHandler;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        BtnClick();
        loadAnimateLogo();
        redirect();
    }

    private void redirect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(YoDB.getPref().read(Constants.ID,"").isEmpty()){
                    intent = new Intent(SplashScreenActivity.this, OnBoardScreenActivity.class);
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    startActivity(intent);
                    finish();
                }else{
                    //checkUser("kyc");
                    //intent = new Intent(SplashScreenActivity.this, DetailsListActivity.class);
                    //intent = new Intent(SplashScreenActivity.this, DetailsListActivity.class);
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    startActivity(intent);
                }
            }
        },4000);
    }

    /*private void checkUser(String type) {

        Log.d("USER_ID",YoDB.getPref().read(Constants.ID,""));

        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE_COMPLETE_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                Log.d("RES_TYPE",type);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");
                    Log.d("ARRAY_LEN", "Length: "+String.valueOf(array.length()));
                    String status = jsonObject.getString("status");
                    //if(status.equals("0")){
                        if(array.length() > 0){
                            if(type.equalsIgnoreCase("kyc")) {
                                Log.d("RES_TYPE",type);
                                checkUser("documents");
                            }
                            else if(type.equalsIgnoreCase("documents")){
                                Log.d("RES_TYPE_NUM","1");
                                new Intent(SplashScreenActivity.this, MainActivity.class);
                                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                            }
                        }else if(array.length() == 0){
                            new Intent(SplashScreenActivity.this, MainActivity.class);
                            overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                        }
                    *//*}else{

                    }*//*
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //CustomProgressDialog.showDialog(SplashScreenActivity.this, false);
                *//*try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("0")) {
                        Log.d("RES_STATUS","0");
                        //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if(type.equalsIgnoreCase("kyc")) {
                            Log.d("RES_TYPE",type);
                            checkUser("documents");
                        }
                        else if(type.equalsIgnoreCase("documents")){
                            Log.d("RES_TYPE_NUM","1");
                            new Intent(SplashScreenActivity.this, MainActivity.class);
                            overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                        }
                    }else if (object.getString("status").equals("1")) {
                        startActivity(new Intent(SplashScreenActivity.this,DetailsListActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*//*
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("ERROR", error.getMessage());
                Toast.makeText(SplashScreenActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
                //CustomProgressDialog.showDialog(SplashScreenActivity.this, false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", YoDB.getPref().read(Constants.ID,""));
                map.put("type ", type);
                return map;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(sr);
    }*/

    private void BtnClick() {

    }

    @Override
    public void onClick(View view) {

    }

    private void loadAnimateLogo() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        binding.ivSplash.setAnimation(animation);
    }
}