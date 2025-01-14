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
public class HeartRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Heart rate cannot be null!")
    @Min(value = 0, message = "Heart rate cannot be negative!")
    private Integer heartRate; // Kalp atış hızı (BPM)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
