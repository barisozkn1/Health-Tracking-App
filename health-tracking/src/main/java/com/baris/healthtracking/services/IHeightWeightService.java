package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.HeightWeightInputDto;
import com.baris.healthtracking.dto.HeightWeightOutputDto;
import com.baris.healthtracking.dto.HeightWeightSummaryDto;

import java.util.List;

public interface IHeightWeightService {
    HeightWeightOutputDto createHeightWeightRecord(HeightWeightInputDto inputDto);
    List<HeightWeightOutputDto> getHeightWeightRecordsByUser(Long userId);
    //void deleteHeightWeightRecord(Long id);
    public HeightWeightOutputDto updateHeightWeightRecord(Long id, HeightWeightInputDto inputDto);
    
    HeightWeightSummaryDto getHeightWeightSummary(Long userId); // Yeni özet metodu
    
    public void deleteHeightWeightRecord(Long heightWeightId, Long userId);
}
