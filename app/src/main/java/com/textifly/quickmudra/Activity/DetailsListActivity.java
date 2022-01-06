package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.textifly.quickmudra.databinding.ActivityDetailsListBinding;
import com.textifly.quickmudra.databinding.RegPopupBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsListActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDetailsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvUsername.setText("Hi " + YoDB.getPref().read(Constants.NAME, ""));

        BtnClick();
        //if(YoDB.getPref().read(Constants.havePopUpShown,"").isEmpty()){
        if (YoDB.getPref().read(Constants.isFirstTime, "").equals("true")) {
            showPopUp();
        }
        //}
        loadPercentage();
    }

    private void showPopUp() {
        YoDB.getPref().write(Constants.havePopUpShown, "", "yes");
        final Dialog dialog = new Dialog(DetailsListActivity.this);
        RegPopupBinding binding = RegPopupBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);

        binding.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoDB.getPref().write(Constants.isFirstTime, "", "false");
                dialog.dismiss();
            }
        });
    }

    private void loadPercentage() {
        /*if (!YoDB.getPref().read(Constants.UploadPercentage, "").isEmpty())
            binding.percentDOC.setText(YoDB.getPref().read(Constants.UploadPercentage, "") + "%");
        else
            binding.percentDOC.setText("0%");*/
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String user_personal_percentage = object.getString("user_personal_percentage");
                    String other_details_percentage = object.getString("other_details_percentage");
                    String kyc_percentage = object.getString("kyc_percentage");
                    String doc_percentage = object.getString("doc_percentage");
                    String alternate_contact_verify = object.getString("alternate_contact_verify");

                    //Log.d("ID_RES",YoDB.getPref().read(Constants.ID,""));
                    Log.d("ID_RES",alternate_contact_verify);

                    if(kyc_percentage.isEmpty()){
                        binding.percentKYC.setText("0%");
                    } else{
                        binding.percentKYC.setText(kyc_percentage+"%");
                    }
                    if(alternate_contact_verify.isEmpty()){
                        binding.percentCV.setText("0%");
                    } else{
                        binding.percentCV.setText("100%");
                    }
                    if(kyc_percentage.isEmpty()){
                        binding.percentKYC.setText("0%");
                    } else{
                        binding.percentKYC.setText(kyc_percentage+"%");
                    }

                    if(user_personal_percentage.isEmpty()){
                        binding.percentPD.setText("0%");
                    } else{
                        binding.percentPD.setText(user_personal_percentage+"%");
                    }

                    if(doc_percentage.isEmpty()){
                        binding.percentDOC.setText("0%");
                    } else{
                        binding.percentDOC.setText(doc_percentage+"%");
                    }

                    if(other_details_percentage.isEmpty()){
                        binding.percentED.setText("0%");
                    } else{
                        binding.percentED.setText(other_details_percentage+"%");
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
                Map<String, String> map = new HashMap<>();
                map.put("userid", YoDB.getPref().read(Constants.ID, ""));
                return map;
            }
        };
        Volley.newRequestQueue(DetailsListActivity.this).add(sr);
    }

    private void BtnClick() {
        YoDB.getPref().write(Constants.UploadNextDoc, "", "address");
        binding.llPersonalDetails.setOnClickListener(this);
        binding.llEducationalDetails.setOnClickListener(this);
        binding.llUploadDocuments.setOnClickListener(this);
        binding.llAlternativeContact.setOnClickListener(this);
        binding.llMailVerification.setOnClickListener(this);
        binding.llWhatsappVerification.setOnClickListener(this);
        binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llPersonalDetails:
                //Toast.makeText(DetailsListActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsListActivity.this, PersonalDetailsActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llEducationalDetails:
                startActivity(new Intent(DetailsListActivity.this, EmploymentDetailsActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llUploadDocuments:
                checkDocumentsPosition();

               // startActivity(new Intent(DetailsListActivity.this, UploadCollegeIdActivity.class));
                /*if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("address")) {
                    startActivity(new Intent(DetailsListActivity.this, UploadDocumentActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("marksheet")) {
                    startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("collegeId")) {
                    startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("signature")) {
                    startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("selfie")) {
                    startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("video")) {
                    startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                } else if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("complete")) {
                    startActivity(new Intent(DetailsListActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(DetailsListActivity.this, UploadDocumentActivity.class));
                }*/
                //overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llAlternativeContact:
                startActivity(new Intent(DetailsListActivity.this, AlternativeContactActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llMailVerification:
                startActivity(new Intent(DetailsListActivity.this, MailVerificationActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.llWhatsappVerification:
                /*startActivity(new Intent(DetailsListActivity.this, VoterActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);*/
                checkKYCPosition();
                break;
            case R.id.tvContinue:
                /*Log.d("Status", YoDB.getPref().read(Constants.UploadNextDoc, ""));
                if (YoDB.getPref().read(Constants.UploadNextDoc, "").equals("complete")) {
                    startActivity(new Intent(DetailsListActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(DetailsListActivity.this, WhatsAppVerificationActivity.class));
                }*/
                startActivity(new Intent(DetailsListActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
        }
    }

    private void checkKYCPosition() {
        CustomProgressDialog.showDialog(DetailsListActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE_COMPLETE_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(DetailsListActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        String file_type = object.getString("file_type");
                        if (file_type.equalsIgnoreCase("voter")) {
                            startActivity(new Intent(DetailsListActivity.this, PanCardActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("pan")) {
                            startActivity(new Intent(DetailsListActivity.this, AadharActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("aadhar")) {
                            startActivity(new Intent(DetailsListActivity.this, PassportActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("passport")) {
                            startActivity(new Intent(DetailsListActivity.this, DrivingLisenceActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("license")) {
                            /*startActivity(new Intent(DetailsListActivity.this, WhatsAppVerificationActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);*/
                            Toast.makeText(DetailsListActivity.this, "100% KYC submission completed", Toast.LENGTH_SHORT).show();
                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        startActivity(new Intent(DetailsListActivity.this, VoterActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(DetailsListActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("type", "kyc");

                return body;
            }
        };
        Volley.newRequestQueue(DetailsListActivity.this).add(sr);
    }

    private void checkDocumentsPosition() {
        CustomProgressDialog.showDialog(DetailsListActivity.this, true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.USER_PROFILE_COMPLETE_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(DetailsListActivity.this, false);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        String file_type = object.getString("file_type");
                        if (file_type.equalsIgnoreCase("addressproof")) { // address proof
                            startActivity(new Intent(DetailsListActivity.this, LastExamMarksheetActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("marksheet")) {
                            startActivity(new Intent(DetailsListActivity.this, UploadCollegeIdActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("collegeid")) {
                            startActivity(new Intent(DetailsListActivity.this, UploadSignaturePhotoActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("signature")) {
                            startActivity(new Intent(DetailsListActivity.this, SelfieActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("profilephoto")) {
                            startActivity(new Intent(DetailsListActivity.this, VideoActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("profilevideo")) {
                            Toast.makeText(DetailsListActivity.this, "100% Documents upload completed", Toast.LENGTH_SHORT).show();
                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        startActivity(new Intent(DetailsListActivity.this, UploadDocumentActivity.class));
                        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(DetailsListActivity.this, false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("type", "documents");

                return body;
            }
        };
        Volley.newRequestQueue(DetailsListActivity.this).add(sr);
    }
}