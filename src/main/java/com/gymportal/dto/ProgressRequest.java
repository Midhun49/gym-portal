package com.gymportal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProgressRequest {
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @NotNull(message = "Calories consumed is required")
    @Min(value = 0, message = "Calories cannot be negative")
    private Integer caloriesConsumed;

    @NotNull(message = "Water intake is required")
    @Min(value = 0, message = "Water intake cannot be negative")
    private Integer waterIntakeMl;

    private String notes;
}
