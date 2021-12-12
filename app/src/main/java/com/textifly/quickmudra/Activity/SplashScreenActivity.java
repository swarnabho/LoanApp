package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySplashScreenBinding binding;
    Animation animation;
    Handler mHandler;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());

        BtnClick();
        loadAnimateLogo();
        redirect();
    }

    private void redirect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(SplashScreenActivity.this, OnBoardScreenActivity.class);startActivity(intent);
                overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                finish();
            }
        },4000);
    }

    private void BtnClick() {

    }

    @Override
    public void onClick(View view) {

    }

    private void loadAnimateLogo() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        binding.ivSplash.setAnimation(animation);
    }
}