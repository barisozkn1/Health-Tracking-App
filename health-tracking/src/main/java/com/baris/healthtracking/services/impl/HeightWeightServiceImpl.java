package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.HeightWeightInputDto;
import com.baris.healthtracking.dto.HeightWeightOutputDto;
import com.baris.healthtracking.dto.HeightWeightSummaryDto;
import com.baris.healthtracking.entites.HeightWeight;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.HeightWeightRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IHeightWeightService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HeightWeightServiceImpl implements IHeightWeightService {

    @Autowired
    private HeightWeightRepository heightWeightRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public HeightWeightOutputDto createHeightWeightRecord(HeightWeightInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        HeightWeight heightWeight = new HeightWeight();
        BeanUtils.copyProperties(inputDto, heightWeight);
        heightWeight.setUser(userOptional.get());

        HeightWeight savedHeightWeight = heightWeightRepository.save(heightWeight);

        HeightWeightOutputDto outputDto = new HeightWeightOutputDto();
        BeanUtils.copyProperties(savedHeightWeight, outputDto);

        return outputDto;
    }
    @Override
    public HeightWeightOutputDto updateHeightWeightRecord(Long id, HeightWeightInputDto inputDto) {
        HeightWeight heightWeight = heightWeightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Height and weight record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        heightWeight.setRecordDate(inputDto.getRecordDate());
        heightWeight.setHeight(inputDto.getHeight());
        heightWeight.setWeight(inputDto.getWeight());
        heightWeight.setUser(userOptional.get());

        HeightWeight updatedHeightWeight = heightWeightRepository.save(heightWeight);

        HeightWeightOutputDto outputDto = new HeightWeightOutputDto();
        BeanUtils.copyProperties(updatedHeightWeight, outputDto);

        return outputDto;
    }


    @Override
    public List<HeightWeightOutputDto> getHeightWeightRecordsByUser(Long userId) {
        List<HeightWeight> heightWeights = heightWeightRepository.findByUserId(userId);
        List<HeightWeightOutputDto> outputDtos = new ArrayList<>();

        for (HeightWeight heightWeight : heightWeights) {
            HeightWeightOutputDto outputDto = new HeightWeightOutputDto();
            BeanUtils.copyProperties(heightWeight, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteHeightWeightRecord(Long heightWeightId, Long userId) {
        int deletedRows = heightWeightRepository.deleteByHeightWeightIdAndUserId(heightWeightId, userId);
        if (deletedRows == 0) {
            throw new RuntimeException("Height-weight record not found or you are not authorized to delete this record.");
        }
    }

    //ek islevler bmi vs vs
    
    private double calculateBMI(double height, double weight) {
        if (height <= 0 || weight <= 0) {
            throw new IllegalArgumentException("Height and weight must be positive values.");
        }
        return weight / (height * height); // Boy metre cinsinden alınmalı
    }

    private String categorizeBMI(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal weight";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    private double calculateIdealWeight(double height, double bmi) {
        return bmi * (height * height); // Boy metre cinsinden
    }

    @Override
    public HeightWeightSummaryDto getHeightWeightSummary(Long userId) {
        List<HeightWeight> records = heightWeightRepository.findByUserId(userId);

        if (records.isEmpty()) {
            throw new RuntimeException("No height-weight records found for user with ID: " + userId);
        }

        // En son height-weight kaydını al
        HeightWeight latestRecord = records.get(records.size() - 1);

        double heightInMeters = latestRecord.getHeight() / 100.0; // cm'den metreye dönüştür
        double bmi = calculateBMI(heightInMeters, latestRecord.getWeight());
        String bmiCategory = categorizeBMI(bmi);

        double idealWeightLow = calculateIdealWeight(heightInMeters, 18.5); // Alt sınır
        double idealWeightHigh = calculateIdealWeight(heightInMeters, 24.9); // Üst sınır

        // Özet DTO oluştur
        HeightWeightSummaryDto summaryDto = new HeightWeightSummaryDto();
        summaryDto.setBmi(bmi);
        summaryDto.setBmiCategory(bmiCategory);
        summaryDto.setIdealWeightLow(idealWeightLow);
        summaryDto.setIdealWeightHigh(idealWeightHigh);

        return summaryDto;
    }

}
