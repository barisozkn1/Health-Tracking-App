package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.WaterIntakeInputDto;
import com.baris.healthtracking.dto.WaterIntakeOutputDto;
import com.baris.healthtracking.dto.WaterIntakeSummaryDto;

import java.util.List;
public interface IWaterIntakeService {
    WaterIntakeOutputDto createWaterIntake(WaterIntakeInputDto inputDto);
    List<WaterIntakeOutputDto> getWaterIntakeByUser(Long userId);
    //void deleteWaterIntake(Long id);
    public WaterIntakeOutputDto updateWaterIntake(Long id, WaterIntakeInputDto inputDto);

    public WaterIntakeSummaryDto getWaterIntakeSummary(Long userId);
    
    public void deleteWaterIntake(Long id, Long userId);
}
