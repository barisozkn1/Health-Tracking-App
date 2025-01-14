package com.baris.healthtracking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HeightWeightOutputDto {

    private Long id;
    private Date recordDate;
    private Double height;
    private Double weight;
}
