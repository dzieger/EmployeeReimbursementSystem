package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Entity
@Table(name = "reimbursements")
public class Reimbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reimbId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties({"username", "password", "reimbursements"})
    private User user;

    @OneToMany(mappedBy = "reimbursement", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("reimbursement")
    private List<ReimbursementHistory> reimbursementHistory;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    public Reimbursement() {
    }

    public Reimbursement(int reimbId, String description, double amount, String status, User user, LocalDateTime dateCreated) {
        this.reimbId = reimbId;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.user = user;
        this.dateCreated = dateCreated;
    }

    public Reimbursement(int reimbId, String description, double amount, String status, User user, List<ReimbursementHistory> reimbursementHistory) {
        this.reimbId = reimbId;
        this.description = description;
        this.amount = amount;
        this.status = status;
        this.user = user;
        this.reimbursementHistory = reimbursementHistory;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ReimbursementHistory> getReimbursementHistory() {
        return reimbursementHistory;
    }

    public void setReimbursementHistory(List<ReimbursementHistory> reimbursementHistory) {
        this.reimbursementHistory = reimbursementHistory;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "reimbId=" + reimbId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }
}
