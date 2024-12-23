package com.revature.models.DTOs;

public class ResolveReimbursementDTO {


    private String status;

    public ResolveReimbursementDTO() {
    }

    public ResolveReimbursementDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResolveReimbursementDTO{" +
                "status='" + status + '\'' +
                '}';
    }
}
