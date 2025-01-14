package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUserId(Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM WaterIntake w WHERE w.id = :waterId AND w.user.id = :userId")
    void deleteByWaterIdAndUserId(@Param("waterId") Long waterId, @Param("userId") Long userId);


}
