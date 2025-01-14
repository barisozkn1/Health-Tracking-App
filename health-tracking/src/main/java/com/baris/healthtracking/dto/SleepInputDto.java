package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class SleepInputDto {

    @NotNull(message = "User ID cannot be null!")
    private Long userId;

    @NotNull(message = "Record date cannot be null!")
    private Date recordDate;

    @NotNull(message = "Sleep duration cannot be null!")
    @Min(value = 0, message = "Sleep duration cannot be negative!")
    private Double duration;

    @NotNull(message = "Daily sleep target cannot be null!")
    @Min(value = 0, message = "Daily sleep target cannot be negative!")
    private Double target;
}
