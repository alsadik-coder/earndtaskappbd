package com.sadik.earntask.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rv_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<HomeTask> sampleTasks = new ArrayList<>();
        sampleTasks.add(new HomeTask("Watch 3 videos", "Finish before 5 PM", "৳12", R.drawable.ic_video));
        sampleTasks.add(new HomeTask("Join Telegram", "Stay for 10 minutes", "৳20", R.drawable.ic_users));
        sampleTasks.add(new HomeTask("Install app", "Rate after install", "৳35", R.drawable.ic_star));
        sampleTasks.add(new HomeTask("Survey form", "5 short questions", "৳15", R.drawable.ic_clipboard));
        sampleTasks.add(new HomeTask("Daily login", "Claim your streak", "৳8", R.drawable.ic_calendar));

        recyclerView.setAdapter(new HomeTaskAdapter(sampleTasks));
    }

    // -------------------------------
    // Model class
    // -------------------------------
    static class HomeTask {
        String title;
        String description;
        String reward;
        int iconRes;

        HomeTask(String title, String description, String reward, int iconRes) {
            this.title = title;
            this.description = description;
            this.reward = reward;
            this.iconRes = iconRes;
        }
    }

    // -------------------------------
    // Adapter
    // -------------------------------
    static class HomeTaskAdapter extends RecyclerView.Adapter<HomeTaskAdapter.TaskViewHolder> {

        private final List<HomeTask> items;

        HomeTaskAdapter(List<HomeTask> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        // -------------------------------
        // ViewHolder
        // -------------------------------
        static class TaskViewHolder extends RecyclerView.ViewHolder {

            ImageView icon;
            TextView title, description, reward;

            TaskViewHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.iv_task_icon);
                title = itemView.findViewById(R.id.tv_task_title);
                description = itemView.findViewById(R.id.tv_task_description);
                reward = itemView.findViewById(R.id.tv_task_reward);
            }

            void bind(HomeTask task) {
                icon.setImageResource(task.iconRes);
                title.setText(task.title);
                description.setText(task.description);
                reward.setText(task.reward);
            }
        }
    }
}