package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.profile.ChangePasswordRequestDto;
import com.baris.healthtracking.dto.profile.ProfileResponseDto;
import com.baris.healthtracking.dto.profile.ProfileUpdateRequestDto;

public interface IProfileService {
	
    ProfileResponseDto getProfileById(Long id);
    ProfileResponseDto updateProfile(Long id, ProfileUpdateRequestDto updateRequest);
    void changePassword(Long id, ChangePasswordRequestDto passwordRequest);
}
