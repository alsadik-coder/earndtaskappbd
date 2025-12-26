package com.sadik.earntask;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskFragment extends Fragment {

    private boolean day1Claimed = false; // Day 1 claimed flag

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnClaim = view.findViewById(R.id.btn_claim);
        btnClaim.setOnClickListener(v -> showDailyCheckinDialog());
    }

    private void showDailyCheckinDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_daily_checkin);
        dialog.setCancelable(true);

        // Find day buttons
        Button btnDay1 = dialog.findViewById(R.id.btnDay1);
        Button btnDay2 = dialog.findViewById(R.id.btnDay2);
        Button btnDay3 = dialog.findViewById(R.id.btnDay3);
        Button btnDay4 = dialog.findViewById(R.id.btnDay4);
        Button btnDay5 = dialog.findViewById(R.id.btnDay5);
        Button btnDay6 = dialog.findViewById(R.id.btnDay6);
        Button btnDay7 = dialog.findViewById(R.id.btnDay7);

        // Close/Claim button
        Button btnClose = dialog.findViewById(R.id.btnCloseDialog);

        // Initially enable only Day 1 if not claimed, others disabled
        btnDay1.setEnabled(!day1Claimed);
        btnDay2.setEnabled(false);
        btnDay3.setEnabled(false);
        btnDay4.setEnabled(false);
        btnDay5.setEnabled(false);
        btnDay6.setEnabled(false);
        btnDay7.setEnabled(false);

        // Day 1 click
        btnDay1.setOnClickListener(v -> {
            day1Claimed = true;
            btnDay1.setEnabled(false);
            Toast.makeText(getContext(), "Day 1 claimed successfully!", Toast.LENGTH_SHORT).show();
        });

        // Close button click (can also act as claim for Day 1)
        btnClose.setOnClickListener(v -> {
            if (!day1Claimed) {
                day1Claimed = true;
                btnDay1.setEnabled(false);
                Toast.makeText(getContext(), "Day 1 claimed successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "You have already claimed today!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
}
