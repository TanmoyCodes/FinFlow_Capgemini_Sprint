package com.finflow.demo.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finflow.demo.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

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

    @GetMapping("/status/{userId}")
    public String getStatus(@PathVariable String userId) {
        return service.getStatus(userId);
    }
}
