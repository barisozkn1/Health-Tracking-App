package com.baris.healthtracking.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baris.healthtracking.dto.profile.ChangePasswordRequestDto;
import com.baris.healthtracking.dto.profile.ProfileResponseDto;
import com.baris.healthtracking.dto.profile.ProfileUpdateRequestDto;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IProfileService;

@Service
public class ProfileServiceImpl implements IProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponseDto getProfileById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));
        return mapToResponseDto(user);
    }

    @Override
    public ProfileResponseDto updateProfile(Long id, ProfileUpdateRequestDto updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        user.setUserName(updateRequest.getUserName());
        user.setEmail(updateRequest.getEmail());
        user.setGender(updateRequest.getGender());

        userRepository.save(user);
        return mapToResponseDto(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequestDto passwordRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mevcut şifre yanlış.");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);
    }

    private ProfileResponseDto mapToResponseDto(User user) {
        ProfileResponseDto response = new ProfileResponseDto();
        response.setId(user.getId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail());
        response.setGender(user.getGender());
        return response;
    }
}
