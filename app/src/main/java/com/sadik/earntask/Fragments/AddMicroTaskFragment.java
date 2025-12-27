package com.sadik.earntask.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.sadik.earntask.R;

public class AddMicroTaskFragment extends Fragment {

    private EditText etTaskName, etReward, etDescription, etUrl;
    private ImageView btnClose;
    private MaterialButton btnAddTask;

    private OnTaskAddedListener listener;

    public AddMicroTaskFragment() {
        // Required empty constructor
    }

    // Optional: set listener from parent fragment/activity
    public void setOnTaskAddedListener(OnTaskAddedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_micro_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init views
        etTaskName = view.findViewById(R.id.etTaskName);
        etReward = view.findViewById(R.id.etReward);
        etDescription = view.findViewById(R.id.etDescription);
        etUrl = view.findViewById(R.id.etUrl);

        btnClose = view.findViewById(R.id.btnClose);
        btnAddTask = view.findViewById(R.id.btnAddTask);

        // close dialog / fragment
        btnClose.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // add task
        btnAddTask.setOnClickListener(v -> addTask());
    }

    private void addTask() {
        String name = etTaskName.getText().toString().trim();
        String rewardStr = etReward.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String url = etUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etTaskName.setError("Task name required");
            return;
        }
        if (TextUtils.isEmpty(rewardStr)) {
            etReward.setError("Reward required");
            return;
        }

        int reward = 0;
        try {
            reward = Integer.parseInt(rewardStr);
        } catch (NumberFormatException e) {
            etReward.setError("Invalid number");
            return;
        }

        // Optionally create a model object
        MicroTask newTask = new MicroTask(name, reward, desc, url);

        // send back to listener
        if (listener != null) {
            listener.onTaskAdded(newTask);
        }

        Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show();

        // close fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    // ------------------------------
    // MicroTask model
    // ------------------------------
    public static class MicroTask {
        public String taskName;
        public int reward;
        public String description;
        public String url;

        public MicroTask(String taskName, int reward, String description, String url) {
            this.taskName = taskName;
            this.reward = reward;
            this.description = description;
            this.url = url;
        }
    }

    // ------------------------------
    // Callback interface
    // ------------------------------
    public interface OnTaskAddedListener {
        void onTaskAdded(MicroTask task);
    }
}
