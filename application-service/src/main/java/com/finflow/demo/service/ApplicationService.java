//package com.finflow.demo.service;
//
//import org.springframework.stereotype.Service;
//
//import com.finflow.demo.LoanApplicationDTO.LoanApplicationDTO;
//import com.finflow.demo.dto.AdminStatsDTO;
//import com.finflow.demo.entity.*;
//import com.finflow.demo.repository.ApplicationRepository;
//
//import java.util.List;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//@Service
//public class ApplicationService {
//
//    
//	private final ApplicationRepository repository;
//    private final DocProducer docProducer;
//
//    public ApplicationService(ApplicationRepository repository, DocProducer docProducer) {
//        this.repository = repository;
//        this.docProducer = docProducer;
//    }
//
//    // ✅ SUBMIT APPLICATION
//    public LoanApplication submitApplication(LoanApplication app) {
//
//        System.out.println("⚙️ Service HIT");
//
//        // Step 1: Save first (get applicationId)
//        app.setStatus(ApplicationStatus.SUBMITTED);
//        LoanApplication saved = repository.save(app);
//
//        System.out.println("📌 Application saved with ID: " + saved.getId());
//
//        // Step 2: Check doc status
//        
//
//        String userId = saved.getUserId();
//
//        // 🔑 Get token from SecurityContext
//        String token = (String) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();
//
//        docProducer.sendDocRequest(userId);
//
//        System.out.println("📤 Doc status request sent for user: " + userId);
//
//        saved.setStatus(ApplicationStatus.DOCS_PENDING);
//
//        return repository.save(saved);
//    }
//
//    // ✅ USER: get own applications
//    public List<LoanApplication> getMyApplications(String userId) {
//        System.out.println("📦 Fetching applications for user: " + userId);
//        return repository.findByUserId(userId);
//    }
//
//    // ✅ ADMIN: get all
//    public List<LoanApplicationDTO> getAllApplications() {
//
//        return repository.findAll()
//                .stream()
//                .map(app -> new LoanApplicationDTO(
//                        app.getId(),
//                        app.getUserId(),
//                        app.getFullName(),
//                        app.getStatus().name()
//                ))
//                .toList();
//    }
//    
//    public List<LoanApplication> getAllApplicationsFull() {
//        return repository.findAll();
//    }
//
//    // ✅ REFRESH DOC STATUS
//    public LoanApplication refreshStatus(Long id) {
//
//        LoanApplication app = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        // 🚫 DO NOT CHANGE if final state
//        if (app.getStatus() == ApplicationStatus.APPROVED ||
//            app.getStatus() == ApplicationStatus.REJECTED) {
//
//            System.out.println("⛔ Skipping refresh: Final state = " + app.getStatus());
//            return app;
//        }
//
//        String userId = app.getUserId();
//
//        String token = (String) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getCredentials();
//
//        String docStatus = docClient.getDocStatus(userId, token);
//
//        if ("VERIFIED".equalsIgnoreCase(docStatus)) {
//            app.setStatus(ApplicationStatus.DOCS_VERIFIED);
//        } else {
//            app.setStatus(ApplicationStatus.DOCS_PENDING);
//        }
//
//        return repository.save(app);
//    }
//
//    // ✅ APPROVE
//    public LoanApplication approve(Long id) {
//        LoanApplication app = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        app.setStatus(ApplicationStatus.APPROVED);
//        return repository.save(app);
//    }
//
//    // ❌ REJECT
//    public LoanApplication reject(Long id) {
//        LoanApplication app = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        app.setStatus(ApplicationStatus.REJECTED);
//        return repository.save(app);
//    }
//    public LoanApplication getApplicationForUser(Long id, String userId) {
//
//        LoanApplication app = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Application not found, make sure user applied the loan"));
//
//        if (!app.getUserId().equals(userId)) {
//            throw new RuntimeException("Access Denied");
//        }
//
//        // 🔥 ADD THIS BLOCK (safe)
//        try {
//            String token = (String) SecurityContextHolder.getContext()
//                    .getAuthentication()
//                    .getCredentials();
//
//            String docStatus = docClient.getDocStatus(userId, token);
//
//            System.out.println("📄 Doc Status (GET): " + docStatus);
//
//            if (app.getStatus() != ApplicationStatus.APPROVED &&
//            	    app.getStatus() != ApplicationStatus.REJECTED) {
//
//            	    try {
//            	        String token4 = (String) SecurityContextHolder.getContext()
//            	                .getAuthentication()
//            	                .getCredentials();
//
//            	        String docStatus4 = docClient.getDocStatus(userId, token4);
//
//            	        System.out.println("📄 Doc Status (GET): " + docStatus4);
//
//            	        if ("VERIFIED".equalsIgnoreCase(docStatus4)) {
//            	            app.setStatus(ApplicationStatus.DOCS_VERIFIED);
//            	        } else {
//            	            app.setStatus(ApplicationStatus.DOCS_PENDING);
//            	        }
//
//            	    } catch (Exception e) {
//            	        System.out.println("⚠️ Doc service failed, returning existing status");
//            	    }
//            	}
//
//        } catch (Exception e) {
//            System.out.println("⚠️ Doc service failed, returning existing status");
//        }
//
//        return app;
//    }
//    
//   
//    
//    
//    public AdminStatsDTO getAdminStats1() {
//
//    	long totalApplications = repository.count();
//    	long approved = repository.countByStatus(ApplicationStatus.APPROVED);
//    	long rejected = repository.countByStatus(ApplicationStatus.REJECTED);
//    	long pending = Math.max(0, totalApplications - (approved + rejected));
//
//        // TEMP: until you connect other services
//        long totalUsers = 0;
//        long docPending = 0;
//        long docVerified = 0;
//
//        return new AdminStatsDTO(
//            totalUsers,
//            totalApplications,
//            approved,
//            rejected,
//            pending,
//            docPending,
//            docVerified
//        );
//    }
//    
//    public AdminStatsDTO getAdminStats() {
//
//        System.out.println("🔥 ENTERED getAdminStats");
//
//        long totalApplications = repository.count();
//        System.out.println("Total: " + totalApplications);
//
//        long approved = repository.countByStatus(ApplicationStatus.APPROVED);
//        System.out.println("Approved: " + approved);
//
//        long rejected = repository.countByStatus(ApplicationStatus.REJECTED);
//        System.out.println("Rejected: " + rejected);
//
//        long pending = Math.max(0, totalApplications - (approved + rejected));
//        System.out.println("Pending: " + pending);
//
//        return new AdminStatsDTO(0, totalApplications, approved, rejected, pending, 0, 0);
//    }
//}

package com.finflow.demo.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.finflow.demo.LoanApplicationDTO.LoanApplicationDTO;
import com.finflow.demo.dto.AdminStatsDTO;
import com.finflow.demo.entity.ApplicationStatus;
import com.finflow.demo.entity.LoanApplication;
import com.finflow.demo.repository.ApplicationRepository;

@Service
public class ApplicationService implements ApplicationServiceInterface {

	private final ApplicationRepository repository;
	private final DocProducer docProducer;
	private final DocClient docClient; // ✅ KEEP (fallback)

	public ApplicationService(ApplicationRepository repository, DocProducer docProducer, DocClient docClient) {
		this.repository = repository;
		this.docProducer = docProducer;
		this.docClient = docClient;
	}

	// ✅ SUBMIT (ASYNC)
	@CacheEvict(value = "loan", allEntries = true)
	public LoanApplication submitApplication(LoanApplication app) {

		System.out.println("⚙️ Service HIT");

		app.setStatus(ApplicationStatus.SUBMITTED);
		LoanApplication saved = repository.save(app);

		System.out.println("📌 Application saved with ID: " + saved.getId());

		String userId = saved.getUserId();

		// ✅ Send message (RabbitMQ)
		docProducer.sendDocRequest(userId);

		System.out.println("📤 Async doc request sent for user: " + userId);

		// Default pending
		saved.setStatus(ApplicationStatus.DOCS_PENDING);

		return repository.save(saved);
	}

	// ✅ USER
	@Cacheable(value = "loanList", key = "#userId")
	public List<LoanApplication> getMyApplications(String userId) {
		return repository.findByUserId(userId);
	}

	// ✅ ADMIN
	public List<LoanApplicationDTO> getAllApplications() {
		return repository.findAll().stream().map(
				app -> new LoanApplicationDTO(app.getId(), app.getUserId(), app.getFullName(), app.getStatus().name()))
				.toList();
	}

	public List<LoanApplication> getAllApplicationsFull() {
		return repository.findAll();
	}

	// ✅ REFRESH (SAFE FALLBACK)
	@CacheEvict(value = { "loan", "loanList" }, allEntries = true)
	public LoanApplication refreshStatus(Long id) {

		LoanApplication app = repository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));

		// Skip final states
		if (app.getStatus() == ApplicationStatus.APPROVED || app.getStatus() == ApplicationStatus.REJECTED) {

			return app;
		}

		try {
			String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

			String userId = app.getUserId();

			String docStatus = docClient.getDocStatus(userId, token);

			System.out.println("📄 Doc Status (refresh): " + docStatus);

			if ("VERIFIED".equalsIgnoreCase(docStatus)) {
				app.setStatus(ApplicationStatus.DOCS_VERIFIED);
			} else {
				app.setStatus(ApplicationStatus.DOCS_PENDING);
			}

		} catch (Exception e) {
			System.out.println("⚠️ Fallback failed, keeping old status");
		}

		return repository.save(app);
	}

	// ✅ APPROVE
	@CacheEvict(value = { "loan", "loanList" }, allEntries = true)
	public LoanApplication approve(Long id) {
		LoanApplication app = repository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));

		app.setStatus(ApplicationStatus.APPROVED);
		return repository.save(app);
	}

	// ❌ REJECT
	@CacheEvict(value = { "loan", "loanList" }, allEntries = true)
	public LoanApplication reject(Long id) {
		LoanApplication app = repository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));

		app.setStatus(ApplicationStatus.REJECTED);
		return repository.save(app);
	}

	// ✅ GET APPLICATION (SAFE)
	@Cacheable(value = "loan", key = "#id + '_' + #userId")
	public LoanApplication getApplicationForUser(Long id, String userId) {

		LoanApplication app = repository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));

		if (!app.getUserId().equals(userId)) {
			throw new RuntimeException("Access Denied");
		}

		// Optional fallback check
		try {
			String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

			String docStatus = docClient.getDocStatus(userId, token);

			if (app.getStatus() != ApplicationStatus.APPROVED && app.getStatus() != ApplicationStatus.REJECTED) {

				if ("VERIFIED".equalsIgnoreCase(docStatus)) {
					app.setStatus(ApplicationStatus.DOCS_VERIFIED);
				}
			}

		} catch (Exception e) {
			// ignore
		}
		System.out.println("🔥 FETCHING FROM DB");
		return app;
	}

	// ✅ STATS
	public AdminStatsDTO getAdminStats() {

		long totalApplications = repository.count();
		long approved = repository.countByStatus(ApplicationStatus.APPROVED);
		long rejected = repository.countByStatus(ApplicationStatus.REJECTED);
		long pending = Math.max(0, totalApplications - (approved + rejected));

		return new AdminStatsDTO(0, totalApplications, approved, rejected, pending, 0, 0);
	}

	
	public LoanApplication getApplicationByIdForAdmin(Long id) {
		System.out.println("🔥 METHOD ENTERED");
		System.out.println("🔥 FETCHING FROM DB (ADMIN)");

		return repository.findById(id).orElseThrow(() -> new RuntimeException("Application not found"));
	}
}