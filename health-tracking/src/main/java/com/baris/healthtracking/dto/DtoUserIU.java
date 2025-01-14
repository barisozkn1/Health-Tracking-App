package com.baris.healthtracking.dto;

import com.baris.healthtracking.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DtoUserIU {

    @NotEmpty(message = "Username cannot be empty!")
    private String userName;

    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Email cannot be empty!")
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    private String password;

    @NotNull(message = "Gender cannot be null!")
    private Gender gender;

    // Modular relations
    private List<WaterIntakeInputDto> waterIntakes;
    private List<CalorieIntakeInputDto> calorieIntakes;
    private List<HeartRateInputDto> heartRates;
    private List<HeightWeightInputDto> heightWeights;
    private List<SleepInputDto> sleeps;
    private List<StepCountInputDto> stepCounts;
}
