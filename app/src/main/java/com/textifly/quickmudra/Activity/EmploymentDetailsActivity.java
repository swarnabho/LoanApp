package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityEmploymentDetailsBinding;

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
                startActivity(new Intent(EmploymentDetailsActivity.this,UploadDocumentActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }
}