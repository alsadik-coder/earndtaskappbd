package com.sadik.earntask.Fragments;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sadik.earntask.R;

import java.io.IOException;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 101;

    private ImageView imgProfile, btnEditImage;
    private TextInputEditText etName, etPhone, etEmail;
    private MaterialButton btnSave;

    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // 🔹 Init views
        imgProfile = view.findViewById(R.id.imgProfile);
        btnEditImage = view.findViewById(R.id.btnEditImage);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        btnSave = view.findViewById(R.id.btnSave);

        // 🔹 Load existing user data
        loadProfile();

        // 🔹 Change profile image
        btnEditImage.setOnClickListener(v -> openGallery());

        // 🔹 Save profile
        btnSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    // ===============================
    // Load user data
    // ===============================
    private void loadProfile() {
        SharedPreferences prefs =
                requireActivity().getSharedPreferences("USER_SESSION", 0);

        etName.setText(prefs.getString("name", ""));
        etPhone.setText(prefs.getString("phone", ""));
        etEmail.setText(prefs.getString("email", ""));
    }

    // ===============================
    // Save user data
    // ===============================
    private void saveProfile() {

        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter full name");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Enter phone number");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email address");
            return;
        }

        SharedPreferences prefs =
                requireActivity().getSharedPreferences("USER_SESSION", 0);

        prefs.edit()
                .putString("name", name)
                .putString("phone", phone)
                .putString("email", email)
                .apply();

        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // 🔙 Back to ProfileFragment
        requireActivity()
                .getSupportFragmentManager()
                .popBackStack();
    }

    // ===============================
    // Open gallery
    // ===============================
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    // ===============================
    // Handle image result
    // ===============================
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            selectedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                imgProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
