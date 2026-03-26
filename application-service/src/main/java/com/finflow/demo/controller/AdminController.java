package com.finflow.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.finflow.demo.dto.AdminStatsDTO;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/application/admin")
public class AdminController {

    private final ApplicationService service;

    public AdminController(ApplicationService service) {
        this.service = service;
    }

    // ✅ ADMIN DASHBOARD
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Admin Dashboard - Application Service";
    }

    // ✅ GET ALL APPLICATIONS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<LoanApplication> getAll() {

        System.out.println("📥 ADMIN FETCH ALL");

        return service.getAllApplications();
    }

    // ✅ APPROVE
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public LoanApplication approve(@PathVariable Long id) {

        System.out.println("✅ ADMIN APPROVE: " + id);

        return service.approve(id);
    }

    // ❌ REJECT
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/reject")
    public LoanApplication reject(@PathVariable Long id) {

        System.out.println("❌ ADMIN REJECT: " + id);

        return service.reject(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public AdminStatsDTO getStats() {
        return service.getAdminStats();
    }
}