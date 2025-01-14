package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.CalorieIntakeInputDto;
import com.baris.healthtracking.dto.CalorieIntakeOutputDto;
import com.baris.healthtracking.dto.CalorieIntakeSummaryDto;
import com.baris.healthtracking.services.ICalorieIntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/calorie")
public class CalorieIntakeController {

    @Autowired
    private ICalorieIntakeService calorieIntakeService;

    @PostMapping("/create")
    public CalorieIntakeOutputDto createCalorieIntake(@RequestBody CalorieIntakeInputDto inputDto) {
        return calorieIntakeService.createCalorieIntake(inputDto);
    }

    @GetMapping("/list/{userId}")
    public List<CalorieIntakeOutputDto> getCalorieIntakeByUser(@PathVariable Long userId) {
        return calorieIntakeService.getCalorieIntakeByUser(userId);
    }

    @DeleteMapping("/delete/{calorieId}")
    public ResponseEntity<?> deleteCalorieIntake(@PathVariable Long calorieId, @RequestParam Long userId) {
        calorieIntakeService.deleteCalorieIntake(calorieId, userId);
        return ResponseEntity.ok("Calorie intake record successfully deleted.");
    }

    @PutMapping("/update/{id}")
    public CalorieIntakeOutputDto updateCalorieIntake(@PathVariable Long id, @RequestBody CalorieIntakeInputDto inputDto) {
        return calorieIntakeService.updateCalorieIntake(id, inputDto);
    }
    
    @GetMapping("/summary/{userId}")
    public CalorieIntakeSummaryDto getCalorieIntakeSummary(@PathVariable Long userId) {
        return calorieIntakeService.getCalorieIntakeSummary(userId);
    }


}
