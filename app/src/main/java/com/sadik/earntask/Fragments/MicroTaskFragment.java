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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sadik.earntask.R;

public class MicroTaskFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_micro_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🔹 Views
        ImageView btnBack = view.findViewById(R.id.btnBack);
        ImageView btnHistory = view.findViewById(R.id.btnHistory);
        FloatingActionButton fabAddTask = view.findViewById(R.id.fabAddTask);

        RecyclerView rvMicroTasks = view.findViewById(R.id.rvMicroTasks);
        rvMicroTasks.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ---------------------------
        // 🔙 Back
        // ---------------------------
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // ---------------------------
        // 🕘 History
        // ---------------------------
        btnHistory.setOnClickListener(v -> {
            Fragment fragment = new TaskHistoryFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // ---------------------------
        // ➕ Add Micro Task
        // ---------------------------
        fabAddTask.setOnClickListener(v -> {
            Fragment fragment = new AddMicroTaskFragment();

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
