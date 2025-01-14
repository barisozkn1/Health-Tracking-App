package com.baris.healthtracking.services.impl;

import com.baris.healthtracking.dto.DtoResetPassword;
import com.baris.healthtracking.dto.DtoPasswordResetRequest;
import com.baris.healthtracking.entites.PasswordResetToken;
import com.baris.healthtracking.entites.User;
import com.baris.healthtracking.exception.CustomException;
import com.baris.healthtracking.repository.PasswordResetTokenRepository;
import com.baris.healthtracking.repository.UserRepository;
import com.baris.healthtracking.services.IPasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements IPasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailServiceImpl mailService;

    @Override
    public void sendResetToken(DtoPasswordResetRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new CustomException("User with this email not found.", 404);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hour

        tokenRepository.save(resetToken);

        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String emailBody = "Click the link to reset your password: " + resetUrl;
        mailService.sendMail(email, "Password Reset Request", emailBody);
    }

    @Override
    public void resetPassword(DtoResetPassword dto) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(dto.getToken());
        if (tokenOptional.isEmpty() || tokenOptional.get().getExpiryDate().before(new Date())) {
            throw new CustomException("Token is invalid or expired.", 400);
        }

        String email = tokenOptional.get().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found.", 404));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(tokenOptional.get());
    }
}
