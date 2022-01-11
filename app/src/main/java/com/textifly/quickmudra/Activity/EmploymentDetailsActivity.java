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

import org.json.JSONArray;
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

        initView();
        BtnClick();
    }

    private void initView() {
        CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.EDUCATIONAL_DETAILS_FETCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("status").equals("0")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            binding.tilEducationDetailse.setText((!object.getString("education_details").equalsIgnoreCase("null")) ? object.getString("education_details") : "");
                            binding.tilInstituteName.getEditText().setText((!object.getString("institute_name").equalsIgnoreCase("null")) ? object.getString("institute_name") : "");
                            binding.tilInstitutePin.getEditText().setText((!object.getString("institute_pin").equalsIgnoreCase("null")) ? object.getString("institute_pin") : "");
                            binding.tilCourseName.getEditText().setText((!object.getString("course_name").equalsIgnoreCase("null")) ? object.getString("course_name") : "");
                            binding.tilCourseStartYear.getEditText().setText((!object.getString("course_start_year").equalsIgnoreCase("null")) ? object.getString("course_start_year") : "");
                            binding.tilCourseEndYear.getEditText().setText((!object.getString("course_end_year").equalsIgnoreCase("null")) ? object.getString("course_end_year") : "");
                            binding.tilExamYear.getEditText().setText((!object.getString("exam_year").equalsIgnoreCase("null")) ? object.getString("exam_year") : "");
                            binding.tilPocketMoney.getEditText().setText((!object.getString("pocket_money").equalsIgnoreCase("null")) ? object.getString("pocket_money") : "");
                            binding.tilMonthlyIncome.getEditText().setText((!object.getString("income").equalsIgnoreCase("null")) ? object.getString("income") : "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(EmploymentDetailsActivity.this,false);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(EmploymentDetailsActivity.this).add(sr);
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
        /*if(binding.tilEducationDetailse.getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Educational details", Toast.LENGTH_SHORT).show();
            binding.tilEducationDetailse.requestFocus();
        }else*/ if(binding.tilInstituteName.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Institute name", Toast.LENGTH_SHORT).show();
            binding.tilInstituteName.requestFocus();
        }else if(binding.tilInstitutePin.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Institute pincode", Toast.LENGTH_SHORT).show();
            binding.tilInstitutePin.requestFocus();
        }else if(binding.tilCourseName.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course name", Toast.LENGTH_SHORT).show();
            binding.tilCourseName.requestFocus();
        }else if(binding.tilCourseStartYear.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course start year", Toast.LENGTH_SHORT).show();
            binding.tilCourseStartYear.requestFocus();
        }else if(binding.tilCourseEndYear.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter Course end year", Toast.LENGTH_SHORT).show();
            binding.tilCourseEndYear.requestFocus();
        }else if(binding.tilExamYear.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter exam year", Toast.LENGTH_SHORT).show();
            binding.tilExamYear.requestFocus();
        }else if(binding.tilPercentage.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter percentage", Toast.LENGTH_SHORT).show();
            binding.tilPercentage.requestFocus();
        }else if(binding.tilPocketMoney.getEditText().getText().toString().isEmpty()){
            Toast.makeText(EmploymentDetailsActivity.this, "Please enter pocket money", Toast.LENGTH_SHORT).show();
            binding.tilPocketMoney.requestFocus();
        }else if(binding.tilMonthlyIncome.getEditText().getText().toString().isEmpty()){
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
                        startActivity(new Intent(EmploymentDetailsActivity.this,DetailsListActivity.class));
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
                map.put("education_details", binding.tilEducationDetailse.getText().toString());
                map.put("college", binding.tilInstituteName.getEditText().getText().toString());
                map.put("institute_pincode", binding.tilInstituteName.getEditText().getText().toString());
                map.put("course_name", binding.tilCourseName.getEditText().getText().toString());
                map.put("course_start_year", binding.tilCourseStartYear.getEditText().getText().toString());
                map.put("course_end_year", binding.tilCourseEndYear.getEditText().getText().toString());
                map.put("exam_year", binding.tilExamYear.getEditText().getText().toString());
                map.put("percentage", binding.tilPercentage.getEditText().getText().toString());
                map.put("pocket_money", binding.tilPocketMoney.getEditText().getText().toString());
                map.put("annual_income", binding.tilMonthlyIncome.getEditText().getText().toString());

                return map;
            }
        };
        Volley.newRequestQueue(EmploymentDetailsActivity.this).add(sr);
    }
}