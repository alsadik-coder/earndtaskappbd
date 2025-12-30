package com.sadik.earntask.Fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Adapter.PaymentHistoryAdapter;
import com.sadik.earntask.Models.PaymentHistoryModel;
import com.sadik.earntask.R;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryFragment extends Fragment {

    private RecyclerView rvPaymentHistory;
    private PaymentHistoryAdapter adapter;
    private List<PaymentHistoryModel> paymentList;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_payment_history, container, false);

        rvPaymentHistory = view.findViewById(R.id.rvPaymentHistory);
        btnBack = view.findViewById(R.id.btnBack);

        rvPaymentHistory.setLayoutManager(
                new LinearLayoutManager(getContext()));

        paymentList = new ArrayList<>();

        // 🔹 Demo data (later API / DB theke asbe)
        paymentList.add(new PaymentHistoryModel(
                "৳ 1,250",
                "bKash",
                "1234567890",
                "+880123456789",
                "Approved"
        ));

        paymentList.add(new PaymentHistoryModel(
                "৳ 500",
                "Nagad",
                "9876543210",
                "+880198765432",
                "Pending"
        ));

        paymentList.add(new PaymentHistoryModel(
                "৳ 300",
                "Rocket",
                "4567891230",
                "+880171234567",
                "Rejected"
        ));

        adapter = new PaymentHistoryAdapter(paymentList);
        rvPaymentHistory.setAdapter(adapter);

        // 🔙 Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
