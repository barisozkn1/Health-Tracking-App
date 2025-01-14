package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.HeightWeightInputDto;
import com.baris.healthtracking.dto.HeightWeightOutputDto;
import com.baris.healthtracking.dto.HeightWeightSummaryDto;
import com.baris.healthtracking.services.IHeightWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/height-weight")
public class HeightWeightController {

    @Autowired
    private IHeightWeightService heightWeightService;

    @PostMapping("/create")
    public HeightWeightOutputDto createHeightWeightRecord(@RequestBody HeightWeightInputDto inputDto) {
        return heightWeightService.createHeightWeightRecord(inputDto);
    }
    @PutMapping("/update/{id}")
    public HeightWeightOutputDto updateHeightWeightRecord(@PathVariable Long id, @RequestBody HeightWeightInputDto inputDto) {
        return heightWeightService.updateHeightWeightRecord(id, inputDto);
    }


    @GetMapping("/list/{userId}")
    public List<HeightWeightOutputDto> getHeightWeightRecordsByUser(@PathVariable Long userId) {
        return heightWeightService.getHeightWeightRecordsByUser(userId);
    }

    @DeleteMapping("/delete/{heightWeightId}")
    public ResponseEntity<?> deleteHeightWeightRecord(@PathVariable Long heightWeightId, @RequestParam Long userId) {
        heightWeightService.deleteHeightWeightRecord(heightWeightId, userId);
        return ResponseEntity.ok("Height-weight record successfully deleted.");
    }

    
    @GetMapping("/summary/{userId}")
    public HeightWeightSummaryDto getHeightWeightSummary(@PathVariable Long userId) {
        return heightWeightService.getHeightWeightSummary(userId);
    }

}
