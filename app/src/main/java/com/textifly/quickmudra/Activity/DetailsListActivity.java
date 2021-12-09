package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityDetailsListBinding;

public class DetailsListActivity extends AppCompatActivity {
    ActivityDetailsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsListBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_details_list);
    }
}