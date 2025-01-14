package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.SleepInputDto;
import com.baris.healthtracking.dto.SleepOutputDto;
import com.baris.healthtracking.dto.SleepSummaryDto;

import java.util.List;

public interface ISleepService {
    SleepOutputDto createSleepRecord(SleepInputDto inputDto);
    List<SleepOutputDto> getSleepRecordsByUser(Long userId);
   // void deleteSleepRecord(Long id);
    public SleepOutputDto updateSleepRecord(Long id, SleepInputDto inputDto);
    
    public SleepSummaryDto getSleepSummary(Long userId);
    
    public void deleteSleepRecord(Long sleepId, Long userId);
}
