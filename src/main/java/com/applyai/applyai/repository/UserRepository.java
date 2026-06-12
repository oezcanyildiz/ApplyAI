package com.applyai.applyai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.applyai.applyai.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email address
    Optional<User> findByEmail(String email);

    // Check if a user with the given email already exists
    boolean existsByEmail(String email);
}
