package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.WaterIntakeInputDto;
import com.baris.healthtracking.dto.WaterIntakeOutputDto;
import com.baris.healthtracking.dto.WaterIntakeSummaryDto;
import com.baris.healthtracking.services.IWaterIntakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/water")
public class WaterIntakeController {

    @Autowired
    private IWaterIntakeService waterIntakeService;

    @PostMapping("/create")
    public WaterIntakeOutputDto createWaterIntake(@RequestBody WaterIntakeInputDto inputDto) {
        return waterIntakeService.createWaterIntake(inputDto);
    }
    @PutMapping("/update/{id}")
    public WaterIntakeOutputDto updateWaterIntake(@PathVariable Long id, @RequestBody WaterIntakeInputDto inputDto) {
        return waterIntakeService.updateWaterIntake(id, inputDto);
    }


    @GetMapping("/list/{userId}")
    public List<WaterIntakeOutputDto> getWaterIntakeByUser(@PathVariable Long userId) {
        return waterIntakeService.getWaterIntakeByUser(userId);
    }

    @DeleteMapping("/delete/{waterId}")
    public ResponseEntity<?> deleteWaterIntake(@PathVariable Long waterId, @RequestParam Long userId) {
        try {
            waterIntakeService.deleteWaterIntake(waterId, userId);
            return ResponseEntity.ok("Water intake record successfully deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


    
    
    @GetMapping("/summary/{userId}")
    public WaterIntakeSummaryDto getWaterIntakeSummary(@PathVariable Long userId) {
        return waterIntakeService.getWaterIntakeSummary(userId);
    }

}
