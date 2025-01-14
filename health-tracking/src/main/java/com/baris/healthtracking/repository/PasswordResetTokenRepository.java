package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByExpiryDateBefore(Date now);
}
