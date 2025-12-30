package com.sadik.earntask.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Models.DepositHistoryItem;
import com.sadik.earntask.R;
import java.util.ArrayList;
import java.util.List;

public class DepositHistoryAdapter extends RecyclerView.Adapter<DepositHistoryAdapter.ViewHolder> {
    private List<DepositHistoryItem> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deposit_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<DepositHistoryItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAmount;
        private TextView tvStatus;
        private TextView tvFromNumber;
        private TextView tvMethod;
        private TextView tvTrxId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvFromNumber = itemView.findViewById(R.id.tvFromNumber);
            tvMethod = itemView.findViewById(R.id.tvMethod);
            tvTrxId = itemView.findViewById(R.id.tvTrxId);
        }

        public void bind(DepositHistoryItem item) {
            // Set amount
            tvAmount.setText("৳ " + String.format("%.2f", item.getAmount()));

            // Set status with appropriate color
            tvStatus.setText(item.getStatus());
            if (item.getStatus().equals("Success")) {
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.green_600));
            } else if (item.getStatus().equals("Pending")) {
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.orange_600));
            } else {
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.red_600));
            }

            // Set other details
            tvFromNumber.setText("From: " + item.getFromNumber());
            tvMethod.setText("Method: " + item.getMethod());
            tvTrxId.setText("TrxID: " + item.getTransactionId());
        }
    }
}