package com.gymportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminProfileRequest {
    @NotBlank(message = "New username is required")
    private String newUsername;
    
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    
    @NotBlank(message = "New password is required")
    private String newPassword;
}
