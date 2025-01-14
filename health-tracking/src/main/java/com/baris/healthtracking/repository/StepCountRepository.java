package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.StepCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StepCountRepository extends JpaRepository<StepCount, Long> {
    List<StepCount> findByUserId(Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM StepCount s WHERE s.id = :stepCountId AND s.user.id = :userId")
    int deleteByStepCountIdAndUserId(@Param("stepCountId") Long stepCountId, @Param("userId") Long userId);
}
