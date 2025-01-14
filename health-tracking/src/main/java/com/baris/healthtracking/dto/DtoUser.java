package com.baris.healthtracking.dto;

import com.baris.healthtracking.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class DtoUser {

    private Long id;
    private String userName;
    private String email;
    private Gender gender;

    // Modular relations
    private List<WaterIntakeOutputDto> waterIntakes;
    private List<CalorieIntakeOutputDto> calorieIntakes;
    private List<HeartRateOutputDto> heartRates;
    private List<HeightWeightOutputDto> heightWeights;
    private List<SleepOutputDto> sleeps;
    private List<StepCountOutputDto> stepCounts;
}
