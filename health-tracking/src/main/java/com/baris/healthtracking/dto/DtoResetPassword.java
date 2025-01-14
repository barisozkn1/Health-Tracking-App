package com.baris.healthtracking.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DtoResetPassword {
    
    @NotEmpty(message = "Token boş bırakılamaz!")
    private String token;
    
    @NotEmpty(message = "Yeni şifre boş bırakılamaz!")
    private String newPassword;
}
