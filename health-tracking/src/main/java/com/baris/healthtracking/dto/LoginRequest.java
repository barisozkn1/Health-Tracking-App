package com.baris.healthtracking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "E-posta boş olamaz!")
    @Email(message = "Geçerli bir e-posta adresi girin!")
    private String email;

    @NotBlank(message = "Şifre boş olamaz!")
    private String password;
}
