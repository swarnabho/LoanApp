package com.textifly.quickmudra.UI.ActivityPage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.textifly.quickmudra.R;
import com.textifly.quickmudra.UI.ActivityPage.Model.LoanStatusModel;

import java.util.List;

public class LoanStatusAdapter extends RecyclerView.Adapter<LoanStatusAdapter.ViewHolder> {
    List<LoanStatusModel> modelList;
    Context context;

    public LoanStatusAdapter(List<LoanStatusModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amount.setText("₹ " + modelList.get(position).getAmount());
        //holder.status.setText(modelList.get(position).getPaid_status());

        if (modelList.get(position).getPaid_status().equalsIgnoreCase("paid")) {
            holder.status.setText(modelList.get(position).getPaid_status());
            holder.disbursedDt.setText("Disbursed : " + modelList.get(position).getDisbursed_date());
            holder.paymentDt.setText("Paid : " + modelList.get(position).getPayment_date());
            holder.llColor.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (modelList.get(position).getPaid_status().equalsIgnoreCase("due")) {
            holder.status.setText("Due");
            holder.disbursedDt.setText("Disbursed : " + modelList.get(position).getDisbursed_date());
            holder.paymentDt.setText("Paid : " + modelList.get(position).getPayment_date());
            holder.llColor.setBackgroundColor(context.getResources().getColor(R.color.goldenColor1));
        } else if (modelList.get(position).getPaid_status().equalsIgnoreCase("overdue")) {
            holder.status.setText("Overdue");
            holder.disbursedDt.setText("Disbursed : " + modelList.get(position).getDisbursed_date());
            holder.paymentDt.setText("Paid : " + modelList.get(position).getPayment_date());
            holder.llColor.setBackgroundColor(context.getResources().getColor(R.color.red));
        }else if (modelList.get(position).getPaid_status().equalsIgnoreCase("Pending")) {
            holder.status.setText("Pending");
            holder.disbursedDt.setText("Requested : " + modelList.get(position).getDisbursed_date());
            holder.paymentDt.setVisibility(View.GONE);
            holder.llColor.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, status, disbursedDt, paymentDt;
        LinearLayout llColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llColor = itemView.findViewById(R.id.llColor);
            amount = itemView.findViewById(R.id.tvAmount);
            status = itemView.findViewById(R.id.tvStatus);
            disbursedDt = itemView.findViewById(R.id.tvDisbursedDt);
            paymentDt = itemView.findViewById(R.id.tvPaymentDt);

        }

    }
}
