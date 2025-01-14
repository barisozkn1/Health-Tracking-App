package com.baris.healthtracking.entites;

import com.baris.healthtracking.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Username cannot be empty!")
    @Size(max = 30, message = "Username cannot exceed 30 characters")
    @Column(unique = true)
    private String userName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    @Column(nullable = false)
    private String password;

    @Column(name = "created_at")
    private java.util.Date createdAt = new java.util.Date();

    @NotNull(message = "Gender cannot be null!")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Relations with modular entities
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WaterIntake> waterIntakes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CalorieIntake> calorieIntakes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HeartRate> heartRates;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HeightWeight> heightWeights;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Sleep> sleeps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StepCount> stepCounts;
}
