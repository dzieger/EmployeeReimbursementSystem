package com.revature.models.DTOs;

public class IncomingPasswordChangeDTO {

    private String oldPassword;
    private String newPassword;

    public IncomingPasswordChangeDTO() {
    }

    public IncomingPasswordChangeDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
