package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class HeightWeightInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Height cannot be null!")
    @Min(value = 50, message = "Height must be at least 50 cm!")
    private Double height;

    @NotNull(message = "Weight cannot be null!")
    @Min(value = 1, message = "Weight must be at least 1 kg!")
    private Double weight;
}
