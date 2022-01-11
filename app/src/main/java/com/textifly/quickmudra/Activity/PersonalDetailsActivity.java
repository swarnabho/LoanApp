package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.textifly.quickmudra.databinding.ActivityPersonalDetailsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PersonalDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPersonalDetailsBinding binding;
    String gender = "";
    Calendar calendar;
    String dateFormatForDB = "";
    RadioButton genderradioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPersonalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();
    }

    private void initView() {
        CustomProgressDialog.showDialog(PersonalDetailsActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.PERSONAL_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(PersonalDetailsActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            binding.etFirstName.setText((!object.getString("fname").equalsIgnoreCase("null")) ? object.getString("fname") : "");
                            binding.etLastName.setText((!object.getString("lname").equalsIgnoreCase("null")) ? object.getString("lname") : "");
                            binding.etFatherName.setText((!object.getString("father_name").equalsIgnoreCase("null")) ? object.getString("father_name") : "");
                            binding.etMotherName.setText((!object.getString("mother_name").equalsIgnoreCase("null")) ? object.getString("mother_name") : "");
                            binding.etAddress.setText((!object.getString("address").equalsIgnoreCase("null")) ? object.getString("address") : "");
                            binding.etPermanentAddress.setText((!object.getString("permanent_address").equalsIgnoreCase("null")) ? object.getString("permanent_address") : "");
                            binding.etDOB.setText((!object.getString("dob").equalsIgnoreCase("null")) ? object.getString("dob") : "");
                            if (object.getString("gender").equalsIgnoreCase("male")) {
                                binding.etMale.setChecked(true);
                                binding.etFemale.setChecked(false);
                            } else if (object.getString("gender").equalsIgnoreCase("female")) {
                                binding.etFemale.setChecked(true);
                                binding.etMale.setChecked(false);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(PersonalDetailsActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(PersonalDetailsActivity.this).add(sr);
    }

    private void BtnClick() {
        binding.tvNext.setOnClickListener(this);
        binding.etDOB.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                checkDetails();
                break;
            case R.id.etDOB:
                calendar = Calendar.getInstance();

                DatePickerDialog dateDialog = new DatePickerDialog(PersonalDetailsActivity.this, datePickerDialog, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                //deliveryDate.setText(SimpleDateFormat.getDateInstance().format(format.format(calendar.getTime())));
                //String dateText = DateFormat.format("yyyy/MM/dd", calendar).toString();
                //date.getDatePicker().setMinDate();
                //dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); //for start date bound
                //dateDialog.getDatePicker();
                dateDialog.show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //updateLabel();
            dateFormatForDB = DateFormat.format("yyyy-MM-dd", calendar).toString();
            binding.etDOB.setText(DateFormat.format("dd-MM-yyyy", calendar).toString());
        }
    };

    private void checkDetails() {
        int selectedId = binding.radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        if (selectedId == -1) {
            Toast.makeText(PersonalDetailsActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
        } else {
            gender = genderradioButton.getText().toString();
            //Toast.makeText(PersonalDetailsActivity.this,genderradioButton.getText(), Toast.LENGTH_SHORT).show();
        }

        if (binding.etFirstName.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
            binding.etFirstName.requestFocus();
        } else if (binding.etLastName.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter last name", Toast.LENGTH_SHORT).show();
            binding.etLastName.requestFocus();
        } else if (binding.etFatherName.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter father name", Toast.LENGTH_SHORT).show();
            binding.etFatherName.requestFocus();
        } else if (binding.etMotherName.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter mother name", Toast.LENGTH_SHORT).show();
            binding.etMotherName.requestFocus();
        } else if (binding.etAddress.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
            binding.etAddress.requestFocus();
        } else if (binding.etPermanentAddress.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter parmanent address", Toast.LENGTH_SHORT).show();
            binding.etPermanentAddress.requestFocus();
        } else if (binding.etDOB.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter Date of birth", Toast.LENGTH_SHORT).show();
            binding.etDOB.requestFocus();
        } else if (binding.etFirstName.getText().toString().isEmpty()) {
            Toast.makeText(PersonalDetailsActivity.this, "Please enter first name", Toast.LENGTH_SHORT).show();
            binding.etFirstName.requestFocus();
        } else if (gender.equals("")) {
            Toast.makeText(PersonalDetailsActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else {
            insertPersonalDetails();
        }
    }

    private void insertPersonalDetails() {
        CustomProgressDialog.showDialog(PersonalDetailsActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.DETAILS_UPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(PersonalDetailsActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        binding.percentPD.setText("100%");
                        startActivity(new Intent(PersonalDetailsActivity.this, DetailsListActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(PersonalDetailsActivity.this, false);
                Toast.makeText(PersonalDetailsActivity.this, "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                map.put("fname", binding.etFirstName.getText().toString());
                map.put("lname", binding.etLastName.getText().toString());
                map.put("father_name", binding.etFatherName.getText().toString());
                map.put("mother_name", binding.etMotherName.getText().toString());
                map.put("address", binding.etAddress.getText().toString());
                map.put("permannet_address", binding.etPermanentAddress.getText().toString());
                map.put("dob", binding.etDOB.getText().toString());
                map.put("gender", gender);

                return map;
            }
        };
        Volley.newRequestQueue(PersonalDetailsActivity.this).add(sr);
    }
}