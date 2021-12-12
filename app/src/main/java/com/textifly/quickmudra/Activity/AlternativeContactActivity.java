package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityAlternativeContactBinding;
import com.textifly.quickmudra.databinding.ActivityUploadDocumentBinding;

public class AlternativeContactActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAlternativeContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlternativeContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();
    }

    private void BtnClick() {
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                startActivity(new Intent(AlternativeContactActivity.this,MailVerificationActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }
}