package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.CalorieIntakeInputDto;
import com.baris.healthtracking.dto.CalorieIntakeOutputDto;
import com.baris.healthtracking.dto.CalorieIntakeSummaryDto;

import java.util.List;

public interface ICalorieIntakeService {
    CalorieIntakeOutputDto createCalorieIntake(CalorieIntakeInputDto inputDto);
    List<CalorieIntakeOutputDto> getCalorieIntakeByUser(Long userId);
    public CalorieIntakeOutputDto updateCalorieIntake(Long id, CalorieIntakeInputDto inputDto);
    
    public CalorieIntakeSummaryDto getCalorieIntakeSummary(Long userId);
    
    public void deleteCalorieIntake(Long calorieId, Long userId);
}
