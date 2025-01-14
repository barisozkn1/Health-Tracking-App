package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CalorieIntakeInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Calorie intake amount cannot be null!")
    @Min(value = 0, message = "Calorie intake cannot be negative!")
    private Double amount;

    @NotNull(message = "Daily calorie target cannot be null!")
    @Min(value = 0, message = "Daily calorie target cannot be negative!")
    private Double target;
}
