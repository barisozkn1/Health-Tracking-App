package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.HeartRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HeartRateRepository extends JpaRepository<HeartRate, Long> {
    List<HeartRate> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM HeartRate hr WHERE hr.id = :heartRateId AND hr.user.id = :userId")
    int deleteByHeartRateIdAndUserId(@Param("heartRateId") Long heartRateId, @Param("userId") Long userId);
}
