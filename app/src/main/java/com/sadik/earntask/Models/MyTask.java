package com.sadik.earntask.Models;
public class MyTask {

    private String taskName;
    private String status; // Pending, Approved, Rejected
    private int reward;
    private String date;
    private String rejectReason;

    public MyTask(String taskName, String status, int reward, String date, String rejectReason) {
        this.taskName = taskName;
        this.status = status;
        this.reward = reward;
        this.date = date;
        this.rejectReason = rejectReason;
    }

    public String getTaskName() { return taskName; }
    public String getStatus() { return status; }
    public int getReward() { return reward; }
    public String getDate() { return date; }
    public String getRejectReason() { return rejectReason; }
}
