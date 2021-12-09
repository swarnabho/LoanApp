package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.textifly.quickmudra.databinding.ActivityUploadDocumentBinding;

public class UploadDocumentActivity extends AppCompatActivity {
    ActivityUploadDocumentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}