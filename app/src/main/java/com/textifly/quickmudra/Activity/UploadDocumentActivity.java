package com.textifly.quickmudra.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.ActivityUploadDocumentBinding;
import com.textifly.quickmudra.databinding.RegPopupBinding;

public class UploadDocumentActivity extends AppCompatActivity implements View.OnClickListener  {
    ActivityUploadDocumentBinding binding;
    TextView tvStart;
    PopupWindow popupWindow;
    ConstraintLayout llCons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BtnClick();
        showPopUp();
    }

    private void showPopUp() {
        /*LayoutInflater layoutInflater = (LayoutInflater) UploadDocumentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.reg_popup,null);

        tvStart =  customView.findViewById(R.id.tvStart);

        popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //display the popup window
        popupWindow.showAtLocation(binding.llCons, Gravity.CENTER, 0, 0);

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UploadDocumentActivity.this,));
                popupWindow.dismiss();
            }
        });*/
        final Dialog dialog = new Dialog(UploadDocumentActivity.this);
        RegPopupBinding binding = RegPopupBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);

        binding.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void BtnClick() {
        //binding.tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.tvContinue:
                startActivity(new Intent(UploadDocumentActivity.this,AlternativeContactActivity.class));
                overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);
                break;*/
        }
    }
}