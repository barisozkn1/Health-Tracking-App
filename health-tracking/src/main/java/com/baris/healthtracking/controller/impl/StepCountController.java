package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.StepCountInputDto;
import com.baris.healthtracking.dto.StepCountOutputDto;
import com.baris.healthtracking.dto.StepCountSummaryDto;
import com.baris.healthtracking.services.IStepCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/step")
public class StepCountController {

    @Autowired
    private IStepCountService stepCountService;

    @PostMapping("/create")
    public StepCountOutputDto createStepCount(@RequestBody StepCountInputDto inputDto) {
        return stepCountService.createStepCount(inputDto);
    }
    @PutMapping("/update/{id}")
    public StepCountOutputDto updateStepCount(@PathVariable Long id, @RequestBody StepCountInputDto inputDto) {
        return stepCountService.updateStepCount(id, inputDto);
    }


    @GetMapping("/list/{userId}")
    public List<StepCountOutputDto> getStepCountsByUser(@PathVariable Long userId) {
        return stepCountService.getStepCountsByUser(userId);
    }

    @DeleteMapping("/delete/{stepCountId}")
    public ResponseEntity<?> deleteStepCount(@PathVariable Long stepCountId, @RequestParam Long userId) {
        stepCountService.deleteStepCount(stepCountId, userId);
        return ResponseEntity.ok("Step count record successfully deleted.");
    }

    
    @GetMapping("/summary/{userId}")
    public StepCountSummaryDto getStepCountSummary(@PathVariable Long userId) {
        return stepCountService.getStepCountSummary(userId);
    }

}
