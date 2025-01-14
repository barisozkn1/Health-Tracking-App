package com.baris.healthtracking.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baris.healthtracking.dto.profile.ChangePasswordRequestDto;
import com.baris.healthtracking.dto.profile.ProfileResponseDto;
import com.baris.healthtracking.dto.profile.ProfileUpdateRequestDto;
import com.baris.healthtracking.services.IProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/profile")
public class ProfileController {

    @Autowired
    private IProfileService profileService;

    @GetMapping("/{id}")
    public ProfileResponseDto getProfile(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public ProfileResponseDto updateProfile(
            @PathVariable Long id, 
            @RequestBody @Valid ProfileUpdateRequestDto updateRequest
    ) {
        return profileService.updateProfile(id, updateRequest);
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id, 
            @RequestBody @Valid ChangePasswordRequestDto passwordRequest
    ) {
        profileService.changePassword(id, passwordRequest);
        return ResponseEntity.ok("Şifre başarıyla güncellendi.");
    }
}
