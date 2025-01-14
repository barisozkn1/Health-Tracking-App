package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.WaterIntakeInputDto;
import com.baris.healthtracking.dto.WaterIntakeOutputDto;
import com.baris.healthtracking.dto.WaterIntakeSummaryDto;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.entites.WaterIntake;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.repository.WaterIntakeRepository;
import com.baris.healthtracking.services.IWaterIntakeService;
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
public class WaterIntakeServiceImpl implements IWaterIntakeService {

    @Autowired
    private WaterIntakeRepository waterIntakeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public WaterIntakeOutputDto createWaterIntake(WaterIntakeInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        WaterIntake waterIntake = new WaterIntake();
        BeanUtils.copyProperties(inputDto, waterIntake);
        waterIntake.setUser(userOptional.get());

        WaterIntake savedWaterIntake = waterIntakeRepository.save(waterIntake);

        WaterIntakeOutputDto outputDto = new WaterIntakeOutputDto();
        BeanUtils.copyProperties(savedWaterIntake, outputDto);

        return outputDto;
    }

    @Override
    public WaterIntakeOutputDto updateWaterIntake(Long id, WaterIntakeInputDto inputDto) {
        WaterIntake waterIntake = waterIntakeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Water intake record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        waterIntake.setRecordDate(inputDto.getRecordDate());
        waterIntake.setAmount(inputDto.getAmount());
        waterIntake.setTarget(inputDto.getTarget());
        waterIntake.setUser(userOptional.get());

        WaterIntake updatedWaterIntake = waterIntakeRepository.save(waterIntake);

        WaterIntakeOutputDto outputDto = new WaterIntakeOutputDto();
        BeanUtils.copyProperties(updatedWaterIntake, outputDto);

        return outputDto;
    }

    
    @Override
    public List<WaterIntakeOutputDto> getWaterIntakeByUser(Long userId) {
        List<WaterIntake> waterIntakes = waterIntakeRepository.findByUserId(userId);
        List<WaterIntakeOutputDto> outputDtos = new ArrayList<>();

        for (WaterIntake waterIntake : waterIntakes) {
            WaterIntakeOutputDto outputDto = new WaterIntakeOutputDto();
            BeanUtils.copyProperties(waterIntake, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteWaterIntake(Long waterId, Long userId) {
        // Kullanıcı yetkisini ve kaydın varlığını kontrol et
        Optional<WaterIntake> waterIntakeOptional = waterIntakeRepository.findById(waterId);
        if (waterIntakeOptional.isEmpty()) {
            throw new RuntimeException("Water intake record not found with ID: " + waterId);
        }

        // Kullanıcı yetkisini kontrol et
        if (!waterIntakeOptional.get().getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this record.");
        }

        // Özel sorgu ile silme işlemini gerçekleştir
        waterIntakeRepository.deleteByWaterIdAndUserId(waterId, userId);
    }




    
    
    /*buradan aşağısı grafikler için yapılar*/
    
    @Override
    public WaterIntakeSummaryDto getWaterIntakeSummary(Long userId) {
        List<WaterIntake> waterIntakes = waterIntakeRepository.findByUserId(userId);

        double weeklyTotal = calculateTotalByTimeFrame(waterIntakes, "WEEK");
        double monthlyTotal = calculateTotalByTimeFrame(waterIntakes, "MONTH");
        double yearlyTotal = calculateTotalByTimeFrame(waterIntakes, "YEAR");
        double goalAchievementRate = calculateGoalAchievementRate(waterIntakes);

        WaterIntakeSummaryDto summaryDto = new WaterIntakeSummaryDto();
        summaryDto.setWeeklyTotal(weeklyTotal);
        summaryDto.setMonthlyTotal(monthlyTotal);
        summaryDto.setYearlyTotal(yearlyTotal);
        summaryDto.setGoalAchievementRate(goalAchievementRate);

        return summaryDto;
    }

    // Zaman bazlı toplam hesaplama
    private double calculateTotalByTimeFrame(List<WaterIntake> waterIntakes, String timeFrame) {
        Date now = new Date();
        return waterIntakes.stream()
            .filter(intake -> isWithinTimeFrame(intake.getRecordDate(), now, timeFrame))
            .mapToDouble(WaterIntake::getAmount)
            .sum();
    }

    // Zaman aralığı kontrolü
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

    // Hedefe ulaşma oranı hesaplama
    private double calculateGoalAchievementRate(List<WaterIntake> waterIntakes) {
        double totalTarget = waterIntakes.stream().mapToDouble(WaterIntake::getTarget).sum();
        double totalAmount = waterIntakes.stream().mapToDouble(WaterIntake::getAmount).sum();

        return totalTarget > 0 ? (totalAmount / totalTarget) * 100 : 0;
    }

}
