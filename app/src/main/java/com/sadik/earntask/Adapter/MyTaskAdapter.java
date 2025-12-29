package com.sadik.earntask.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sadik.earntask.Models.MyTask;
import com.sadik.earntask.R;

import java.util.List;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {

    private Context context;
    private List<MyTask> taskList;

    public MyTaskAdapter(Context context, List<MyTask> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_my_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyTask task = taskList.get(position);

        holder.tvTaskName.setText(task.getTaskName());
        holder.tvReward.setText("Reward: ৳" + task.getReward());
        holder.tvDate.setText("Submitted: " + task.getDate());

        // Status handling
        holder.tvStatus.setText(task.getStatus());

        if (task.getStatus().equalsIgnoreCase("Pending")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            holder.btnViewReason.setVisibility(View.GONE);

        } else if (task.getStatus().equalsIgnoreCase("Approved")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_approved);
            holder.btnViewReason.setVisibility(View.GONE);

        } else if (task.getStatus().equalsIgnoreCase("Rejected")) {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rejected);
            holder.btnViewReason.setVisibility(View.VISIBLE);

            holder.btnViewReason.setOnClickListener(v ->
                    Toast.makeText(context,
                            task.getRejectReason(),
                            Toast.LENGTH_LONG).show()
            );
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName, tvStatus, tvReward, tvDate, btnViewReason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tvTaskName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvReward = itemView.findViewById(R.id.tvReward);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnViewReason = itemView.findViewById(R.id.btnViewReason);
        }
    }
}
