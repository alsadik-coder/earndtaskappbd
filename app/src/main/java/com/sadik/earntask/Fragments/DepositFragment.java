package com.sadik.earntask.Fragments;

import android.os.Bundle;
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

import com.sadik.earntask.ApiClient;
import com.sadik.earntask.ApiInterface;
import com.sadik.earntask.PrefManager;
import com.sadik.earntask.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositFragment extends Fragment {
    // View references
    private TextView tvTitle;
    private ImageView btnBack;
    private ImageView btnHistory;
    private TextView tvBalance;
    private EditText etAmount;
    private EditText etFromNumber;
    private EditText etMethod;
    private EditText etTransactionId;
    private Button btnDeposit;
    private CardView cardPaymentSummary;

    // TODO: Replace with actual data from MySQL when team member implements it
    private double currentBalance = 1250.0;

    ApiInterface api; PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);

        // Initialize all views with findViewById
        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupClickListeners();
        loadUserData();

        api= ApiClient.getClient().create(ApiInterface.class);
        pref=new PrefManager(requireContext());
        loadBalance();
        setupClickListeners();
    }

    private void initViews(View view) {
        // Toolbar views
        tvTitle = view.findViewById(R.id.tvTitle);
        btnBack = view.findViewById(R.id.btnBack);
        btnHistory = view.findViewById(R.id.btnHistory);

        // Balance views
        tvBalance = view.findViewById(R.id.tvBalance);

        // Form views
        etAmount = view.findViewById(R.id.etAmount);
        etFromNumber = view.findViewById(R.id.etFromNumber);
        etMethod = view.findViewById(R.id.etMethod);
        etTransactionId = view.findViewById(R.id.etTransactionId);
        btnDeposit = view.findViewById(R.id.btnDeposit);

        // Payment summary
        cardPaymentSummary = view.findViewById(R.id.cardPaymentSummary);

        // Initialize UI elements
        tvTitle.setText("Deposit");
        displayBalance();
    }

    private void loadUserData() {
        // TODO: Team member will replace this with MySQL data fetching
        // For now, using hardcoded value
        displayBalance();

        // Hide payment summary initially
        cardPaymentSummary.setVisibility(View.GONE);
    }

    private void displayBalance() {
        tvBalance.setText("৳ " + String.format("%.2f", currentBalance));
    }

    private void setupClickListeners() {
        // Back button functionality
        btnBack.setOnClickListener(v -> {
            // Navigate back to previous screen
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // History button functionality
        btnHistory.setOnClickListener(v -> {
            // Navigate to deposit history
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new DepositHistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Deposit button functionality
        btnDeposit.setOnClickListener(v -> {
            if (validateInput()) {
                processDeposit();
            }
        });
    }

    private boolean validateInput() {
        String amount = etAmount.getText().toString().trim();
        String fromNumber = etFromNumber.getText().toString().trim();
        String method = etMethod.getText().toString().trim();
        String transactionId = etTransactionId.getText().toString().trim();

        // Validate amount
        if (TextUtils.isEmpty(amount)) {
            etAmount.setError("Please enter amount");
            return false;
        }

        if (Double.parseDouble(amount) <= 0) {
            etAmount.setError("Amount must be greater than 0");
            return false;
        }

        // Validate phone number
        if (TextUtils.isEmpty(fromNumber)) {
            etFromNumber.setError("Please enter your number");
            return false;
        }

        if (fromNumber.length() != 11 || !fromNumber.startsWith("01")) {
            etFromNumber.setError("Please enter a valid 11-digit number starting with 01");
            return false;
        }

        // Validate payment method
        if (TextUtils.isEmpty(method)) {
            etMethod.setError("Please enter payment method");
            return false;
        }

        // Validate transaction ID
        if (TextUtils.isEmpty(transactionId)) {
            etTransactionId.setError("Please enter transaction ID");
            return false;
        }

        return true;
    }


    private void simulateDepositProcess(double amount, String fromNumber, String method, String transactionId) {
        // Simulate network delay
        btnDeposit.postDelayed(() -> {
            // TODO: Replace with actual MySQL update by team member
            // For now, updating local balance
            currentBalance += amount;
            displayBalance();

            // Show success message
            Toast.makeText(getContext(), "Deposit successful!", Toast.LENGTH_SHORT).show();

            // Reset form
            resetForm();

            // Show last payment details
            showLastPaymentDetails(amount, fromNumber, method, transactionId);

            // Reset button state
            btnDeposit.setEnabled(true);
            btnDeposit.setText("Proceed to Deposit");

            // TODO: Team member should save this data to MySQL
            // Pass this data to team member's MySQL operations:
            // amount, fromNumber, method, transactionId, timestamp, status
        }, 2000);
    }

    private void resetForm() {
        etAmount.setText("");
        etFromNumber.setText("");
        etMethod.setText("");
        etTransactionId.setText("");
    }

    private void showLastPaymentDetails(double amount, String fromNumber, String method, String transactionId) {
        // Show the payment summary card
        cardPaymentSummary.setVisibility(View.VISIBLE);

        // TODO: Update these TextViews with IDs in XML
        // For now, this is a placeholder
        // Team member can update this section when they implement MySQL operations
    }


    void loadBalance(){
        api.getBalance(pref.getUserId()).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r){
                try{
                    JSONObject o=new JSONObject(r.body().string());
                    tvBalance.setText("৳ "+o.getString("balance"));
                }catch(Exception e){}
            }
            public void onFailure(Call<ResponseBody> c,Throwable t){}
        });
    }

    void processDeposit(){
        String amount=etAmount.getText().toString().trim();
        String from=etFromNumber.getText().toString().trim();
        String method=etMethod.getText().toString().trim();
        String trx=etTransactionId.getText().toString().trim();

        btnDeposit.setEnabled(false);
        btnDeposit.setText("Processing...");

        api.deposit(pref.getUserId(),amount,from,method,trx).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> c,Response<ResponseBody> r){
                try{
                    JSONObject o=new JSONObject(r.body().string());
                    Toast.makeText(getContext(),o.getString("status").equals("success")?"Request Sent":o.getString("msg"),Toast.LENGTH_LONG).show();
                }catch(Exception e){}
                btnDeposit.setEnabled(true);
                btnDeposit.setText("Proceed to Deposit");
                resetForm();
            }
            public void onFailure(Call<ResponseBody> c, Throwable t){
                Toast.makeText(getContext(),"Network Error",Toast.LENGTH_LONG).show();
                btnDeposit.setEnabled(true);
                btnDeposit.setText("Proceed to Deposit");
            }
        });
    }

}