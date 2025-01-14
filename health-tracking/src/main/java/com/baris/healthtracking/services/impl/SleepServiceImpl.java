package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.SleepInputDto;
import com.baris.healthtracking.dto.SleepOutputDto;
import com.baris.healthtracking.dto.SleepSummaryDto;
import com.baris.healthtracking.entites.Sleep;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.SleepRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.ISleepService;
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
public class SleepServiceImpl implements ISleepService {

    @Autowired
    private SleepRepository sleepRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SleepOutputDto createSleepRecord(SleepInputDto inputDto) {
        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        Sleep sleep = new Sleep();
        BeanUtils.copyProperties(inputDto, sleep);
        sleep.setUser(userOptional.get());

        Sleep savedSleep = sleepRepository.save(sleep);

        SleepOutputDto outputDto = new SleepOutputDto();
        BeanUtils.copyProperties(savedSleep, outputDto);

        return outputDto;
    }
    @Override
    public SleepOutputDto updateSleepRecord(Long id, SleepInputDto inputDto) {
        Sleep sleep = sleepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sleep record not found with ID: " + id));

        Optional<User> userOptional = userRepository.findById(inputDto.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + inputDto.getUserId());
        }

        sleep.setRecordDate(inputDto.getRecordDate());
        sleep.setDuration(inputDto.getDuration());
        sleep.setTarget(inputDto.getTarget());
        sleep.setUser(userOptional.get());

        Sleep updatedSleep = sleepRepository.save(sleep);

        SleepOutputDto outputDto = new SleepOutputDto();
        BeanUtils.copyProperties(updatedSleep, outputDto);

        return outputDto;
    }


    @Override
    public List<SleepOutputDto> getSleepRecordsByUser(Long userId) {
        List<Sleep> sleeps = sleepRepository.findByUserId(userId);
        List<SleepOutputDto> outputDtos = new ArrayList<>();

        for (Sleep sleep : sleeps) {
            SleepOutputDto outputDto = new SleepOutputDto();
            BeanUtils.copyProperties(sleep, outputDto);
            outputDtos.add(outputDto);
        }

        return outputDtos;
    }

    @Override
    @Transactional
    public void deleteSleepRecord(Long sleepId, Long userId) {
        int deletedRows = sleepRepository.deleteBySleepIdAndUserId(sleepId, userId);
        if (deletedRows == 0) {
            throw new RuntimeException("Sleep record not found or you are not authorized to delete this record.");
        }
    }

    
    //grafik icin asagıdaki kurgu
    
    @Override
    public SleepSummaryDto getSleepSummary(Long userId) {
        List<Sleep> sleeps = sleepRepository.findByUserId(userId);

        double weeklyTotal = calculateTotalByTimeFrame(sleeps, "WEEK");
        double monthlyTotal = calculateTotalByTimeFrame(sleeps, "MONTH");
        double yearlyTotal = calculateTotalByTimeFrame(sleeps, "YEAR");
        double goalAchievementRate = calculateGoalAchievementRate(sleeps);

        SleepSummaryDto summaryDto = new SleepSummaryDto();
        summaryDto.setWeeklyTotal(weeklyTotal);
        summaryDto.setMonthlyTotal(monthlyTotal);
        summaryDto.setYearlyTotal(yearlyTotal);
        summaryDto.setGoalAchievementRate(goalAchievementRate);

        return summaryDto;
    }
    
    private double calculateTotalByTimeFrame(List<Sleep> sleeps, String timeFrame) {
        Date now = new Date(); // Şu anki tarih
        return sleeps.stream()
                .filter(sleep -> isWithinTimeFrame(sleep.getRecordDate(), now, timeFrame))
                .mapToDouble(Sleep::getDuration) // Uyku süresi (duration)
                .sum();
    }

    private boolean isWithinTimeFrame(Date recordDate, Date now, String timeFrame) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        if (timeFrame.equals("WEEK")) {
            cal.add(Calendar.DAY_OF_YEAR, -7); // Son bir hafta
        } else if (timeFrame.equals("MONTH")) {
            cal.add(Calendar.MONTH, -1); // Son bir ay
        } else if (timeFrame.equals("YEAR")) {
            cal.add(Calendar.YEAR, -1); // Son bir yıl
        }

        return recordDate.after(cal.getTime()); // Belirtilen zaman aralığında mı?
    }


    private double calculateGoalAchievementRate(List<Sleep> sleeps) {
        // Toplam hedef süresi
        double totalTarget = sleeps.stream()
                                   .mapToDouble(Sleep::getTarget) // Hedef süre (target)
                                   .sum();
        // Toplam uyku süresi
        double totalDuration = sleeps.stream()
                                     .mapToDouble(Sleep::getDuration) // Gerçekleşen uyku süresi (duration)
                                     .sum();

        // Hedef oranını hesapla
        return totalTarget > 0 ? (totalDuration / totalTarget) * 100 : 0;
    }

}
