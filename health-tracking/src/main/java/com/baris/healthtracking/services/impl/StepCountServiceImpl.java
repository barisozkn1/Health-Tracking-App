package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.StepCountInputDto;
import com.baris.healthtracking.dto.StepCountOutputDto;
import com.baris.healthtracking.dto.StepCountSummaryDto;
import com.baris.healthtracking.entites.StepCount;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.StepCountRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IStepCountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StepCountServiceImpl implements IStepCountService {

    @Autowired
    private StepCountRepository stepCountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public StepCountOutputDto createStepCount(StepCountInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        StepCount stepCount = new StepCount();
        BeanUtils.copyProperties(inputDto, stepCount);
        stepCount.setUser(userOptional.get());

        StepCount savedStepCount = stepCountRepository.save(stepCount);

        StepCountOutputDto outputDto = new StepCountOutputDto();
        BeanUtils.copyProperties(savedStepCount, outputDto);

        return outputDto;
    }
    @Override
    public StepCountOutputDto updateStepCount(Long id, StepCountInputDto inputDto) {
        StepCount stepCount = stepCountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Step count record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        stepCount.setRecordDate(inputDto.getRecordDate());
        stepCount.setStepCount(inputDto.getStepCount());
        stepCount.setTarget(inputDto.getTarget());
        stepCount.setUser(userOptional.get());

        StepCount updatedStepCount = stepCountRepository.save(stepCount);

        StepCountOutputDto outputDto = new StepCountOutputDto();
        BeanUtils.copyProperties(updatedStepCount, outputDto);

        return outputDto;
    }


    @Override
    public List<StepCountOutputDto> getStepCountsByUser(Long userId) {
        List<StepCount> stepCounts = stepCountRepository.findByUserId(userId);
        List<StepCountOutputDto> outputDtos = new ArrayList<>();

        for (StepCount stepCount : stepCounts) {
            StepCountOutputDto outputDto = new StepCountOutputDto();
            BeanUtils.copyProperties(stepCount, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteStepCount(Long stepCountId, Long userId) {
        int deletedRows = stepCountRepository.deleteByStepCountIdAndUserId(stepCountId, userId);
        if (deletedRows == 0) {
            throw new RuntimeException("Step count record not found or you are not authorized to delete this record.");
        }
    }

    
    //grafik icin asagıda yer alıyor ek işlevler
    
    @Override
    public StepCountSummaryDto getStepCountSummary(Long userId) {
        List<StepCount> stepCounts = stepCountRepository.findByUserId(userId);

        double weeklyTotal = calculateTotalByTimeFrame(stepCounts, "WEEK");
        double monthlyTotal = calculateTotalByTimeFrame(stepCounts, "MONTH");
        double yearlyTotal = calculateTotalByTimeFrame(stepCounts, "YEAR");
        double goalAchievementRate = calculateGoalAchievementRate(stepCounts);

        StepCountSummaryDto summaryDto = new StepCountSummaryDto();
        summaryDto.setWeeklyTotal(weeklyTotal);
        summaryDto.setMonthlyTotal(monthlyTotal);
        summaryDto.setYearlyTotal(yearlyTotal);
        summaryDto.setGoalAchievementRate(goalAchievementRate);

        return summaryDto;
    }

    // Ortak yardımcı metotlar
    private double calculateTotalByTimeFrame(List<StepCount> stepCounts, String timeFrame) {
        Date now = new Date();
        return stepCounts.stream()
                .filter(count -> isWithinTimeFrame(count.getRecordDate(), now, timeFrame))
                .mapToDouble(StepCount::getStepCount)
                .sum();
    }

    private boolean isWithinTimeFrame(Date recordDate, Date now, String timeFrame) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        if (timeFrame.equals("WEEK")) {
            cal.add(Calendar.DAY_OF_YEAR, -7);
        } else if (timeFrame.equals("MONTH")) {
            cal.add(Calendar.MONTH, -1);
        } else if (timeFrame.equals("YEAR")) {
            cal.add(Calendar.YEAR, -1);
        }

        return recordDate.after(cal.getTime());
    }

    private double calculateGoalAchievementRate(List<StepCount> stepCounts) {
        double totalTarget = stepCounts.stream().mapToDouble(StepCount::getTarget).sum();
        double totalSteps = stepCounts.stream().mapToDouble(StepCount::getStepCount).sum();

        return totalTarget > 0 ? (totalSteps / totalTarget) * 100 : 0;
    }

}
