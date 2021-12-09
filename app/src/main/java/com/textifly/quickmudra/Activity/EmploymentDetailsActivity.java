package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
    }

    @Override
    public void onClick(View view) {

    }
}