package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.HeartRateInputDto;
import com.baris.healthtracking.dto.HeartRateOutputDto;
import com.baris.healthtracking.dto.HeartRateSummaryDto;
import com.baris.healthtracking.services.IHeartRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/heart-rate")
public class HeartRateController {

    @Autowired
    private IHeartRateService heartRateService;

    @PostMapping("/create")
    public HeartRateOutputDto createHeartRateRecord(@RequestBody HeartRateInputDto inputDto) {
        return heartRateService.createHeartRateRecord(inputDto);
    }
    @PutMapping("/update/{id}")
    public HeartRateOutputDto updateHeartRateRecord(@PathVariable Long id, @RequestBody HeartRateInputDto inputDto) {
        return heartRateService.updateHeartRateRecord(id, inputDto);
    }


    @GetMapping("/list/{userId}")
    public List<HeartRateOutputDto> getHeartRateRecordsByUser(@PathVariable Long userId) {
        return heartRateService.getHeartRateRecordsByUser(userId);
    }

    @DeleteMapping("/delete/{heartRateId}")
    public ResponseEntity<?> deleteHeartRateRecord(@PathVariable Long heartRateId, @RequestParam Long userId) {
        heartRateService.deleteHeartRateRecord(heartRateId, userId);
        return ResponseEntity.ok("Heart rate record successfully deleted.");
    }

    
    @GetMapping("/summary/{userId}")
    public HeartRateSummaryDto getHeartRateSummary(@PathVariable Long userId, @RequestParam int age) {
        return heartRateService.getHeartRateSummary(userId, age);
    }

}
