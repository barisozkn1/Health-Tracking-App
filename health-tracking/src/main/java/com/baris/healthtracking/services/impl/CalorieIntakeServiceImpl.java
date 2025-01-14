package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.CalorieIntakeInputDto;
import com.baris.healthtracking.dto.CalorieIntakeOutputDto;
import com.baris.healthtracking.dto.CalorieIntakeSummaryDto;
import com.baris.healthtracking.entites.CalorieIntake;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.CalorieIntakeRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.ICalorieIntakeService;
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
public class CalorieIntakeServiceImpl implements ICalorieIntakeService {

    @Autowired
    private CalorieIntakeRepository calorieIntakeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CalorieIntakeOutputDto createCalorieIntake(CalorieIntakeInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        CalorieIntake calorieIntake = new CalorieIntake();
        BeanUtils.copyProperties(inputDto, calorieIntake);
        calorieIntake.setUser(userOptional.get());

        CalorieIntake savedCalorieIntake = calorieIntakeRepository.save(calorieIntake);

        CalorieIntakeOutputDto outputDto = new CalorieIntakeOutputDto();
        BeanUtils.copyProperties(savedCalorieIntake, outputDto);

        return outputDto;
    }
    @Override
    public CalorieIntakeOutputDto updateCalorieIntake(Long id, CalorieIntakeInputDto inputDto) {
        CalorieIntake calorieIntake = calorieIntakeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calorie intake record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        calorieIntake.setRecordDate(inputDto.getRecordDate());
        calorieIntake.setAmount(inputDto.getAmount());
        calorieIntake.setTarget(inputDto.getTarget());
        calorieIntake.setUser(userOptional.get());

        CalorieIntake updatedCalorieIntake = calorieIntakeRepository.save(calorieIntake);

        CalorieIntakeOutputDto outputDto = new CalorieIntakeOutputDto();
        BeanUtils.copyProperties(updatedCalorieIntake, outputDto);

        return outputDto;
    }


    @Override
    public List<CalorieIntakeOutputDto> getCalorieIntakeByUser(Long userId) {
        List<CalorieIntake> calorieIntakes = calorieIntakeRepository.findByUserId(userId);
        List<CalorieIntakeOutputDto> outputDtos = new ArrayList<>();

        for (CalorieIntake calorieIntake : calorieIntakes) {
            CalorieIntakeOutputDto outputDto = new CalorieIntakeOutputDto();
            BeanUtils.copyProperties(calorieIntake, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteCalorieIntake(Long calorieId, Long userId) {
        int deletedRows = calorieIntakeRepository.deleteByCalorieIdAndUserId(calorieId, userId);
        if (deletedRows == 0) {
            throw new RuntimeException("Calorie intake record not found or you are not authorized to delete this record.");
        }
    }

    
    //grafik vs icin ek islevler
    
    @Override
    public CalorieIntakeSummaryDto getCalorieIntakeSummary(Long userId) {
        List<CalorieIntake> calorieIntakes = calorieIntakeRepository.findByUserId(userId);

        double weeklyTotal = calculateTotalByTimeFrame(calorieIntakes, "WEEK");
        double monthlyTotal = calculateTotalByTimeFrame(calorieIntakes, "MONTH");
        double yearlyTotal = calculateTotalByTimeFrame(calorieIntakes, "YEAR");
        double goalAchievementRate = calculateGoalAchievementRate(calorieIntakes);

        CalorieIntakeSummaryDto summaryDto = new CalorieIntakeSummaryDto();
        summaryDto.setWeeklyTotal(weeklyTotal);
        summaryDto.setMonthlyTotal(monthlyTotal);
        summaryDto.setYearlyTotal(yearlyTotal);
        summaryDto.setGoalAchievementRate(goalAchievementRate);

        return summaryDto;
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
    // Ortak yardımcı metotlar
    private double calculateTotalByTimeFrame(List<CalorieIntake> calorieIntakes, String timeFrame) {
        Date now = new Date();
        return calorieIntakes.stream()
                .filter(intake -> isWithinTimeFrame(intake.getRecordDate(), now, timeFrame))
                .mapToDouble(CalorieIntake::getAmount)
                .sum();
    }

    private double calculateGoalAchievementRate(List<CalorieIntake> calorieIntakes) {
        double totalTarget = calorieIntakes.stream().mapToDouble(CalorieIntake::getTarget).sum();
        double totalAmount = calorieIntakes.stream().mapToDouble(CalorieIntake::getAmount).sum();

        return totalTarget > 0 ? (totalAmount / totalTarget) * 100 : 0;
    }

}
