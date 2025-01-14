package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class StepCountInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Step count cannot be null!")
    @Min(value = 0, message = "Step count cannot be negative!")
    private Integer stepCount;

    @NotNull(message = "Daily step target cannot be null!")
    @Min(value = 0, message = "Daily step target cannot be negative!")
    private Integer target;
}
