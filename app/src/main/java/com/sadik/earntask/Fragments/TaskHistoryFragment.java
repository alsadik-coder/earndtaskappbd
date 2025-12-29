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

import com.sadik.earntask.Adapter.MyTaskAdapter;
import com.sadik.earntask.Models.MyTask;
import com.sadik.earntask.R;

import java.util.ArrayList;
import java.util.List;

public class TaskHistoryFragment extends Fragment {

    private RecyclerView rvMyTasks;
    private ImageView btnBack;
    private MyTaskAdapter adapter;
    private List<MyTask> taskList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMyTasks = view.findViewById(R.id.rvMyTasks);
        btnBack = view.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        setupRecycler();
        loadDummyData(); // later replace with API / DB
    }

    private void setupRecycler() {
        taskList = new ArrayList<>();
        adapter = new MyTaskAdapter(requireContext(), taskList);

        rvMyTasks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvMyTasks.setAdapter(adapter);
    }

    private void loadDummyData() {
        taskList.add(new MyTask(
                "Facebook Page Like",
                "Pending",
                10,
                "12 Sep 2025",
                null
        ));

        taskList.add(new MyTask(
                "Install App",
                "Approved",
                15,
                "10 Sep 2025",
                null
        ));

        taskList.add(new MyTask(
                "YouTube Subscribe",
                "Rejected",
                20,
                "08 Sep 2025",
                "Screenshot not clear"
        ));

        adapter.notifyDataSetChanged();
    }
}
