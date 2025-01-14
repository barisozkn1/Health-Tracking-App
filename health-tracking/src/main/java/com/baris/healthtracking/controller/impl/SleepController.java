package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.SleepInputDto;
import com.baris.healthtracking.dto.SleepOutputDto;
import com.baris.healthtracking.dto.SleepSummaryDto;
import com.baris.healthtracking.services.ISleepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/sleep")
public class SleepController {

    @Autowired
    private ISleepService sleepService;

    @PostMapping("/create")
    public SleepOutputDto createSleepRecord(@RequestBody SleepInputDto inputDto) {
        return sleepService.createSleepRecord(inputDto);
    }
    @PutMapping("/update/{id}")
    public SleepOutputDto updateSleepRecord(@PathVariable Long id, @RequestBody SleepInputDto inputDto) {
        return sleepService.updateSleepRecord(id, inputDto);
    }


    @GetMapping("/list/{userId}")
    public List<SleepOutputDto> getSleepRecordsByUser(@PathVariable Long userId) {
        return sleepService.getSleepRecordsByUser(userId);
    }

    @DeleteMapping("/delete/{sleepId}")
    public ResponseEntity<?> deleteSleepRecord(@PathVariable Long sleepId, @RequestParam Long userId) {
        sleepService.deleteSleepRecord(sleepId, userId);
        return ResponseEntity.ok("Sleep record successfully deleted.");
    }

    
    @GetMapping("/summary/{userId}")
    public SleepSummaryDto getSleepSummary(@PathVariable Long userId) {
        return sleepService.getSleepSummary(userId);
    }

}
