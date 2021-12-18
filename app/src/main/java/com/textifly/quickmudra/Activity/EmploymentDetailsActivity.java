package com.textifly.quickmudra.Activity;

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
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityEmploymentDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmploymentDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityEmploymentDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmploymentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        BtnClick();
    }

    private void BtnClick() {
        binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvContinue:
                checkDetails();
                break;
        }
    }

    private void checkDetails() {
        if(binding.tilEducationDetailse.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Educational details", Toast.LENGTH_SHORT).show();
            binding.tilEducationDetailse.requestFocus();
        }else if(binding.tilInstituteName.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Institute name", Toast.LENGTH_SHORT).show();
            binding.tilInstituteName.requestFocus();
        }else if(binding.tilInstitutePin.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Institute pincode", Toast.LENGTH_SHORT).show();
            binding.tilInstitutePin.requestFocus();
        }else if(binding.tilCourseName.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course name", Toast.LENGTH_SHORT).show();
            binding.tilCourseName.requestFocus();
        }else if(binding.tilCourseStartYear.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course start year", Toast.LENGTH_SHORT).show();
            binding.tilCourseStartYear.requestFocus();
        }else if(binding.tilCourseEndYear.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course end year", Toast.LENGTH_SHORT).show();
            binding.tilCourseEndYear.requestFocus();
        }else if(binding.tilExamYear.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter exam year", Toast.LENGTH_SHORT).show();
            binding.tilExamYear.requestFocus();
        }else if(binding.tilPercentage.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter percentage", Toast.LENGTH_SHORT).show();
            binding.tilPercentage.requestFocus();
        }else if(binding.tilPocketMoney.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter pocket money", Toast.LENGTH_SHORT).show();
            binding.tilPocketMoney.requestFocus();
        }else if(binding.tilMonthlyIncome.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter monthly income", Toast.LENGTH_SHORT).show();
            binding.tilMonthlyIncome.requestFocus();
        }else{
            insertData();
        }
    }

    private void insertData() {
        CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.DETAILS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        binding.percentPD.setText("100%");
                        startActivity(new Intent(EmploymentDetailsActivity.this,UploadDocumentActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,false);
                Toast.makeText(EmploymentDetailsActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                map.put("college", binding.tilInstituteName.getText().toString());
                map.put("institute_pincode", binding.tilInstituteName.getText().toString());
                map.put("course_name", binding.tilCourseName.getText().toString());
                map.put("course_start_year", binding.tilCourseStartYear.getText().toString());
                map.put("course_end_year", binding.tilCourseEndYear.getText().toString());
                map.put("exam_year", binding.tilExamYear.getText().toString());
                map.put("percentage", binding.tilPercentage.getText().toString());
                //map.put("pocketmoney", binding.tilPocketMoney.getText().toString()); missing in api

                return map;
            }
        };
        Volley.newRequestQueue(EmploymentDetailsActivity.this).add(sr);
    }
}