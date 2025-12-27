package com.sadik.earntask.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Fragments.TaskDetailsFragment;
import com.sadik.earntask.Models.MicroTask;
import com.sadik.earntask.R;

import java.util.List;

public class MicroTaskAdapter
        extends RecyclerView.Adapter<MicroTaskAdapter.TaskViewHolder> {

    private final List<MicroTask> taskList;
    private final Fragment parentFragment;

    public MicroTaskAdapter(List<MicroTask> taskList, Fragment parentFragment) {
        this.taskList = taskList;
        this.parentFragment = parentFragment;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_micro_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        MicroTask task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName, tvTaskShort, tvReward, btnDetails;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvTaskShort = itemView.findViewById(R.id.tvTaskShort);
            tvReward = itemView.findViewById(R.id.tvReward);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }

        void bind(MicroTask task) {
            tvTaskName.setText(task.name);
            tvTaskShort.setText(task.shortInfo);
            tvReward.setText(task.reward);

            // Details click → open TaskDetailsFragment
            btnDetails.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString("name", task.name);
                bundle.putString("reward", task.reward);
                bundle.putString("requirements", task.requirements);

                // Optional: pass screenshots count and user note
                bundle.putStringArrayList("screenshots",
                        task.screenshotUris != null
                                ? new java.util.ArrayList<>(task.screenshotUris)
                                : new java.util.ArrayList<>());
                bundle.putString("user_note", task.userNote != null ? task.userNote : "");

                Fragment fragment = new TaskDetailsFragment();
                fragment.setArguments(bundle);

                parentFragment.requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}
