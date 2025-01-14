package com.baris.healthtracking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WaterIntakeOutputDto {

    private Long id;
    private Date recordDate;
    private Double amount;
    private Double target;
}
