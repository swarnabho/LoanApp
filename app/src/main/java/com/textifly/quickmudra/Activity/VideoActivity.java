package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.ActivityVideoBinding;

import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener  {
    ActivityVideoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.percentPD.setText(YoDB.getPref().read(Constants.UploadPercentage,"")+"%");
        BtnClick();
        showToast();
    }

    private void showToast() {
        final Handler h=new Handler();
        final  Runnable r=new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(),"please told on video:My name is.... and I am responsible to repay my loans on Dopfin",Toast.LENGTH_SHORT).show();
            }
        };

        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                h.post(r);
            }
        },2000, 5000);
    }

    private void BtnClick() {
        binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvContinue:
                loadPercentage();
                YoDB.getPref().write(Constants.UploadNextDoc,"","complete");
                startActivity(new Intent(VideoActivity.this, DetailsListActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;
        }
    }
    private void loadPercentage() {
        int percentage = (600 / 6) ;
        YoDB.getPref().write(Constants.UploadPercentage, "", String.valueOf(percentage));
        /*Log.d("Percentage", percentage + "%");
        Constants.UploadPercentage = String.valueOf(percentage);*/
        binding.percentPD.setText(percentage + "%");
    }
}