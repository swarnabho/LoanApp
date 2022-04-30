package com.textifly.quickmudra.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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
    String tvKyc,tvPan,tvPersonalDetails,tvEmploymentDetails,tvDocuments,tvAlternetNo,tvMailVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvKyc = binding.tvKyc.getText().toString();
        tvPan = binding.tvPan.getText().toString();
        tvPersonalDetails = binding.tvPersonalDetails.getText().toString();
        tvEmploymentDetails = binding.tvEmploymentDetails.getText().toString();
        tvDocuments = binding.tvDocuments.getText().toString();
        tvAlternetNo = binding.tvAlternetNo.getText().toString();
        tvMailVerify = binding.tvMailVerify.getText().toString();
        binding.tvUsername.setText("Hi " + YoDB.getPref().read(Constants.NAME, ""));

        BtnClick();
        //if(YoDB.getPref().read(Constants.havePopUpShown,"").isEmpty()){
        /*if (YoDB.getPref().read(Constants.isFirstTime, "").equals("true")) {
            showPopUp();
        }*/

        showPopUp();
        //}
        getDocumentStatus();
        loadPercentage();
    }

    private void getDocumentStatus() {
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.GET_DOCUMENT_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("STATUS_RES",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String kycstatus = object.getString("kycstatus");
                    String other_details_status = object.getString("other_details_status");
                    String pan_status = object.getString("pan_status");
                    String personal_status = object.getString("personal_status");
                    String addressproof_status = object.getString("addressproof_status");
                    String marksheet_status = object.getString("marksheet_status");
                    String signature_status = object.getString("signature_status");
                    String college_status = object.getString("college_status");
                    String profilephoto_status = object.getString("profilephoto_status");
                    String profilevideo_status = object.getString("profilevideo_status");
                    String mailverify_status = object.getString("mailverify_status");
                    String alternate_number_status = object.getString("alternate_number_status");


                    //Mail Verify
                    if(mailverify_status.equalsIgnoreCase("pending")){
                        SpannableString spnMail = new SpannableString(tvMailVerify + " \tRequest Pending");
                        spnMail.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvMailVerify.length() + 1, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnMail.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvMailVerify.length() + 1, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnMail.setSpan(new AbsoluteSizeSpan(15, true), tvMailVerify.length() + 1, spnMail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvMailVerify.setText(spnMail);

                    }else if(mailverify_status.equalsIgnoreCase("approved")){
                        SpannableString spnMail = new SpannableString(tvMailVerify + " \tApproved");
                        spnMail.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvMailVerify.length() + 1, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnMail.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvMailVerify.length() + 1, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnMail.setSpan(new AbsoluteSizeSpan(15, true), tvMailVerify.length() + 1, spnMail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvMailVerify.setText(spnMail);

                    }else if(mailverify_status.equalsIgnoreCase("reject")){
                        SpannableString spnMail = new SpannableString(tvMailVerify + " Rejected\n\tPlease re-submit the KYC document");
                        spnMail.setSpan(new ForegroundColorSpan(Color.RED),
                                tvMailVerify.length() + 2, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnMail.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvMailVerify.length() + 1, // start
                                spnMail.length(), // end
                                spnMail.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnMail.setSpan(new AbsoluteSizeSpan(15, true), tvMailVerify.length() + 1, spnMail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvMailVerify.setText(spnMail);

                    }

                    //Alternate Number
                    if(alternate_number_status.equalsIgnoreCase("pending")){
                        SpannableString spnAltNo = new SpannableString(tvAlternetNo + " \tRequest Pending");
                        spnAltNo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvAlternetNo.length() + 1, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnAltNo.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvAlternetNo.length() + 1, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnAltNo.setSpan(new AbsoluteSizeSpan(15, true), tvAlternetNo.length() + 1, spnAltNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvAlternetNo.setText(spnAltNo);

                    }else if(alternate_number_status.equalsIgnoreCase("approved")){
                        SpannableString spnAltNo = new SpannableString(tvAlternetNo + " \tApproved");
                        spnAltNo.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvAlternetNo.length() + 1, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnAltNo.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvAlternetNo.length() + 1, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnAltNo.setSpan(new AbsoluteSizeSpan(15, true), tvAlternetNo.length() + 1, spnAltNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvAlternetNo.setText(spnAltNo);

                    }else if(alternate_number_status.equalsIgnoreCase("reject")){
                        SpannableString spnAltNo = new SpannableString(tvAlternetNo + " Rejected\n\tPlease re-submit the KYC document");
                        spnAltNo.setSpan(new ForegroundColorSpan(Color.RED),
                                tvAlternetNo.length() + 2, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnAltNo.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvAlternetNo.length() + 1, // start
                                spnAltNo.length(), // end
                                spnAltNo.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnAltNo.setSpan(new AbsoluteSizeSpan(15, true), tvAlternetNo.length() + 1, spnAltNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvAlternetNo.setText(spnAltNo);

                    }

                    //Documents & Selfie
                    if(addressproof_status.equalsIgnoreCase("pending") ||
                            marksheet_status.equalsIgnoreCase("pending") ||
                            signature_status.equalsIgnoreCase("pending") ||
                            college_status.equalsIgnoreCase("pending") ||
                            profilephoto_status.equalsIgnoreCase("pending") ||
                            profilevideo_status.equalsIgnoreCase("pending") ){

                        SpannableString spnDoc = new SpannableString(tvDocuments + " \tRequest Pending");
                        spnDoc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnDoc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnDoc.setSpan(new AbsoluteSizeSpan(15, true), tvDocuments.length() + 1, spnDoc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvDocuments.setText(spnDoc);
                    }else if(addressproof_status.equalsIgnoreCase("approved") &&
                            marksheet_status.equalsIgnoreCase("approved") &&
                            signature_status.equalsIgnoreCase("approved") &&
                            college_status.equalsIgnoreCase("approved") &&
                            profilephoto_status.equalsIgnoreCase("approved") &&
                            profilevideo_status.equalsIgnoreCase("approved") ){

                        SpannableString spnDoc = new SpannableString(tvDocuments + " \tApproved");
                        spnDoc.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnDoc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnDoc.setSpan(new AbsoluteSizeSpan(15, true), tvDocuments.length() + 1, spnDoc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvDocuments.setText(spnDoc);
                    }else if(addressproof_status.equalsIgnoreCase("reject") ||
                            marksheet_status.equalsIgnoreCase("reject") ||
                            signature_status.equalsIgnoreCase("reject") ||
                            college_status.equalsIgnoreCase("reject") ||
                            profilephoto_status.equalsIgnoreCase("reject") ||
                            profilevideo_status.equalsIgnoreCase("reject") ){

                        SpannableString spnDoc = new SpannableString(tvDocuments + "  Rejected\n\tPlease re-submit the documents");
                        spnDoc.setSpan(new ForegroundColorSpan(Color.RED),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnDoc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvDocuments.length() + 1, // start
                                spnDoc.length(), // end
                                spnDoc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnDoc.setSpan(new AbsoluteSizeSpan(15, true), tvDocuments.length() + 1, spnDoc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvDocuments.setText(spnDoc);
                    }


                    // For KYC
                    if(kycstatus.equalsIgnoreCase("pending")){
                        SpannableString spnKyc = new SpannableString(tvKyc + " \tRequest Pending");
                        spnKyc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvKyc.length() + 1, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnKyc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvKyc.length() + 1, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnKyc.setSpan(new AbsoluteSizeSpan(15, true), tvKyc.length() + 1, spnKyc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvKyc.setText(spnKyc);

                    }else if(kycstatus.equalsIgnoreCase("approved")){
                        SpannableString spnKyc = new SpannableString(tvKyc + " \tApproved");
                        spnKyc.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvKyc.length() + 1, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnKyc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvKyc.length() + 1, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnKyc.setSpan(new AbsoluteSizeSpan(15, true), tvKyc.length() + 1, spnKyc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvKyc.setText(spnKyc);

                    }else if(kycstatus.equalsIgnoreCase("reject")){
                        SpannableString spnKyc = new SpannableString(tvKyc + " Rejected\n\tPlease re-submit the KYC document");
                        spnKyc.setSpan(new ForegroundColorSpan(Color.RED),
                                tvKyc.length() + 2, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnKyc.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvKyc.length() + 1, // start
                                spnKyc.length(), // end
                                spnKyc.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnKyc.setSpan(new AbsoluteSizeSpan(15, true), tvKyc.length() + 1, spnKyc.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvKyc.setText(spnKyc);

                    }

                    // For PAN
                    if(pan_status.equalsIgnoreCase("pending")){
                        SpannableString spnPan = new SpannableString(tvPan + " \tRequest Pending");
                        spnPan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPan.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPan.setSpan(new AbsoluteSizeSpan(15, true), tvPan.length() + 1, spnPan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPan.setText(spnPan);

                    }else if(pan_status.equalsIgnoreCase("approved")){
                        SpannableString spnPan = new SpannableString(tvPan + " \tApproved");
                        spnPan.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPan.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPan.setSpan(new AbsoluteSizeSpan(15, true), tvPan.length() + 1, spnPan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPan.setText(spnPan);

                    }else if(pan_status.equalsIgnoreCase("reject")){
                        SpannableString spnPan = new SpannableString(tvPan + " Rejected\nPlease re-submit the PAN details");
                        spnPan.setSpan(new ForegroundColorSpan(Color.RED),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPan.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPan.length() + 1, // start
                                spnPan.length(), // end
                                spnPan.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPan.setSpan(new AbsoluteSizeSpan(15, true), tvPan.length() + 1, spnPan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPan.setText(spnPan);

                    }


                    // For PERSONAL DETAILS
                    if(personal_status.equalsIgnoreCase("pending")){
                        SpannableString spnPersonalDetails = new SpannableString(tvPersonalDetails + " \tRequest Pending");
                        spnPersonalDetails.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPersonalDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPersonalDetails.setSpan(new AbsoluteSizeSpan(15, true), tvPersonalDetails.length() + 1, spnPersonalDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPersonalDetails.setText(spnPersonalDetails);

                    }else if(personal_status.equalsIgnoreCase("approved")){
                        SpannableString spnPersonalDetails = new SpannableString(tvPersonalDetails + " \tApproved");
                        spnPersonalDetails.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPersonalDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPersonalDetails.setSpan(new AbsoluteSizeSpan(15, true), tvPersonalDetails.length() + 1, spnPersonalDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPersonalDetails.setText(spnPersonalDetails);

                    }else if(personal_status.equalsIgnoreCase("reject")){
                        SpannableString spnPersonalDetails = new SpannableString(tvPersonalDetails + " Rejected\nPlease re-submit the Personal details");
                        spnPersonalDetails.setSpan(new ForegroundColorSpan(Color.RED),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnPersonalDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvPersonalDetails.length() + 1, // start
                                spnPersonalDetails.length(), // end
                                spnPersonalDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnPersonalDetails.setSpan(new AbsoluteSizeSpan(15, true), tvPersonalDetails.length() + 1, spnPersonalDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvPersonalDetails.setText(spnPersonalDetails);

                    }

                    // For Employment Details and College Details
                    if(other_details_status.equalsIgnoreCase("pending")){
                        SpannableString spnEmpColDetails = new SpannableString(tvEmploymentDetails + " \tRequest Pending");
                        spnEmpColDetails.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.goldenColor1)),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnEmpColDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnEmpColDetails.setSpan(new AbsoluteSizeSpan(15, true), tvEmploymentDetails.length() + 1, spnEmpColDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvEmploymentDetails.setText(spnEmpColDetails);

                    }else if(other_details_status.equalsIgnoreCase("approved")){
                        SpannableString spnEmpColDetails = new SpannableString(tvEmploymentDetails + " \tApproved");
                        spnEmpColDetails.setSpan(new ForegroundColorSpan(Color.GREEN),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnEmpColDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnEmpColDetails.setSpan(new AbsoluteSizeSpan(15, true), tvEmploymentDetails.length() + 1, spnEmpColDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvEmploymentDetails.setText(spnEmpColDetails);

                    }else if(other_details_status.equalsIgnoreCase("reject")){
                        SpannableString spnEmpColDetails = new SpannableString(tvEmploymentDetails + " Rejected\nPlease re-submit the Employment details and College details");
                        spnEmpColDetails.setSpan(new ForegroundColorSpan(Color.RED),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE);

                        spnEmpColDetails.setSpan(
                                new StyleSpan(Typeface.BOLD),
                                tvEmploymentDetails.length() + 1, // start
                                spnEmpColDetails.length(), // end
                                spnEmpColDetails.SPAN_EXCLUSIVE_INCLUSIVE
                        );
                        spnEmpColDetails.setSpan(new AbsoluteSizeSpan(15, true), tvEmploymentDetails.length() + 1, spnEmpColDetails.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.tvEmploymentDetails.setText(spnEmpColDetails);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(DetailsListActivity.this).add(sr);
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
                    String pan_percentage = object.getString("pan_percentage");
                    String mail_verify_percentage = object.getString("mail_verify");

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

                    if(pan_percentage.isEmpty()){
                        binding.percentPan.setText("0%");
                    } else{
                        binding.percentPan.setText(pan_percentage+"%");
                    }

                    if(user_personal_percentage.isEmpty() ||user_personal_percentage.equalsIgnoreCase("null")){
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

                    if(mail_verify_percentage.isEmpty() || mail_verify_percentage.equals("0")){
                        binding.percentMV.setText("0%");
                    } else{
                        binding.percentMV.setText("100%");
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
        binding.llPanCard.setOnClickListener(this);
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
                //checkDocumentsPosition();

                startActivity(new Intent(DetailsListActivity.this, UploadDocumentActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
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
            case R.id.llPanCard:
                startActivity(new Intent(DetailsListActivity.this, PanCardActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
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
                        String frontImg = object.getString("image1");
                        String backImg = object.getString("image2");
                        String id_no = object.getString("id_no");
                        if (file_type.equalsIgnoreCase("voter")) {
                            Intent intent = new Intent(DetailsListActivity.this, VoterActivity.class);
                            intent.putExtra("frontImg",frontImg);
                            intent.putExtra("backImg",backImg);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } /*else if (file_type.equalsIgnoreCase("pan")) {
                            startActivity(new Intent(DetailsListActivity.this, AadharActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        }*/ else if (file_type.equalsIgnoreCase("aadhar")) {
                            Intent intent = new Intent(DetailsListActivity.this, AadharActivity.class);
                            intent.putExtra("frontImg",frontImg);
                            intent.putExtra("backImg",backImg);
                            intent.putExtra("id_no",id_no);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("passport")) {
                            Intent intent = new Intent(DetailsListActivity.this, PassportActivity.class);
                            intent.putExtra("frontImg",frontImg);
                            startActivity(intent);
                            //startActivity(new Intent(DetailsListActivity.this, PassportActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                        } else if (file_type.equalsIgnoreCase("license")) {
                            Intent intent = new Intent(DetailsListActivity.this, DrivingLisenceActivity.class);
                            intent.putExtra("frontImg",frontImg);
                            startActivity(intent);
                            //startActivity(new Intent(DetailsListActivity.this, DrivingLisenceActivity.class));
                            overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                            //Toast.makeText(DetailsListActivity.this, "100% KYC submission completed", Toast.LENGTH_SHORT).show();
                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        startActivity(new Intent(DetailsListActivity.this, WhatsAppVerificationActivity.class));
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