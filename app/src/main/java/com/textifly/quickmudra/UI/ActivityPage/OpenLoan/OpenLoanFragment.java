package com.textifly.quickmudra.UI.ActivityPage.OpenLoan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.ManageSharedPreferenceData.YoDB;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.UI.ActivityPage.Adapter.LoanStatusAdapter;
import com.textifly.quickmudra.UI.ActivityPage.Model.LoanStatusModel;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.FragmentOpenLoanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenLoanFragment extends Fragment {
    FragmentOpenLoanBinding binding;
    List<LoanStatusModel> modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOpenLoanBinding.inflate(inflater,container,false);
        setLayout();
        loadLoan();
        return binding.getRoot();
    }

    private void setLayout() {
        binding.rvOpenLoan.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    private void loadLoan() {
        modelList = new ArrayList<>();
        //CustomProgressDialog.showDialog(getActivity(),true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.DUE_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(), false);
                try {
                    Log.d("RESPONSE",response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        binding.rvOpenLoan.setVisibility(View.VISIBLE);
                        binding.llNoDataFound.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        Log.d("JSON ARRAY",jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("loans_id");
                            String amount = object.getString("loan_amount");
                            String paid_status = object.getString("listfor");
                            String disbursed_date = object.getString("date");
                            String payment_date = object.getString("due_date");
                            modelList.add(new LoanStatusModel(id,amount,paid_status,disbursed_date,payment_date));
                        }
                        LoanStatusAdapter adapter = new LoanStatusAdapter(modelList,getActivity());
                        binding.rvOpenLoan.setAdapter(adapter);
                    }else{
                        binding.rvOpenLoan.setVisibility(View.GONE);
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                body.put("user_id", YoDB.getPref().read(Constants.ID,""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(sr);
    }
}