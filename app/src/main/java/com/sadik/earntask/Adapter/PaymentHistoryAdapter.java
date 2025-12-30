package com.sadik.earntask.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Models.PaymentHistoryModel;
import com.sadik.earntask.R;

import java.util.List;

public class PaymentHistoryAdapter
        extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {

    private List<PaymentHistoryModel> paymentList;

    public PaymentHistoryAdapter(List<PaymentHistoryModel> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PaymentHistoryModel model = paymentList.get(position);

        holder.tvAmount.setText(model.getAmount());
        holder.tvMethod.setText(model.getMethod());
        holder.tvTxnId.setText("Txn ID: " + model.getTxnId());
        holder.tvUserNumber.setText("User: " + model.getUserNumber());
        holder.tvStatus.setText(model.getStatus());

        // 🔹 Status color handling
        switch (model.getStatus().toLowerCase()) {
            case "approved":
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // green
                break;
            case "rejected":
                holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // red
                break;
            default:
                holder.tvStatus.setTextColor(Color.parseColor("#F9A825")); // pending
                break;
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount, tvMethod, tvTxnId, tvUserNumber, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvMethod = itemView.findViewById(R.id.tvMethod);
            tvTxnId = itemView.findViewById(R.id.tvTxnId);
            tvUserNumber = itemView.findViewById(R.id.tvUserNumber);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
