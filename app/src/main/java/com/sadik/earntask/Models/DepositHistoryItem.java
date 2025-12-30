package com.sadik.earntask.Models;

public class DepositHistoryItem {
    private double amount;
    private String fromNumber;
    private String method;
    private String transactionId;
    private String status;
    private String timestamp;

    public DepositHistoryItem(double amount, String fromNumber, String method,
                              String transactionId, String status, String timestamp) {
        this.amount = amount;
        this.fromNumber = fromNumber;
        this.method = method;
        this.transactionId = transactionId;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters
    public double getAmount() { return amount; }
    public String getFromNumber() { return fromNumber; }
    public String getMethod() { return method; }
    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
}