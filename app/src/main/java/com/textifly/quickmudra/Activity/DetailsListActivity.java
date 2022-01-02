package com.textifly.quickmudra.Activity;

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

import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.ActivityDetailsListBinding;
import com.textifly.quickmudra.databinding.RegPopupBinding;

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
        if (!YoDB.getPref().read(Constants.UploadPercentage, "").isEmpty())
            binding.percentDOC.setText(YoDB.getPref().read(Constants.UploadPercentage, "") + "%");
        else
            binding.percentDOC.setText("0%");
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
                if (YoDB.getPref().read(Constants.UploadNextDoc, "").equalsIgnoreCase("address")) {
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
                }
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
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
                startActivity(new Intent(DetailsListActivity.this, VoterActivity.class));
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.tvContinue:
                Log.d("Status",YoDB.getPref().read(Constants.UploadNextDoc,""));
                if(YoDB.getPref().read(Constants.UploadNextDoc,"").equals("complete")){
                    startActivity(new Intent(DetailsListActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(DetailsListActivity.this, WhatsAppVerificationActivity.class));
                }
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
        }
    }

}