package com.sadik.earntask.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.sadik.earntask.Adapter.ScreenshotAdapter;
import com.sadik.earntask.R;

import java.util.ArrayList;

public class TaskDetailsFragment extends Fragment {

    private static final int PICK_IMAGES = 101;

    private TextView tvTaskName, tvTaskReward, tvRequirements;
    private ImageView btnBack;
    private EditText etUserNote;
    private MaterialCardView cardScreenshot;
    private MaterialButton btnSubmitTask;
    private RecyclerView rvScreenshots;

    private ArrayList<Uri> screenshotUris = new ArrayList<>();
    private ScreenshotAdapter screenshotAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_details, container, false);

        tvTaskName = view.findViewById(R.id.tvTaskName);
        tvTaskReward = view.findViewById(R.id.tvTaskReward);
        tvRequirements = view.findViewById(R.id.tvRequirements);
        btnBack = view.findViewById(R.id.btnBack);
        etUserNote = view.findViewById(R.id.etUserNote);
        cardScreenshot = view.findViewById(R.id.cardScreenshot);
        btnSubmitTask = view.findViewById(R.id.btnSubmitTask);
        rvScreenshots = new RecyclerView(requireContext());

        // RecyclerView for screenshots (inside cardScreenshot)
        rvScreenshots = view.findViewById(R.id.rvScreenshots);
        rvScreenshots.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        screenshotAdapter = new ScreenshotAdapter(screenshotUris);
        rvScreenshots.setAdapter(screenshotAdapter);

        // 🔹 Back button
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        // 🔹 Load arguments
        if (getArguments() != null) {
            tvTaskName.setText(getArguments().getString("name"));
            tvTaskReward.setText(getArguments().getString("reward"));
            tvRequirements.setText(getArguments().getString("requirements"));

            // restore previous screenshots if exists
            ArrayList<String> list = getArguments().getStringArrayList("screenshots");
            if (list != null) {
                for (String s : list) screenshotUris.add(Uri.parse(s));
            }

            // restore user note
            etUserNote.setText(getArguments().getString("user_note", ""));
        }

        // 🔹 Screenshot click (open gallery)
        cardScreenshot.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_IMAGES);
        });

        // 🔹 Submit button
        btnSubmitTask.setOnClickListener(v -> {
            if (screenshotUris.isEmpty()) {
                Toast.makeText(requireContext(), "Please upload at least one screenshot", Toast.LENGTH_SHORT).show();
                return;
            }

            String userNote = etUserNote.getText().toString().trim();

            // TODO: API call or save locally
            Toast.makeText(requireContext(),
                    "Task submitted with " + screenshotUris.size() + " screenshots",
                    Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    screenshotUris.add(uri);
                }
            } else if (data.getData() != null) {
                screenshotUris.add(data.getData());
            }

            screenshotAdapter.notifyDataSetChanged();
        }
    }
}
