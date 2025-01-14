package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.HeartRateInputDto;
import com.baris.healthtracking.dto.HeartRateOutputDto;
import com.baris.healthtracking.dto.HeartRateSummaryDto;

import java.util.List;

public interface IHeartRateService {
    HeartRateOutputDto createHeartRateRecord(HeartRateInputDto inputDto);
    List<HeartRateOutputDto> getHeartRateRecordsByUser(Long userId);
    //void deleteHeartRateRecord(Long id);
    public HeartRateOutputDto updateHeartRateRecord(Long id, HeartRateInputDto inputDto);
    
    HeartRateSummaryDto getHeartRateSummary(Long userId, int age); // Yeni özet metodu
    
    public void deleteHeartRateRecord(Long heartRateId, Long userId);
}
