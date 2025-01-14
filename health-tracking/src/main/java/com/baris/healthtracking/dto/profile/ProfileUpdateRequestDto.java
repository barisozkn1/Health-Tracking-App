package com.baris.healthtracking.dto.profile;

import com.baris.healthtracking.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProfileUpdateRequestDto {
    @NotEmpty(message = "Kullanıcı adı boş bırakılamaz.")
    private String userName;

    @NotEmpty(message = "Email boş bırakılamaz.")
    @Email(message = "Geçerli bir email adresi giriniz.")
    private String email;

    private Gender gender;
}
