package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityMobileNoVerificationBinding;

public class MobileNoVerificationActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityMobileNoVerificationBinding binding;
    int length = 0;
    String phno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMobileNoVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BtnClick();

        binding.etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                length = binding.etMobileNumber.getText().length();
                if(length == 10){
                    binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg));
                }else{
                    binding.btnContinue.setBackground(getResources().getDrawable(R.drawable.button_bg1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void BtnClick() {
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnContinue:
                if(length == 10){
                    phno = "+"+binding.ccp.getSelectedCountryCode()+" "+binding.etMobileNumber.getText().toString();
                    otpCheck();
                }else{
                    Toast.makeText(MobileNoVerificationActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    binding.etMobileNumber.requestFocus();
                }
                break;
        }
    }

    private void otpCheck() {
        Toast.makeText(MobileNoVerificationActivity.this, "Ph no.: "+phno, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MobileNoVerificationActivity.this, OtpValidationActivity.class);
        intent.putExtra("phno",phno);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}