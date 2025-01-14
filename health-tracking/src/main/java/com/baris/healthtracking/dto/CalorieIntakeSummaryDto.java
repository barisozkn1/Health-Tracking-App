package com.baris.healthtracking.dto;

import lombok.Data;

@Data
public class CalorieIntakeSummaryDto {
    private double weeklyTotal;
    private double monthlyTotal;
    private double yearlyTotal;
    private double goalAchievementRate;
}
