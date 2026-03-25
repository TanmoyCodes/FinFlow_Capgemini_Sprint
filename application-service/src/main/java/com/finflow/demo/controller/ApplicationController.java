package com.finflow.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.finflow.demo.dto.ApplicationRequest;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    // ✅ SUBMIT APPLICATION
    @PostMapping("/apply")
    public LoanApplication submit(@RequestBody ApplicationRequest request) {

        System.out.println("📥 Controller HIT - APPLY");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        System.out.println("👤 UserId from JWT: " + userId);

        LoanApplication app = new LoanApplication();

        // 🔐 Always from JWT
        app.setUserId(userId);

        // 👤 PERSONAL
        app.setFullName(request.getFullName());
        app.setDateOfBirth(request.getDateOfBirth());
        app.setGender(request.getGender());
        app.setContactNumber(request.getContactNumber());
        app.setEmail(request.getEmail());
        app.setCurrentAddress(request.getCurrentAddress());
        app.setPermanentAddress(request.getPermanentAddress());

        // 💼 EMPLOYMENT
        app.setOccupation(request.getOccupation());
        app.setEmployerName(request.getEmployerName());
        app.setIncome(request.getIncome());
        app.setWorkExperience(request.getWorkExperience());

        // 💰 LOAN
        app.setAmount(request.getAmount());
        app.setPurpose(request.getPurpose());
        app.setTenure(request.getTenure());

        return service.submitApplication(app);
    }

    // ✅ GET MY APPLICATIONS
    @GetMapping("/my")
    public List<LoanApplication> getMyApplications() {

        System.out.println("📥 GET MY APPLICATIONS");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        return service.getMyApplications(userId);
    }

    // ✅ REFRESH DOC STATUS
    @PostMapping("/status/{loanId}")
    public LoanApplication refresh(@PathVariable Long loanId) {

        System.out.println("🔄 Refreshing status for application: " + loanId);

        return service.refreshStatus(loanId);
    }

    // ✅ GET SINGLE APPLICATION (USER)
    @GetMapping("/{id}")
    public LoanApplication getOne(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        return service.getApplicationForUser(id, userId);
    }
}