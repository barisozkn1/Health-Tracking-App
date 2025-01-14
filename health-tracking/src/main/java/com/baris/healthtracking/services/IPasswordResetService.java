package com.baris.healthtracking.services;

import com.baris.healthtracking.dto.DtoResetPassword;
import com.baris.healthtracking.dto.DtoPasswordResetRequest;

public interface IPasswordResetService {
    void sendResetToken(DtoPasswordResetRequest request);
    void resetPassword(DtoResetPassword dto);
}
