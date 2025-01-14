package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.StepCountInputDto;
import com.baris.healthtracking.dto.StepCountOutputDto;
import com.baris.healthtracking.dto.StepCountSummaryDto;

import java.util.List;

public interface IStepCountService {
    StepCountOutputDto createStepCount(StepCountInputDto inputDto);
    List<StepCountOutputDto> getStepCountsByUser(Long userId);
    //void deleteStepCount(Long id);
    public StepCountOutputDto updateStepCount(Long id, StepCountInputDto inputDto);
    
    public StepCountSummaryDto getStepCountSummary(Long userId);
    
    public void deleteStepCount(Long stepCountId, Long userId);
}
