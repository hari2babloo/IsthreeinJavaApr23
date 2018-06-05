package com.example.hari.isthreeinjava.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hari on 13/4/18.
 */

public class WalletHistorymodel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jobId")
    @Expose
    private String jobId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount_payable")
    @Expose
    private String amountPayable;
    @SerializedName("transaction_amount")
    @Expose
    private String transactionAmount;
    @SerializedName("balance_amount_to_pay")
    @Expose
    private String balanceAmountToPay;
    @SerializedName("balanceAddedToWallet")
    @Expose
    private String balanceAddedToWallet;
    @SerializedName("transaction_time")
    @Expose
    private String transactionTime;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("customerId")
    @Expose
    private String customerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(String amountPayable) {
        this.amountPayable = amountPayable;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getBalanceAmountToPay() {
        return balanceAmountToPay;
    }

    public void setBalanceAmountToPay(String balanceAmountToPay) {
        this.balanceAmountToPay = balanceAmountToPay;
    }

    public String getBalanceAddedToWallet() {
        return balanceAddedToWallet;
    }

    public void setBalanceAddedToWallet(String balanceAddedToWallet) {
        this.balanceAddedToWallet = balanceAddedToWallet;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
