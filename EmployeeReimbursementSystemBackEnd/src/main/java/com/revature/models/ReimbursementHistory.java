package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Entity
@Table(name = "reimbursement_history")
public class ReimbursementHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reimbId")
    @JsonIgnoreProperties("user")
    private Reimbursement reimbursement;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private String updatedBy;

    private String fieldChanged;

    private String newValue;

    private String oldValue;

    public ReimbursementHistory() {
    }

    public ReimbursementHistory(int historyId, Reimbursement reimbursement, LocalDateTime timestamp, String updatedBy, String fieldChanged, String newValue, String oldValue) {
        this.historyId = historyId;
        this.reimbursement = reimbursement;
        this.timestamp = timestamp;
        this.updatedBy = updatedBy;
        this.fieldChanged = fieldChanged;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public Reimbursement getReimbursement() {
        return reimbursement;
    }

    public void setReimbursement(Reimbursement reimbursement) {
        this.reimbursement = reimbursement;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFieldChanged() {
        return fieldChanged;
    }

    public void setFieldChanged(String fieldChanged) {
        this.fieldChanged = fieldChanged;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    @Override
    public String toString() {
        return "ReimbursementHistoryRepository{" +
                "historyId=" + historyId +
                ", reimbursement=" + reimbursement.getReimbId() +
                ", timestamp=" + timestamp +
                ", updatedBy=" + updatedBy +
                ", fieldChanged='" + fieldChanged + '\'' +
                ", newValue='" + newValue + '\'' +
                ", oldValue='" + oldValue + '\'' +
                '}';
    }
}
