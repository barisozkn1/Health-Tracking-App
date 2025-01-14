package com.baris.healthtracking.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoChangePassword {
    
    @NotEmpty(message = "Current password cannot be empty!")
    private String currentPassword;

    @NotEmpty(message = "New password cannot be empty!")
    private String newPassword;
}
