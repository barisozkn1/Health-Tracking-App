package com.baris.healthtracking.dto;

import lombok.Data;

@Data
public class WaterIntakeSummaryDto {
    private double weeklyTotal; // Haftalık toplam tüketim
    private double monthlyTotal; // Aylık toplam tüketim
    private double yearlyTotal; // Yıllık toplam tüketim
    private double goalAchievementRate; // Hedefe ulaşma oranı (%)
}
