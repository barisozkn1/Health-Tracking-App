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
public class WaterIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Water intake amount cannot be null!")
    @Min(value = 0, message = "Water intake cannot be negative!")
    private Double amount; // Tüketilen su miktarı (litre)

    @NotNull(message = "Daily water target cannot be null!")
    @Min(value = 0, message = "Daily water target cannot be negative!")
    private Double target; // Günlük hedef (litre)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
