package com.finflow.demo.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.finflow.demo.LoanApplicationDTO.LoanApplicationDTO;
import com.finflow.demo.dto.AdminStatsDTO;
import com.finflow.demo.entity.LoanApplication;

public interface ApplicationServiceInterface {
	@Cacheable(value = "loan", key = "#id")
	LoanApplication getApplicationByIdForAdmin(Long id);
	List<LoanApplicationDTO> getAllApplications();
    LoanApplication approve(Long id);
    LoanApplication reject(Long id);
    AdminStatsDTO getAdminStats();
    
}
