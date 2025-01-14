package com.baris.healthtracking.dto.profile;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    @NotEmpty(message = "Mevcut şifre boş bırakılamaz.")
    private String currentPassword;

    @NotEmpty(message = "Yeni şifre boş bırakılamaz.")
    private String newPassword;
}

