package com.textifly.quickmudra.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.textifly.quickmudra.databinding.LayoutProgressDialogBinding;

public class CustomProgressDialog {
    private static Dialog mProgressDialog;

    public static void showDialog(Context context,boolean isView){
        if(isView == true){
            showProgressDialog(context);
        }else if(isView == false){
            mProgressDialog.dismiss();
        }
    }

    private static void showProgressDialog(Context context) {
        mProgressDialog = new Dialog(context);
        LayoutProgressDialogBinding binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context));
        mProgressDialog.setContentView(binding.getRoot());
        mProgressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = mProgressDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.NO_GRAVITY;
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

    }
}
