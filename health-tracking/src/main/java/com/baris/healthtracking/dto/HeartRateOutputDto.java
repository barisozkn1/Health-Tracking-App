package com.baris.healthtracking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HeartRateOutputDto {

    private Long id;
    private Date recordDate;
    private Integer heartRate;
}
