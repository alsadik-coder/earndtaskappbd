package com.sadik.earntask.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.sadik.earntask.LoginActivity;
import com.sadik.earntask.PrefManager;
import com.sadik.earntask.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView tvName = view.findViewById(R.id.tv_profile_name);
        TextView tvEmail = view.findViewById(R.id.tv_profile_email);
        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);

        PrefManager prefManager = new PrefManager(requireContext());

        // 🔹 Show name & email
        tvName.setText(
                requireContext()
                        .getSharedPreferences("user_pref", 0)
                        .getString("name", "User")
        );

        tvEmail.setText(
                requireContext()
                        .getSharedPreferences("user_pref", 0)
                        .getString("email", "user@email.com")
        );

        // 🔹 Logout
        btnLogout.setOnClickListener(v -> {
            prefManager.logout();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        ///

        return view;
    }
}
