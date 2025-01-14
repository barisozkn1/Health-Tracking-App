package com.baris.healthtracking.controller.impl;

import com.baris.healthtracking.dto.DtoResetPassword;
import com.baris.healthtracking.dto.DtoPasswordResetRequest;
import com.baris.healthtracking.services.IPasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/api/psword")
public class PasswordResetController {

    @Autowired
    private IPasswordResetService passwordResetService;

    @PostMapping("/send-reset-token")
    public void sendResetToken(@RequestBody DtoPasswordResetRequest request) {
        passwordResetService.sendResetToken(request);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody DtoResetPassword dto) {
        passwordResetService.resetPassword(dto);
    }
}
