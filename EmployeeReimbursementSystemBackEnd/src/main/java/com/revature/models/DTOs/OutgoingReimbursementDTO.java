package com.revature.models.DTOs;

import com.revature.models.ReimbursementHistory;
import com.revature.models.User;

import java.util.List;

public class OutgoingReimbursementDTO {

    private int reimbId;

    private String description;

    private double amount;

    private String status;

    private int userId;

    private List<ReimbursementHistory> reimbursementHistory;


    public OutgoingReimbursementDTO() {
    }

    public OutgoingReimbursementDTO(int reimbId, String description, double amount, String status, int userId, List<ReimbursementHistory> history) {
        this.reimbId = reimbId;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.userId = userId;
        this.reimbursementHistory = history;
    }

    public int getReimbId() {
        return reimbId;
    }

    public void setReimbId(int reimbId) {
        this.reimbId = reimbId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ReimbursementHistory> getHistory() {
        return reimbursementHistory;
    }

    public void setHistory(List<ReimbursementHistory> history) {
        this.reimbursementHistory = history;
    }

    @Override
    public String toString() {
        return "OutgoingReimbursementDTO{" +
                "reimbId=" + reimbId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
