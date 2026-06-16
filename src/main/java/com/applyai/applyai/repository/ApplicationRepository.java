package com.applyai.applyai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.applyai.applyai.enums.ApplicationStatus;
import com.applyai.applyai.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserId(Long userId);

    Optional<Application> findByIdAndUserId(Long id, Long userId);

    List<Application> findByUserIdAndStatus(Long userId,  ApplicationStatus status);


}
