package com.applyai.applyai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.applyai.applyai.enums.ApplicationStatus;
import com.applyai.applyai.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUserId(Long userId);

    List<Application> findByUserIdAndApplicationStatus(Long userId,  ApplicationStatus status);


}
