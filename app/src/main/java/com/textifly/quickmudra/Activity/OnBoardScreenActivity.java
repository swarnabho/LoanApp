package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.textifly.quickmudra.Adapter.OnBoardAdapter;
import com.textifly.quickmudra.Model.OnBoardModel;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityOnBoardScreenBinding;

import java.util.ArrayList;
import java.util.List;

public class OnBoardScreenActivity extends AppCompatActivity implements View.OnClickListener{

    ActivityOnBoardScreenBinding binding;
    List<OnBoardModel> modelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadOnBoardingPages();
        BtnClick();
    }

    private void BtnClick() {
        binding.tvStart.setOnClickListener(this);
    }

    private void loadOnBoardingPages() {
        int image[] = new int[]{R.drawable.one, R.drawable.two, R.drawable.three};
        String desc[] = new String[]{
                "Approved Loan Instantly.\nGet Loan in no Time",
                "Refer this to Your Friends and \nget Q Coins",
                "Get Loan Directly in Your \nAccount or Payment Wallet"
        };
        modelList = new ArrayList<>();

        int length = image.length;
        for (int i = 0; i < length; i++) {
            modelList.add(new OnBoardModel(image[i],desc[i]));
        }
        OnBoardAdapter adapter = new OnBoardAdapter(modelList,this);
        binding.imageSlider.setSliderAdapter(adapter);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.imageSlider.setAutoCycleDirection(SliderView.FOCUS_RIGHT);
        binding.imageSlider.setScrollTimeInSec(3); //set scroll delay in seconds
        binding.imageSlider.startAutoCycle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvStart:
                startActivity(new Intent(OnBoardScreenActivity.this, CameraPermissionActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                //finish();
                break;
        }
    }

}