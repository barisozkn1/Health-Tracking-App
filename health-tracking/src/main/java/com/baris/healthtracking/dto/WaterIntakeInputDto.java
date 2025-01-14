package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class WaterIntakeInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Water intake amount cannot be null!")
    @Min(value = 0, message = "Water intake cannot be negative!")
    private Double amount;

    @NotNull(message = "Daily water target cannot be null!")
    @Min(value = 0, message = "Daily water target cannot be negative!")
    private Double target;
}
