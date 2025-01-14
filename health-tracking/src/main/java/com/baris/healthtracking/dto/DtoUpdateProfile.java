package com.baris.healthtracking.dto;

import com.baris.healthtracking.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoUpdateProfile {
    
    @NotEmpty(message = "Username alanı boş bırakılamaz!")
    @Size(max = 30, message = "Username 30 karakterden fazla olamaz")
    private String userName;

    @NotEmpty(message = "Email alanı boş bırakılamaz!")
    @Email(message = "Lütfen geçerli bir email adresi giriniz")
    private String email;

    private Gender gender;
}
