package com.finflow.demo.service;

import org.springframework.stereotype.Service;

import com.finflow.demo.entity.*;
import com.finflow.demo.repository.ApplicationRepository;

import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ApplicationService {

    private final ApplicationRepository repository;
    private final DocClient docClient;

    public ApplicationService(ApplicationRepository repository, DocClient docClient) {
        this.repository = repository;
        this.docClient = docClient;
    }

    // ✅ SUBMIT APPLICATION
    public LoanApplication submitApplication(LoanApplication app) {

        System.out.println("⚙️ Service HIT");

        // Step 1: Save first (get applicationId)
        app.setStatus(ApplicationStatus.SUBMITTED);
        LoanApplication saved = repository.save(app);

        System.out.println("📌 Application saved with ID: " + saved.getId());

        // Step 2: Check doc status
        

        String userId = saved.getUserId();

        // 🔑 Get token from SecurityContext
        String token = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials();

        String docStatus = docClient.getDocStatus(userId, token);

        System.out.println("📄 Doc Status: " + docStatus);

        if ("VERIFIED".equalsIgnoreCase(docStatus)) {
            saved.setStatus(ApplicationStatus.DOCS_VERIFIED);
            System.out.println("✅ Docs VERIFIED");
        } else {
            saved.setStatus(ApplicationStatus.DOCS_PENDING);
            System.out.println("⏳ Docs PENDING");
        }

        return repository.save(saved);
    }

    // ✅ USER: get own applications
    public List<LoanApplication> getMyApplications(String userId) {
        System.out.println("📦 Fetching applications for user: " + userId);
        return repository.findByUserId(userId);
    }

    // ✅ ADMIN: get all
    public List<LoanApplication> getAllApplications() {
        System.out.println("📦 Admin fetching all applications");
        return repository.findAll();
    }

    // ✅ REFRESH DOC STATUS
    public LoanApplication refreshStatus(Long id) {

        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));


String userId = app.getUserId();

String token = (String) SecurityContextHolder.getContext()
        .getAuthentication()
        .getCredentials();

String docStatus = docClient.getDocStatus(userId, token);

        if ("VERIFIED".equalsIgnoreCase(docStatus)) {
            app.setStatus(ApplicationStatus.DOCS_VERIFIED);
        } else {
            app.setStatus(ApplicationStatus.DOCS_PENDING);
        }

        return repository.save(app);
    }

    // ✅ APPROVE
    public LoanApplication approve(Long id) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.APPROVED);
        return repository.save(app);
    }

    // ❌ REJECT
    public LoanApplication reject(Long id) {
        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(ApplicationStatus.REJECTED);
        return repository.save(app);
    }
    public LoanApplication getApplicationForUser(Long id, String userId) {

        LoanApplication app = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getUserId().equals(userId)) {
            throw new RuntimeException("Access Denied");
        }

        return app;
    }
}