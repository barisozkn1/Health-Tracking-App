package com.baris.healthtracking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.baris.healthtracking.entites.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailIgnoreCase(String email); // Büyük/küçük harf duyarsız sorgu
}
