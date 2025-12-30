package com.sadik.earntask.Models;

public class PaymentHistoryModel {

    private String amount;
    private String method;
    private String txnId;
    private String userNumber;
    private String status;

    public PaymentHistoryModel(String amount, String method,
                               String txnId, String userNumber, String status) {
        this.amount = amount;
        this.method = method;
        this.txnId = txnId;
        this.userNumber = userNumber;
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getTxnId() {
        return txnId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getStatus() {
        return status;
    }
}

