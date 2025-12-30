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

import com.sadik.earntask.Adapter.NotificationAdapter;
import com.sadik.earntask.Models.NotificationModel;
import com.sadik.earntask.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<NotificationModel> notificationList;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rvNotifications = view.findViewById(R.id.rvNotifications);
        btnBack = view.findViewById(R.id.btnBack);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationList = new ArrayList<>();

        // 🔹 Demo data (later API / Firebase / DB theke asbe)
        notificationList.add(new NotificationModel(
                "Task Approved",
                "Your submitted task has been approved successfully.",
                "2h ago"
        ));

        notificationList.add(new NotificationModel(
                "Reward Added",
                "৳5 has been added to your wallet.",
                "5h ago"
        ));

        notificationList.add(new NotificationModel(
                "New Task Available",
                "A new micro task is available for you.",
                "1 day ago"
        ));

        adapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(adapter);

        // 🔙 Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
