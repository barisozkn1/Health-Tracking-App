package com.baris.healthtracking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StepCountOutputDto {

    private Long id;
    private Date recordDate;
    private Integer stepCount;
    private Integer target;
}
