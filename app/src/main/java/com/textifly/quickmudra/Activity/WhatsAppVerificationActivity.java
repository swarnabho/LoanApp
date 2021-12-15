package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityMailVerificationBinding;
import com.textifly.quickmudra.databinding.ActivityWhatsAppVerificationBinding;

public class WhatsAppVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityWhatsAppVerificationBinding binding;
    String[] spinner;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhatsAppVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        BtnClick();
    }

    private void initView() {
        binding.llVoterId.setVisibility(View.GONE);
        binding.llPanCard.setVisibility(View.GONE);
        binding.llAadhar.setVisibility(View.GONE);
        binding.llDrivingLicence.setVisibility(View.GONE);
        binding.llPassport.setVisibility(View.GONE);

        spinner = new String[]{"Voter ID", "PAN Card", "Passport", "Driving License", "Aadhaar"};
        ArrayAdapter<String> obj = new ArrayAdapter<String>(WhatsAppVerificationActivity.this, android.R.layout.simple_list_item_1, spinner);
        binding.spn.setAdapter(obj);

        binding.spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                data = binding.spn.getSelectedItem().toString();
                if (data.equalsIgnoreCase("PAN Card")) {
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.VISIBLE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);
                }else if (data.equalsIgnoreCase("Voter ID")) {
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    binding.llVoterId.setVisibility(View.VISIBLE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);
                }else if (data.equalsIgnoreCase("Aadhaar")) {
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.VISIBLE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.GONE);
                }else if (data.equalsIgnoreCase("Passport")) {
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.GONE);
                    binding.llPassport.setVisibility(View.VISIBLE);
                }else if (data.equalsIgnoreCase("Driving License")) {
                    //startActivity(new Intent(WhatsAppVerificationActivity.this, PanCardActivity.class));
                    binding.llVoterId.setVisibility(View.GONE);
                    binding.llPanCard.setVisibility(View.GONE);
                    binding.llAadhar.setVisibility(View.GONE);
                    binding.llDrivingLicence.setVisibility(View.VISIBLE);
                    binding.llPassport.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void BtnClick() {
        binding.btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:

                //overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
        }
    }
}