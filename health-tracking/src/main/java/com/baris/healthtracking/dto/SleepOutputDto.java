package com.baris.healthtracking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SleepOutputDto {

    private Long id;
    private Date recordDate;
    private Double duration;
    private Double target;
}
