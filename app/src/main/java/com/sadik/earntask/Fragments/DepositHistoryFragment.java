package com.sadik.earntask.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Adapter.DepositHistoryAdapter;
import com.sadik.earntask.Models.DepositHistoryItem;
import com.sadik.earntask.R;
import java.util.ArrayList;
import java.util.List;

public class DepositHistoryFragment extends Fragment {
    // View references
    private TextView tvTitle;
    private ImageView btnBack;
    private RecyclerView rvDepositHistory;
    private DepositHistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit_history, container, false);

        // Initialize all views with findViewById
        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        loadDepositHistory();
    }

    private void initViews(View view) {
        // Toolbar views
        tvTitle = view.findViewById(R.id.tvTitle);
        btnBack = view.findViewById(R.id.btnBack);

        // RecyclerView
        rvDepositHistory = view.findViewById(R.id.rvDepositHistory);

        // Set up toolbar
        tvTitle.setText("Deposit History");

        // Back button click
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        adapter = new DepositHistoryAdapter();
        rvDepositHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDepositHistory.setAdapter(adapter);
    }

    private void loadDepositHistory() {
        // TODO: Team member will fetch this data from MySQL
        // For now, using dummy data
        List<DepositHistoryItem> dummyData = getDummyDepositHistory();
        adapter.updateItems(dummyData);

        /* Team member should implement something like:
        1. Create a method to fetch deposit history from MySQL
        2. Parse the response and create DepositHistoryItem objects
        3. Call adapter.updateItems() with the fetched data
        */
    }

    private List<DepositHistoryItem> getDummyDepositHistory() {
        List<DepositHistoryItem> items = new ArrayList<>();

        // Add some dummy data for testing
        items.add(new DepositHistoryItem(500, "01712345678", "bKash", "BKS123456789", "Success", "2023-06-12 14:30"));
        items.add(new DepositHistoryItem(1000, "01898765432", "Nagad", "NGD987654321", "Success", "2023-06-10 10:15"));
        items.add(new DepositHistoryItem(750, "01911223344", "Rocket", "RKT112233445", "Pending", "2023-06-08 16:45"));

        return items;
    }
}