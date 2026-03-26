package com.finflow.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finflow.demo.entity.Document;
import com.finflow.demo.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    // ✅ EXISTING (DO NOT TOUCH)
    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) {

        System.out.println("📥 Upload Controller HIT");
        System.out.println("👤 userId: " + userId);

        service.upload(file, userId);

        return "Uploaded Successfully for user: " +  userId;
    }

    // ✅ EXISTING (DO NOT TOUCH)
    @GetMapping("/status/{userId}")
    public String getStatus(@PathVariable String userId) {
        return service.getStatus(userId);
    }

    // ✅ EXISTING (ALREADY ADDED)
    @GetMapping("/admin/all")
    public List<Document> getAllDocuments() {
        return service.getAllDocuments();
    }

    // 🆕 ADD THIS
    @PutMapping("/admin/verify/{userId}")
    public String verifyDocument(@PathVariable String userId) {
        return service.verifyDocument(userId);
    }

    // 🆕 ADD THIS
    @PutMapping("/admin/reject/{userId}")
    public String rejectDocument(@PathVariable String userId) {
        return service.rejectDocument(userId);
    }
}