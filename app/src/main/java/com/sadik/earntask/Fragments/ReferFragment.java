package com.sadik.earntask.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.sadik.earntask.R;

public class ReferFragment extends Fragment {

    private EditText etReferralCode;
    private Button btnSubmitReferral;
    private CardView cvSubmitReferralCard;

    private TextView tvReferralCode;
    private TextView tvReferralLink;
    private ImageView btnCopyCode;
    private ImageView btnCopyLink;
    private ImageView btnBack;

    private SharedPreferences prefs;

    private static final String PREF_NAME = "referral_prefs";
    private static final String KEY_USED = "referral_used";

    // Replace with actual values from your backend
    private static final String REFERRAL_CODE = "SIAM1234";
    private static final String REFERRAL_LINK = "https://earntaka.com/referral/SIAM1234";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_refer, container, false);

        initializeViews(view);
        setupPreferences();
        setupClickListeners();
        checkReferralStatus();

        return view;
    }

    private void initializeViews(View view) {
        etReferralCode = view.findViewById(R.id.etSubmitReferralCode);
        btnSubmitReferral = view.findViewById(R.id.btnSubmitReferral);
        cvSubmitReferralCard = view.findViewById(R.id.cvSubmitReferralCard); // Add this ID to XML

        tvReferralCode = view.findViewById(R.id.tvReferralCode);
        tvReferralLink = view.findViewById(R.id.tvReferralLink);
        btnCopyCode = view.findViewById(R.id.btnCopyCode);
        btnCopyLink = view.findViewById(R.id.btnCopyLink);
        btnBack = view.findViewById(R.id.btnBack);
    }

    private void setupPreferences() {
        if (getContext() != null) {
            prefs = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    private void checkReferralStatus() {
        // 🔒 If already used → hide only the submit card
        if (prefs != null && prefs.getBoolean(KEY_USED, false)) {
            if (cvSubmitReferralCard != null) {
                cvSubmitReferralCard.setVisibility(View.GONE);
            }
        } else {
            if (cvSubmitReferralCard != null) {
                cvSubmitReferralCard.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setupClickListeners() {
        // Submit referral code
        if (btnSubmitReferral != null) {
            btnSubmitReferral.setOnClickListener(v -> handleSubmit());
        }

        // Copy referral code
        if (btnCopyCode != null) {
            btnCopyCode.setOnClickListener(v -> copyToClipboard(
                    REFERRAL_CODE,
                    "Referral code copied!"
            ));
        }

        // Copy referral link
        if (btnCopyLink != null) {
            btnCopyLink.setOnClickListener(v -> copyToClipboard(
                    REFERRAL_LINK,
                    "Referral link copied!"
            ));
        }

        // Back button
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    private void handleSubmit() {
        if (etReferralCode == null) return;

        String code = etReferralCode.getText().toString().trim().toUpperCase();

        if (TextUtils.isEmpty(code)) {
            etReferralCode.setError("Enter referral code");
            return;
        }

        if (code.length() < 4) {
            etReferralCode.setError("Invalid code format");
            return;
        }

        btnSubmitReferral.setEnabled(false);
        btnSubmitReferral.setText("Submitting...");

        String deviceId = getDeviceId();

        // 🚀 Send to server
        submitReferral(code, deviceId);
    }

    private void submitReferral(String code, String deviceId) {
        /*
         TODO: REPLACE WITH ACTUAL API CALL

         SERVER SHOULD:
         1. Check if device_id already exists in database
         2. If exists → reject with error
         3. Validate referral code exists
         4. Add ৳5 to user's wallet
         5. Increment referrer's referral count
         6. Mark device_id as used

         Example API call:
         ApiService.submitReferral(code, deviceId)
            .enqueue(new Callback<ReferralResponse>() {
                @Override
                public void onResponse(Call<ReferralResponse> call, Response<ReferralResponse> response) {
                    handleServerResponse(response);
                }

                @Override
                public void onFailure(Call<ReferralResponse> call, Throwable t) {
                    handleError(t);
                }
            });
        */

        // ---- MOCK SERVER RESPONSE (REMOVE IN PRODUCTION) ----
        simulateServerResponse(code);
        // -----------------------------------------------------
    }

    private void simulateServerResponse(String code) {
        // Simulate network delay
        new android.os.Handler().postDelayed(() -> {
            // Mock different scenarios
            boolean serverSaysAlreadyUsed = false;
            boolean serverSaysInvalid = false;
            boolean serverSuccess = true;

            if (getContext() == null) return;

            if (serverSaysAlreadyUsed) {
                showToast("This device has already used a referral code");
                resetSubmitButton();
                return;
            }

            if (serverSaysInvalid) {
                showToast("Invalid referral code. Please check and try again.");
                resetSubmitButton();
                return;
            }

            if (serverSuccess) {
                // 🔐 Lock forever - save to SharedPreferences
                if (prefs != null) {
                    prefs.edit()
                            .putBoolean(KEY_USED, true)
                            .apply();
                }

                showToast("৳5 added to your wallet 🎉");

                // Hide the submit card
                if (cvSubmitReferralCard != null) {
                    cvSubmitReferralCard.setVisibility(View.GONE);
                }

                // Clear input
                if (etReferralCode != null) {
                    etReferralCode.setText("");
                }
            }
        }, 1500); // 1.5 second delay
    }

    private void resetSubmitButton() {
        if (btnSubmitReferral != null) {
            btnSubmitReferral.setEnabled(true);
            btnSubmitReferral.setText("Submit Code");
        }
    }

    private void copyToClipboard(String text, String message) {
        if (getContext() == null) return;

        ClipboardManager clipboard = (ClipboardManager)
                getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Referral", text);
            clipboard.setPrimaryClip(clip);
            showToast(message);
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private String getDeviceId() {
        if (getContext() == null) {
            return "unknown_device";
        }

        try {
            return Settings.Secure.getString(
                    getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown_device";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up references to prevent memory leaks
        etReferralCode = null;
        btnSubmitReferral = null;
        cvSubmitReferralCard = null;
        tvReferralCode = null;
        tvReferralLink = null;
        btnCopyCode = null;
        btnCopyLink = null;
        btnBack = null;
    }
}