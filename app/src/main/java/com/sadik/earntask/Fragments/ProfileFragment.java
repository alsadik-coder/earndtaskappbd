package com.sadik.earntask.Fragments;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sadik.earntask.LoginActivity;
import com.sadik.earntask.R;

public class ProfileFragment extends Fragment {

    private ImageView btnEditProfile;
    private TextView tvUserName, tvUserEmail;
    private TextView btnMyTasks, btnPayments, btnNotifications, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 🔹 Init views
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        btnMyTasks = view.findViewById(R.id.btnMyTasks);
        btnPayments = view.findViewById(R.id.btnPayments);
        btnNotifications = view.findViewById(R.id.btnNotifications);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 🔹 Load user data (example)
        tvUserName.setText("Abdullah");
        tvUserEmail.setText("abdullah@email.com");

        // 🔹 Edit profile
        btnEditProfile.setOnClickListener(v ->
                openFragment(new EditProfileFragment())
        );

        // 🔹 My Tasks
        btnMyTasks.setOnClickListener(v ->
                openFragment(new MicroTaskFragment())
        );

        // 🔹 Payment History
        btnPayments.setOnClickListener(v ->
                openFragment(new PaymentHistoryFragment())
        );

        // 🔹 Notifications
        btnNotifications.setOnClickListener(v ->
                openFragment(new NotificationFragment())
        );

        // 🔹 Logout
        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    // 🔹 Fragment navigation helper
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction =
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // 🔹 Logout logic
    private void logoutUser() {
        SharedPreferences prefs =
                requireActivity().getSharedPreferences("USER_SESSION", 0);

        prefs.edit().clear().apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
