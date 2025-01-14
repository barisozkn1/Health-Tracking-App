package com.baris.healthtracking.dto;

import lombok.Data;

@Data
public class HeartRateSummaryDto {
    private double averageHeartRate; // Ortalama kalp atış hızı
    private double hrv; // Kalp atış hızı değişkenliği
    private String exerciseAnalysis; // Egzersiz analizi (optimal, düşük, yüksek)
}
