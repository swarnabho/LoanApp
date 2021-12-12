package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityDetailsListBinding;

public class DetailsListActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityDetailsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();
    }

    private void BtnClick() {
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
        switch (view.getId()){
            case R.id.llPersonalDetails:
                //Toast.makeText(DetailsListActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailsListActivity.this,PersonalDetailsActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.llEducationalDetails:
                startActivity(new Intent(DetailsListActivity.this,EmploymentDetailsActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.llUploadDocuments:
                startActivity(new Intent(DetailsListActivity.this,UploadDocumentActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.llAlternativeContact:
                startActivity(new Intent(DetailsListActivity.this,AlternativeContactActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.llMailVerification:
                startActivity(new Intent(DetailsListActivity.this,MailVerificationActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.llWhatsappVerification:
                startActivity(new Intent(DetailsListActivity.this,WhatsAppVerificationActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
            case R.id.tvContinue:
                startActivity(new Intent(DetailsListActivity.this,PersonalDetailsActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }
}