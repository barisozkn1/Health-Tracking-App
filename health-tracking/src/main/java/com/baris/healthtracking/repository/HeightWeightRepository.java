package com.baris.healthtracking.repository;

import com.baris.healthtracking.entites.HeightWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HeightWeightRepository extends JpaRepository<HeightWeight, Long> {
    List<HeightWeight> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM HeightWeight hw WHERE hw.id = :heightWeightId AND hw.user.id = :userId")
    int deleteByHeightWeightIdAndUserId(@Param("heightWeightId") Long heightWeightId, @Param("userId") Long userId);
}
