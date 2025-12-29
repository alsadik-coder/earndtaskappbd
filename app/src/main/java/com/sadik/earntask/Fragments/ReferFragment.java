package com.sadik.earntask.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.sadik.earntask.R;

public class ReferFragment extends Fragment {

    // UI Components
    private ImageView btnBack, btnCopyCode, btnCopyLink;
    private TextView tvReferralCode, tvReferralLink, tvReferrals, tvEarnings;
    private EditText etSubmitReferralCode;
    private CardView cvSubmitReferralCard;

    // SharedPreferences for storing data
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ReferralPrefs";
    private static final String KEY_REFERRAL_USED = "referral_used";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_USER_EARNINGS = "user_earnings";
    private static final String KEY_REFERRAL_COUNT = "referral_count";

    // Referral reward amount
    private static final int REFERRAL_REWARD = 5; // 5 Taka

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refer, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Initialize UI components
        initViews(view);

        // Set up click listeners
        setupClickListeners();

        // Load user data
        loadUserData();

        // Check if referral card should be hidden
        checkReferralCardVisibility();

        return view;
    }

    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnCopyCode = view.findViewById(R.id.btnCopyCode);
        btnCopyLink = view.findViewById(R.id.btnCopyLink);
        tvReferralCode = view.findViewById(R.id.tvReferralCode);
        tvReferralLink = view.findViewById(R.id.tvReferralLink);
        tvReferrals = view.findViewById(R.id.tvReferrals);
        tvEarnings = view.findViewById(R.id.tvEarnings);
        etSubmitReferralCode = view.findViewById(R.id.etSubmitReferralCode);
        cvSubmitReferralCard = view.findViewById(R.id.cvSubmitReferralCard);

        // Set up submit button
        View btnSubmitReferral = view.findViewById(R.id.btnSubmitReferral);
        btnSubmitReferral.setOnClickListener(v -> submitReferralCode());

        // Set up share options (you would need to implement these methods)
        view.findViewById(R.id.shareWhatsApp).setOnClickListener(v -> shareViaWhatsApp());
        view.findViewById(R.id.shareMessenger).setOnClickListener(v -> shareViaMessenger());
        view.findViewById(R.id.shareTelegram).setOnClickListener(v -> shareViaTelegram());
        view.findViewById(R.id.shareEmail).setOnClickListener(v -> shareViaEmail());
        view.findViewById(R.id.shareSMS).setOnClickListener(v -> shareViaSMS());

        // Set up CTA button

    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // Copy referral code
        btnCopyCode.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Referral Code", tvReferralCode.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "রেফারেল কোড কপি করা হয়েছে", Toast.LENGTH_SHORT).show();
        });

        // Copy referral link
        btnCopyLink.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Referral Link", tvReferralLink.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "রেফারেল লিঙ্ক কপি করা হয়েছে", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        // Load user earnings
        int earnings = sharedPreferences.getInt(KEY_USER_EARNINGS, 0);
        tvEarnings.setText("৳" + earnings);

        // Load referral count
        int referralCount = sharedPreferences.getInt(KEY_REFERRAL_COUNT, 0);
        tvReferrals.setText(String.valueOf(referralCount));

        // Generate or load user's referral code
        String referralCode = sharedPreferences.getString("user_referral_code", "");
        if (TextUtils.isEmpty(referralCode)) {
            // Generate a new referral code (you might want to use a more sophisticated method)
            referralCode = generateReferralCode();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_referral_code", referralCode);
            editor.apply();
        }
        tvReferralCode.setText(referralCode);

        // Generate referral link
        String referralLink = "https://earntaka.com/referral/" + referralCode;
        tvReferralLink.setText(referralLink);
    }

    private void checkReferralCardVisibility() {
        // Check if user has already used a referral code
        boolean referralUsed = sharedPreferences.getBoolean(KEY_REFERRAL_USED, false);

        if (referralUsed) {
            // Hide the submit referral card
            cvSubmitReferralCard.setVisibility(View.GONE);
        } else {
            // Show the submit referral card
            cvSubmitReferralCard.setVisibility(View.VISIBLE);
        }
    }

    private void submitReferralCode() {
        String enteredCode = etSubmitReferralCode.getText().toString().trim();

        // Check if the entered code is empty
        if (TextUtils.isEmpty(enteredCode)) {
            Toast.makeText(getContext(), "অনুগ্রহ করে একটি রেফারেল কোড লিখুন", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user has already used a referral code
        boolean referralUsed = sharedPreferences.getBoolean(KEY_REFERRAL_USED, false);
        if (referralUsed) {
            Toast.makeText(getContext(), "আপনি ইতিমধ্যে একটি রেফারেল কোড ব্যবহার করেছেন", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get device ID
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // Check if this device has already used a referral code
        String savedDeviceId = sharedPreferences.getString(KEY_DEVICE_ID, "");
        if (!TextUtils.isEmpty(savedDeviceId) && savedDeviceId.equals(deviceId)) {
            Toast.makeText(getContext(), "এই ডিভাইস থেকে ইতিমধ্যে একটি রেফারেল কোড ব্যবহার করা হয়েছে", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the referral code (this would typically involve a server call)
        if (validateReferralCode(enteredCode)) {
            // Code is valid, process the reward
            processReferralReward(enteredCode, deviceId);
        } else {
            Toast.makeText(getContext(), "অবৈধ রেফারেল কোড", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateReferralCode(String code) {
        // In a real app, you would validate this against a server
        // For demo purposes, we'll consider any 6-8 character code as valid
        return code.length() >= 6 && code.length() <= 8;
    }

    private void processReferralReward(String referralCode, String deviceId) {
        // Update user's earnings
        int currentEarnings = sharedPreferences.getInt(KEY_USER_EARNINGS, 0);
        int newEarnings = currentEarnings + REFERRAL_REWARD;

        // Update referral count
        int referralCount = sharedPreferences.getInt(KEY_REFERRAL_COUNT, 0);
        int newReferralCount = referralCount + 1;

        // Save updated data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_EARNINGS, newEarnings);
        editor.putInt(KEY_REFERRAL_COUNT, newReferralCount);
        editor.putBoolean(KEY_REFERRAL_USED, true);
        editor.putString(KEY_DEVICE_ID, deviceId);
        editor.putString("used_referral_code", referralCode);
        editor.apply();

        // Update UI
        tvEarnings.setText("৳" + newEarnings);
        tvReferrals.setText(String.valueOf(newReferralCount));

        // Hide the submit referral card
        cvSubmitReferralCard.setVisibility(View.GONE);

        // Show success message
        Snackbar.make(getView(), "অভিনন্দন! আপনি " + REFERRAL_REWARD + " টাকা পেয়েছেন", Snackbar.LENGTH_LONG).show();

        // Clear the input field
        etSubmitReferralCode.setText("");
    }

    private String generateReferralCode() {
        // Simple referral code generation - in a real app, you'd want a more robust method
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    // Share methods (implement as needed)
    private void shareViaWhatsApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        shareIntent.setPackage("com.whatsapp");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "WhatsApp ইনস্টল করা নেই", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaMessenger() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        shareIntent.setPackage("com.facebook.orca");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Messenger ইনস্টল করা নেই", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaTelegram() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        shareIntent.setPackage("org.telegram.messenger");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Telegram ইনস্টল করা নেই", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "আয় করার জন্য রেফারেল কোড");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        try {
            startActivity(emailIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "কোনো ইমেল অ্যাপ্লিকেশন পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareViaSMS() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("sms_body", "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        try {
            startActivity(smsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "SMS অ্যাপ্লিকেশন পাওয়া যায়নি", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareReferralLink() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "আমার রেফারেল কোড ব্যবহার করে আয় করুন: " + tvReferralCode.getText().toString() +
                "\nলিঙ্ক: " + tvReferralLink.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"));
    }
}