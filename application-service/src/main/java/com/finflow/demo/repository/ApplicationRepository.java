package com.finflow.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finflow.demo.entity.ApplicationStatus;
import com.finflow.demo.entity.LoanApplication;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<LoanApplication, Long> {

    List<LoanApplication> findByUserId(String userId);
    //hello
    long countByStatus(ApplicationStatus status);
    long count();
    
}