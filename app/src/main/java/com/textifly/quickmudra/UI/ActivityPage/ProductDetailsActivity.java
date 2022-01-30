package com.textifly.quickmudra.UI.ActivityPage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.textifly.quickmudra.CustomDialog.CustomProgressDialog;
import com.textifly.quickmudra.R;
import com.textifly.quickmudra.Utils.Urls;
import com.textifly.quickmudra.databinding.ActivityProductDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvPayNow.setVisibility(View.GONE);
        loadProductDetails();
    }

    private void loadProductDetails() {
        CustomProgressDialog.showDialog(ProductDetailsActivity.this,true);
        StringRequest sr = new StringRequest(Request.Method.POST, Urls.ORDER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CustomProgressDialog.showDialog(ProductDetailsActivity.this,false);
                try {
                    JSONObject object = new JSONObject(response);
                    binding.tvTitle.setText(object.getString("loans_id"));
                    binding.tvAmount.setText("₹ "+object.getString("repayment_amount"));
                    binding.tvStatus.setText(object.getString("loan_status"));
                    binding.tvPaidOn.setText(object.getString("due_date"));
                    binding.tvIntBefDueDt.setText(object.getString("processing_fee"));
                    binding.tvTotalFee.setText("₹ "+object.getString("total_fee"));
                    binding.tvTotalAmountPaid.setText("₹ "+object.getString("repayment_amount"));
                    binding.tvDisbursedAmount.setText("₹ "+object.getString("loan_amount"));
                    binding.tvIntAftrDueDt.setText("₹ "+object.getString("total_interest"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomProgressDialog.showDialog(ProductDetailsActivity.this,false);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> body = new HashMap<>();
                body.put("loans_id",getIntent().getStringExtra("loan_id"));
                return body;
            }
        };

        Volley.newRequestQueue(ProductDetailsActivity.this).add(sr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.llMenu:

                break;
        }
    }
}