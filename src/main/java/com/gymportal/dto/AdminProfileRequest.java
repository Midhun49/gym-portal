package com.gymportal.dto;

import lombok.Data;

@Data
public class AdminProfileRequest {
    private String newUsername;
    private String currentPassword;
    private String newPassword;
}
