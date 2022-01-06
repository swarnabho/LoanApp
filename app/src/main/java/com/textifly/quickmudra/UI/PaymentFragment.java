package com.textifly.quickmudra.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.databinding.FragmentPaymentBinding;

public class PaymentFragment extends Fragment implements View.OnClickListener{
    FragmentPaymentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater,container,false);
        BtnClick();

        return binding.getRoot();
    }

    private void BtnClick() {
        binding.llPhonePe.setOnClickListener(this);
        binding.llNEFT.setOnClickListener(this);
        binding.llGooglePay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llPhonePe:
                binding.phonePe.setVisibility(View.VISIBLE);
                binding.llBanlDetails.setVisibility(View.GONE);
                binding.googlePe.setVisibility(View.GONE);
                break;
            case R.id.llNEFT:
                binding.phonePe.setVisibility(View.GONE);
                binding.llBanlDetails.setVisibility(View.VISIBLE);
                binding.googlePe.setVisibility(View.GONE);
                break;
            case R.id.llGooglePay:
                binding.phonePe.setVisibility(View.GONE);
                binding.llBanlDetails.setVisibility(View.GONE);
                binding.googlePe.setVisibility(View.VISIBLE);
                break;
        }
    }
}