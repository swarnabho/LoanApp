package com.textifly.quickmudra.UI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.Activity.DetailsListActivity;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.MainActivity;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.FragmentHomeBinding;
import com.textifly.quickmudra.databinding.HomePopupBinding;
import com.textifly.quickmudra.databinding.RegPopupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {
    FragmentHomeBinding binding;
    private boolean backPress = false;
    private String amount = "", tenure = "";
    private final int GST = 18;

    List<String> amountList = new ArrayList<>();
    ArrayAdapter<String> amountAdapter;

    List<String> timeList = new ArrayList<>();
    ArrayAdapter<String> timeAdapter;

    boolean isActive = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        BtnClick();
        setAmountSpinner();
        setTimeSpinner();
        //if (YoDB.getPref().read(Constants.haveActivated, "").isEmpty()) {
        checkActive();
        //showPopUp();
        //}

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        return binding.getRoot();
    }

    private void checkActive() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.USER_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        //binding.etChooseAmount.setEnabled(false);
                        isActive = false;
                        showPopUp();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userid", YoDB.getPref().read(Constants.ID, ""));
                return map;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void setAmountSpinner() {
        String[] loanAmount = {"Select Amount", "500", "1000", "2000"};

        amountAdapter
                = new ArrayAdapter(
                getActivity(), android.R.layout.simple_spinner_item, loanAmount);

        amountAdapter.setDropDownViewResource(R.layout.spinner_list);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        binding.spinnerAmount.setAdapter(amountAdapter);
        binding.spinnerAmount.setSelected(false);  // must
        binding.spinnerAmount.setSelection(0, true);

        binding.spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();
                if (!item.equals("Select Amount")) {
                    binding.etChooseAmount.setText(item);
                    amount = item;
                    binding.llTime.setVisibility(View.VISIBLE);
                    binding.spinnerAmount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.spinnerAmount.setVisibility(View.GONE);
            }
        });

    }

    private void setTimeSpinner() {
        String[] time = {"Select Tanure", "30 Days", "60 Days", "90 Days"};

        timeAdapter
                = new ArrayAdapter(
                getActivity(), android.R.layout.simple_spinner_item, time);

        timeAdapter.setDropDownViewResource(R.layout.spinner_list);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        binding.spinnerTanure.setAdapter(timeAdapter);
        binding.spinnerTanure.setSelected(false);  // must
        binding.spinnerTanure.setSelection(0, true);

        binding.spinnerTanure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getSelectedItem().toString();
                if (!item.equals("Select Tanure")) {
                    binding.etChooseTime.setText(item);
                    if(item.equals("30 Days")){
                        tenure = "30";
                    }else if(item.equals("60 Days")){
                        tenure = "60";
                    }else if(item.equals("90 Days")){
                        tenure = "90";
                    }
                    getProcessDetails();
                    binding.spinnerTanure.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                binding.spinnerTanure.setVisibility(View.GONE);
            }
        });
    }

    private void getProcessDetails() {
        CustomProgressDialog.showDialog(getActivity(), true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.GET_PROCESS_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(), false);
                binding.llDetails.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        //Toast.makeText(getActivity(), "hi", Toast.LENGTH_SHORT).show();
                        int amount = Integer.parseInt(jsonObject.getString("amount"));
                        int interest = Integer.parseInt(jsonObject.getString("interest"));
                        Double process_charge = Double.parseDouble(jsonObject.getString("process_charge"));

                        Double interestWithGST = ((process_charge * GST) / 100);
                        double disburse_amnt = amount - (process_charge + interestWithGST);
                        Log.d("interestWithGST","interestWithGST: "+interestWithGST);
                        binding.tvDusburseAmount.setText("₹ "+String.format("%.2f", disburse_amnt));
                        binding.tvProcessingFee.setText("₹ "+jsonObject.getString("process_charge"));
                        binding.tvGST.setText("₹ "+String.format("%.2f", interestWithGST));
                        binding.tvRepayment.setText("₹ "+""+(amount+Integer.parseInt(jsonObject.getString("process_charge"))));
                        binding.tvAdjustedRepayment.setText("₹ "+""+(amount+Integer.parseInt(jsonObject.getString("process_charge"))));
                        //binding.tvFinalAdjustedRepayment.setText("₹ "+""+(amount+Integer.parseInt(jsonObject.getString("process_charge"))));
                    }else{
                        //Toast.makeText(getActivity(), "bye", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(getActivity(), false);
                Toast.makeText(getActivity(), "Getting some troubles", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("amount", amount);
                body.put("tenure", tenure);
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }

    private void showPopUp() {
        YoDB.getPref().write(Constants.haveActivated, "", "yes");
        final Dialog dialog = new Dialog(getActivity());
        HomePopupBinding binding = HomePopupBinding.inflate(LayoutInflater.from(getActivity()));
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

    @Override
    public void onStop() {
        super.onStop();
        callback.setEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setEnabled(true);
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            Log.e("Home Fragment== ", "Called");
            if (backPress) {
                //ManageLoginData.clearLoginData(); // Logout(Temporary)
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Press again to exit", Toast.LENGTH_SHORT).show();
            }
            pressTimer();
            backPress = true;
        }
    };

    private void pressTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        }, 2000);
    }

    private void BtnClick() {
        binding.llMenu.setOnClickListener(this);
        binding.etChooseAmount.setOnClickListener(this);
        binding.etChooseTime.setOnClickListener(this);
        binding.txtRequest.setOnClickListener(this);
        binding.cvCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llMenu:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.cvCheck:
                startActivity(new Intent(getActivity(), DetailsListActivity.class));
                getActivity().overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
                break;
            case R.id.etChooseAmount:
                if (isActive) {
                    binding.spinnerAmount.performClick();
                    binding.spinnerAmount.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Please wait until your account is activate", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etChooseTime:
                binding.spinnerTanure.performClick();
                binding.spinnerTanure.setVisibility(View.VISIBLE);
                break;
            case R.id.txtRequest:
                checkRequest(view);
                break;
        }
    }

    private void checkRequest(View view) {
        if (binding.etChooseAmount.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please select borrow amount", Toast.LENGTH_SHORT).show();
        } else if (binding.etChooseTime.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please select tanure of loan", Toast.LENGTH_SHORT).show();
        } else {
            loanRequest(view);
        }
    }

    private void loanRequest(View view) {
        String amount = binding.etChooseAmount.getText().toString();
        String time = binding.etChooseTime.getText().toString();

        Bundle srchbundle = new Bundle();
        srchbundle.putString("amount", "amount");
        srchbundle.putString("time", "time");
        Navigation.findNavController(view).navigate(R.id.action_nav_home_to_payment, srchbundle);

        /*Intent intent = new Intent(getActivity(),PaymentFragment.class);
        intent.putExtra("amount",amount);
        intent.putExtra("time",time);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.fade_in_animation,R.anim.fade_out_animation);*/
    }
}