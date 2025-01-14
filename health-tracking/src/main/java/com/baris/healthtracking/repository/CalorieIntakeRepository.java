package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.CalorieIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CalorieIntakeRepository extends JpaRepository<CalorieIntake, Long> {
    List<CalorieIntake> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CalorieIntake c WHERE c.id = :calorieId AND c.user.id = :userId")
    int deleteByCalorieIdAndUserId(@Param("calorieId") Long calorieId, @Param("userId") Long userId);
}
