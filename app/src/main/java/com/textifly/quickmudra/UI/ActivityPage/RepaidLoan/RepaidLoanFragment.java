package com.textifly.quickmudra.UI.ActivityPage.RepaidLoan;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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
import com.textifly.quickmudra.UI.ActivityPage.Adapter.LoanStatusAdapter;
import com.textifly.quickmudra.UI.ActivityPage.Model.LoanStatusModel;
import com.textifly.quickmudra.Utils.Constants;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.FragmentRepaidLoanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepaidLoanFragment extends Fragment {
    FragmentRepaidLoanBinding binding;
    List<LoanStatusModel> modelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRepaidLoanBinding.inflate(inflater, container, false);

        binding.llNoDataFound.setVisibility(View.GONE);
        binding.rvRepaidLoan.setVisibility(View.VISIBLE);

        setLayout();
        loadLoanDetails();
        return binding.getRoot();
    }

    private void setLayout() {
        binding.rvRepaidLoan.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
    }

    private void loadLoanDetails() {
        modelList = new ArrayList<>();
        CustomProgressDialog.showDialog(getActivity(), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.PAID_LOANS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(getActivity(), false);
                try {
                    Log.d("RESPONSE",response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("0")) {
                        binding.rvRepaidLoan.setVisibility(View.VISIBLE);
                        binding.llNoDataFound.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        Log.d("JSON ARRAY",jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("loans_id");
                            String amount = object.getString("loan_amount");
                            String paid_status = object.getString("paid_status");
                            String disbursed_date = object.getString("date");
                            String payment_date = object.getString("due_date");
                            modelList.add(new LoanStatusModel(id,amount,paid_status,disbursed_date,payment_date));
                        }
                        LoanStatusAdapter adapter = new LoanStatusAdapter(modelList,getActivity());
                        binding.rvRepaidLoan.setAdapter(adapter);
                    }else{
                        binding.rvRepaidLoan.setVisibility(View.GONE);
                        binding.llNoDataFound.setVisibility(View.VISIBLE);
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
                body.put("user_id", YoDB.getPref().read(Constants.ID, ""));
                return body;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}