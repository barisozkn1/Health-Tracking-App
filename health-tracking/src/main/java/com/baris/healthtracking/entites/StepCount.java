package com.baris.healthtracking.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Step count cannot be null!")
    @Min(value = 0, message = "Step count cannot be negative!")
    private Integer stepCount; // Günlük adım sayısı

    @NotNull(message = "Daily step target cannot be null!")
    @Min(value = 0, message = "Daily step target cannot be negative!")
    private Integer target; // Günlük adım hedefi

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
