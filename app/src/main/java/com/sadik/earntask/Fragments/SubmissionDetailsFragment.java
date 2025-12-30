package com.sadik.earntask.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sadik.earntask.Adapter.ScreenshotAdapter;
import com.sadik.earntask.R;

import java.util.ArrayList;
import java.util.List;

public class SubmissionDetailsFragment extends Fragment {

    private ImageView btnBack;
    private MaterialButton btnApprove, btnDecline;
    private TextView tvUserName, tvSubmittedText;
    private RecyclerView rvScreenshots;

    private ScreenshotAdapter screenshotAdapter;
    private List<Uri> screenshotList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_submission_details, container, false);

        initViews(view);
        setupRecyclerView();
        setupClickListeners();
        loadDummyData(); // later replace with real data

        return view;
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnApprove = view.findViewById(R.id.btnApprove);
        btnDecline = view.findViewById(R.id.btnDecline);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvSubmittedText = view.findViewById(R.id.tvSubmittedText);
        rvScreenshots = view.findViewById(R.id.rvScreenshots);
    }

    private void setupRecyclerView() {
        rvScreenshots.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        screenshotAdapter = new ScreenshotAdapter(screenshotList);
        rvScreenshots.setAdapter(screenshotAdapter);
    }

    private void setupClickListeners() {
        // 🔙 Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // ✅ Approve
        btnApprove.setOnClickListener(v -> approveSubmission());

        // ❌ Decline
        btnDecline.setOnClickListener(v -> declineSubmission());
    }

    private void approveSubmission() {
        // TODO: API / Firebase / Database approve logic
        Toast.makeText(getContext(), "Submission Approved ✅", Toast.LENGTH_SHORT).show();
        if (getActivity() != null) getActivity().onBackPressed();
    }

    private void declineSubmission() {
        // TODO: API / Firebase / Database decline logic
        Toast.makeText(getContext(), "Submission Declined ❌", Toast.LENGTH_SHORT).show();
        if (getActivity() != null) getActivity().onBackPressed();
    }

    private void loadDummyData() {
        tvUserName.setText("Abdullah");
        tvSubmittedText.setText("This is user submitted task description.");

        // Dummy screenshot URIs (replace with real images or paths)
        screenshotList.add(Uri.parse("android.resource://com.sadik.earntask/drawable/img1"));
        screenshotList.add(Uri.parse("android.resource://com.sadik.earntask/drawable/img2"));
        screenshotList.add(Uri.parse("android.resource://com.sadik.earntask/drawable/img3"));

        screenshotAdapter.notifyDataSetChanged();
    }
}
