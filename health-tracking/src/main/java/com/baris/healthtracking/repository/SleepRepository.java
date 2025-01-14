package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SleepRepository extends JpaRepository<Sleep, Long> {
    List<Sleep> findByUserId(Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Sleep s WHERE s.id = :sleepId AND s.user.id = :userId")
    int deleteBySleepIdAndUserId(@Param("sleepId") Long sleepId, @Param("userId") Long userId);
}
