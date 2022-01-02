package com.textifly.quickmudra.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.databinding.ActivityRazorPayBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RazorPayActivity extends AppCompatActivity /*implements PaymentResultListener*/ {
    ActivityRazorPayBinding binding;
    private static final String TAG = RazorPayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRazorPayBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        //Checkout.preload(getApplicationContext());

        //initView();
        //startPayment();
    }

    /*private void startPayment() {
        //int amount = Math.round(Float.parseFloat(getIntent().getStringExtra("payable_amount"))*100);
        int amount = Math.round(Float.parseFloat(getIntent().getStringExtra("amount"))*100);

        Checkout checkout = new Checkout();

        checkout.setKeyID("rzp_test_bpMWpJf8zAnAJt");
        //checkout.setImage(R.drawable.splash);
        checkout.setImage(R.drawable.logo);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","Dopfin");
            jsonObject.put("description","Payment");
            jsonObject.put("theme.color","#1b1092");
            jsonObject.put("currency","INR");
            jsonObject.put("amount",amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", YoDB.getPref().read(Constants.EMAIL,""));
            //preFill.put("contact", getIntent().getStringExtra("shipping_phone"));
            preFill.put("contact", YoDB.getPref().read(Constants.PHONE,""));

            jsonObject.put("prefill", preFill);

            *//*jsonObject.put("prefill.contact", YoDB.getPref().read(Constants.PHONE,""));
            jsonObject.put("prefill.email",YoDB.getPref().read(Constants.EMAIL,""));*//*

            Log.e("json", String.valueOf(jsonObject));

            checkout.open(RazorPayActivity.this,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
    }

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }*/
}