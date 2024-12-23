package com.revature.models.DTOs;

public class UpdateReimbursementDTO {
    private String description;

    public UpdateReimbursementDTO() {
    }

    public UpdateReimbursementDTO(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "UpdateReimbursementDTO{" +
                "description='" + description + '\'' +
                '}';
    }
}
