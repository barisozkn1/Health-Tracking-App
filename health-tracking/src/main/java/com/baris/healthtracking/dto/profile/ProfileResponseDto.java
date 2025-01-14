package com.baris.healthtracking.dto.profile;

import com.baris.healthtracking.enums.Gender;

import lombok.Data;

@Data
public class ProfileResponseDto {
    private Long id;
    private String userName;
    private String email;
    private Gender gender;
}