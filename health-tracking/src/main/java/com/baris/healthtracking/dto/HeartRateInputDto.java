package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class HeartRateInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Heart rate cannot be null!")
    @Min(value = 0, message = "Heart rate cannot be negative!")
    private Integer heartRate;
}
