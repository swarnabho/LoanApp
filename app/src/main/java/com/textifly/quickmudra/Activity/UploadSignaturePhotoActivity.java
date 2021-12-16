package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.ActivityUploadSignaturePhotoBinding;

public class UploadSignaturePhotoActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityUploadSignaturePhotoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadSignaturePhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.percentPD.setText(YoDB.getPref().read(Constants.UploadPercentage,"")+"%");
        BtnClick();
    }

    private void BtnClick() {
        binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvContinue:
                loadPercentage();
                YoDB.getPref().write(Constants.UploadNextDoc,"","selfie");
                startActivity(new Intent(UploadSignaturePhotoActivity.this, SelfieActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }
    private void loadPercentage() {
        int percentage = (400 / 6) ;
        YoDB.getPref().write(Constants.UploadPercentage, "", String.valueOf(percentage));
        /*Log.d("Percentage", percentage + "%");
        Constants.UploadPercentage = String.valueOf(percentage);*/
        binding.percentPD.setText(percentage + "%");
    }
}