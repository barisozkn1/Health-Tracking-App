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
public class HeightWeight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Height cannot be null!")
    @Min(value = 50, message = "Height must be at least 50 cm!")
    private Double height; // Boy (cm)

    @NotNull(message = "Weight cannot be null!")
    @Min(value = 1, message = "Weight must be at least 1 kg!")
    private Double weight; // Kilo (kg)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
