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
public class Sleep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Record date cannot be null!")
    @Temporal(TemporalType.DATE)
    private Date recordDate;

    @NotNull(message = "Sleep duration cannot be null!")
    @Min(value = 0, message = "Sleep duration cannot be negative!")
    private Double duration; // Uyku süresi (saat)

    @NotNull(message = "Daily sleep target cannot be null!")
    @Min(value = 0, message = "Daily sleep target cannot be negative!")
    private Double target; // Günlük uyku hedefi (saat)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
