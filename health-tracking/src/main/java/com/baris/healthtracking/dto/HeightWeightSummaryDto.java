package com.baris.healthtracking.dto;

import lombok.Data;

@Data
public class HeightWeightSummaryDto {
    private double bmi; // Vücut kitle indeksi
    private String bmiCategory; // BMI kategorisi
    private double idealWeightLow; // İdeal kilo alt sınırı
    private double idealWeightHigh; // İdeal kilo üst sınırı
}
