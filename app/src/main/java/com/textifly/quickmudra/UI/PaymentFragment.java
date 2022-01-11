package com.textifly.quickmudra.UI;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.FragmentPaymentBinding;

import java.util.HashMap;
import java.util.Map;

public class PaymentFragment extends Fragment implements View.OnClickListener {
    FragmentPaymentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        BtnClick();

        return binding.getRoot();
    }

    private void BtnClick() {
        binding.llPhonePe.setOnClickListener(this);
        binding.llNEFT.setOnClickListener(this);
        binding.llGooglePay.setOnClickListener(this);
        binding.llPaytm.setOnClickListener(this);
        binding.tvSaveBankDetails.setOnClickListener(this);
        binding.tvPhonePeUpi.setOnClickListener(this);
        binding.tvGPayUpi.setOnClickListener(this);
        binding.tvPaytmUpi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPhonePe:
                binding.phonePe.setVisibility(View.VISIBLE);
                binding.llBanlDetails.setVisibility(View.GONE);
                binding.googlePe.setVisibility(View.GONE);
                binding.paytm.setVisibility(View.GONE);
                break;
            case R.id.tvPhonePeUpi:
                checkUPIdetails("phonePe");
                break;
            case R.id.tvGPayUpi:
                checkUPIdetails("gPay");
                break;
            case R.id.tvPaytmUpi:
                checkUPIdetails("paytm");
                break;
            case R.id.tvSaveBankDetails:
                checkBankDetails();
                break;
            case R.id.llNEFT:
                binding.phonePe.setVisibility(View.GONE);
                binding.llBanlDetails.setVisibility(View.VISIBLE);
                binding.googlePe.setVisibility(View.GONE);
                binding.paytm.setVisibility(View.GONE);
                break;
            case R.id.llGooglePay:
                binding.phonePe.setVisibility(View.GONE);
                binding.llBanlDetails.setVisibility(View.GONE);
                binding.googlePe.setVisibility(View.VISIBLE);
                binding.paytm.setVisibility(View.GONE);
                break;
            case R.id.llPaytm:
                binding.phonePe.setVisibility(View.GONE);
                binding.llBanlDetails.setVisibility(View.GONE);
                binding.googlePe.setVisibility(View.GONE);
                binding.paytm.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void checkUPIdetails(String upiType) {
        String upi = "";
        if (upiType.equalsIgnoreCase("phonePe")) {
            if (binding.tilPhonePeUpi.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter Upi id", Toast.LENGTH_SHORT).show();
                binding.tilPhonePeUpi.requestFocus();
            } else {
                upi = binding.tilPhonePeUpi.getEditText().getText().toString();
                saveUpi(upi);
            }
        } else if (upiType.equalsIgnoreCase("gPay")) {
            if (binding.tilGpayUpi.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter Upi id", Toast.LENGTH_SHORT).show();
                binding.tilGpayUpi.requestFocus();
            } else {
                upi = binding.tilGpayUpi.getEditText().getText().toString();
                saveUpi(upi);
            }
        } else if (upiType.equalsIgnoreCase("paytm")) {
            if (binding.tilPaytm.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please enter Upi id", Toast.LENGTH_SHORT).show();
                binding.tilPaytm.requestFocus();
            } else {
                upi = binding.tilPaytm.getEditText().getText().toString();
                saveUpi(upi);
            }
        }
    }

    private void saveUpi(String upi) {
        CustomProgressDialog.showDialog(getActivity(),true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADD_BANK_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(),false);
                Toast.makeText(getActivity(), "Upi Saved successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(getActivity(),false);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("user_id",YoDB.getPref().read(Constants.ID,""));
                body.put("payment_type","upi");
                body.put("upi_id",upi);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void checkBankDetails() {
        String bankName = binding.tilBankName.getEditText().getText().toString();
        String accountNo = binding.tilBankAccountNo.getEditText().getText().toString();
        String holderName = binding.tilHolderName.getEditText().getText().toString();
        String branch = binding.tilBankBranch.getEditText().getText().toString();
        String ifsc = binding.tilIFSCNo.getEditText().getText().toString();

        if (bankName.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter bank name", Toast.LENGTH_SHORT).show();
            binding.tilBankName.requestFocus();
        } else if (accountNo.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter account number", Toast.LENGTH_SHORT).show();
            binding.tilBankAccountNo.requestFocus();
        } else if (holderName.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter Account holder name", Toast.LENGTH_SHORT).show();
            binding.tilHolderName.requestFocus();
        } else if (branch.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter bank branch", Toast.LENGTH_SHORT).show();
            binding.tilBankBranch.requestFocus();
        } else if (ifsc.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter ifsc code", Toast.LENGTH_SHORT).show();
            binding.tilIFSCNo.requestFocus();
        } else {
            saveBankDetails(bankName, accountNo, holderName, branch, ifsc);
        }
    }

    private void saveBankDetails(String bankName, String accountNo, String holderName, String branch, String ifsc) {
        CustomProgressDialog.showDialog(getActivity(), true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ADD_BANK_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(), false);
                Toast.makeText(getActivity(), "Bank Details Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(getActivity(), false);
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                body.put("payment_type", "neft");
                body.put("account_no", accountNo);
                //body.put("bankName", bankName);
                //body.put("holderName", holderName);
                body.put("branch_name", branch);
                body.put("ifsc_code", ifsc);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }
}