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
public class CalorieIntake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Calorie intake amount cannot be null!")
    @Min(value = 0, message = "Calorie intake cannot be negative!")
    private Double amount; // Alınan kalori miktarı

    @NotNull(message = "Daily calorie target cannot be null!")
    @Min(value = 0, message = "Daily calorie target cannot be negative!")
    private Double target; // Günlük kalori hedefi

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
