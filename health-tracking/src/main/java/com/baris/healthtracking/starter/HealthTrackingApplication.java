package com.baris.healthtracking.starter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.baris.healthtracking.services.impl.UserServiceImpl;

import org.springframework.context.annotation.Bean;

import jakarta.persistence.Entity;

@SpringBootApplication(scanBasePackages = "com.baris.healthtracking")
@EntityScan(basePackages = {"com.baris.healthtracking"})
//@SpringBootApplication
@ComponentScan(basePackages = {"com.baris.healthtracking"})
@EnableJpaRepositories(basePackages = {"com.baris.healthtracking"})
public class HealthTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthTrackingApplication.class, args);
	}
	
    @Bean
    public CommandLineRunner run(UserServiceImpl userService) {
        return args -> {
            userService.updateUserPasswords();
            System.out.println("Tüm kullanıcı şifreleri kontrol edildi ve güncellendi.");
        };
    }

}
