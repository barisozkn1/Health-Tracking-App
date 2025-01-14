package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.HeartRateInputDto;
import com.baris.healthtracking.dto.HeartRateOutputDto;
import com.baris.healthtracking.dto.HeartRateSummaryDto;
import com.baris.healthtracking.entites.HeartRate;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.HeartRateRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IHeartRateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HeartRateServiceImpl implements IHeartRateService {

    @Autowired
    private HeartRateRepository heartRateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public HeartRateOutputDto createHeartRateRecord(HeartRateInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        HeartRate heartRate = new HeartRate();
        BeanUtils.copyProperties(inputDto, heartRate);
        heartRate.setUser(userOptional.get());

        HeartRate savedHeartRate = heartRateRepository.save(heartRate);

        HeartRateOutputDto outputDto = new HeartRateOutputDto();
        BeanUtils.copyProperties(savedHeartRate, outputDto);

        return outputDto;
    }
    @Override
    public HeartRateOutputDto updateHeartRateRecord(Long id, HeartRateInputDto inputDto) {
        HeartRate heartRate = heartRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Heart rate record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        heartRate.setRecordDate(inputDto.getRecordDate());
        heartRate.setHeartRate(inputDto.getHeartRate());
        heartRate.setUser(userOptional.get());

        HeartRate updatedHeartRate = heartRateRepository.save(heartRate);

        HeartRateOutputDto outputDto = new HeartRateOutputDto();
        BeanUtils.copyProperties(updatedHeartRate, outputDto);

        return outputDto;
    }


    @Override
    public List<HeartRateOutputDto> getHeartRateRecordsByUser(Long userId) {
        List<HeartRate> heartRates = heartRateRepository.findByUserId(userId);
        List<HeartRateOutputDto> outputDtos = new ArrayList<>();

        for (HeartRate heartRate : heartRates) {
            HeartRateOutputDto outputDto = new HeartRateOutputDto();
            BeanUtils.copyProperties(heartRate, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteHeartRateRecord(Long heartRateId, Long userId) {
        int deletedRows = heartRateRepository.deleteByHeartRateIdAndUserId(heartRateId, userId);
        if (deletedRows == 0) {
            throw new RuntimeException("Heart rate record not found or you are not authorized to delete this record.");
        }
    }

    //kalp atısı ile alakalı ekstra işlevler
    
    private double calculateAverageHeartRate(List<HeartRate> heartRates) {
        return heartRates.stream()
                         .mapToDouble(HeartRate::getHeartRate)
                         .average()
                         .orElse(0.0);
    }

    private double calculateHeartRateVariability(List<HeartRate> heartRates) {
        double average = calculateAverageHeartRate(heartRates);
        return Math.sqrt(heartRates.stream()
                                   .mapToDouble(rate -> Math.pow(rate.getHeartRate() - average, 2))
                                   .average()
                                   .orElse(0.0));
    }

    private double calculateMaximumHeartRate(int age) {
        return 220 - age; // Yaşa göre maksimum kalp atış hızı (genel formül)
    }

    private String evaluateHeartRateForExercise(double heartRate, double maxHeartRate) {
        double lowerBound = maxHeartRate * 0.5;
        double upperBound = maxHeartRate * 0.85;

        if (heartRate < lowerBound) return "Low for exercise";
        if (heartRate > upperBound) return "High for exercise";
        return "Optimal for exercise";
    }
    
    @Override
    public HeartRateSummaryDto getHeartRateSummary(Long userId, int age) {
        List<HeartRate> heartRates = heartRateRepository.findByUserId(userId);

        if (heartRates.isEmpty()) {
            throw new RuntimeException("No heart rate records found for user with ID: " + userId);
        }

        double averageHeartRate = calculateAverageHeartRate(heartRates);
        double hrv = calculateHeartRateVariability(heartRates);
        double maxHeartRate = calculateMaximumHeartRate(age);

        // En son kayıt için egzersiz analizi yap
        HeartRate latestHeartRate = heartRates.get(heartRates.size() - 1);
        String exerciseAnalysis = evaluateHeartRateForExercise(latestHeartRate.getHeartRate(), maxHeartRate);

        HeartRateSummaryDto summaryDto = new HeartRateSummaryDto();
        summaryDto.setAverageHeartRate(averageHeartRate);
        summaryDto.setHrv(hrv);
        summaryDto.setExerciseAnalysis(exerciseAnalysis);

        return summaryDto;
    }


}
