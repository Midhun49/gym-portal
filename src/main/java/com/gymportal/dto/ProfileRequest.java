package com.gymportal.dto;

import com.gymportal.entity.MemberProfile;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProfileRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;

    @NotNull(message = "Gender is required")
    private MemberProfile.Gender gender;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double heightCm;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @NotNull(message = "Fitness goal is required")
    private MemberProfile.FitnessGoal fitnessGoal;

    @NotNull(message = "Activity level is required")
    private MemberProfile.ActivityLevel activityLevel;

    @NotNull(message = "Diet type is required")
    private MemberProfile.DietType dietType;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;
}
